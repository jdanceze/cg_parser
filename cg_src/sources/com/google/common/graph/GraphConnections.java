package com.google.common.graph;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.Set;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
/* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/graph/GraphConnections.class */
interface GraphConnections<N, V> {
    Set<N> adjacentNodes();

    Set<N> predecessors();

    Set<N> successors();

    @NullableDecl
    V value(N n);

    void removePredecessor(N n);

    @CanIgnoreReturnValue
    V removeSuccessor(N n);

    void addPredecessor(N n, V v);

    @CanIgnoreReturnValue
    V addSuccessor(N n, V v);
}
