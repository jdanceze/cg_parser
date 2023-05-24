package gnu.trove.impl.unmodifiable;

import gnu.trove.TIntCollection;
import gnu.trove.iterator.TIntIterator;
import gnu.trove.procedure.TIntProcedure;
import java.io.Serializable;
import java.util.Collection;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/impl/unmodifiable/TUnmodifiableIntCollection.class */
public class TUnmodifiableIntCollection implements TIntCollection, Serializable {
    private static final long serialVersionUID = 1820017752578914078L;
    final TIntCollection c;

    public TUnmodifiableIntCollection(TIntCollection c) {
        if (c == null) {
            throw new NullPointerException();
        }
        this.c = c;
    }

    @Override // gnu.trove.TIntCollection
    public int size() {
        return this.c.size();
    }

    @Override // gnu.trove.TIntCollection
    public boolean isEmpty() {
        return this.c.isEmpty();
    }

    @Override // gnu.trove.TIntCollection
    public boolean contains(int o) {
        return this.c.contains(o);
    }

    @Override // gnu.trove.TIntCollection
    public int[] toArray() {
        return this.c.toArray();
    }

    @Override // gnu.trove.TIntCollection
    public int[] toArray(int[] a) {
        return this.c.toArray(a);
    }

    public String toString() {
        return this.c.toString();
    }

    @Override // gnu.trove.TIntCollection
    public int getNoEntryValue() {
        return this.c.getNoEntryValue();
    }

    @Override // gnu.trove.TIntCollection
    public boolean forEach(TIntProcedure procedure) {
        return this.c.forEach(procedure);
    }

    @Override // gnu.trove.TIntCollection
    public TIntIterator iterator() {
        return new TIntIterator() { // from class: gnu.trove.impl.unmodifiable.TUnmodifiableIntCollection.1
            TIntIterator i;

            {
                this.i = TUnmodifiableIntCollection.this.c.iterator();
            }

            @Override // gnu.trove.iterator.TIterator, java.util.Iterator
            public boolean hasNext() {
                return this.i.hasNext();
            }

            @Override // gnu.trove.iterator.TIntIterator
            public int next() {
                return this.i.next();
            }

            @Override // gnu.trove.iterator.TIterator, java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override // gnu.trove.TIntCollection
    public boolean add(int e) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.TIntCollection
    public boolean remove(int o) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.TIntCollection
    public boolean containsAll(Collection<?> coll) {
        return this.c.containsAll(coll);
    }

    @Override // gnu.trove.TIntCollection
    public boolean containsAll(TIntCollection coll) {
        return this.c.containsAll(coll);
    }

    @Override // gnu.trove.TIntCollection
    public boolean containsAll(int[] array) {
        return this.c.containsAll(array);
    }

    @Override // gnu.trove.TIntCollection
    public boolean addAll(TIntCollection coll) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.TIntCollection
    public boolean addAll(Collection<? extends Integer> coll) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.TIntCollection
    public boolean addAll(int[] array) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.TIntCollection
    public boolean removeAll(Collection<?> coll) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.TIntCollection
    public boolean removeAll(TIntCollection coll) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.TIntCollection
    public boolean removeAll(int[] array) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.TIntCollection
    public boolean retainAll(Collection<?> coll) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.TIntCollection
    public boolean retainAll(TIntCollection coll) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.TIntCollection
    public boolean retainAll(int[] array) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.TIntCollection
    public void clear() {
        throw new UnsupportedOperationException();
    }
}
