package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.Map;
import java.util.Set;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
@GwtCompatible
/* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/collect/BiMap.class */
public interface BiMap<K, V> extends Map<K, V> {
    @Override // com.google.common.collect.BiMap
    @CanIgnoreReturnValue
    @NullableDecl
    V put(@NullableDecl K k, @NullableDecl V v);

    @CanIgnoreReturnValue
    @NullableDecl
    V forcePut(@NullableDecl K k, @NullableDecl V v);

    @Override // com.google.common.collect.BiMap
    void putAll(Map<? extends K, ? extends V> map);

    @Override // com.google.common.collect.BiMap
    Set<V> values();

    BiMap<V, K> inverse();
}
