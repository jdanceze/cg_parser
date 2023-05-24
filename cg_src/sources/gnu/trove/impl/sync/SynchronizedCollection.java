package gnu.trove.impl.sync;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/impl/sync/SynchronizedCollection.class */
class SynchronizedCollection<E> implements Collection<E>, Serializable {
    private static final long serialVersionUID = 3053995032091335093L;
    final Collection<E> c;
    final Object mutex;

    /* JADX INFO: Access modifiers changed from: package-private */
    public SynchronizedCollection(Collection<E> c, Object mutex) {
        this.c = c;
        this.mutex = mutex;
    }

    @Override // java.util.Collection
    public int size() {
        int size;
        synchronized (this.mutex) {
            size = this.c.size();
        }
        return size;
    }

    @Override // java.util.Collection
    public boolean isEmpty() {
        boolean isEmpty;
        synchronized (this.mutex) {
            isEmpty = this.c.isEmpty();
        }
        return isEmpty;
    }

    @Override // java.util.Collection
    public boolean contains(Object o) {
        boolean contains;
        synchronized (this.mutex) {
            contains = this.c.contains(o);
        }
        return contains;
    }

    @Override // java.util.Collection
    public Object[] toArray() {
        Object[] array;
        synchronized (this.mutex) {
            array = this.c.toArray();
        }
        return array;
    }

    @Override // java.util.Collection
    public <T> T[] toArray(T[] a) {
        T[] tArr;
        synchronized (this.mutex) {
            tArr = (T[]) this.c.toArray(a);
        }
        return tArr;
    }

    @Override // java.util.Collection, java.lang.Iterable
    public Iterator<E> iterator() {
        return this.c.iterator();
    }

    @Override // java.util.Collection
    public boolean add(E e) {
        boolean add;
        synchronized (this.mutex) {
            add = this.c.add(e);
        }
        return add;
    }

    @Override // java.util.Collection
    public boolean remove(Object o) {
        boolean remove;
        synchronized (this.mutex) {
            remove = this.c.remove(o);
        }
        return remove;
    }

    @Override // java.util.Collection
    public boolean containsAll(Collection<?> coll) {
        boolean containsAll;
        synchronized (this.mutex) {
            containsAll = this.c.containsAll(coll);
        }
        return containsAll;
    }

    @Override // java.util.Collection
    public boolean addAll(Collection<? extends E> coll) {
        boolean addAll;
        synchronized (this.mutex) {
            addAll = this.c.addAll(coll);
        }
        return addAll;
    }

    @Override // java.util.Collection
    public boolean removeAll(Collection<?> coll) {
        boolean removeAll;
        synchronized (this.mutex) {
            removeAll = this.c.removeAll(coll);
        }
        return removeAll;
    }

    @Override // java.util.Collection
    public boolean retainAll(Collection<?> coll) {
        boolean retainAll;
        synchronized (this.mutex) {
            retainAll = this.c.retainAll(coll);
        }
        return retainAll;
    }

    @Override // java.util.Collection
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
