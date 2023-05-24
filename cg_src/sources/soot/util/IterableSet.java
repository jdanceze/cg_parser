package soot.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
/* loaded from: gencallgraphv3.jar:soot/util/IterableSet.class */
public class IterableSet<T> extends HashChain<T> implements Set<T> {
    /* JADX WARN: Multi-variable type inference failed */
    public IterableSet(Collection<T> c) {
        addAll(c);
    }

    public IterableSet() {
    }

    @Override // soot.util.HashChain, java.util.AbstractCollection, java.util.Collection
    public boolean add(T o) {
        if (o == null) {
            throw new IllegalArgumentException("Cannot add \"null\" to an IterableSet.");
        }
        if (contains(o)) {
            return false;
        }
        return super.add(o);
    }

    @Override // soot.util.HashChain, java.util.AbstractCollection, java.util.Collection, soot.util.Chain
    public boolean remove(Object o) {
        if (o == null || !contains(o)) {
            return false;
        }
        return super.remove(o);
    }

    @Override // java.util.Collection, java.util.Set
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (this == o) {
            return true;
        }
        if (!(o instanceof IterableSet)) {
            return false;
        }
        IterableSet<?> other = (IterableSet) o;
        if (size() != other.size()) {
            return false;
        }
        Iterator<T> it = iterator();
        while (it.hasNext()) {
            T t = it.next();
            if (!other.contains(t)) {
                return false;
            }
        }
        return true;
    }

    @Override // java.util.Collection, java.util.Set
    public int hashCode() {
        int code = 23 * size();
        Iterator<T> it = iterator();
        while (it.hasNext()) {
            T t = it.next();
            code += t.hashCode();
        }
        return code;
    }

    public Object clone() {
        IterableSet<T> s = new IterableSet<>();
        s.addAll(this);
        return s;
    }

    public boolean isSubsetOf(IterableSet<T> other) {
        if (other == null) {
            throw new IllegalArgumentException("Cannot set compare an IterableSet with \"null\".");
        }
        if (size() > other.size()) {
            return false;
        }
        Iterator<T> it = iterator();
        while (it.hasNext()) {
            T t = it.next();
            if (!other.contains(t)) {
                return false;
            }
        }
        return true;
    }

    public boolean isSupersetOf(IterableSet<T> other) {
        if (other == null) {
            throw new IllegalArgumentException("Cannot set compare an IterableSet with \"null\".");
        }
        if (size() < other.size()) {
            return false;
        }
        Iterator<T> it = other.iterator();
        while (it.hasNext()) {
            T t = it.next();
            if (!contains(t)) {
                return false;
            }
        }
        return true;
    }

    public boolean isStrictSubsetOf(IterableSet<T> other) {
        if (other == null) {
            throw new IllegalArgumentException("Cannot set compare an IterableSet with \"null\".");
        }
        if (size() >= other.size()) {
            return false;
        }
        return isSubsetOf(other);
    }

    public boolean isStrictSupersetOf(IterableSet<T> other) {
        if (other == null) {
            throw new IllegalArgumentException("Cannot set compare an IterableSet with \"null\".");
        }
        if (size() <= other.size()) {
            return false;
        }
        return isSupersetOf(other);
    }

    public boolean intersects(IterableSet<T> other) {
        if (other == null) {
            throw new IllegalArgumentException("Cannot set intersect an IterableSet with \"null\".");
        }
        if (other.size() < size()) {
            Iterator<T> it = other.iterator();
            while (it.hasNext()) {
                T t = it.next();
                if (contains(t)) {
                    return true;
                }
            }
            return false;
        }
        Iterator<T> it2 = iterator();
        while (it2.hasNext()) {
            T t2 = it2.next();
            if (other.contains(t2)) {
                return true;
            }
        }
        return false;
    }

    public IterableSet<T> intersection(IterableSet<T> other) {
        if (other == null) {
            throw new IllegalArgumentException("Cannot set intersect an IterableSet with \"null\".");
        }
        IterableSet<T> c = new IterableSet<>();
        if (other.size() < size()) {
            Iterator<T> it = other.iterator();
            while (it.hasNext()) {
                T t = it.next();
                if (contains(t)) {
                    c.add(t);
                }
            }
        } else {
            Iterator<T> it2 = iterator();
            while (it2.hasNext()) {
                T t2 = it2.next();
                if (other.contains(t2)) {
                    c.add(t2);
                }
            }
        }
        return c;
    }

    public IterableSet<T> union(IterableSet<T> other) {
        if (other == null) {
            throw new IllegalArgumentException("Cannot set union an IterableSet with \"null\".");
        }
        IterableSet<T> c = new IterableSet<>();
        c.addAll(this);
        c.addAll(other);
        return c;
    }

    @Override // soot.util.HashChain, java.util.AbstractCollection
    public String toString() {
        StringBuilder b = new StringBuilder();
        Iterator<T> it = iterator();
        while (it.hasNext()) {
            T t = it.next();
            b.append(t.toString()).append('\n');
        }
        return b.toString();
    }

    public UnmodifiableIterableSet<T> asUnmodifiable() {
        return new UnmodifiableIterableSet<>(this);
    }
}
