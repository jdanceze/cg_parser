package soot.util;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
/* compiled from: DeterministicHashMap.java */
/* loaded from: gencallgraphv3.jar:soot/util/TrustingMonotonicArraySet.class */
class TrustingMonotonicArraySet<T> extends AbstractSet<T> {
    private static final int DEFAULT_SIZE = 8;
    private int numElements;
    private int maxElements;
    private T[] elements;

    public TrustingMonotonicArraySet() {
        this.maxElements = 8;
        this.elements = (T[]) new Object[8];
        this.numElements = 0;
    }

    public TrustingMonotonicArraySet(T[] tArr) {
        this();
        for (T element : tArr) {
            add(element);
        }
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public void clear() {
        this.numElements = 0;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean contains(Object obj) {
        for (int i = 0; i < this.numElements; i++) {
            if (this.elements[i].equals(obj)) {
                return true;
            }
        }
        return false;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean add(T e) {
        if (this.numElements == this.maxElements) {
            doubleCapacity();
        }
        T[] tArr = this.elements;
        int i = this.numElements;
        this.numElements = i + 1;
        tArr[i] = e;
        return true;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public int size() {
        return this.numElements;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
    public Iterator<T> iterator() {
        return new ArrayIterator();
    }

    /* compiled from: DeterministicHashMap.java */
    /* loaded from: gencallgraphv3.jar:soot/util/TrustingMonotonicArraySet$ArrayIterator.class */
    private class ArrayIterator implements Iterator<T> {
        private int nextIndex = 0;

        ArrayIterator() {
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.nextIndex < TrustingMonotonicArraySet.this.numElements;
        }

        @Override // java.util.Iterator
        public T next() throws NoSuchElementException {
            if (this.nextIndex < TrustingMonotonicArraySet.this.numElements) {
                Object[] objArr = TrustingMonotonicArraySet.this.elements;
                int i = this.nextIndex;
                this.nextIndex = i + 1;
                return (T) objArr[i];
            }
            throw new NoSuchElementException();
        }

        @Override // java.util.Iterator
        public void remove() throws NoSuchElementException {
            if (this.nextIndex == 0) {
                throw new NoSuchElementException();
            }
            TrustingMonotonicArraySet.this.removeElementAt(this.nextIndex - 1);
            this.nextIndex--;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeElementAt(int index) {
        throw new UnsupportedOperationException();
    }

    private void doubleCapacity() {
        int newSize = this.maxElements * 2;
        T[] tArr = (T[]) new Object[newSize];
        System.arraycopy(this.elements, 0, tArr, 0, this.numElements);
        this.elements = tArr;
        this.maxElements = newSize;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public T[] toArray() {
        T[] tArr = (T[]) new Object[this.numElements];
        System.arraycopy(this.elements, 0, tArr, 0, this.numElements);
        return tArr;
    }
}
