package soot.toolkits.scalar;

import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.scalar.FlowAnalysis;
/* loaded from: gencallgraphv3.jar:soot/toolkits/scalar/BackwardFlowAnalysis.class */
public abstract class BackwardFlowAnalysis<N, A> extends FlowAnalysis<N, A> {
    public BackwardFlowAnalysis(DirectedGraph<N> graph) {
        super(graph);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public boolean isForward() {
        return false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public void doAnalysis() {
        doAnalysis(FlowAnalysis.GraphView.BACKWARD, FlowAnalysis.InteractionFlowHandler.BACKWARD, this.unitToAfterFlow, this.unitToBeforeFlow);
    }
}
