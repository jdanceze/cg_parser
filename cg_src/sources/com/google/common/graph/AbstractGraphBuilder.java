package com.google.common.graph;

import com.google.common.base.Optional;
/* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/graph/AbstractGraphBuilder.class */
abstract class AbstractGraphBuilder<N> {
    final boolean directed;
    boolean allowsSelfLoops = false;
    ElementOrder<N> nodeOrder = ElementOrder.insertion();
    Optional<Integer> expectedNodeCount = Optional.absent();

    /* JADX INFO: Access modifiers changed from: package-private */
    public AbstractGraphBuilder(boolean directed) {
        this.directed = directed;
    }
}
