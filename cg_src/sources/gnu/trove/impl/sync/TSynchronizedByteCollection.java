package gnu.trove.impl.sync;

import gnu.trove.TByteCollection;
import gnu.trove.iterator.TByteIterator;
import gnu.trove.procedure.TByteProcedure;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/impl/sync/TSynchronizedByteCollection.class */
public class TSynchronizedByteCollection implements TByteCollection, Serializable {
    private static final long serialVersionUID = 3053995032091335093L;
    final TByteCollection c;
    final Object mutex;

    public TSynchronizedByteCollection(TByteCollection c) {
        if (c == null) {
            throw new NullPointerException();
        }
        this.c = c;
        this.mutex = this;
    }

    public TSynchronizedByteCollection(TByteCollection c, Object mutex) {
        this.c = c;
        this.mutex = mutex;
    }

    @Override // gnu.trove.TByteCollection
    public int size() {
        int size;
        synchronized (this.mutex) {
            size = this.c.size();
        }
        return size;
    }

    @Override // gnu.trove.TByteCollection
    public boolean isEmpty() {
        boolean isEmpty;
        synchronized (this.mutex) {
            isEmpty = this.c.isEmpty();
        }
        return isEmpty;
    }

    @Override // gnu.trove.TByteCollection
    public boolean contains(byte o) {
        boolean contains;
        synchronized (this.mutex) {
            contains = this.c.contains(o);
        }
        return contains;
    }

    @Override // gnu.trove.TByteCollection
    public byte[] toArray() {
        byte[] array;
        synchronized (this.mutex) {
            array = this.c.toArray();
        }
        return array;
    }

    @Override // gnu.trove.TByteCollection
    public byte[] toArray(byte[] a) {
        byte[] array;
        synchronized (this.mutex) {
            array = this.c.toArray(a);
        }
        return array;
    }

    @Override // gnu.trove.TByteCollection
    public TByteIterator iterator() {
        return this.c.iterator();
    }

    @Override // gnu.trove.TByteCollection
    public boolean add(byte e) {
        boolean add;
        synchronized (this.mutex) {
            add = this.c.add(e);
        }
        return add;
    }

    @Override // gnu.trove.TByteCollection
    public boolean remove(byte o) {
        boolean remove;
        synchronized (this.mutex) {
            remove = this.c.remove(o);
        }
        return remove;
    }

    @Override // gnu.trove.TByteCollection
    public boolean containsAll(Collection<?> coll) {
        boolean containsAll;
        synchronized (this.mutex) {
            containsAll = this.c.containsAll(coll);
        }
        return containsAll;
    }

    @Override // gnu.trove.TByteCollection
    public boolean containsAll(TByteCollection coll) {
        boolean containsAll;
        synchronized (this.mutex) {
            containsAll = this.c.containsAll(coll);
        }
        return containsAll;
    }

    @Override // gnu.trove.TByteCollection
    public boolean containsAll(byte[] array) {
        boolean containsAll;
        synchronized (this.mutex) {
            containsAll = this.c.containsAll(array);
        }
        return containsAll;
    }

    @Override // gnu.trove.TByteCollection
    public boolean addAll(Collection<? extends Byte> coll) {
        boolean addAll;
        synchronized (this.mutex) {
            addAll = this.c.addAll(coll);
        }
        return addAll;
    }

    @Override // gnu.trove.TByteCollection
    public boolean addAll(TByteCollection coll) {
        boolean addAll;
        synchronized (this.mutex) {
            addAll = this.c.addAll(coll);
        }
        return addAll;
    }

    @Override // gnu.trove.TByteCollection
    public boolean addAll(byte[] array) {
        boolean addAll;
        synchronized (this.mutex) {
            addAll = this.c.addAll(array);
        }
        return addAll;
    }

    @Override // gnu.trove.TByteCollection
    public boolean removeAll(Collection<?> coll) {
        boolean removeAll;
        synchronized (this.mutex) {
            removeAll = this.c.removeAll(coll);
        }
        return removeAll;
    }

    @Override // gnu.trove.TByteCollection
    public boolean removeAll(TByteCollection coll) {
        boolean removeAll;
        synchronized (this.mutex) {
            removeAll = this.c.removeAll(coll);
        }
        return removeAll;
    }

    @Override // gnu.trove.TByteCollection
    public boolean removeAll(byte[] array) {
        boolean removeAll;
        synchronized (this.mutex) {
            removeAll = this.c.removeAll(array);
        }
        return removeAll;
    }

    @Override // gnu.trove.TByteCollection
    public boolean retainAll(Collection<?> coll) {
        boolean retainAll;
        synchronized (this.mutex) {
            retainAll = this.c.retainAll(coll);
        }
        return retainAll;
    }

    @Override // gnu.trove.TByteCollection
    public boolean retainAll(TByteCollection coll) {
        boolean retainAll;
        synchronized (this.mutex) {
            retainAll = this.c.retainAll(coll);
        }
        return retainAll;
    }

    @Override // gnu.trove.TByteCollection
    public boolean retainAll(byte[] array) {
        boolean retainAll;
        synchronized (this.mutex) {
            retainAll = this.c.retainAll(array);
        }
        return retainAll;
    }

    @Override // gnu.trove.TByteCollection
    public byte getNoEntryValue() {
        return this.c.getNoEntryValue();
    }

    @Override // gnu.trove.TByteCollection
    public boolean forEach(TByteProcedure procedure) {
        boolean forEach;
        synchronized (this.mutex) {
            forEach = this.c.forEach(procedure);
        }
        return forEach;
    }

    @Override // gnu.trove.TByteCollection
    public void clear() {
        synchronized (this.mutex) {
            this.c.clear();
        }
    }

    public String toString() {
        String obj;
        synchronized (this.mutex) {
            obj = this.c.toString();
        }
        return obj;
    }

    private void writeObject(ObjectOutputStream s) throws IOException {
        synchronized (this.mutex) {
            s.defaultWriteObject();
        }
    }
}
