package org.jf.util;

import java.util.ListIterator;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/util/AbstractListIterator.class */
public abstract class AbstractListIterator<T> implements ListIterator<T> {
    @Override // java.util.ListIterator, java.util.Iterator
    public boolean hasNext() {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.ListIterator, java.util.Iterator
    public T next() {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.ListIterator
    public boolean hasPrevious() {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.ListIterator
    public T previous() {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.ListIterator
    public int nextIndex() {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.ListIterator
    public int previousIndex() {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.ListIterator, java.util.Iterator
    public void remove() {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.ListIterator
    public void set(T t) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.ListIterator
    public void add(T t) {
        throw new UnsupportedOperationException();
    }
}
