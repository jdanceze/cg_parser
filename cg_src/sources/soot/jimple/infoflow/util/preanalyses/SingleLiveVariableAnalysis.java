package soot.jimple.infoflow.util.preanalyses;

import soot.Local;
import soot.Unit;
import soot.ValueBox;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.scalar.ArraySparseSet;
import soot.toolkits.scalar.BackwardFlowAnalysis;
import soot.toolkits.scalar.FlowSet;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/util/preanalyses/SingleLiveVariableAnalysis.class */
public class SingleLiveVariableAnalysis extends BackwardFlowAnalysis<Unit, FlowSet<Local>> {
    Local queryLocal;
    Unit turnUnit;
    int runtime;

    public SingleLiveVariableAnalysis(DirectedGraph<Unit> graph, Local queryLocal, Unit turnUnit) {
        super(graph);
        this.queryLocal = queryLocal;
        this.turnUnit = turnUnit;
        long timeBefore = System.nanoTime();
        doAnalysis();
        this.runtime = (int) Math.round((System.nanoTime() - timeBefore) / 1000.0d);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.FlowAnalysis
    public void flowThrough(FlowSet<Local> in, Unit unit, FlowSet<Local> out) {
        if (unit == this.turnUnit) {
            out.clear();
            return;
        }
        in.copy(out);
        for (ValueBox box : unit.getDefBoxes()) {
            if (box.getValue() == this.queryLocal) {
                out.remove(this.queryLocal);
            }
        }
        for (ValueBox box2 : unit.getUseBoxes()) {
            if (box2.getValue() == this.queryLocal) {
                out.add(this.queryLocal);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public FlowSet<Local> newInitialFlow() {
        return new ArraySparseSet();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public void merge(FlowSet<Local> in1, FlowSet<Local> in2, FlowSet<Local> out) {
        in1.union(in2, out);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public void copy(FlowSet<Local> in, FlowSet<Local> out) {
        in.copy(out);
    }

    public boolean canOmitAlias(Unit unit) {
        return getFlowBefore(unit).size() == 0;
    }

    public int getRuntimeInMicroseconds() {
        return this.runtime;
    }
}
