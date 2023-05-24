package com.google.common.cache;

import com.google.common.annotations.GwtCompatible;
@GwtCompatible
/* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/cache/Weigher.class */
public interface Weigher<K, V> {
    int weigh(K k, V v);
}
