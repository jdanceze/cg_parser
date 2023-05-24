package soot.util;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Set;
@Deprecated
/* loaded from: gencallgraphv3.jar:soot/util/IdentityHashSet.class */
public class IdentityHashSet<E> extends AbstractSet<E> implements Set<E> {
    protected IdentityHashMap<E, E> delegate = new IdentityHashMap<>();

    public IdentityHashSet() {
    }

    /* JADX WARN: Multi-variable type inference failed */
    public IdentityHashSet(Collection<E> original) {
        addAll(original);
    }

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
        int result = (31 * 1) + (this.delegate == null ? 0 : this.delegate.hashCode());
        return result;
    }

    @Override // java.util.AbstractSet, java.util.Collection, java.util.Set
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        IdentityHashSet<?> other = (IdentityHashSet) obj;
        if (this.delegate == null) {
            if (other.delegate != null) {
                return false;
            }
            return true;
        } else if (!this.delegate.equals(other.delegate)) {
            return false;
        } else {
            return true;
        }
    }

    @Override // java.util.AbstractCollection
    public String toString() {
        return this.delegate.keySet().toString();
    }
}
