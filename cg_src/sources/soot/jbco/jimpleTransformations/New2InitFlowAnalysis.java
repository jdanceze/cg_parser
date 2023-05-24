package soot.jbco.jimpleTransformations;

import soot.Local;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.jimple.DefinitionStmt;
import soot.jimple.NewExpr;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.scalar.ArraySparseSet;
import soot.toolkits.scalar.BackwardFlowAnalysis;
import soot.toolkits.scalar.FlowSet;
/* loaded from: gencallgraphv3.jar:soot/jbco/jimpleTransformations/New2InitFlowAnalysis.class */
public class New2InitFlowAnalysis extends BackwardFlowAnalysis<Unit, FlowSet> {
    FlowSet emptySet;

    public New2InitFlowAnalysis(DirectedGraph<Unit> graph) {
        super(graph);
        this.emptySet = new ArraySparseSet();
        doAnalysis();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.FlowAnalysis
    public void flowThrough(FlowSet in, Unit d, FlowSet out) {
        in.copy(out);
        if (d instanceof DefinitionStmt) {
            DefinitionStmt ds = (DefinitionStmt) d;
            if (ds.getRightOp() instanceof NewExpr) {
                Value v = ds.getLeftOp();
                if ((v instanceof Local) && in.contains(v)) {
                    out.remove(v);
                    return;
                }
                return;
            }
            return;
        }
        for (ValueBox useBox : d.getUseBoxes()) {
            Value v2 = useBox.getValue();
            if (v2 instanceof Local) {
                out.add(v2);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public FlowSet newInitialFlow() {
        return this.emptySet.mo2534clone();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public FlowSet entryInitialFlow() {
        return this.emptySet.mo2534clone();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public void merge(FlowSet in1, FlowSet in2, FlowSet out) {
        in1.union(in2, out);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public void copy(FlowSet source, FlowSet dest) {
        source.copy(dest);
    }
}
