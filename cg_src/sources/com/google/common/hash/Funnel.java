package com.google.common.hash;

import com.google.common.annotations.Beta;
import java.io.Serializable;
@Beta
/* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/hash/Funnel.class */
public interface Funnel<T> extends Serializable {
    void funnel(T t, PrimitiveSink primitiveSink);
}
