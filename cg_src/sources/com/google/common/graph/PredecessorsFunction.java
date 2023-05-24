package com.google.common.graph;

import com.google.common.annotations.Beta;
@Beta
/* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/graph/PredecessorsFunction.class */
public interface PredecessorsFunction<N> {
    Iterable<? extends N> predecessors(N n);
}
