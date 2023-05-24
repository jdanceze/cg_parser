package soot.toolkits.graph;

import java.util.List;
/* loaded from: gencallgraphv3.jar:soot/toolkits/graph/MutableDirectedGraph.class */
public interface MutableDirectedGraph<N> extends DirectedGraph<N> {
    void addEdge(N n, N n2);

    void removeEdge(N n, N n2);

    boolean containsEdge(N n, N n2);

    List<N> getNodes();

    void addNode(N n);

    void removeNode(N n);

    boolean containsNode(N n);
}
