package soot.toolkits.graph;
/* loaded from: gencallgraphv3.jar:soot/toolkits/graph/MutableEdgeLabelledDirectedGraph.class */
public interface MutableEdgeLabelledDirectedGraph<N, L> extends EdgeLabelledDirectedGraph<N, L> {
    void addEdge(N n, N n2, L l);

    void removeEdge(N n, N n2, L l);

    void removeAllEdges(N n, N n2);

    void removeAllEdges(L l);

    void addNode(N n);

    void removeNode(N n);
}
