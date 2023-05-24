package com.google.common.graph;

import com.google.common.annotations.Beta;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
@Beta
/* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/graph/MutableNetwork.class */
public interface MutableNetwork<N, E> extends Network<N, E> {
    @CanIgnoreReturnValue
    boolean addNode(N n);

    @CanIgnoreReturnValue
    boolean addEdge(N n, N n2, E e);

    @CanIgnoreReturnValue
    boolean addEdge(EndpointPair<N> endpointPair, E e);

    @CanIgnoreReturnValue
    boolean removeNode(N n);

    @CanIgnoreReturnValue
    boolean removeEdge(E e);
}
