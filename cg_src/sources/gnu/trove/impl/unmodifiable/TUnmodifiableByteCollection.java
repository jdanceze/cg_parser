package gnu.trove.impl.unmodifiable;

import gnu.trove.TByteCollection;
import gnu.trove.iterator.TByteIterator;
import gnu.trove.procedure.TByteProcedure;
import java.io.Serializable;
import java.util.Collection;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/impl/unmodifiable/TUnmodifiableByteCollection.class */
public class TUnmodifiableByteCollection implements TByteCollection, Serializable {
    private static final long serialVersionUID = 1820017752578914078L;
    final TByteCollection c;

    public TUnmodifiableByteCollection(TByteCollection c) {
        if (c == null) {
            throw new NullPointerException();
        }
        this.c = c;
    }

    @Override // gnu.trove.TByteCollection
    public int size() {
        return this.c.size();
    }

    @Override // gnu.trove.TByteCollection
    public boolean isEmpty() {
        return this.c.isEmpty();
    }

    @Override // gnu.trove.TByteCollection
    public boolean contains(byte o) {
        return this.c.contains(o);
    }

    @Override // gnu.trove.TByteCollection
    public byte[] toArray() {
        return this.c.toArray();
    }

    @Override // gnu.trove.TByteCollection
    public byte[] toArray(byte[] a) {
        return this.c.toArray(a);
    }

    public String toString() {
        return this.c.toString();
    }

    @Override // gnu.trove.TByteCollection
    public byte getNoEntryValue() {
        return this.c.getNoEntryValue();
    }

    @Override // gnu.trove.TByteCollection
    public boolean forEach(TByteProcedure procedure) {
        return this.c.forEach(procedure);
    }

    @Override // gnu.trove.TByteCollection
    public TByteIterator iterator() {
        return new TByteIterator() { // from class: gnu.trove.impl.unmodifiable.TUnmodifiableByteCollection.1
            TByteIterator i;

            {
                this.i = TUnmodifiableByteCollection.this.c.iterator();
            }

            @Override // gnu.trove.iterator.TIterator, java.util.Iterator
            public boolean hasNext() {
                return this.i.hasNext();
            }

            @Override // gnu.trove.iterator.TByteIterator
            public byte next() {
                return this.i.next();
            }

            @Override // gnu.trove.iterator.TIterator, java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override // gnu.trove.TByteCollection
    public boolean add(byte e) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.TByteCollection
    public boolean remove(byte o) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.TByteCollection
    public boolean containsAll(Collection<?> coll) {
        return this.c.containsAll(coll);
    }

    @Override // gnu.trove.TByteCollection
    public boolean containsAll(TByteCollection coll) {
        return this.c.containsAll(coll);
    }

    @Override // gnu.trove.TByteCollection
    public boolean containsAll(byte[] array) {
        return this.c.containsAll(array);
    }

    @Override // gnu.trove.TByteCollection
    public boolean addAll(TByteCollection coll) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.TByteCollection
    public boolean addAll(Collection<? extends Byte> coll) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.TByteCollection
    public boolean addAll(byte[] array) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.TByteCollection
    public boolean removeAll(Collection<?> coll) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.TByteCollection
    public boolean removeAll(TByteCollection coll) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.TByteCollection
    public boolean removeAll(byte[] array) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.TByteCollection
    public boolean retainAll(Collection<?> coll) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.TByteCollection
    public boolean retainAll(TByteCollection coll) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.TByteCollection
    public boolean retainAll(byte[] array) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.TByteCollection
    public void clear() {
        throw new UnsupportedOperationException();
    }
}
