package soot.jimple.spark.ondemand.genericutil;

import java.util.Collection;
/* loaded from: gencallgraphv3.jar:soot/jimple/spark/ondemand/genericutil/Stack.class */
public final class Stack<T> implements Cloneable {
    private T[] elems;
    private int size;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !Stack.class.desiredAssertionStatus();
    }

    public Stack(int numElems_) {
        this.size = 0;
        this.elems = (T[]) new Object[numElems_];
    }

    public Stack() {
        this(4);
    }

    public void push(T obj_) {
        if (!$assertionsDisabled && obj_ == null) {
            throw new AssertionError();
        }
        if (this.size == this.elems.length) {
            Object[] tmp = this.elems;
            this.elems = (T[]) new Object[tmp.length * 2];
            System.arraycopy(tmp, 0, this.elems, 0, tmp.length);
        }
        this.elems[this.size] = obj_;
        this.size++;
    }

    public void pushAll(Collection<T> c) {
        for (T t : c) {
            push(t);
        }
    }

    public T pop() {
        if (this.size == 0) {
            return null;
        }
        this.size--;
        T ret = this.elems[this.size];
        this.elems[this.size] = null;
        return ret;
    }

    public T peek() {
        if (this.size == 0) {
            return null;
        }
        return this.elems[this.size - 1];
    }

    public int size() {
        return this.size;
    }

    public boolean isEmpty() {
        return this.size == 0;
    }

    public void clear() {
        this.size = 0;
    }

    /* renamed from: clone */
    public Stack<T> m2833clone() {
        try {
            Stack<T> ret = (Stack) super.clone();
            ret.elems = (T[]) new Object[this.elems.length];
            System.arraycopy(this.elems, 0, ret.elems, 0, this.size);
            return ret;
        } catch (CloneNotSupportedException e) {
            throw new InternalError();
        }
    }

    public Object get(int i) {
        return this.elems[i];
    }

    public boolean contains(Object o) {
        return Util.arrayContains(this.elems, o, this.size);
    }

    public int indexOf(T o) {
        for (int i = 0; i < this.size && this.elems[i] != null; i++) {
            if (this.elems[i].equals(o)) {
                return i;
            }
        }
        return -1;
    }

    public String toString() {
        StringBuffer s = new StringBuffer();
        s.append("[");
        for (int i = 0; i < this.size && this.elems[i] != null; i++) {
            if (i > 0) {
                s.append(", ");
            }
            s.append(this.elems[i].toString());
        }
        s.append("]");
        return s.toString();
    }
}
