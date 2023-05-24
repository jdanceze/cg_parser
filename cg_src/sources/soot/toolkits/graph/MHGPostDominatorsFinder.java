package soot.toolkits.graph;
/* loaded from: gencallgraphv3.jar:soot/toolkits/graph/MHGPostDominatorsFinder.class */
public class MHGPostDominatorsFinder<N> extends MHGDominatorsFinder<N> {
    public MHGPostDominatorsFinder(DirectedGraph<N> graph) {
        super(new InverseGraph(graph));
    }
}
