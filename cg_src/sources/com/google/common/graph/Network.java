package com.google.common.graph;

import com.google.common.annotations.Beta;
import java.util.Set;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
@Beta
/* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/graph/Network.class */
public interface Network<N, E> extends SuccessorsFunction<N>, PredecessorsFunction<N> {
    Set<N> nodes();

    Set<E> edges();

    Graph<N> asGraph();

    boolean isDirected();

    boolean allowsParallelEdges();

    boolean allowsSelfLoops();

    ElementOrder<N> nodeOrder();

    ElementOrder<E> edgeOrder();

    Set<N> adjacentNodes(N n);

    Set<N> predecessors(N n);

    @Override // com.google.common.graph.SuccessorsFunction, com.google.common.graph.Graph
    Set<N> successors(N n);

    Set<E> incidentEdges(N n);

    Set<E> inEdges(N n);

    Set<E> outEdges(N n);

    int degree(N n);

    int inDegree(N n);

    int outDegree(N n);

    EndpointPair<N> incidentNodes(E e);

    Set<E> adjacentEdges(E e);

    Set<E> edgesConnecting(N n, N n2);

    Set<E> edgesConnecting(EndpointPair<N> endpointPair);

    @NullableDecl
    E edgeConnectingOrNull(N n, N n2);

    @NullableDecl
    E edgeConnectingOrNull(EndpointPair<N> endpointPair);

    boolean hasEdgeConnecting(N n, N n2);

    boolean hasEdgeConnecting(EndpointPair<N> endpointPair);

    boolean equals(@NullableDecl Object obj);

    int hashCode();
}
