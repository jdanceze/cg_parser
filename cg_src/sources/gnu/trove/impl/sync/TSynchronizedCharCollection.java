package gnu.trove.impl.sync;

import gnu.trove.TCharCollection;
import gnu.trove.iterator.TCharIterator;
import gnu.trove.procedure.TCharProcedure;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/impl/sync/TSynchronizedCharCollection.class */
public class TSynchronizedCharCollection implements TCharCollection, Serializable {
    private static final long serialVersionUID = 3053995032091335093L;
    final TCharCollection c;
    final Object mutex;

    public TSynchronizedCharCollection(TCharCollection c) {
        if (c == null) {
            throw new NullPointerException();
        }
        this.c = c;
        this.mutex = this;
    }

    public TSynchronizedCharCollection(TCharCollection c, Object mutex) {
        this.c = c;
        this.mutex = mutex;
    }

    @Override // gnu.trove.TCharCollection
    public int size() {
        int size;
        synchronized (this.mutex) {
            size = this.c.size();
        }
        return size;
    }

    @Override // gnu.trove.TCharCollection
    public boolean isEmpty() {
        boolean isEmpty;
        synchronized (this.mutex) {
            isEmpty = this.c.isEmpty();
        }
        return isEmpty;
    }

    @Override // gnu.trove.TCharCollection
    public boolean contains(char o) {
        boolean contains;
        synchronized (this.mutex) {
            contains = this.c.contains(o);
        }
        return contains;
    }

    @Override // gnu.trove.TCharCollection
    public char[] toArray() {
        char[] array;
        synchronized (this.mutex) {
            array = this.c.toArray();
        }
        return array;
    }

    @Override // gnu.trove.TCharCollection
    public char[] toArray(char[] a) {
        char[] array;
        synchronized (this.mutex) {
            array = this.c.toArray(a);
        }
        return array;
    }

    @Override // gnu.trove.TCharCollection
    public TCharIterator iterator() {
        return this.c.iterator();
    }

    @Override // gnu.trove.TCharCollection
    public boolean add(char e) {
        boolean add;
        synchronized (this.mutex) {
            add = this.c.add(e);
        }
        return add;
    }

    @Override // gnu.trove.TCharCollection
    public boolean remove(char o) {
        boolean remove;
        synchronized (this.mutex) {
            remove = this.c.remove(o);
        }
        return remove;
    }

    @Override // gnu.trove.TCharCollection
    public boolean containsAll(Collection<?> coll) {
        boolean containsAll;
        synchronized (this.mutex) {
            containsAll = this.c.containsAll(coll);
        }
        return containsAll;
    }

    @Override // gnu.trove.TCharCollection
    public boolean containsAll(TCharCollection coll) {
        boolean containsAll;
        synchronized (this.mutex) {
            containsAll = this.c.containsAll(coll);
        }
        return containsAll;
    }

    @Override // gnu.trove.TCharCollection
    public boolean containsAll(char[] array) {
        boolean containsAll;
        synchronized (this.mutex) {
            containsAll = this.c.containsAll(array);
        }
        return containsAll;
    }

    @Override // gnu.trove.TCharCollection
    public boolean addAll(Collection<? extends Character> coll) {
        boolean addAll;
        synchronized (this.mutex) {
            addAll = this.c.addAll(coll);
        }
        return addAll;
    }

    @Override // gnu.trove.TCharCollection
    public boolean addAll(TCharCollection coll) {
        boolean addAll;
        synchronized (this.mutex) {
            addAll = this.c.addAll(coll);
        }
        return addAll;
    }

    @Override // gnu.trove.TCharCollection
    public boolean addAll(char[] array) {
        boolean addAll;
        synchronized (this.mutex) {
            addAll = this.c.addAll(array);
        }
        return addAll;
    }

    @Override // gnu.trove.TCharCollection
    public boolean removeAll(Collection<?> coll) {
        boolean removeAll;
        synchronized (this.mutex) {
            removeAll = this.c.removeAll(coll);
        }
        return removeAll;
    }

    @Override // gnu.trove.TCharCollection
    public boolean removeAll(TCharCollection coll) {
        boolean removeAll;
        synchronized (this.mutex) {
            removeAll = this.c.removeAll(coll);
        }
        return removeAll;
    }

    @Override // gnu.trove.TCharCollection
    public boolean removeAll(char[] array) {
        boolean removeAll;
        synchronized (this.mutex) {
            removeAll = this.c.removeAll(array);
        }
        return removeAll;
    }

    @Override // gnu.trove.TCharCollection
    public boolean retainAll(Collection<?> coll) {
        boolean retainAll;
        synchronized (this.mutex) {
            retainAll = this.c.retainAll(coll);
        }
        return retainAll;
    }

    @Override // gnu.trove.TCharCollection
    public boolean retainAll(TCharCollection coll) {
        boolean retainAll;
        synchronized (this.mutex) {
            retainAll = this.c.retainAll(coll);
        }
        return retainAll;
    }

    @Override // gnu.trove.TCharCollection
    public boolean retainAll(char[] array) {
        boolean retainAll;
        synchronized (this.mutex) {
            retainAll = this.c.retainAll(array);
        }
        return retainAll;
    }

    @Override // gnu.trove.TCharCollection
    public char getNoEntryValue() {
        return this.c.getNoEntryValue();
    }

    @Override // gnu.trove.TCharCollection
    public boolean forEach(TCharProcedure procedure) {
        boolean forEach;
        synchronized (this.mutex) {
            forEach = this.c.forEach(procedure);
        }
        return forEach;
    }

    @Override // gnu.trove.TCharCollection
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
