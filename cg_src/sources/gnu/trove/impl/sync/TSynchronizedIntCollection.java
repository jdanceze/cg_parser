package gnu.trove.impl.sync;

import gnu.trove.TIntCollection;
import gnu.trove.iterator.TIntIterator;
import gnu.trove.procedure.TIntProcedure;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/impl/sync/TSynchronizedIntCollection.class */
public class TSynchronizedIntCollection implements TIntCollection, Serializable {
    private static final long serialVersionUID = 3053995032091335093L;
    final TIntCollection c;
    final Object mutex;

    public TSynchronizedIntCollection(TIntCollection c) {
        if (c == null) {
            throw new NullPointerException();
        }
        this.c = c;
        this.mutex = this;
    }

    public TSynchronizedIntCollection(TIntCollection c, Object mutex) {
        this.c = c;
        this.mutex = mutex;
    }

    @Override // gnu.trove.TIntCollection
    public int size() {
        int size;
        synchronized (this.mutex) {
            size = this.c.size();
        }
        return size;
    }

    @Override // gnu.trove.TIntCollection
    public boolean isEmpty() {
        boolean isEmpty;
        synchronized (this.mutex) {
            isEmpty = this.c.isEmpty();
        }
        return isEmpty;
    }

    @Override // gnu.trove.TIntCollection
    public boolean contains(int o) {
        boolean contains;
        synchronized (this.mutex) {
            contains = this.c.contains(o);
        }
        return contains;
    }

    @Override // gnu.trove.TIntCollection
    public int[] toArray() {
        int[] array;
        synchronized (this.mutex) {
            array = this.c.toArray();
        }
        return array;
    }

    @Override // gnu.trove.TIntCollection
    public int[] toArray(int[] a) {
        int[] array;
        synchronized (this.mutex) {
            array = this.c.toArray(a);
        }
        return array;
    }

    @Override // gnu.trove.TIntCollection
    public TIntIterator iterator() {
        return this.c.iterator();
    }

    @Override // gnu.trove.TIntCollection
    public boolean add(int e) {
        boolean add;
        synchronized (this.mutex) {
            add = this.c.add(e);
        }
        return add;
    }

    @Override // gnu.trove.TIntCollection
    public boolean remove(int o) {
        boolean remove;
        synchronized (this.mutex) {
            remove = this.c.remove(o);
        }
        return remove;
    }

    @Override // gnu.trove.TIntCollection
    public boolean containsAll(Collection<?> coll) {
        boolean containsAll;
        synchronized (this.mutex) {
            containsAll = this.c.containsAll(coll);
        }
        return containsAll;
    }

    @Override // gnu.trove.TIntCollection
    public boolean containsAll(TIntCollection coll) {
        boolean containsAll;
        synchronized (this.mutex) {
            containsAll = this.c.containsAll(coll);
        }
        return containsAll;
    }

    @Override // gnu.trove.TIntCollection
    public boolean containsAll(int[] array) {
        boolean containsAll;
        synchronized (this.mutex) {
            containsAll = this.c.containsAll(array);
        }
        return containsAll;
    }

    @Override // gnu.trove.TIntCollection
    public boolean addAll(Collection<? extends Integer> coll) {
        boolean addAll;
        synchronized (this.mutex) {
            addAll = this.c.addAll(coll);
        }
        return addAll;
    }

    @Override // gnu.trove.TIntCollection
    public boolean addAll(TIntCollection coll) {
        boolean addAll;
        synchronized (this.mutex) {
            addAll = this.c.addAll(coll);
        }
        return addAll;
    }

    @Override // gnu.trove.TIntCollection
    public boolean addAll(int[] array) {
        boolean addAll;
        synchronized (this.mutex) {
            addAll = this.c.addAll(array);
        }
        return addAll;
    }

    @Override // gnu.trove.TIntCollection
    public boolean removeAll(Collection<?> coll) {
        boolean removeAll;
        synchronized (this.mutex) {
            removeAll = this.c.removeAll(coll);
        }
        return removeAll;
    }

    @Override // gnu.trove.TIntCollection
    public boolean removeAll(TIntCollection coll) {
        boolean removeAll;
        synchronized (this.mutex) {
            removeAll = this.c.removeAll(coll);
        }
        return removeAll;
    }

    @Override // gnu.trove.TIntCollection
    public boolean removeAll(int[] array) {
        boolean removeAll;
        synchronized (this.mutex) {
            removeAll = this.c.removeAll(array);
        }
        return removeAll;
    }

    @Override // gnu.trove.TIntCollection
    public boolean retainAll(Collection<?> coll) {
        boolean retainAll;
        synchronized (this.mutex) {
            retainAll = this.c.retainAll(coll);
        }
        return retainAll;
    }

    @Override // gnu.trove.TIntCollection
    public boolean retainAll(TIntCollection coll) {
        boolean retainAll;
        synchronized (this.mutex) {
            retainAll = this.c.retainAll(coll);
        }
        return retainAll;
    }

    @Override // gnu.trove.TIntCollection
    public boolean retainAll(int[] array) {
        boolean retainAll;
        synchronized (this.mutex) {
            retainAll = this.c.retainAll(array);
        }
        return retainAll;
    }

    @Override // gnu.trove.TIntCollection
    public int getNoEntryValue() {
        return this.c.getNoEntryValue();
    }

    @Override // gnu.trove.TIntCollection
    public boolean forEach(TIntProcedure procedure) {
        boolean forEach;
        synchronized (this.mutex) {
            forEach = this.c.forEach(procedure);
        }
        return forEach;
    }

    @Override // gnu.trove.TIntCollection
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
