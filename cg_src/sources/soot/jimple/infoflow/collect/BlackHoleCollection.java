package soot.jimple.infoflow.collect;

import java.util.Collection;
import java.util.Iterator;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/collect/BlackHoleCollection.class */
public class BlackHoleCollection<E> implements Collection<E> {
    @Override // java.util.Collection
    public int size() {
        return 0;
    }

    @Override // java.util.Collection
    public boolean isEmpty() {
        return true;
    }

    @Override // java.util.Collection
    public boolean contains(Object o) {
        return false;
    }

    @Override // java.util.Collection, java.lang.Iterable
    public Iterator<E> iterator() {
        return new Iterator<E>() { // from class: soot.jimple.infoflow.collect.BlackHoleCollection.1
            @Override // java.util.Iterator
            public boolean hasNext() {
                return false;
            }

            @Override // java.util.Iterator
            public E next() {
                return null;
            }

            @Override // java.util.Iterator
            public void remove() {
            }
        };
    }

    @Override // java.util.Collection
    public Object[] toArray() {
        return new Object[0];
    }

    @Override // java.util.Collection
    public <T> T[] toArray(T[] tArr) {
        return tArr;
    }

    @Override // java.util.Collection
    public boolean add(E e) {
        return true;
    }

    @Override // java.util.Collection
    public boolean remove(Object o) {
        return false;
    }

    @Override // java.util.Collection
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override // java.util.Collection
    public boolean addAll(Collection<? extends E> c) {
        return true;
    }

    @Override // java.util.Collection
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override // java.util.Collection
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override // java.util.Collection
    public void clear() {
    }
}
