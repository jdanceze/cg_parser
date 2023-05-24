package gnu.trove.impl.unmodifiable;

import gnu.trove.TCharCollection;
import gnu.trove.iterator.TCharIterator;
import gnu.trove.procedure.TCharProcedure;
import java.io.Serializable;
import java.util.Collection;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/impl/unmodifiable/TUnmodifiableCharCollection.class */
public class TUnmodifiableCharCollection implements TCharCollection, Serializable {
    private static final long serialVersionUID = 1820017752578914078L;
    final TCharCollection c;

    public TUnmodifiableCharCollection(TCharCollection c) {
        if (c == null) {
            throw new NullPointerException();
        }
        this.c = c;
    }

    @Override // gnu.trove.TCharCollection
    public int size() {
        return this.c.size();
    }

    @Override // gnu.trove.TCharCollection
    public boolean isEmpty() {
        return this.c.isEmpty();
    }

    @Override // gnu.trove.TCharCollection
    public boolean contains(char o) {
        return this.c.contains(o);
    }

    @Override // gnu.trove.TCharCollection
    public char[] toArray() {
        return this.c.toArray();
    }

    @Override // gnu.trove.TCharCollection
    public char[] toArray(char[] a) {
        return this.c.toArray(a);
    }

    public String toString() {
        return this.c.toString();
    }

    @Override // gnu.trove.TCharCollection
    public char getNoEntryValue() {
        return this.c.getNoEntryValue();
    }

    @Override // gnu.trove.TCharCollection
    public boolean forEach(TCharProcedure procedure) {
        return this.c.forEach(procedure);
    }

    @Override // gnu.trove.TCharCollection
    public TCharIterator iterator() {
        return new TCharIterator() { // from class: gnu.trove.impl.unmodifiable.TUnmodifiableCharCollection.1
            TCharIterator i;

            {
                this.i = TUnmodifiableCharCollection.this.c.iterator();
            }

            @Override // gnu.trove.iterator.TIterator, java.util.Iterator
            public boolean hasNext() {
                return this.i.hasNext();
            }

            @Override // gnu.trove.iterator.TCharIterator
            public char next() {
                return this.i.next();
            }

            @Override // gnu.trove.iterator.TIterator, java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override // gnu.trove.TCharCollection
    public boolean add(char e) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.TCharCollection
    public boolean remove(char o) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.TCharCollection
    public boolean containsAll(Collection<?> coll) {
        return this.c.containsAll(coll);
    }

    @Override // gnu.trove.TCharCollection
    public boolean containsAll(TCharCollection coll) {
        return this.c.containsAll(coll);
    }

    @Override // gnu.trove.TCharCollection
    public boolean containsAll(char[] array) {
        return this.c.containsAll(array);
    }

    @Override // gnu.trove.TCharCollection
    public boolean addAll(TCharCollection coll) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.TCharCollection
    public boolean addAll(Collection<? extends Character> coll) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.TCharCollection
    public boolean addAll(char[] array) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.TCharCollection
    public boolean removeAll(Collection<?> coll) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.TCharCollection
    public boolean removeAll(TCharCollection coll) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.TCharCollection
    public boolean removeAll(char[] array) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.TCharCollection
    public boolean retainAll(Collection<?> coll) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.TCharCollection
    public boolean retainAll(TCharCollection coll) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.TCharCollection
    public boolean retainAll(char[] array) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.TCharCollection
    public void clear() {
        throw new UnsupportedOperationException();
    }
}
