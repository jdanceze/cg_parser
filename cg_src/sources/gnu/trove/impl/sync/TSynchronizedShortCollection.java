package gnu.trove.impl.sync;

import gnu.trove.TShortCollection;
import gnu.trove.iterator.TShortIterator;
import gnu.trove.procedure.TShortProcedure;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/impl/sync/TSynchronizedShortCollection.class */
public class TSynchronizedShortCollection implements TShortCollection, Serializable {
    private static final long serialVersionUID = 3053995032091335093L;
    final TShortCollection c;
    final Object mutex;

    public TSynchronizedShortCollection(TShortCollection c) {
        if (c == null) {
            throw new NullPointerException();
        }
        this.c = c;
        this.mutex = this;
    }

    public TSynchronizedShortCollection(TShortCollection c, Object mutex) {
        this.c = c;
        this.mutex = mutex;
    }

    @Override // gnu.trove.TShortCollection
    public int size() {
        int size;
        synchronized (this.mutex) {
            size = this.c.size();
        }
        return size;
    }

    @Override // gnu.trove.TShortCollection
    public boolean isEmpty() {
        boolean isEmpty;
        synchronized (this.mutex) {
            isEmpty = this.c.isEmpty();
        }
        return isEmpty;
    }

    @Override // gnu.trove.TShortCollection
    public boolean contains(short o) {
        boolean contains;
        synchronized (this.mutex) {
            contains = this.c.contains(o);
        }
        return contains;
    }

    @Override // gnu.trove.TShortCollection
    public short[] toArray() {
        short[] array;
        synchronized (this.mutex) {
            array = this.c.toArray();
        }
        return array;
    }

    @Override // gnu.trove.TShortCollection
    public short[] toArray(short[] a) {
        short[] array;
        synchronized (this.mutex) {
            array = this.c.toArray(a);
        }
        return array;
    }

    @Override // gnu.trove.TShortCollection
    public TShortIterator iterator() {
        return this.c.iterator();
    }

    @Override // gnu.trove.TShortCollection
    public boolean add(short e) {
        boolean add;
        synchronized (this.mutex) {
            add = this.c.add(e);
        }
        return add;
    }

    @Override // gnu.trove.TShortCollection
    public boolean remove(short o) {
        boolean remove;
        synchronized (this.mutex) {
            remove = this.c.remove(o);
        }
        return remove;
    }

    @Override // gnu.trove.TShortCollection
    public boolean containsAll(Collection<?> coll) {
        boolean containsAll;
        synchronized (this.mutex) {
            containsAll = this.c.containsAll(coll);
        }
        return containsAll;
    }

    @Override // gnu.trove.TShortCollection
    public boolean containsAll(TShortCollection coll) {
        boolean containsAll;
        synchronized (this.mutex) {
            containsAll = this.c.containsAll(coll);
        }
        return containsAll;
    }

    @Override // gnu.trove.TShortCollection
    public boolean containsAll(short[] array) {
        boolean containsAll;
        synchronized (this.mutex) {
            containsAll = this.c.containsAll(array);
        }
        return containsAll;
    }

    @Override // gnu.trove.TShortCollection
    public boolean addAll(Collection<? extends Short> coll) {
        boolean addAll;
        synchronized (this.mutex) {
            addAll = this.c.addAll(coll);
        }
        return addAll;
    }

    @Override // gnu.trove.TShortCollection
    public boolean addAll(TShortCollection coll) {
        boolean addAll;
        synchronized (this.mutex) {
            addAll = this.c.addAll(coll);
        }
        return addAll;
    }

    @Override // gnu.trove.TShortCollection
    public boolean addAll(short[] array) {
        boolean addAll;
        synchronized (this.mutex) {
            addAll = this.c.addAll(array);
        }
        return addAll;
    }

    @Override // gnu.trove.TShortCollection
    public boolean removeAll(Collection<?> coll) {
        boolean removeAll;
        synchronized (this.mutex) {
            removeAll = this.c.removeAll(coll);
        }
        return removeAll;
    }

    @Override // gnu.trove.TShortCollection
    public boolean removeAll(TShortCollection coll) {
        boolean removeAll;
        synchronized (this.mutex) {
            removeAll = this.c.removeAll(coll);
        }
        return removeAll;
    }

    @Override // gnu.trove.TShortCollection
    public boolean removeAll(short[] array) {
        boolean removeAll;
        synchronized (this.mutex) {
            removeAll = this.c.removeAll(array);
        }
        return removeAll;
    }

    @Override // gnu.trove.TShortCollection
    public boolean retainAll(Collection<?> coll) {
        boolean retainAll;
        synchronized (this.mutex) {
            retainAll = this.c.retainAll(coll);
        }
        return retainAll;
    }

    @Override // gnu.trove.TShortCollection
    public boolean retainAll(TShortCollection coll) {
        boolean retainAll;
        synchronized (this.mutex) {
            retainAll = this.c.retainAll(coll);
        }
        return retainAll;
    }

    @Override // gnu.trove.TShortCollection
    public boolean retainAll(short[] array) {
        boolean retainAll;
        synchronized (this.mutex) {
            retainAll = this.c.retainAll(array);
        }
        return retainAll;
    }

    @Override // gnu.trove.TShortCollection
    public short getNoEntryValue() {
        return this.c.getNoEntryValue();
    }

    @Override // gnu.trove.TShortCollection
    public boolean forEach(TShortProcedure procedure) {
        boolean forEach;
        synchronized (this.mutex) {
            forEach = this.c.forEach(procedure);
        }
        return forEach;
    }

    @Override // gnu.trove.TShortCollection
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
