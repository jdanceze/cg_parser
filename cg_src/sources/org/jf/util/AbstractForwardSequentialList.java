package org.jf.util;

import java.util.AbstractSequentialList;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/util/AbstractForwardSequentialList.class */
public abstract class AbstractForwardSequentialList<T> extends AbstractSequentialList<T> {
    @Override // java.util.AbstractSequentialList, java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.List
    @Nonnull
    public abstract Iterator<T> iterator();

    /* JADX INFO: Access modifiers changed from: private */
    @Nonnull
    public Iterator<T> iterator(int index) {
        if (index < 0) {
            throw new NoSuchElementException();
        }
        Iterator<T> it = iterator();
        for (int i = 0; i < index; i++) {
            it.next();
        }
        return it;
    }

    @Override // java.util.AbstractSequentialList, java.util.AbstractList, java.util.List
    @Nonnull
    public ListIterator<T> listIterator(final int initialIndex) {
        try {
            final Iterator<T> initialIterator = iterator(initialIndex);
            return new AbstractListIterator<T>() { // from class: org.jf.util.AbstractForwardSequentialList.1
                private int index;
                @Nullable
                private Iterator<T> forwardIterator;

                {
                    this.index = initialIndex - 1;
                    this.forwardIterator = initialIterator;
                }

                @Nonnull
                private Iterator<T> getForwardIterator() {
                    if (this.forwardIterator == null) {
                        try {
                            this.forwardIterator = AbstractForwardSequentialList.this.iterator(this.index + 1);
                        } catch (IndexOutOfBoundsException e) {
                            throw new NoSuchElementException();
                        }
                    }
                    return this.forwardIterator;
                }

                @Override // org.jf.util.AbstractListIterator, java.util.ListIterator, java.util.Iterator
                public boolean hasNext() {
                    return getForwardIterator().hasNext();
                }

                @Override // org.jf.util.AbstractListIterator, java.util.ListIterator
                public boolean hasPrevious() {
                    return this.index >= 0;
                }

                @Override // org.jf.util.AbstractListIterator, java.util.ListIterator, java.util.Iterator
                public T next() {
                    T ret = getForwardIterator().next();
                    this.index++;
                    return ret;
                }

                @Override // org.jf.util.AbstractListIterator, java.util.ListIterator
                public int nextIndex() {
                    return this.index + 1;
                }

                @Override // org.jf.util.AbstractListIterator, java.util.ListIterator
                public T previous() {
                    this.forwardIterator = null;
                    try {
                        AbstractForwardSequentialList abstractForwardSequentialList = AbstractForwardSequentialList.this;
                        int i = this.index;
                        this.index = i - 1;
                        return (T) abstractForwardSequentialList.iterator(i).next();
                    } catch (IndexOutOfBoundsException e) {
                        throw new NoSuchElementException();
                    }
                }

                @Override // org.jf.util.AbstractListIterator, java.util.ListIterator
                public int previousIndex() {
                    return this.index;
                }
            };
        } catch (NoSuchElementException e) {
            throw new IndexOutOfBoundsException();
        }
    }

    @Override // java.util.AbstractList, java.util.List
    @Nonnull
    public ListIterator<T> listIterator() {
        return listIterator(0);
    }
}
