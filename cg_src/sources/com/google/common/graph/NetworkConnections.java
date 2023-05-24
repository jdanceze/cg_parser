package com.google.common.graph;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.Set;
/* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/graph/NetworkConnections.class */
interface NetworkConnections<N, E> {
    Set<N> adjacentNodes();

    Set<N> predecessors();

    Set<N> successors();

    Set<E> incidentEdges();

    Set<E> inEdges();

    Set<E> outEdges();

    Set<E> edgesConnecting(N n);

    N adjacentNode(E e);

    @CanIgnoreReturnValue
    N removeInEdge(E e, boolean z);

    @CanIgnoreReturnValue
    N removeOutEdge(E e);

    void addInEdge(E e, N n, boolean z);

    void addOutEdge(E e, N n);
}
