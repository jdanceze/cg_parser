package com.google.common.graph;

import com.google.common.annotations.Beta;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
@Beta
/* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/graph/MutableGraph.class */
public interface MutableGraph<N> extends Graph<N> {
    @CanIgnoreReturnValue
    boolean addNode(N n);

    @CanIgnoreReturnValue
    boolean putEdge(N n, N n2);

    @CanIgnoreReturnValue
    boolean putEdge(EndpointPair<N> endpointPair);

    @CanIgnoreReturnValue
    boolean removeNode(N n);

    @CanIgnoreReturnValue
    boolean removeEdge(N n, N n2);

    @CanIgnoreReturnValue
    boolean removeEdge(EndpointPair<N> endpointPair);
}
