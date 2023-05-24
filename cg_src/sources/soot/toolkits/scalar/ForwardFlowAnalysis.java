package soot.toolkits.scalar;

import soot.Timers;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.scalar.FlowAnalysis;
/* loaded from: gencallgraphv3.jar:soot/toolkits/scalar/ForwardFlowAnalysis.class */
public abstract class ForwardFlowAnalysis<N, A> extends FlowAnalysis<N, A> {
    public ForwardFlowAnalysis(DirectedGraph<N> graph) {
        super(graph);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public boolean isForward() {
        return true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public void doAnalysis() {
        int i = doAnalysis(FlowAnalysis.GraphView.FORWARD, FlowAnalysis.InteractionFlowHandler.FORWARD, this.unitToBeforeFlow, this.unitToAfterFlow);
        Timers.v().totalFlowNodes += this.graph.size();
        Timers.v().totalFlowComputations += i;
    }
}
