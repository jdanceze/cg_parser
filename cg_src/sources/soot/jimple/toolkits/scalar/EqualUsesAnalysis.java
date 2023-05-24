package soot.jimple.toolkits.scalar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.EquivalentValue;
import soot.Local;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.jimple.DefinitionStmt;
import soot.jimple.Stmt;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.ArraySparseSet;
import soot.toolkits.scalar.FlowSet;
import soot.toolkits.scalar.ForwardFlowAnalysis;
@Deprecated
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/scalar/EqualUsesAnalysis.class */
public class EqualUsesAnalysis extends ForwardFlowAnalysis<Unit, FlowSet<Object>> {
    private static final Logger logger = LoggerFactory.getLogger(EqualUsesAnalysis.class);
    protected final EqualLocalsAnalysis el;
    protected Map<Stmt, Local> stmtToLocal;
    protected Set<Stmt> useStmts;
    protected Collection<Local> useLocals;
    protected List<Stmt> boundaryStmts;
    protected List<Stmt> redefStmts;
    protected Map<Stmt, List<Object>> firstUseToAliasSet;

    public EqualUsesAnalysis(UnitGraph g) {
        super(g);
        this.stmtToLocal = null;
        this.useStmts = null;
        this.useLocals = null;
        this.boundaryStmts = null;
        this.redefStmts = null;
        this.firstUseToAliasSet = null;
        this.el = new EqualLocalsAnalysis(g);
    }

    public boolean areEqualUses(Stmt firstStmt, Local firstLocal, Stmt secondStmt, Local secondLocal) {
        return areEqualUses(firstStmt, firstLocal, secondStmt, secondLocal, new ArrayList());
    }

    public boolean areEqualUses(Stmt firstStmt, Local firstLocal, Stmt secondStmt, Local secondLocal, List<Stmt> boundaryStmts) {
        Map<Stmt, Local> stmtToLocal = new HashMap<>();
        stmtToLocal.put(firstStmt, firstLocal);
        stmtToLocal.put(secondStmt, secondLocal);
        return areEqualUses(stmtToLocal, boundaryStmts);
    }

    public boolean areEqualUses(Map<Stmt, Local> stmtToLocal) {
        return areEqualUses(stmtToLocal, new ArrayList());
    }

    public boolean areEqualUses(Map<Stmt, Local> stmtToLocal, List<Stmt> boundaryStmts) {
        this.stmtToLocal = stmtToLocal;
        this.useStmts = stmtToLocal.keySet();
        this.useLocals = stmtToLocal.values();
        this.boundaryStmts = boundaryStmts;
        this.redefStmts = new ArrayList();
        this.firstUseToAliasSet = new HashMap();
        doAnalysis();
        for (Stmt u : this.useStmts) {
            FlowSet<Object> fs = getFlowBefore(u);
            for (Stmt next : this.redefStmts) {
                if (fs.contains(next)) {
                    return false;
                }
            }
            List<Object> aliases = null;
            for (Object o : fs) {
                if (o instanceof List) {
                    aliases = (List) o;
                }
            }
            if (aliases != null && !aliases.contains(new EquivalentValue(stmtToLocal.get(u)))) {
                return false;
            }
        }
        return true;
    }

    public Map<Stmt, List<Object>> getFirstUseToAliasSet() {
        return this.firstUseToAliasSet;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public void merge(FlowSet<Object> inSet1, FlowSet<Object> inSet2, FlowSet<Object> outSet) {
        inSet1.union(inSet2, outSet);
        List<Object> aliases1 = null;
        List<Object> aliases2 = null;
        for (Object o : outSet) {
            if (o instanceof List) {
                if (aliases1 == null) {
                    aliases1 = (List) o;
                } else {
                    aliases2 = (List) o;
                }
            }
        }
        if (aliases1 != null && aliases2 != null) {
            outSet.remove(aliases2);
            Iterator<Object> aliasIt = aliases1.iterator();
            while (aliasIt.hasNext()) {
                if (!aliases2.contains(aliasIt.next())) {
                    aliasIt.remove();
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.FlowAnalysis
    public void flowThrough(FlowSet<Object> in, Unit unit, FlowSet<Object> out) {
        Stmt stmt = (Stmt) unit;
        in.copy(out);
        List<Value> newDefs = new ArrayList<>();
        for (ValueBox vb : stmt.getDefBoxes()) {
            newDefs.add(vb.getValue());
        }
        for (Local useLocal : this.useLocals) {
            if (newDefs.contains(useLocal)) {
                for (Object o : out) {
                    if (o instanceof Stmt) {
                        Stmt s = (Stmt) o;
                        if (this.stmtToLocal.get(s) == useLocal) {
                            this.redefStmts.add(stmt);
                        }
                    }
                }
            }
        }
        if (this.redefStmts.contains(stmt)) {
            out.add(stmt);
        }
        if (this.boundaryStmts.contains(stmt)) {
            out.clear();
        }
        if (this.useStmts.contains(stmt)) {
            if (out.size() == 0) {
                Local l = this.stmtToLocal.get(stmt);
                List<Object> aliasList = this.el.getCopiesOfAt(l, stmt);
                if (aliasList.isEmpty()) {
                    aliasList.add(l);
                }
                this.firstUseToAliasSet.put(stmt, new ArrayList(aliasList));
                out.add(aliasList);
            }
            out.add(stmt);
        }
        if (stmt instanceof DefinitionStmt) {
            List<EquivalentValue> aliases = null;
            for (Object o2 : out) {
                if (o2 instanceof List) {
                    aliases = (List) o2;
                }
            }
            if (aliases != null) {
                if (aliases.contains(new EquivalentValue(((DefinitionStmt) stmt).getRightOp()))) {
                    for (Value v : newDefs) {
                        aliases.add(new EquivalentValue(v));
                    }
                    return;
                }
                for (Value v2 : newDefs) {
                    aliases.remove(new EquivalentValue(v2));
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public void copy(FlowSet<Object> source, FlowSet<Object> dest) {
        source.copy(dest);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public FlowSet<Object> entryInitialFlow() {
        return new ArraySparseSet();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public FlowSet<Object> newInitialFlow() {
        return new ArraySparseSet();
    }
}
