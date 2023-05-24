package com.sun.xml.bind.v2.util;

import java.util.AbstractList;
import java.util.Arrays;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/util/CollisionCheckStack.class */
public final class CollisionCheckStack<E> extends AbstractList<E> {
    static final /* synthetic */ boolean $assertionsDisabled;
    private int size = 0;
    private boolean latestPushResult = false;
    private boolean useIdentity = true;
    private final int[] initialHash = new int[17];
    private Object[] data = new Object[16];
    private int[] next = new int[16];

    static {
        $assertionsDisabled = !CollisionCheckStack.class.desiredAssertionStatus();
    }

    public void setUseIdentity(boolean useIdentity) {
        this.useIdentity = useIdentity;
    }

    public boolean getUseIdentity() {
        return this.useIdentity;
    }

    public boolean getLatestPushResult() {
        return this.latestPushResult;
    }

    public boolean push(E o) {
        if (this.data.length == this.size) {
            expandCapacity();
        }
        this.data[this.size] = o;
        int hash = hash(o);
        boolean r = findDuplicate(o, hash);
        this.next[this.size] = this.initialHash[hash];
        this.initialHash[hash] = this.size + 1;
        this.size++;
        this.latestPushResult = r;
        return this.latestPushResult;
    }

    public void pushNocheck(E o) {
        if (this.data.length == this.size) {
            expandCapacity();
        }
        this.data[this.size] = o;
        this.next[this.size] = -1;
        this.size++;
    }

    public boolean findDuplicate(E o) {
        int hash = hash(o);
        return findDuplicate(o, hash);
    }

    @Override // java.util.AbstractList, java.util.List
    public E get(int index) {
        return (E) this.data[index];
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public int size() {
        return this.size;
    }

    private int hash(Object o) {
        return ((this.useIdentity ? System.identityHashCode(o) : o.hashCode()) & Integer.MAX_VALUE) % this.initialHash.length;
    }

    public E pop() {
        this.size--;
        E e = (E) this.data[this.size];
        this.data[this.size] = null;
        int n = this.next[this.size];
        if (n >= 0) {
            int hash = hash(e);
            if (!$assertionsDisabled && this.initialHash[hash] != this.size + 1) {
                throw new AssertionError();
            }
            this.initialHash[hash] = n;
        }
        return e;
    }

    public E peek() {
        return (E) this.data[this.size - 1];
    }

    private boolean findDuplicate(E o, int hash) {
        int i = this.initialHash[hash];
        while (true) {
            int p = i;
            if (p != 0) {
                int p2 = p - 1;
                Object existing = this.data[p2];
                if (this.useIdentity) {
                    if (existing == o) {
                        return true;
                    }
                } else if (o.equals(existing)) {
                    return true;
                }
                i = this.next[p2];
            } else {
                return false;
            }
        }
    }

    private void expandCapacity() {
        int oldSize = this.data.length;
        int newSize = oldSize * 2;
        Object[] d = new Object[newSize];
        int[] n = new int[newSize];
        System.arraycopy(this.data, 0, d, 0, oldSize);
        System.arraycopy(this.next, 0, n, 0, oldSize);
        this.data = d;
        this.next = n;
    }

    public void reset() {
        if (this.size > 0) {
            this.size = 0;
            Arrays.fill(this.initialHash, 0);
        }
    }

    public String getCycleString() {
        Object x;
        StringBuilder sb = new StringBuilder();
        int i = size() - 1;
        Object obj = get(i);
        sb.append(obj);
        do {
            sb.append(" -> ");
            i--;
            x = get(i);
            sb.append(x);
        } while (obj != x);
        return sb.toString();
    }
}
