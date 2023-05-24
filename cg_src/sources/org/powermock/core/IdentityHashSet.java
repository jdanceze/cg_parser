package org.powermock.core;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/core/IdentityHashSet.class */
public class IdentityHashSet<E> extends AbstractSet<E> {
    protected final Map<E, Boolean> backedMap = new ListMap();

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public int size() {
        return this.backedMap.size();
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean contains(Object o) {
        return this.backedMap.containsKey(o);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean add(E o) {
        return this.backedMap.put(o, Boolean.TRUE) == null;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
    public Iterator<E> iterator() {
        return this.backedMap.keySet().iterator();
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean remove(Object o) {
        return this.backedMap.remove(o) != null;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public void clear() {
        this.backedMap.clear();
    }
}
