package heros;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
/* loaded from: gencallgraphv3.jar:heros-1.2.3-SNAPSHOT.jar:heros/TwoElementSet.class */
public class TwoElementSet<E> extends AbstractSet<E> {
    protected final E first;
    protected final E second;

    public TwoElementSet(E first, E second) {
        this.first = first;
        this.second = second;
    }

    public static <E> TwoElementSet<E> twoElementSet(E first, E second) {
        return new TwoElementSet<>(first, second);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
    public Iterator<E> iterator() {
        return new Iterator<E>() { // from class: heros.TwoElementSet.1
            int elementsRead = 0;

            @Override // java.util.Iterator
            public boolean hasNext() {
                return this.elementsRead < 2;
            }

            @Override // java.util.Iterator
            public E next() {
                switch (this.elementsRead) {
                    case 0:
                        this.elementsRead++;
                        return TwoElementSet.this.first;
                    case 1:
                        this.elementsRead++;
                        return TwoElementSet.this.second;
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
        return 2;
    }
}
