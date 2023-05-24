package soot.jimple.toolkits.scalar;

import java.util.ArrayList;
import java.util.List;
import soot.EquivalentValue;
import soot.Local;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.jimple.DefinitionStmt;
import soot.jimple.IdentityStmt;
import soot.jimple.Stmt;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.ArraySparseSet;
import soot.toolkits.scalar.FlowSet;
import soot.toolkits.scalar.ForwardFlowAnalysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/scalar/EqualLocalsAnalysis.class */
public class EqualLocalsAnalysis extends ForwardFlowAnalysis<Unit, FlowSet<Object>> {
    protected Local l;
    protected Stmt s;

    public EqualLocalsAnalysis(UnitGraph g) {
        super(g);
        this.l = null;
        this.s = null;
    }

    public List<Object> getCopiesOfAt(Local l, Stmt s) {
        this.l = l;
        this.s = s;
        doAnalysis();
        FlowSet<Object> fs = getFlowBefore(s);
        ArrayList<Object> aliasList = new ArrayList<>(fs.size());
        for (Object o : fs) {
            aliasList.add(o);
        }
        if (!aliasList.contains(new EquivalentValue(l))) {
            aliasList.clear();
            aliasList.trimToSize();
        }
        return aliasList;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.FlowAnalysis
    public void flowThrough(FlowSet<Object> in, Unit unit, FlowSet<Object> out) {
        in.copy(out);
        List<EquivalentValue> newDefs = new ArrayList<>();
        for (ValueBox next : unit.getDefBoxes()) {
            newDefs.add(new EquivalentValue(next.getValue()));
        }
        if (newDefs.contains(new EquivalentValue(this.l))) {
            List<Stmt> existingDefStmts = new ArrayList<>();
            for (Object o : out) {
                if (o instanceof Stmt) {
                    existingDefStmts.add((Stmt) o);
                }
            }
            out.clear();
            for (EquivalentValue next2 : newDefs) {
                out.add(next2);
            }
            if (unit instanceof DefinitionStmt) {
                DefinitionStmt du = (DefinitionStmt) unit;
                if (!du.containsInvokeExpr() && !(unit instanceof IdentityStmt)) {
                    out.add(new EquivalentValue(du.getRightOp()));
                }
            }
            for (Stmt def : existingDefStmts) {
                List<Value> sNewDefs = new ArrayList<>();
                for (ValueBox next3 : def.getDefBoxes()) {
                    sNewDefs.add(next3.getValue());
                }
                if (def instanceof DefinitionStmt) {
                    if (out.contains(new EquivalentValue(((DefinitionStmt) def).getRightOp()))) {
                        for (Value v : sNewDefs) {
                            out.add(new EquivalentValue(v));
                        }
                    } else {
                        for (Value v2 : sNewDefs) {
                            out.remove(new EquivalentValue(v2));
                        }
                    }
                }
            }
        } else if (unit instanceof DefinitionStmt) {
            if (out.contains(new EquivalentValue(this.l))) {
                if (out.contains(new EquivalentValue(((DefinitionStmt) unit).getRightOp()))) {
                    for (EquivalentValue ev : newDefs) {
                        out.add(ev);
                    }
                    return;
                }
                for (EquivalentValue ev2 : newDefs) {
                    out.remove(ev2);
                }
                return;
            }
            out.add(unit);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public void merge(FlowSet<Object> in1, FlowSet<Object> in2, FlowSet<Object> out) {
        in1.intersection(in2, out);
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
