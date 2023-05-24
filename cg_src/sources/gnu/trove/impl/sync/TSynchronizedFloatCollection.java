package gnu.trove.impl.sync;

import gnu.trove.TFloatCollection;
import gnu.trove.iterator.TFloatIterator;
import gnu.trove.procedure.TFloatProcedure;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/impl/sync/TSynchronizedFloatCollection.class */
public class TSynchronizedFloatCollection implements TFloatCollection, Serializable {
    private static final long serialVersionUID = 3053995032091335093L;
    final TFloatCollection c;
    final Object mutex;

    public TSynchronizedFloatCollection(TFloatCollection c) {
        if (c == null) {
            throw new NullPointerException();
        }
        this.c = c;
        this.mutex = this;
    }

    public TSynchronizedFloatCollection(TFloatCollection c, Object mutex) {
        this.c = c;
        this.mutex = mutex;
    }

    @Override // gnu.trove.TFloatCollection
    public int size() {
        int size;
        synchronized (this.mutex) {
            size = this.c.size();
        }
        return size;
    }

    @Override // gnu.trove.TFloatCollection
    public boolean isEmpty() {
        boolean isEmpty;
        synchronized (this.mutex) {
            isEmpty = this.c.isEmpty();
        }
        return isEmpty;
    }

    @Override // gnu.trove.TFloatCollection
    public boolean contains(float o) {
        boolean contains;
        synchronized (this.mutex) {
            contains = this.c.contains(o);
        }
        return contains;
    }

    @Override // gnu.trove.TFloatCollection
    public float[] toArray() {
        float[] array;
        synchronized (this.mutex) {
            array = this.c.toArray();
        }
        return array;
    }

    @Override // gnu.trove.TFloatCollection
    public float[] toArray(float[] a) {
        float[] array;
        synchronized (this.mutex) {
            array = this.c.toArray(a);
        }
        return array;
    }

    @Override // gnu.trove.TFloatCollection
    public TFloatIterator iterator() {
        return this.c.iterator();
    }

    @Override // gnu.trove.TFloatCollection
    public boolean add(float e) {
        boolean add;
        synchronized (this.mutex) {
            add = this.c.add(e);
        }
        return add;
    }

    @Override // gnu.trove.TFloatCollection
    public boolean remove(float o) {
        boolean remove;
        synchronized (this.mutex) {
            remove = this.c.remove(o);
        }
        return remove;
    }

    @Override // gnu.trove.TFloatCollection
    public boolean containsAll(Collection<?> coll) {
        boolean containsAll;
        synchronized (this.mutex) {
            containsAll = this.c.containsAll(coll);
        }
        return containsAll;
    }

    @Override // gnu.trove.TFloatCollection
    public boolean containsAll(TFloatCollection coll) {
        boolean containsAll;
        synchronized (this.mutex) {
            containsAll = this.c.containsAll(coll);
        }
        return containsAll;
    }

    @Override // gnu.trove.TFloatCollection
    public boolean containsAll(float[] array) {
        boolean containsAll;
        synchronized (this.mutex) {
            containsAll = this.c.containsAll(array);
        }
        return containsAll;
    }

    @Override // gnu.trove.TFloatCollection
    public boolean addAll(Collection<? extends Float> coll) {
        boolean addAll;
        synchronized (this.mutex) {
            addAll = this.c.addAll(coll);
        }
        return addAll;
    }

    @Override // gnu.trove.TFloatCollection
    public boolean addAll(TFloatCollection coll) {
        boolean addAll;
        synchronized (this.mutex) {
            addAll = this.c.addAll(coll);
        }
        return addAll;
    }

    @Override // gnu.trove.TFloatCollection
    public boolean addAll(float[] array) {
        boolean addAll;
        synchronized (this.mutex) {
            addAll = this.c.addAll(array);
        }
        return addAll;
    }

    @Override // gnu.trove.TFloatCollection
    public boolean removeAll(Collection<?> coll) {
        boolean removeAll;
        synchronized (this.mutex) {
            removeAll = this.c.removeAll(coll);
        }
        return removeAll;
    }

    @Override // gnu.trove.TFloatCollection
    public boolean removeAll(TFloatCollection coll) {
        boolean removeAll;
        synchronized (this.mutex) {
            removeAll = this.c.removeAll(coll);
        }
        return removeAll;
    }

    @Override // gnu.trove.TFloatCollection
    public boolean removeAll(float[] array) {
        boolean removeAll;
        synchronized (this.mutex) {
            removeAll = this.c.removeAll(array);
        }
        return removeAll;
    }

    @Override // gnu.trove.TFloatCollection
    public boolean retainAll(Collection<?> coll) {
        boolean retainAll;
        synchronized (this.mutex) {
            retainAll = this.c.retainAll(coll);
        }
        return retainAll;
    }

    @Override // gnu.trove.TFloatCollection
    public boolean retainAll(TFloatCollection coll) {
        boolean retainAll;
        synchronized (this.mutex) {
            retainAll = this.c.retainAll(coll);
        }
        return retainAll;
    }

    @Override // gnu.trove.TFloatCollection
    public boolean retainAll(float[] array) {
        boolean retainAll;
        synchronized (this.mutex) {
            retainAll = this.c.retainAll(array);
        }
        return retainAll;
    }

    @Override // gnu.trove.TFloatCollection
    public float getNoEntryValue() {
        return this.c.getNoEntryValue();
    }

    @Override // gnu.trove.TFloatCollection
    public boolean forEach(TFloatProcedure procedure) {
        boolean forEach;
        synchronized (this.mutex) {
            forEach = this.c.forEach(procedure);
        }
        return forEach;
    }

    @Override // gnu.trove.TFloatCollection
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
