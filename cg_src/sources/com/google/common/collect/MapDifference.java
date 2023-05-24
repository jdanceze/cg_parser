package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import java.util.Map;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
@GwtCompatible
/* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/collect/MapDifference.class */
public interface MapDifference<K, V> {

    /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/collect/MapDifference$ValueDifference.class */
    public interface ValueDifference<V> {
        V leftValue();

        V rightValue();

        boolean equals(@NullableDecl Object obj);

        int hashCode();
    }

    boolean areEqual();

    Map<K, V> entriesOnlyOnLeft();

    Map<K, V> entriesOnlyOnRight();

    Map<K, V> entriesInCommon();

    Map<K, ValueDifference<V>> entriesDiffering();

    boolean equals(@NullableDecl Object obj);

    int hashCode();
}
