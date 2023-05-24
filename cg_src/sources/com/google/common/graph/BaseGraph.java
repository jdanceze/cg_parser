package com.google.common.graph;

import java.util.Set;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/graph/BaseGraph.class */
public interface BaseGraph<N> extends SuccessorsFunction<N>, PredecessorsFunction<N> {
    Set<N> nodes();

    Set<EndpointPair<N>> edges();

    boolean isDirected();

    boolean allowsSelfLoops();

    ElementOrder<N> nodeOrder();

    Set<N> adjacentNodes(N n);

    Set<N> predecessors(N n);

    @Override // com.google.common.graph.SuccessorsFunction, com.google.common.graph.Graph
    Set<N> successors(N n);

    Set<EndpointPair<N>> incidentEdges(N n);

    int degree(N n);

    int inDegree(N n);

    int outDegree(N n);

    boolean hasEdgeConnecting(N n, N n2);

    boolean hasEdgeConnecting(EndpointPair<N> endpointPair);
}
