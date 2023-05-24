package gnu.trove.impl.unmodifiable;

import gnu.trove.TLongCollection;
import gnu.trove.iterator.TLongIterator;
import gnu.trove.procedure.TLongProcedure;
import java.io.Serializable;
import java.util.Collection;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/impl/unmodifiable/TUnmodifiableLongCollection.class */
public class TUnmodifiableLongCollection implements TLongCollection, Serializable {
    private static final long serialVersionUID = 1820017752578914078L;
    final TLongCollection c;

    public TUnmodifiableLongCollection(TLongCollection c) {
        if (c == null) {
            throw new NullPointerException();
        }
        this.c = c;
    }

    @Override // gnu.trove.TLongCollection
    public int size() {
        return this.c.size();
    }

    @Override // gnu.trove.TLongCollection
    public boolean isEmpty() {
        return this.c.isEmpty();
    }

    @Override // gnu.trove.TLongCollection
    public boolean contains(long o) {
        return this.c.contains(o);
    }

    @Override // gnu.trove.TLongCollection
    public long[] toArray() {
        return this.c.toArray();
    }

    @Override // gnu.trove.TLongCollection
    public long[] toArray(long[] a) {
        return this.c.toArray(a);
    }

    public String toString() {
        return this.c.toString();
    }

    @Override // gnu.trove.TLongCollection
    public long getNoEntryValue() {
        return this.c.getNoEntryValue();
    }

    @Override // gnu.trove.TLongCollection
    public boolean forEach(TLongProcedure procedure) {
        return this.c.forEach(procedure);
    }

    @Override // gnu.trove.TLongCollection
    public TLongIterator iterator() {
        return new TLongIterator() { // from class: gnu.trove.impl.unmodifiable.TUnmodifiableLongCollection.1
            TLongIterator i;

            {
                this.i = TUnmodifiableLongCollection.this.c.iterator();
            }

            @Override // gnu.trove.iterator.TIterator, java.util.Iterator
            public boolean hasNext() {
                return this.i.hasNext();
            }

            @Override // gnu.trove.iterator.TLongIterator
            public long next() {
                return this.i.next();
            }

            @Override // gnu.trove.iterator.TIterator, java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override // gnu.trove.TLongCollection
    public boolean add(long e) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.TLongCollection
    public boolean remove(long o) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.TLongCollection
    public boolean containsAll(Collection<?> coll) {
        return this.c.containsAll(coll);
    }

    @Override // gnu.trove.TLongCollection
    public boolean containsAll(TLongCollection coll) {
        return this.c.containsAll(coll);
    }

    @Override // gnu.trove.TLongCollection
    public boolean containsAll(long[] array) {
        return this.c.containsAll(array);
    }

    @Override // gnu.trove.TLongCollection
    public boolean addAll(TLongCollection coll) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.TLongCollection
    public boolean addAll(Collection<? extends Long> coll) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.TLongCollection
    public boolean addAll(long[] array) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.TLongCollection
    public boolean removeAll(Collection<?> coll) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.TLongCollection
    public boolean removeAll(TLongCollection coll) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.TLongCollection
    public boolean removeAll(long[] array) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.TLongCollection
    public boolean retainAll(Collection<?> coll) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.TLongCollection
    public boolean retainAll(TLongCollection coll) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.TLongCollection
    public boolean retainAll(long[] array) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.TLongCollection
    public void clear() {
        throw new UnsupportedOperationException();
    }
}
