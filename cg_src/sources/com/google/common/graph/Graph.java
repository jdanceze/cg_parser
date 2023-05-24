package com.google.common.graph;

import com.google.common.annotations.Beta;
import java.util.Set;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
@Beta
/* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/graph/Graph.class */
public interface Graph<N> extends BaseGraph<N> {
    @Override // 
    Set<N> nodes();

    @Override // com.google.common.graph.BaseGraph
    Set<EndpointPair<N>> edges();

    @Override // 
    boolean isDirected();

    @Override // 
    boolean allowsSelfLoops();

    @Override // 
    ElementOrder<N> nodeOrder();

    @Override // 
    Set<N> adjacentNodes(N n);

    @Override // 
    Set<N> predecessors(N n);

    @Override // 
    Set<N> successors(N n);

    @Override // com.google.common.graph.BaseGraph
    Set<EndpointPair<N>> incidentEdges(N n);

    @Override // com.google.common.graph.BaseGraph
    int degree(N n);

    @Override // com.google.common.graph.BaseGraph
    int inDegree(N n);

    @Override // com.google.common.graph.BaseGraph
    int outDegree(N n);

    @Override // com.google.common.graph.BaseGraph
    boolean hasEdgeConnecting(N n, N n2);

    @Override // com.google.common.graph.BaseGraph
    boolean hasEdgeConnecting(EndpointPair<N> endpointPair);

    boolean equals(@NullableDecl Object obj);

    int hashCode();
}
