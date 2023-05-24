package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multisets;
import com.google.common.collect.SortedMultisets;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NavigableSet;
import java.util.Set;
import org.checkerframework.checker.nullness.compatqual.MonotonicNonNullDecl;
@GwtCompatible(emulated = true)
/* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/collect/DescendingMultiset.class */
abstract class DescendingMultiset<E> extends ForwardingMultiset<E> implements SortedMultiset<E> {
    @MonotonicNonNullDecl
    private transient Comparator<? super E> comparator;
    @MonotonicNonNullDecl
    private transient NavigableSet<E> elementSet;
    @MonotonicNonNullDecl
    private transient Set<Multiset.Entry<E>> entrySet;

    abstract SortedMultiset<E> forwardMultiset();

    abstract Iterator<Multiset.Entry<E>> entryIterator();

    @Override // com.google.common.collect.SortedMultiset, com.google.common.collect.SortedIterable
    public Comparator<? super E> comparator() {
        Comparator<? super E> result = this.comparator;
        if (result == null) {
            Ordering reverse = Ordering.from(forwardMultiset().comparator()).reverse();
            this.comparator = reverse;
            return reverse;
        }
        return result;
    }

    @Override // com.google.common.collect.ForwardingMultiset, com.google.common.collect.Multiset
    public NavigableSet<E> elementSet() {
        NavigableSet<E> result = this.elementSet;
        if (result == null) {
            SortedMultisets.NavigableElementSet navigableElementSet = new SortedMultisets.NavigableElementSet(this);
            this.elementSet = navigableElementSet;
            return navigableElementSet;
        }
        return result;
    }

    @Override // com.google.common.collect.SortedMultiset
    public Multiset.Entry<E> pollFirstEntry() {
        return forwardMultiset().pollLastEntry();
    }

    @Override // com.google.common.collect.SortedMultiset
    public Multiset.Entry<E> pollLastEntry() {
        return forwardMultiset().pollFirstEntry();
    }

    @Override // com.google.common.collect.SortedMultiset
    public SortedMultiset<E> headMultiset(E toElement, BoundType boundType) {
        return forwardMultiset().tailMultiset(toElement, boundType).descendingMultiset();
    }

    @Override // com.google.common.collect.SortedMultiset
    public SortedMultiset<E> subMultiset(E fromElement, BoundType fromBoundType, E toElement, BoundType toBoundType) {
        return forwardMultiset().subMultiset(toElement, toBoundType, fromElement, fromBoundType).descendingMultiset();
    }

    @Override // com.google.common.collect.SortedMultiset
    public SortedMultiset<E> tailMultiset(E fromElement, BoundType boundType) {
        return forwardMultiset().headMultiset(fromElement, boundType).descendingMultiset();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.common.collect.ForwardingMultiset, com.google.common.collect.ForwardingCollection, com.google.common.collect.ForwardingObject
    public Multiset<E> delegate() {
        return forwardMultiset();
    }

    @Override // com.google.common.collect.SortedMultiset
    public SortedMultiset<E> descendingMultiset() {
        return forwardMultiset();
    }

    @Override // com.google.common.collect.SortedMultiset
    public Multiset.Entry<E> firstEntry() {
        return forwardMultiset().lastEntry();
    }

    @Override // com.google.common.collect.SortedMultiset
    public Multiset.Entry<E> lastEntry() {
        return forwardMultiset().firstEntry();
    }

    @Override // com.google.common.collect.ForwardingMultiset, com.google.common.collect.Multiset
    public Set<Multiset.Entry<E>> entrySet() {
        Set<Multiset.Entry<E>> result = this.entrySet;
        if (result == null) {
            Set<Multiset.Entry<E>> createEntrySet = createEntrySet();
            this.entrySet = createEntrySet;
            return createEntrySet;
        }
        return result;
    }

    Set<Multiset.Entry<E>> createEntrySet() {
        return new Multisets.EntrySet<E>() { // from class: com.google.common.collect.DescendingMultiset.1EntrySetImpl
            @Override // com.google.common.collect.Multisets.EntrySet
            Multiset<E> multiset() {
                return DescendingMultiset.this;
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
            public Iterator<Multiset.Entry<E>> iterator() {
                return DescendingMultiset.this.entryIterator();
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
            public int size() {
                return DescendingMultiset.this.forwardMultiset().entrySet().size();
            }
        };
    }

    @Override // com.google.common.collect.ForwardingCollection, java.util.Collection, java.lang.Iterable, java.util.Set
    public Iterator<E> iterator() {
        return Multisets.iteratorImpl(this);
    }

    @Override // com.google.common.collect.ForwardingCollection, java.util.Collection, java.util.Set
    public Object[] toArray() {
        return standardToArray();
    }

    @Override // com.google.common.collect.ForwardingCollection, java.util.Collection, java.util.Set
    public <T> T[] toArray(T[] array) {
        return (T[]) standardToArray(array);
    }

    @Override // com.google.common.collect.ForwardingObject
    public String toString() {
        return entrySet().toString();
    }
}
