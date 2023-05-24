package com.google.common.cache;

import com.google.common.annotations.GwtCompatible;
@GwtCompatible
/* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/cache/LongAddable.class */
interface LongAddable {
    void increment();

    void add(long j);

    long sum();
}
