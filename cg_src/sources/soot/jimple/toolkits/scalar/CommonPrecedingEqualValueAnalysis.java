package soot.jimple.toolkits.scalar;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.EquivalentValue;
import soot.Unit;
import soot.ValueBox;
import soot.jimple.DefinitionStmt;
import soot.jimple.Stmt;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.ArraySparseSet;
import soot.toolkits.scalar.BackwardFlowAnalysis;
import soot.toolkits.scalar.FlowSet;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/scalar/CommonPrecedingEqualValueAnalysis.class */
public class CommonPrecedingEqualValueAnalysis extends BackwardFlowAnalysis<Unit, FlowSet<Object>> {
    private static final Logger logger = LoggerFactory.getLogger(CommonPrecedingEqualValueAnalysis.class);
    protected Map<? extends Unit, List<Object>> unitToAliasSet;
    protected Stmt s;

    public CommonPrecedingEqualValueAnalysis(UnitGraph g) {
        super(g);
        this.unitToAliasSet = null;
        this.s = null;
    }

    public List<Object> getCommonAncestorValuesOf(Map<? extends Unit, List<Object>> unitToAliasSet, Stmt s) {
        this.unitToAliasSet = unitToAliasSet;
        this.s = s;
        doAnalysis();
        FlowSet<Object> fs = getFlowAfter(s);
        List<Object> ancestorList = new ArrayList<>(fs.size());
        for (Object o : fs) {
            ancestorList.add(o);
        }
        return ancestorList;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public void merge(FlowSet<Object> in1, FlowSet<Object> in2, FlowSet<Object> out) {
        in1.intersection(in2, out);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.FlowAnalysis
    public void flowThrough(FlowSet<Object> in, Unit unit, FlowSet<Object> out) {
        in.copy(out);
        List<EquivalentValue> newDefs = new ArrayList<>();
        for (ValueBox vb : unit.getDefBoxes()) {
            newDefs.add(new EquivalentValue(vb.getValue()));
        }
        List<Object> aliases = this.unitToAliasSet.get(unit);
        if (aliases != null) {
            out.clear();
            for (Object next : aliases) {
                out.add(next);
            }
        } else if (unit instanceof DefinitionStmt) {
            for (EquivalentValue ev : newDefs) {
                out.remove(ev);
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
