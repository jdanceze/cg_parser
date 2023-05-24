package gnu.trove.impl.unmodifiable;

import gnu.trove.TDoubleCollection;
import gnu.trove.iterator.TDoubleIterator;
import gnu.trove.procedure.TDoubleProcedure;
import java.io.Serializable;
import java.util.Collection;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/impl/unmodifiable/TUnmodifiableDoubleCollection.class */
public class TUnmodifiableDoubleCollection implements TDoubleCollection, Serializable {
    private static final long serialVersionUID = 1820017752578914078L;
    final TDoubleCollection c;

    public TUnmodifiableDoubleCollection(TDoubleCollection c) {
        if (c == null) {
            throw new NullPointerException();
        }
        this.c = c;
    }

    @Override // gnu.trove.TDoubleCollection
    public int size() {
        return this.c.size();
    }

    @Override // gnu.trove.TDoubleCollection
    public boolean isEmpty() {
        return this.c.isEmpty();
    }

    @Override // gnu.trove.TDoubleCollection
    public boolean contains(double o) {
        return this.c.contains(o);
    }

    @Override // gnu.trove.TDoubleCollection
    public double[] toArray() {
        return this.c.toArray();
    }

    @Override // gnu.trove.TDoubleCollection
    public double[] toArray(double[] a) {
        return this.c.toArray(a);
    }

    public String toString() {
        return this.c.toString();
    }

    @Override // gnu.trove.TDoubleCollection
    public double getNoEntryValue() {
        return this.c.getNoEntryValue();
    }

    @Override // gnu.trove.TDoubleCollection
    public boolean forEach(TDoubleProcedure procedure) {
        return this.c.forEach(procedure);
    }

    @Override // gnu.trove.TDoubleCollection
    public TDoubleIterator iterator() {
        return new TDoubleIterator() { // from class: gnu.trove.impl.unmodifiable.TUnmodifiableDoubleCollection.1
            TDoubleIterator i;

            {
                this.i = TUnmodifiableDoubleCollection.this.c.iterator();
            }

            @Override // gnu.trove.iterator.TIterator, java.util.Iterator
            public boolean hasNext() {
                return this.i.hasNext();
            }

            @Override // gnu.trove.iterator.TDoubleIterator
            public double next() {
                return this.i.next();
            }

            @Override // gnu.trove.iterator.TIterator, java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override // gnu.trove.TDoubleCollection
    public boolean add(double e) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.TDoubleCollection
    public boolean remove(double o) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.TDoubleCollection
    public boolean containsAll(Collection<?> coll) {
        return this.c.containsAll(coll);
    }

    @Override // gnu.trove.TDoubleCollection
    public boolean containsAll(TDoubleCollection coll) {
        return this.c.containsAll(coll);
    }

    @Override // gnu.trove.TDoubleCollection
    public boolean containsAll(double[] array) {
        return this.c.containsAll(array);
    }

    @Override // gnu.trove.TDoubleCollection
    public boolean addAll(TDoubleCollection coll) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.TDoubleCollection
    public boolean addAll(Collection<? extends Double> coll) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.TDoubleCollection
    public boolean addAll(double[] array) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.TDoubleCollection
    public boolean removeAll(Collection<?> coll) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.TDoubleCollection
    public boolean removeAll(TDoubleCollection coll) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.TDoubleCollection
    public boolean removeAll(double[] array) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.TDoubleCollection
    public boolean retainAll(Collection<?> coll) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.TDoubleCollection
    public boolean retainAll(TDoubleCollection coll) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.TDoubleCollection
    public boolean retainAll(double[] array) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.TDoubleCollection
    public void clear() {
        throw new UnsupportedOperationException();
    }
}
