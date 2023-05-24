package soot.toolkits.graph;

import java.util.List;
/* loaded from: gencallgraphv3.jar:soot/toolkits/graph/EdgeLabelledDirectedGraph.class */
public interface EdgeLabelledDirectedGraph<N, L> extends DirectedGraph<N> {
    List<L> getLabelsForEdges(N n, N n2);

    DirectedGraph<N> getEdgesForLabel(L l);

    boolean containsEdge(N n, N n2, L l);

    boolean containsAnyEdge(N n, N n2);

    boolean containsAnyEdge(L l);

    boolean containsNode(N n);
}
