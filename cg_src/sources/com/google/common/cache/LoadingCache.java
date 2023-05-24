package com.google.common.cache;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
@GwtCompatible
/* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/cache/LoadingCache.class */
public interface LoadingCache<K, V> extends Cache<K, V>, Function<K, V> {
    V get(K k) throws ExecutionException;

    V getUnchecked(K k);

    ImmutableMap<K, V> getAll(Iterable<? extends K> iterable) throws ExecutionException;

    @Deprecated
    V apply(K k);

    void refresh(K k);

    @Override // com.google.common.cache.Cache
    ConcurrentMap<K, V> asMap();
}
