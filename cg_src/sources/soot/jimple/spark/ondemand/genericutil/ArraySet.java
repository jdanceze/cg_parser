package soot.jimple.spark.ondemand.genericutil;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
/* loaded from: gencallgraphv3.jar:soot/jimple/spark/ondemand/genericutil/ArraySet.class */
public class ArraySet<T> extends AbstractSet<T> {
    private static final ArraySet EMPTY;
    private T[] _elems;
    private int _curIndex;
    private final boolean checkDupes;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !ArraySet.class.desiredAssertionStatus();
        EMPTY = new ArraySet<Object>(0, true) { // from class: soot.jimple.spark.ondemand.genericutil.ArraySet.1
            @Override // soot.jimple.spark.ondemand.genericutil.ArraySet, java.util.AbstractCollection, java.util.Collection, java.util.Set
            public boolean add(Object obj_) {
                throw new RuntimeException();
            }
        };
    }

    public static final <T> ArraySet<T> empty() {
        return EMPTY;
    }

    public ArraySet(int numElems_, boolean checkDupes) {
        this._curIndex = 0;
        this._elems = (T[]) new Object[numElems_];
        this.checkDupes = checkDupes;
    }

    public ArraySet() {
        this(1, true);
    }

    public ArraySet(ArraySet<T> other) {
        this._curIndex = 0;
        int size = other._curIndex;
        this._elems = (T[]) new Object[size];
        this.checkDupes = other.checkDupes;
        this._curIndex = size;
        System.arraycopy(other._elems, 0, this._elems, 0, size);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public ArraySet(Collection<T> other) {
        this(other.size(), true);
        addAll(other);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean add(T obj_) {
        if ($assertionsDisabled || obj_ != null) {
            if (this.checkDupes && contains(obj_)) {
                return false;
            }
            if (this._curIndex == this._elems.length) {
                Object[] tmp = this._elems;
                this._elems = (T[]) new Object[tmp.length * 2];
                System.arraycopy(tmp, 0, this._elems, 0, tmp.length);
            }
            this._elems[this._curIndex] = obj_;
            this._curIndex++;
            return true;
        }
        throw new AssertionError();
    }

    public boolean addAll(ArraySet<T> other) {
        boolean ret = false;
        for (int i = 0; i < other.size(); i++) {
            boolean added = add(other.get(i));
            ret = ret || added;
        }
        return ret;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean contains(Object obj_) {
        for (int i = 0; i < this._curIndex; i++) {
            if (this._elems[i].equals(obj_)) {
                return true;
            }
        }
        return false;
    }

    public boolean intersects(ArraySet<T> other) {
        for (int i = 0; i < other.size(); i++) {
            if (contains(other.get(i))) {
                return true;
            }
        }
        return false;
    }

    public void forall(ObjectVisitor<T> visitor_) {
        for (int i = 0; i < this._curIndex; i++) {
            visitor_.visit(this._elems[i]);
        }
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public int size() {
        return this._curIndex;
    }

    public T get(int i) {
        return this._elems[i];
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean remove(Object obj_) {
        int ind = 0;
        while (ind < this._curIndex && !this._elems[ind].equals(obj_)) {
            ind++;
        }
        if (ind == this._curIndex) {
            return false;
        }
        return remove(ind);
    }

    public boolean remove(int ind) {
        System.arraycopy(this._elems, ind + 1, this._elems, ind, this._curIndex - (ind + 1));
        this._curIndex--;
        return true;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public void clear() {
        this._curIndex = 0;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override // java.util.AbstractCollection
    public String toString() {
        StringBuffer ret = new StringBuffer();
        ret.append('[');
        for (int i = 0; i < size(); i++) {
            ret.append(get(i).toString());
            if (i + 1 < size()) {
                ret.append(", ");
            }
        }
        ret.append(']');
        return ret.toString();
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public Object[] toArray() {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean addAll(Collection<? extends T> c) {
        boolean ret = false;
        for (T element : c) {
            boolean added = add(element);
            ret = ret || added;
        }
        return ret;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
    public Iterator<T> iterator() {
        return new ArraySetIterator();
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public <U> U[] toArray(U[] uArr) {
        for (int i = 0; i < this._curIndex; i++) {
            T t = this._elems[i];
            uArr[i] = t;
        }
        return uArr;
    }

    /* loaded from: gencallgraphv3.jar:soot/jimple/spark/ondemand/genericutil/ArraySet$ArraySetIterator.class */
    public class ArraySetIterator implements Iterator<T> {
        int ind = 0;
        final int setSize;

        public ArraySetIterator() {
            this.setSize = ArraySet.this.size();
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.ind < this.setSize;
        }

        @Override // java.util.Iterator
        public T next() {
            if (this.ind >= this.setSize) {
                throw new NoSuchElementException();
            }
            ArraySet arraySet = ArraySet.this;
            int i = this.ind;
            this.ind = i + 1;
            return (T) arraySet.get(i);
        }
    }
}
