package org.apache.tools.ant.util;

import java.util.Collection;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Stack;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/util/IdentityStack.class */
public class IdentityStack<E> extends Stack<E> {
    private static final long serialVersionUID = -5555522620060077046L;

    public static <E> IdentityStack<E> getInstance(Stack<E> s) {
        if (s instanceof IdentityStack) {
            return (IdentityStack) s;
        }
        IdentityStack<E> result = new IdentityStack<>();
        if (s != null) {
            result.addAll(s);
        }
        return result;
    }

    public IdentityStack() {
    }

    public IdentityStack(E o) {
        push(o);
    }

    @Override // java.util.Vector, java.util.AbstractCollection, java.util.Collection, java.util.List
    public synchronized boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    @Override // java.util.Vector
    public synchronized int indexOf(Object o, int pos) {
        int size = size();
        for (int i = pos; i < size; i++) {
            if (get(i) == o) {
                return i;
            }
        }
        return -1;
    }

    @Override // java.util.Vector
    public synchronized int lastIndexOf(Object o, int pos) {
        for (int i = pos; i >= 0; i--) {
            if (get(i) == o) {
                return i;
            }
        }
        return -1;
    }

    @Override // java.util.Vector, java.util.AbstractCollection, java.util.Collection, java.util.List
    public synchronized boolean removeAll(Collection<?> c) {
        if (!(c instanceof Set)) {
            c = new HashSet((Collection<? extends Object>) c);
        }
        return super.removeAll(c);
    }

    @Override // java.util.Vector, java.util.AbstractCollection, java.util.Collection, java.util.List
    public synchronized boolean retainAll(Collection<?> c) {
        if (!(c instanceof Set)) {
            c = new HashSet((Collection<? extends Object>) c);
        }
        return super.retainAll(c);
    }

    @Override // java.util.Vector, java.util.AbstractCollection, java.util.Collection, java.util.List
    public synchronized boolean containsAll(Collection<?> c) {
        IdentityHashMap<Object, Boolean> map = new IdentityHashMap<>();
        Iterator it = iterator();
        while (it.hasNext()) {
            Object e = it.next();
            map.put(e, Boolean.TRUE);
        }
        return map.keySet().containsAll(c);
    }
}
