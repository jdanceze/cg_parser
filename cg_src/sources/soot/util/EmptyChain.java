package soot.util;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
/* loaded from: gencallgraphv3.jar:soot/util/EmptyChain.class */
public class EmptyChain<T> implements Chain<T> {
    private static final long serialVersionUID = 1675685752701192002L;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:soot/util/EmptyChain$EmptyIteratorSingleton.class */
    public static class EmptyIteratorSingleton {
        static final Iterator<Object> INSTANCE = new Iterator<Object>() { // from class: soot.util.EmptyChain.EmptyIteratorSingleton.1
            @Override // java.util.Iterator
            public boolean hasNext() {
                return false;
            }

            @Override // java.util.Iterator
            public Object next() {
                throw new NoSuchElementException();
            }

            @Override // java.util.Iterator
            public void remove() {
                throw new NoSuchElementException();
            }
        };

        private EmptyIteratorSingleton() {
        }
    }

    private static <X> Iterator<X> emptyIterator() {
        Iterator<X> retVal = (Iterator<X>) EmptyIteratorSingleton.INSTANCE;
        return retVal;
    }

    /* loaded from: gencallgraphv3.jar:soot/util/EmptyChain$EmptyChainSingleton.class */
    private static class EmptyChainSingleton {
        static final EmptyChain<Object> INSTANCE = new EmptyChain<>();

        private EmptyChainSingleton() {
        }
    }

    public static <X> EmptyChain<X> v() {
        EmptyChain<X> retVal = (EmptyChain<X>) EmptyChainSingleton.INSTANCE;
        return retVal;
    }

    public String toString() {
        return "[]";
    }

    @Override // java.util.Collection
    public boolean isEmpty() {
        return true;
    }

    @Override // java.util.Collection
    public boolean contains(Object o) {
        return false;
    }

    @Override // java.util.Collection
    public Object[] toArray() {
        return new Object[0];
    }

    @Override // java.util.Collection
    public <X> X[] toArray(X[] xArr) {
        return (X[]) new Object[0];
    }

    @Override // java.util.Collection
    public boolean add(T e) {
        throw new RuntimeException("Cannot add elements to an unmodifiable chain");
    }

    @Override // java.util.Collection
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override // java.util.Collection
    public boolean addAll(Collection<? extends T> c) {
        throw new RuntimeException("Cannot add elements to an unmodifiable chain");
    }

    @Override // java.util.Collection
    public boolean removeAll(Collection<?> c) {
        throw new RuntimeException("Cannot remove elements from an unmodifiable chain");
    }

    @Override // java.util.Collection
    public boolean retainAll(Collection<?> c) {
        throw new RuntimeException("Cannot add elements to an unmodifiable chain or remove ones from such chain");
    }

    @Override // java.util.Collection
    public void clear() {
        throw new RuntimeException("Cannot remove elements from an unmodifiable chain");
    }

    @Override // soot.util.Chain
    public void insertBefore(List<T> toInsert, T point) {
        throw new RuntimeException("Cannot add elements to an unmodifiable chain");
    }

    @Override // soot.util.Chain
    public void insertAfter(List<T> toInsert, T point) {
        throw new RuntimeException("Cannot add elements to an unmodifiable chain");
    }

    @Override // soot.util.Chain
    public void insertAfter(T toInsert, T point) {
        throw new RuntimeException("Cannot add elements to an unmodifiable chain");
    }

    @Override // soot.util.Chain
    public void insertBefore(T toInsert, T point) {
        throw new RuntimeException("Cannot add elements to an unmodifiable chain");
    }

    @Override // soot.util.Chain
    public void insertBefore(Chain<T> toInsert, T point) {
        throw new RuntimeException("Cannot add elements to an unmodifiable chain");
    }

    @Override // soot.util.Chain
    public void insertAfter(Chain<T> toInsert, T point) {
        throw new RuntimeException("Cannot add elements to an unmodifiable chain");
    }

    @Override // soot.util.Chain
    public void swapWith(T out, T in) {
        throw new RuntimeException("Cannot replace elements in an unmodifiable chain");
    }

    @Override // soot.util.Chain, java.util.Collection
    public boolean remove(Object u) {
        throw new RuntimeException("Cannot remove elements from an unmodifiable chain");
    }

    @Override // soot.util.Chain
    public void addFirst(T u) {
        throw new RuntimeException("Cannot add elements to an unmodifiable chain");
    }

    @Override // soot.util.Chain
    public void addLast(T u) {
        throw new RuntimeException("Cannot add elements to an unmodifiable chain");
    }

    @Override // soot.util.Chain
    public void removeFirst() {
        throw new RuntimeException("Cannot remove elements from an unmodifiable chain");
    }

    @Override // soot.util.Chain
    public void removeLast() {
        throw new RuntimeException("Cannot remove elements from an unmodifiable chain");
    }

    @Override // soot.util.Chain
    public boolean follows(T someObject, T someReferenceObject) {
        return false;
    }

    @Override // soot.util.Chain
    public T getFirst() {
        throw new NoSuchElementException();
    }

    @Override // soot.util.Chain
    public T getLast() {
        throw new NoSuchElementException();
    }

    @Override // soot.util.Chain
    public T getSuccOf(T point) {
        throw new NoSuchElementException();
    }

    @Override // soot.util.Chain
    public T getPredOf(T point) {
        throw new NoSuchElementException();
    }

    @Override // soot.util.Chain
    public Iterator<T> snapshotIterator() {
        return emptyIterator();
    }

    @Override // soot.util.Chain, java.util.Collection, java.lang.Iterable
    public Iterator<T> iterator() {
        return emptyIterator();
    }

    @Override // soot.util.Chain
    public Iterator<T> iterator(T u) {
        return emptyIterator();
    }

    @Override // soot.util.Chain
    public Iterator<T> iterator(T head, T tail) {
        return emptyIterator();
    }

    @Override // soot.util.Chain, java.util.Collection
    public int size() {
        return 0;
    }

    @Override // soot.util.Chain
    public long getModificationCount() {
        return 0L;
    }

    @Override // soot.util.Chain
    public Collection<T> getElementsUnsorted() {
        return Collections.emptyList();
    }

    @Override // soot.util.Chain
    public void insertAfter(Collection<? extends T> toInsert, T point) {
        throw new RuntimeException("Cannot add elements to an unmodifiable chain");
    }

    @Override // soot.util.Chain
    public void insertBefore(Collection<? extends T> toInsert, T point) {
        throw new RuntimeException("Cannot add elements to an unmodifiable chain");
    }
}
