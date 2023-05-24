package soot.toolkits.scalar;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Map;
import soot.options.Options;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.graph.interaction.InteractionHandler;
/* loaded from: gencallgraphv3.jar:soot/toolkits/scalar/AbstractFlowAnalysis.class */
public abstract class AbstractFlowAnalysis<N, A> {
    protected final DirectedGraph<N> graph;
    protected final Map<N, A> unitToBeforeFlow;
    protected Map<N, A> filterUnitToBeforeFlow = Collections.emptyMap();

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract A newInitialFlow();

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract boolean isForward();

    protected abstract void merge(A a, A a2, A a3);

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract void copy(A a, A a2);

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract void doAnalysis();

    public AbstractFlowAnalysis(DirectedGraph<N> graph) {
        this.graph = graph;
        this.unitToBeforeFlow = new IdentityHashMap((graph.size() * 2) + 1);
        if (Options.v().interactive_mode()) {
            InteractionHandler.v().handleCfgEvent(graph);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public A entryInitialFlow() {
        return newInitialFlow();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean treatTrapHandlersAsEntries() {
        return false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void merge(N succNode, A in1, A in2, A out) {
        merge(in1, in2, out);
    }

    public A getFlowBefore(N s) {
        return this.unitToBeforeFlow.get(s);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void mergeInto(N succNode, A inout, A in) {
        A tmp = newInitialFlow();
        merge(succNode, inout, in, tmp);
        copy(tmp, inout);
    }
}
