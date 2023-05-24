package gnu.trove.impl.unmodifiable;

import gnu.trove.TFloatCollection;
import gnu.trove.iterator.TFloatIterator;
import gnu.trove.procedure.TFloatProcedure;
import java.io.Serializable;
import java.util.Collection;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/impl/unmodifiable/TUnmodifiableFloatCollection.class */
public class TUnmodifiableFloatCollection implements TFloatCollection, Serializable {
    private static final long serialVersionUID = 1820017752578914078L;
    final TFloatCollection c;

    public TUnmodifiableFloatCollection(TFloatCollection c) {
        if (c == null) {
            throw new NullPointerException();
        }
        this.c = c;
    }

    @Override // gnu.trove.TFloatCollection
    public int size() {
        return this.c.size();
    }

    @Override // gnu.trove.TFloatCollection
    public boolean isEmpty() {
        return this.c.isEmpty();
    }

    @Override // gnu.trove.TFloatCollection
    public boolean contains(float o) {
        return this.c.contains(o);
    }

    @Override // gnu.trove.TFloatCollection
    public float[] toArray() {
        return this.c.toArray();
    }

    @Override // gnu.trove.TFloatCollection
    public float[] toArray(float[] a) {
        return this.c.toArray(a);
    }

    public String toString() {
        return this.c.toString();
    }

    @Override // gnu.trove.TFloatCollection
    public float getNoEntryValue() {
        return this.c.getNoEntryValue();
    }

    @Override // gnu.trove.TFloatCollection
    public boolean forEach(TFloatProcedure procedure) {
        return this.c.forEach(procedure);
    }

    @Override // gnu.trove.TFloatCollection
    public TFloatIterator iterator() {
        return new TFloatIterator() { // from class: gnu.trove.impl.unmodifiable.TUnmodifiableFloatCollection.1
            TFloatIterator i;

            {
                this.i = TUnmodifiableFloatCollection.this.c.iterator();
            }

            @Override // gnu.trove.iterator.TIterator, java.util.Iterator
            public boolean hasNext() {
                return this.i.hasNext();
            }

            @Override // gnu.trove.iterator.TFloatIterator
            public float next() {
                return this.i.next();
            }

            @Override // gnu.trove.iterator.TIterator, java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override // gnu.trove.TFloatCollection
    public boolean add(float e) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.TFloatCollection
    public boolean remove(float o) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.TFloatCollection
    public boolean containsAll(Collection<?> coll) {
        return this.c.containsAll(coll);
    }

    @Override // gnu.trove.TFloatCollection
    public boolean containsAll(TFloatCollection coll) {
        return this.c.containsAll(coll);
    }

    @Override // gnu.trove.TFloatCollection
    public boolean containsAll(float[] array) {
        return this.c.containsAll(array);
    }

    @Override // gnu.trove.TFloatCollection
    public boolean addAll(TFloatCollection coll) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.TFloatCollection
    public boolean addAll(Collection<? extends Float> coll) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.TFloatCollection
    public boolean addAll(float[] array) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.TFloatCollection
    public boolean removeAll(Collection<?> coll) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.TFloatCollection
    public boolean removeAll(TFloatCollection coll) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.TFloatCollection
    public boolean removeAll(float[] array) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.TFloatCollection
    public boolean retainAll(Collection<?> coll) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.TFloatCollection
    public boolean retainAll(TFloatCollection coll) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.TFloatCollection
    public boolean retainAll(float[] array) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.TFloatCollection
    public void clear() {
        throw new UnsupportedOperationException();
    }
}
