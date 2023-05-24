package soot.toolkits.scalar;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import soot.Unit;
import soot.toolkits.graph.DirectedGraph;
/* loaded from: gencallgraphv3.jar:soot/toolkits/scalar/BranchedFlowAnalysis.class */
public abstract class BranchedFlowAnalysis<N extends Unit, A> extends AbstractFlowAnalysis<N, A> {
    protected final Map<Unit, List<A>> unitToAfterFallFlow;
    protected final Map<Unit, List<A>> unitToAfterBranchFlow;

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract void flowThrough(A a, Unit unit, List<A> list, List<A> list2);

    public BranchedFlowAnalysis(DirectedGraph<N> graph) {
        super(graph);
        this.unitToAfterFallFlow = new HashMap((graph.size() * 2) + 1, 0.7f);
        this.unitToAfterBranchFlow = new HashMap((graph.size() * 2) + 1, 0.7f);
    }

    public A getFallFlowAfter(Unit s) {
        List<A> fl = this.unitToAfterFallFlow.get(s);
        return fl.isEmpty() ? newInitialFlow() : fl.get(0);
    }

    public List<A> getBranchFlowAfter(Unit s) {
        return this.unitToAfterBranchFlow.get(s);
    }
}
