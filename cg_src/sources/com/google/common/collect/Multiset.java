package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.errorprone.annotations.CompatibleWith;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
@GwtCompatible
/* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/collect/Multiset.class */
public interface Multiset<E> extends Collection<E> {

    /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/collect/Multiset$Entry.class */
    public interface Entry<E> {
        E getElement();

        int getCount();

        boolean equals(Object obj);

        int hashCode();

        String toString();
    }

    @Override // java.util.Collection, com.google.common.collect.Multiset
    int size();

    int count(@NullableDecl @CompatibleWith("E") Object obj);

    @CanIgnoreReturnValue
    int add(@NullableDecl E e, int i);

    @Override // java.util.Collection, com.google.common.collect.Multiset
    @CanIgnoreReturnValue
    boolean add(E e);

    @CanIgnoreReturnValue
    int remove(@NullableDecl @CompatibleWith("E") Object obj, int i);

    @Override // java.util.Collection, com.google.common.collect.Multiset
    @CanIgnoreReturnValue
    boolean remove(@NullableDecl Object obj);

    @CanIgnoreReturnValue
    int setCount(E e, int i);

    @CanIgnoreReturnValue
    boolean setCount(E e, int i, int i2);

    Set<E> elementSet();

    Set<Entry<E>> entrySet();

    @Override // com.google.common.collect.Multiset
    boolean equals(@NullableDecl Object obj);

    @Override // com.google.common.collect.Multiset
    int hashCode();

    String toString();

    @Override // java.util.Collection, java.lang.Iterable, com.google.common.collect.Multiset
    Iterator<E> iterator();

    @Override // java.util.Collection, com.google.common.collect.Multiset
    boolean contains(@NullableDecl Object obj);

    @Override // java.util.Collection
    boolean containsAll(Collection<?> collection);

    @Override // java.util.Collection, com.google.common.collect.Multiset
    @CanIgnoreReturnValue
    boolean removeAll(Collection<?> collection);

    @Override // java.util.Collection, com.google.common.collect.Multiset
    @CanIgnoreReturnValue
    boolean retainAll(Collection<?> collection);
}
