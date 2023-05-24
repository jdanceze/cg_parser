package org.apache.tools.ant.util;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.Vector;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/util/VectorSet.class */
public final class VectorSet<E> extends Vector<E> {
    private static final long serialVersionUID = 1;
    private final HashSet<E> set;

    public VectorSet() {
        this.set = new HashSet<>();
    }

    public VectorSet(int initialCapacity) {
        super(initialCapacity);
        this.set = new HashSet<>();
    }

    public VectorSet(int initialCapacity, int capacityIncrement) {
        super(initialCapacity, capacityIncrement);
        this.set = new HashSet<>();
    }

    public VectorSet(Collection<? extends E> c) {
        this.set = new HashSet<>();
        if (c != null) {
            addAll(c);
        }
    }

    @Override // java.util.Vector, java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.util.List
    public synchronized boolean add(E o) {
        if (!this.set.contains(o)) {
            doAdd(size(), o);
            return true;
        }
        return false;
    }

    @Override // java.util.Vector, java.util.AbstractList, java.util.List
    public void add(int index, E o) {
        doAdd(index, o);
    }

    private synchronized void doAdd(int index, E o) {
        if (this.set.add(o)) {
            int count = size();
            ensureCapacity(count + 1);
            if (index != count) {
                System.arraycopy(this.elementData, index, this.elementData, index + 1, count - index);
            }
            this.elementData[index] = o;
            this.elementCount++;
        }
    }

    @Override // java.util.Vector
    public synchronized void addElement(E o) {
        doAdd(size(), o);
    }

    @Override // java.util.Vector, java.util.AbstractCollection, java.util.Collection, java.util.List
    public synchronized boolean addAll(Collection<? extends E> c) {
        boolean changed = false;
        for (E e : c) {
            changed |= add(e);
        }
        return changed;
    }

    @Override // java.util.Vector, java.util.AbstractList, java.util.List
    public synchronized boolean addAll(int index, Collection<? extends E> c) {
        LinkedList<E> toAdd = new LinkedList<>();
        for (E e : c) {
            if (this.set.add(e)) {
                toAdd.add(e);
            }
        }
        if (toAdd.isEmpty()) {
            return false;
        }
        int count = size();
        ensureCapacity(count + toAdd.size());
        if (index != count) {
            System.arraycopy(this.elementData, index, this.elementData, index + toAdd.size(), count - index);
        }
        Iterator<E> it = toAdd.iterator();
        while (it.hasNext()) {
            Object o = it.next();
            int i = index;
            index++;
            this.elementData[i] = o;
        }
        this.elementCount += toAdd.size();
        return true;
    }

    @Override // java.util.Vector, java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.util.List
    public synchronized void clear() {
        super.clear();
        this.set.clear();
    }

    @Override // java.util.Vector
    public Object clone() {
        VectorSet<E> vs = (VectorSet) super.clone();
        vs.set.addAll(this.set);
        return vs;
    }

    @Override // java.util.Vector, java.util.AbstractCollection, java.util.Collection, java.util.List
    public synchronized boolean contains(Object o) {
        return this.set.contains(o);
    }

    @Override // java.util.Vector, java.util.AbstractCollection, java.util.Collection, java.util.List
    public synchronized boolean containsAll(Collection<?> c) {
        return this.set.containsAll(c);
    }

    @Override // java.util.Vector
    public void insertElementAt(E o, int index) {
        doAdd(index, o);
    }

    @Override // java.util.Vector, java.util.AbstractList, java.util.List
    public synchronized E remove(int index) {
        E o = get(index);
        remove(o);
        return o;
    }

    @Override // java.util.Vector, java.util.AbstractCollection, java.util.Collection, java.util.List
    public boolean remove(Object o) {
        return doRemove(o);
    }

    private synchronized boolean doRemove(Object o) {
        if (this.set.remove(o)) {
            int index = indexOf(o);
            if (index < this.elementData.length - 1) {
                System.arraycopy(this.elementData, index + 1, this.elementData, index, (this.elementData.length - index) - 1);
            }
            this.elementCount--;
            return true;
        }
        return false;
    }

    @Override // java.util.Vector, java.util.AbstractCollection, java.util.Collection, java.util.List
    public synchronized boolean removeAll(Collection<?> c) {
        boolean changed = false;
        for (Object o : c) {
            changed |= remove(o);
        }
        return changed;
    }

    @Override // java.util.Vector
    public synchronized void removeAllElements() {
        this.set.clear();
        super.removeAllElements();
    }

    @Override // java.util.Vector
    public boolean removeElement(Object o) {
        return doRemove(o);
    }

    @Override // java.util.Vector
    public synchronized void removeElementAt(int index) {
        remove(get(index));
    }

    @Override // java.util.Vector, java.util.AbstractList
    public synchronized void removeRange(int fromIndex, int toIndex) {
        while (toIndex > fromIndex) {
            toIndex--;
            remove(toIndex);
        }
    }

    @Override // java.util.Vector, java.util.AbstractCollection, java.util.Collection, java.util.List
    public synchronized boolean retainAll(Collection<?> c) {
        if (!(c instanceof Set)) {
            c = new HashSet((Collection<? extends Object>) c);
        }
        LinkedList<E> l = new LinkedList<>();
        Iterator<E> it = iterator();
        while (it.hasNext()) {
            E o = it.next();
            if (!c.contains(o)) {
                l.addLast(o);
            }
        }
        if (!l.isEmpty()) {
            removeAll(l);
            return true;
        }
        return false;
    }

    @Override // java.util.Vector, java.util.AbstractList, java.util.List
    public synchronized E set(int index, E o) {
        E orig = get(index);
        if (this.set.add(o)) {
            this.elementData[index] = o;
            this.set.remove(orig);
        } else {
            int oldIndexOfO = indexOf(o);
            remove(o);
            remove(orig);
            add(oldIndexOfO > index ? index : index - 1, o);
        }
        return orig;
    }

    @Override // java.util.Vector
    public void setElementAt(E o, int index) {
        set(index, o);
    }
}
