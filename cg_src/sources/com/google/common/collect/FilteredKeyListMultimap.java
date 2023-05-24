package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Predicate;
import java.util.Collection;
import java.util.List;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
/* JADX INFO: Access modifiers changed from: package-private */
@GwtCompatible
/* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/collect/FilteredKeyListMultimap.class */
public final class FilteredKeyListMultimap<K, V> extends FilteredKeyMultimap<K, V> implements ListMultimap<K, V> {
    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.collect.FilteredKeyMultimap, com.google.common.collect.Multimap, com.google.common.collect.ListMultimap
    public /* bridge */ /* synthetic */ Collection get(Object obj) {
        return get((FilteredKeyListMultimap<K, V>) obj);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap, com.google.common.collect.ListMultimap
    public /* bridge */ /* synthetic */ Collection replaceValues(Object obj, Iterable iterable) {
        return replaceValues((FilteredKeyListMultimap<K, V>) obj, iterable);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public FilteredKeyListMultimap(ListMultimap<K, V> unfiltered, Predicate<? super K> keyPredicate) {
        super(unfiltered, keyPredicate);
    }

    @Override // com.google.common.collect.FilteredKeyMultimap, com.google.common.collect.FilteredMultimap
    public ListMultimap<K, V> unfiltered() {
        return (ListMultimap) super.unfiltered();
    }

    @Override // com.google.common.collect.FilteredKeyMultimap, com.google.common.collect.Multimap, com.google.common.collect.ListMultimap
    public List<V> get(K key) {
        return (List) super.get((FilteredKeyListMultimap<K, V>) key);
    }

    @Override // com.google.common.collect.FilteredKeyMultimap, com.google.common.collect.Multimap, com.google.common.collect.ListMultimap
    public List<V> removeAll(@NullableDecl Object key) {
        return (List) super.removeAll(key);
    }

    @Override // com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap, com.google.common.collect.ListMultimap
    public List<V> replaceValues(K key, Iterable<? extends V> values) {
        return (List) super.replaceValues((FilteredKeyListMultimap<K, V>) key, (Iterable) values);
    }
}
