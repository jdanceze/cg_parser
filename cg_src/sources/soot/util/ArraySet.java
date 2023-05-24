package soot.util;

import java.util.AbstractSet;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
/* loaded from: gencallgraphv3.jar:soot/util/ArraySet.class */
public class ArraySet<E> extends AbstractSet<E> {
    private static final int DEFAULT_SIZE = 8;
    private int numElements;
    private int maxElements;
    private E[] elements;

    public ArraySet(int size) {
        this.maxElements = size;
        this.elements = (E[]) new Object[size];
        this.numElements = 0;
    }

    public ArraySet() {
        this(8);
    }

    public ArraySet(E[] eArr) {
        this();
        for (E element : eArr) {
            add(element);
        }
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public final void clear() {
        this.numElements = 0;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public final boolean contains(Object obj) {
        for (int i = 0; i < this.numElements; i++) {
            if (this.elements[i].equals(obj)) {
                return true;
            }
        }
        return false;
    }

    public final boolean addElement(E e) {
        if (e == null) {
            throw new RuntimeException("oops");
        }
        if (this.numElements == this.maxElements) {
            doubleCapacity();
        }
        E[] eArr = this.elements;
        int i = this.numElements;
        this.numElements = i + 1;
        eArr[i] = e;
        return true;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public final boolean add(E e) {
        if (e == null) {
            throw new RuntimeException("oops");
        }
        if (contains(e)) {
            return false;
        }
        if (this.numElements == this.maxElements) {
            doubleCapacity();
        }
        E[] eArr = this.elements;
        int i = this.numElements;
        this.numElements = i + 1;
        eArr[i] = e;
        return true;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public final boolean addAll(Collection<? extends E> s) {
        E[] eArr;
        if (s instanceof ArraySet) {
            boolean ret = false;
            ArraySet<? extends E> as = (ArraySet) s;
            for (E elem : as.elements) {
                ret |= add(elem);
            }
            return ret;
        }
        return super.addAll(s);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public final int size() {
        return this.numElements;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
    public final Iterator<E> iterator() {
        return new ArrayIterator();
    }

    /* loaded from: gencallgraphv3.jar:soot/util/ArraySet$ArrayIterator.class */
    private class ArrayIterator implements Iterator<E> {
        int nextIndex = 0;

        ArrayIterator() {
        }

        @Override // java.util.Iterator
        public final boolean hasNext() {
            return this.nextIndex < ArraySet.this.numElements;
        }

        @Override // java.util.Iterator
        public final E next() throws NoSuchElementException {
            if (this.nextIndex < ArraySet.this.numElements) {
                Object[] objArr = ArraySet.this.elements;
                int i = this.nextIndex;
                this.nextIndex = i + 1;
                return (E) objArr[i];
            }
            throw new NoSuchElementException();
        }

        @Override // java.util.Iterator
        public final void remove() throws NoSuchElementException {
            if (this.nextIndex == 0) {
                throw new NoSuchElementException();
            }
            ArraySet.this.removeElementAt(this.nextIndex - 1);
            this.nextIndex--;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeElementAt(int index) {
        if (index == this.numElements - 1) {
            this.numElements--;
            return;
        }
        System.arraycopy(this.elements, index + 1, this.elements, index, this.numElements - (index + 1));
        this.numElements--;
    }

    private void doubleCapacity() {
        int newSize = (this.maxElements * 2) + 1;
        E[] eArr = (E[]) new Object[newSize];
        System.arraycopy(this.elements, 0, eArr, 0, this.numElements);
        this.elements = eArr;
        this.maxElements = newSize;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public final E[] toArray() {
        E[] eArr = (E[]) new Object[this.numElements];
        System.arraycopy(this.elements, 0, eArr, 0, this.numElements);
        return eArr;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public final <T> T[] toArray(T[] tArr) {
        if (tArr.length < this.numElements) {
            return (T[]) Arrays.copyOf(this.elements, this.numElements, tArr.getClass());
        }
        System.arraycopy(this.elements, 0, tArr, 0, this.numElements);
        if (tArr.length > this.numElements) {
            tArr[this.numElements] = null;
        }
        return tArr;
    }

    public final Object[] getUnderlyingArray() {
        return this.elements;
    }
}
