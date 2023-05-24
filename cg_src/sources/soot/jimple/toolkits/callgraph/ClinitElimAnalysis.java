package soot.jimple.toolkits.callgraph;

import java.util.Iterator;
import soot.Scene;
import soot.SootMethod;
import soot.Unit;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.ArraySparseSet;
import soot.toolkits.scalar.FlowSet;
import soot.toolkits.scalar.ForwardFlowAnalysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/callgraph/ClinitElimAnalysis.class */
public class ClinitElimAnalysis extends ForwardFlowAnalysis<Unit, FlowSet<SootMethod>> {
    private final CallGraph cg;
    private final UnitGraph g;

    public ClinitElimAnalysis(UnitGraph g) {
        super(g);
        this.cg = Scene.v().getCallGraph();
        this.g = g;
        doAnalysis();
    }

    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public void merge(FlowSet<SootMethod> in1, FlowSet<SootMethod> in2, FlowSet<SootMethod> out) {
        in1.intersection(in2, out);
    }

    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public void copy(FlowSet<SootMethod> src, FlowSet<SootMethod> dest) {
        src.copy(dest);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.FlowAnalysis
    public void copyFreshToExisting(FlowSet<SootMethod> in, FlowSet<SootMethod> dest) {
        in.copyFreshToExisting(dest);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.FlowAnalysis
    public void flowThrough(FlowSet<SootMethod> inVal, Unit stmt, FlowSet<SootMethod> outVal) {
        inVal.copy(outVal);
        Iterator<Edge> edges = this.cg.edgesOutOf(stmt);
        while (edges.hasNext()) {
            Edge e = edges.next();
            if (e.isClinit()) {
                outVal.add(e.tgt());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public FlowSet<SootMethod> entryInitialFlow() {
        return new ArraySparseSet();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public FlowSet<SootMethod> newInitialFlow() {
        ArraySparseSet<SootMethod> set = new ArraySparseSet<>();
        Iterator<Edge> mIt = this.cg.edgesOutOf(this.g.getBody().getMethod());
        while (mIt.hasNext()) {
            Edge edge = mIt.next();
            if (edge.isClinit()) {
                set.add(edge.tgt());
            }
        }
        return set;
    }
}
