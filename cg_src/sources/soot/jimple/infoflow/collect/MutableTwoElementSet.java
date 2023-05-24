package soot.jimple.infoflow.collect;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/collect/MutableTwoElementSet.class */
public class MutableTwoElementSet<E> extends AbstractSet<E> {
    protected E first;
    protected E second;

    public MutableTwoElementSet() {
        this.first = null;
        this.second = null;
    }

    public MutableTwoElementSet(E first, E second) {
        this.first = first;
        this.second = second;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean add(E item) {
        if (this.first == null) {
            this.first = item;
            return true;
        } else if (this.first != null && this.first.equals(item)) {
            return false;
        } else {
            if (this.second == null) {
                this.second = item;
                return true;
            } else if (this.second != null && this.second.equals(item)) {
                return false;
            } else {
                throw new RuntimeException("Capacity exceeded");
            }
        }
    }

    public static <E> MutableTwoElementSet<E> twoElementSet(E first, E second) {
        return new MutableTwoElementSet<>(first, second);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
    public Iterator<E> iterator() {
        return new Iterator<E>() { // from class: soot.jimple.infoflow.collect.MutableTwoElementSet.1
            int size = -1;
            int elementsRead = 0;

            @Override // java.util.Iterator
            public boolean hasNext() {
                if (this.size < 0) {
                    this.size = MutableTwoElementSet.this.size();
                }
                return this.elementsRead < this.size;
            }

            @Override // java.util.Iterator
            public E next() {
                switch (this.elementsRead) {
                    case 0:
                        this.elementsRead++;
                        return MutableTwoElementSet.this.first;
                    case 1:
                        this.elementsRead++;
                        return MutableTwoElementSet.this.second;
                    default:
                        throw new NoSuchElementException();
                }
            }

            @Override // java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public int size() {
        int size = 0;
        if (this.first != null) {
            size = 0 + 1;
        }
        if (this.second != null) {
            size++;
        }
        return size;
    }
}
