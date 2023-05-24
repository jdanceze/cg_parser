package com.google.common.graph;

import com.google.common.annotations.Beta;
@Beta
/* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/graph/SuccessorsFunction.class */
public interface SuccessorsFunction<N> {
    Iterable<? extends N> successors(N n);
}
