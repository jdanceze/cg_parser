package gnu.trove.impl.unmodifiable;

import gnu.trove.TShortCollection;
import gnu.trove.iterator.TShortIterator;
import gnu.trove.procedure.TShortProcedure;
import java.io.Serializable;
import java.util.Collection;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/impl/unmodifiable/TUnmodifiableShortCollection.class */
public class TUnmodifiableShortCollection implements TShortCollection, Serializable {
    private static final long serialVersionUID = 1820017752578914078L;
    final TShortCollection c;

    public TUnmodifiableShortCollection(TShortCollection c) {
        if (c == null) {
            throw new NullPointerException();
        }
        this.c = c;
    }

    @Override // gnu.trove.TShortCollection
    public int size() {
        return this.c.size();
    }

    @Override // gnu.trove.TShortCollection
    public boolean isEmpty() {
        return this.c.isEmpty();
    }

    @Override // gnu.trove.TShortCollection
    public boolean contains(short o) {
        return this.c.contains(o);
    }

    @Override // gnu.trove.TShortCollection
    public short[] toArray() {
        return this.c.toArray();
    }

    @Override // gnu.trove.TShortCollection
    public short[] toArray(short[] a) {
        return this.c.toArray(a);
    }

    public String toString() {
        return this.c.toString();
    }

    @Override // gnu.trove.TShortCollection
    public short getNoEntryValue() {
        return this.c.getNoEntryValue();
    }

    @Override // gnu.trove.TShortCollection
    public boolean forEach(TShortProcedure procedure) {
        return this.c.forEach(procedure);
    }

    @Override // gnu.trove.TShortCollection
    public TShortIterator iterator() {
        return new TShortIterator() { // from class: gnu.trove.impl.unmodifiable.TUnmodifiableShortCollection.1
            TShortIterator i;

            {
                this.i = TUnmodifiableShortCollection.this.c.iterator();
            }

            @Override // gnu.trove.iterator.TIterator, java.util.Iterator
            public boolean hasNext() {
                return this.i.hasNext();
            }

            @Override // gnu.trove.iterator.TShortIterator
            public short next() {
                return this.i.next();
            }

            @Override // gnu.trove.iterator.TIterator, java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override // gnu.trove.TShortCollection
    public boolean add(short e) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.TShortCollection
    public boolean remove(short o) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.TShortCollection
    public boolean containsAll(Collection<?> coll) {
        return this.c.containsAll(coll);
    }

    @Override // gnu.trove.TShortCollection
    public boolean containsAll(TShortCollection coll) {
        return this.c.containsAll(coll);
    }

    @Override // gnu.trove.TShortCollection
    public boolean containsAll(short[] array) {
        return this.c.containsAll(array);
    }

    @Override // gnu.trove.TShortCollection
    public boolean addAll(TShortCollection coll) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.TShortCollection
    public boolean addAll(Collection<? extends Short> coll) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.TShortCollection
    public boolean addAll(short[] array) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.TShortCollection
    public boolean removeAll(Collection<?> coll) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.TShortCollection
    public boolean removeAll(TShortCollection coll) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.TShortCollection
    public boolean removeAll(short[] array) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.TShortCollection
    public boolean retainAll(Collection<?> coll) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.TShortCollection
    public boolean retainAll(TShortCollection coll) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.TShortCollection
    public boolean retainAll(short[] array) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.TShortCollection
    public void clear() {
        throw new UnsupportedOperationException();
    }
}
