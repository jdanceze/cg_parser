package soot.toolkits.scalar;

import soot.Local;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.toolkits.graph.DirectedBodyGraph;
/* loaded from: gencallgraphv3.jar:soot/toolkits/scalar/InitAnalysis.class */
public class InitAnalysis extends ForwardFlowAnalysis<Unit, FlowSet<Local>> {
    protected final FlowSet<Local> allLocals;

    public InitAnalysis(DirectedBodyGraph<Unit> g) {
        super(g);
        this.allLocals = new ArraySparseSet();
        FlowSet<Local> allLocalsRef = this.allLocals;
        for (Local loc : g.getBody().getLocals()) {
            allLocalsRef.add(loc);
        }
        doAnalysis();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public FlowSet<Local> entryInitialFlow() {
        return new ArraySparseSet();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public FlowSet<Local> newInitialFlow() {
        FlowSet<Local> ret = new ArraySparseSet<>();
        this.allLocals.copy(ret);
        return ret;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.FlowAnalysis
    public void flowThrough(FlowSet<Local> in, Unit unit, FlowSet<Local> out) {
        in.copy(out);
        for (ValueBox defBox : unit.getDefBoxes()) {
            Value lhs = defBox.getValue();
            if (lhs instanceof Local) {
                out.add((Local) lhs);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public void merge(FlowSet<Local> in1, FlowSet<Local> in2, FlowSet<Local> out) {
        in1.intersection(in2, out);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public void copy(FlowSet<Local> source, FlowSet<Local> dest) {
        source.copy(dest);
    }
}
