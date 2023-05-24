package soot.util;

import heros.solver.Pair;
import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
/* loaded from: gencallgraphv3.jar:soot/util/AbstractMultiMap.class */
public abstract class AbstractMultiMap<K, V> implements MultiMap<K, V>, Serializable {
    private static final long serialVersionUID = 4558567794548019671L;

    /* loaded from: gencallgraphv3.jar:soot/util/AbstractMultiMap$EntryIterator.class */
    private class EntryIterator implements Iterator<Pair<K, V>> {
        Iterator<K> keyIterator;
        Iterator<V> valueIterator;
        K currentKey;

        private EntryIterator() {
            this.keyIterator = AbstractMultiMap.this.keySet().iterator();
            this.valueIterator = null;
            this.currentKey = null;
        }

        /* synthetic */ EntryIterator(AbstractMultiMap abstractMultiMap, EntryIterator entryIterator) {
            this();
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            if (this.valueIterator != null && this.valueIterator.hasNext()) {
                return true;
            }
            this.valueIterator = null;
            this.currentKey = null;
            return this.keyIterator.hasNext();
        }

        @Override // java.util.Iterator
        public Pair<K, V> next() {
            if (this.valueIterator == null) {
                this.currentKey = this.keyIterator.next();
                this.valueIterator = AbstractMultiMap.this.get(this.currentKey).iterator();
            }
            return new Pair<>(this.currentKey, this.valueIterator.next());
        }

        @Override // java.util.Iterator
        public void remove() {
            if (this.valueIterator == null) {
                return;
            }
            this.valueIterator.remove();
            if (AbstractMultiMap.this.get(this.currentKey).isEmpty()) {
                this.keyIterator.remove();
                this.valueIterator = null;
                this.currentKey = null;
            }
        }
    }

    @Override // soot.util.MultiMap
    public boolean putAll(MultiMap<K, V> m) {
        boolean hasNew = false;
        for (K key : m.keySet()) {
            if (putAll(key, m.get(key))) {
                hasNew = true;
            }
        }
        return hasNew;
    }

    @Override // soot.util.MultiMap
    public boolean putAll(Map<K, Collection<V>> m) {
        boolean hasNew = false;
        for (K key : m.keySet()) {
            if (putAll(key, m.get(key))) {
                hasNew = true;
            }
        }
        return hasNew;
    }

    @Override // soot.util.MultiMap
    public boolean isEmpty() {
        return numKeys() == 0;
    }

    @Override // soot.util.MultiMap
    public boolean contains(K key, V value) {
        Set<V> set = get(key);
        if (set == null) {
            return false;
        }
        return set.contains(value);
    }

    @Override // java.lang.Iterable
    public Iterator<Pair<K, V>> iterator() {
        return new EntryIterator(this, null);
    }

    @Override // soot.util.MultiMap
    public boolean putMap(Map<K, V> m) {
        boolean hasNew = false;
        for (Map.Entry<K, V> entry : m.entrySet()) {
            if (put(entry.getKey(), entry.getValue())) {
                hasNew = true;
            }
        }
        return hasNew;
    }
}
