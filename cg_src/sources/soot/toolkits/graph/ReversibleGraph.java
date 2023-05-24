package soot.toolkits.graph;
/* loaded from: gencallgraphv3.jar:soot/toolkits/graph/ReversibleGraph.class */
public interface ReversibleGraph<N> extends MutableDirectedGraph<N> {
    boolean isReversed();

    ReversibleGraph<N> reverse();
}
