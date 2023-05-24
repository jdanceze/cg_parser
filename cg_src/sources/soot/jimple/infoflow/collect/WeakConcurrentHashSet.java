package soot.jimple.infoflow.collect;

import com.google.common.collect.MapMaker;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.concurrent.ConcurrentMap;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/collect/WeakConcurrentHashSet.class */
public class WeakConcurrentHashSet<E> extends AbstractSet<E> {
    protected ConcurrentMap<E, E> delegate = new MapMaker().weakKeys().concurrencyLevel(Runtime.getRuntime().availableProcessors()).makeMap();

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public int size() {
        return this.delegate.size();
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean contains(Object o) {
        return this.delegate.containsKey(o);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
    public Iterator<E> iterator() {
        return this.delegate.keySet().iterator();
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean add(E o) {
        return this.delegate.put(o, o) == null;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean remove(Object o) {
        return this.delegate.remove(o) != null;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public void clear() {
        this.delegate.entrySet().clear();
    }

    @Override // java.util.AbstractSet, java.util.Collection, java.util.Set
    public int hashCode() {
        return this.delegate.hashCode();
    }

    @Override // java.util.AbstractSet, java.util.Collection, java.util.Set
    public boolean equals(Object obj) {
        return (obj instanceof ConcurrentHashSet) && this.delegate.equals(obj);
    }

    @Override // java.util.AbstractCollection
    public String toString() {
        return this.delegate.keySet().toString();
    }
}
