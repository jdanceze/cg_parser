package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.errorprone.annotations.CompatibleWith;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
@GwtCompatible
/* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/collect/Table.class */
public interface Table<R, C, V> {

    /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/collect/Table$Cell.class */
    public interface Cell<R, C, V> {
        @NullableDecl
        R getRowKey();

        @NullableDecl
        C getColumnKey();

        @NullableDecl
        V getValue();

        boolean equals(@NullableDecl Object obj);

        int hashCode();
    }

    boolean contains(@NullableDecl @CompatibleWith("R") Object obj, @NullableDecl @CompatibleWith("C") Object obj2);

    boolean containsRow(@NullableDecl @CompatibleWith("R") Object obj);

    boolean containsColumn(@NullableDecl @CompatibleWith("C") Object obj);

    boolean containsValue(@NullableDecl @CompatibleWith("V") Object obj);

    V get(@NullableDecl @CompatibleWith("R") Object obj, @NullableDecl @CompatibleWith("C") Object obj2);

    boolean isEmpty();

    int size();

    boolean equals(@NullableDecl Object obj);

    int hashCode();

    void clear();

    @CanIgnoreReturnValue
    @NullableDecl
    V put(R r, C c, V v);

    void putAll(Table<? extends R, ? extends C, ? extends V> table);

    @CanIgnoreReturnValue
    @NullableDecl
    V remove(@NullableDecl @CompatibleWith("R") Object obj, @NullableDecl @CompatibleWith("C") Object obj2);

    Map<C, V> row(R r);

    Map<R, V> column(C c);

    Set<Cell<R, C, V>> cellSet();

    Set<R> rowKeySet();

    Set<C> columnKeySet();

    Collection<V> values();

    Map<R, Map<C, V>> rowMap();

    Map<C, Map<R, V>> columnMap();
}
