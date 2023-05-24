package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Predicate;
import java.util.Map;
@GwtCompatible
/* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/collect/FilteredMultimap.class */
interface FilteredMultimap<K, V> extends Multimap<K, V> {
    Multimap<K, V> unfiltered();

    Predicate<? super Map.Entry<K, V>> entryPredicate();
}
