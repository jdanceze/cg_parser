package soot.util;

import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
/* loaded from: gencallgraphv3.jar:soot/util/IterableMap.class */
public class IterableMap<K, V> implements Map<K, V> {
    private final HashMap<K, V> content_map;
    private final HashMap<V, HashChain<K>> back_map;
    private final HashChain<K> key_chain;
    private final HashChain<V> value_chain;
    private transient Set<K> keySet;
    private transient Set<V> valueSet;
    private transient Collection<V> values;

    public IterableMap() {
        this(7, 0.7f);
    }

    public IterableMap(int initialCapacity) {
        this(initialCapacity, 0.7f);
    }

    public IterableMap(int initialCapacity, float loadFactor) {
        this.keySet = null;
        this.valueSet = null;
        this.values = null;
        this.content_map = new HashMap<>(initialCapacity, loadFactor);
        this.back_map = new HashMap<>(initialCapacity, loadFactor);
        this.key_chain = new HashChain<>();
        this.value_chain = new HashChain<>();
    }

    @Override // java.util.Map
    public void clear() {
        Iterator<K> it = this.key_chain.iterator();
        while (it.hasNext()) {
            K next = it.next();
            this.content_map.remove(next);
        }
        Iterator<V> it2 = this.value_chain.iterator();
        while (it2.hasNext()) {
            V next2 = it2.next();
            this.back_map.remove(next2);
        }
        this.key_chain.clear();
        this.value_chain.clear();
    }

    public Iterator<K> iterator() {
        return this.key_chain.iterator();
    }

    @Override // java.util.Map
    public boolean containsKey(Object key) {
        return this.key_chain.contains(key);
    }

    @Override // java.util.Map
    public boolean containsValue(Object value) {
        return this.value_chain.contains(value);
    }

    @Override // java.util.Map
    public Set<Map.Entry<K, V>> entrySet() {
        return this.content_map.entrySet();
    }

    @Override // java.util.Map
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof IterableMap)) {
            return false;
        }
        IterableMap<?, ?> other = (IterableMap) o;
        if (!this.key_chain.equals(other.key_chain)) {
            return false;
        }
        Iterator<K> it = this.key_chain.iterator();
        while (it.hasNext()) {
            K ko = it.next();
            if (other.content_map.get(ko) != this.content_map.get(ko)) {
                return false;
            }
        }
        return true;
    }

    @Override // java.util.Map
    public V get(Object key) {
        return this.content_map.get(key);
    }

    @Override // java.util.Map
    public int hashCode() {
        return this.content_map.hashCode();
    }

    @Override // java.util.Map
    public boolean isEmpty() {
        return this.key_chain.isEmpty();
    }

    @Override // java.util.Map
    public Set<K> keySet() {
        if (this.keySet == null) {
            this.keySet = new AbstractSet<K>() { // from class: soot.util.IterableMap.1
                @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
                public Iterator<K> iterator() {
                    return IterableMap.this.key_chain.iterator();
                }

                @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
                public int size() {
                    return IterableMap.this.key_chain.size();
                }

                @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
                public boolean contains(Object o) {
                    return IterableMap.this.key_chain.contains(o);
                }

                @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
                public boolean remove(Object o) {
                    if (IterableMap.this.key_chain.contains(o)) {
                        if (IterableMap.this.content_map.get(o) != null) {
                            return IterableMap.this.remove(o) != null;
                        }
                        IterableMap.this.remove(o);
                        return true;
                    }
                    return false;
                }

                @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
                public void clear() {
                    IterableMap.this.clear();
                }
            };
        }
        return this.keySet;
    }

    public Set<V> valueSet() {
        if (this.valueSet == null) {
            this.valueSet = new AbstractSet<V>() { // from class: soot.util.IterableMap.2
                @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
                public Iterator<V> iterator() {
                    return IterableMap.this.value_chain.iterator();
                }

                @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
                public int size() {
                    return IterableMap.this.value_chain.size();
                }

                @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
                public boolean contains(Object o) {
                    return IterableMap.this.value_chain.contains(o);
                }

                @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
                public boolean remove(Object o) {
                    if (IterableMap.this.value_chain.contains(o)) {
                        HashChain c = (HashChain) IterableMap.this.back_map.get(o);
                        Iterator it = c.snapshotIterator();
                        while (it.hasNext()) {
                            Object ko = it.next();
                            if (IterableMap.this.content_map.get(o) == null) {
                                IterableMap.this.remove(ko);
                            } else if (IterableMap.this.remove(ko) == null) {
                                return false;
                            }
                        }
                        return true;
                    }
                    return false;
                }

                @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
                public void clear() {
                    IterableMap.this.clear();
                }
            };
        }
        return this.valueSet;
    }

    @Override // java.util.Map
    public V put(K key, V value) {
        if (this.key_chain.contains(key)) {
            V old_value = this.content_map.get(key);
            if (old_value == value) {
                return value;
            }
            HashChain<K> kc = this.back_map.get(old_value);
            kc.remove(key);
            if (kc.isEmpty()) {
                this.value_chain.remove(old_value);
                this.back_map.remove(old_value);
            }
            HashChain<K> kc2 = this.back_map.get(value);
            if (kc2 == null) {
                kc2 = new HashChain<>();
                this.back_map.put(value, kc2);
                this.value_chain.add(value);
            }
            kc2.add(key);
            return old_value;
        }
        this.key_chain.add(key);
        this.content_map.put(key, value);
        HashChain<K> kc3 = this.back_map.get(value);
        if (kc3 == null) {
            kc3 = new HashChain<>();
            this.back_map.put(value, kc3);
            this.value_chain.add(value);
        }
        kc3.add(key);
        return null;
    }

    @Override // java.util.Map
    public void putAll(Map<? extends K, ? extends V> t) {
        Iterator<K> it;
        if (t instanceof IterableMap) {
            it = ((IterableMap) t).key_chain.iterator();
        } else {
            it = t.keySet().iterator();
        }
        while (it.hasNext()) {
            K key = it.next();
            put(key, t.get(key));
        }
    }

    @Override // java.util.Map
    public V remove(Object key) {
        if (!this.key_chain.contains(key)) {
            return null;
        }
        this.key_chain.remove(key);
        V value = this.content_map.remove(key);
        HashChain<K> c = this.back_map.get(value);
        c.remove(key);
        if (c.isEmpty()) {
            this.back_map.remove(value);
        }
        return value;
    }

    @Override // java.util.Map
    public int size() {
        return this.key_chain.size();
    }

    @Override // java.util.Map
    public Collection<V> values() {
        if (this.values == null) {
            this.values = new AbstractCollection<V>() { // from class: soot.util.IterableMap.3
                @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
                public Iterator<V> iterator() {
                    return new Mapping_Iterator(IterableMap.this.key_chain, IterableMap.this.content_map);
                }

                @Override // java.util.AbstractCollection, java.util.Collection
                public int size() {
                    return IterableMap.this.key_chain.size();
                }

                @Override // java.util.AbstractCollection, java.util.Collection
                public boolean contains(Object o) {
                    return IterableMap.this.value_chain.contains(o);
                }

                @Override // java.util.AbstractCollection, java.util.Collection
                public void clear() {
                    IterableMap.this.clear();
                }
            };
        }
        return this.values;
    }

    /* loaded from: gencallgraphv3.jar:soot/util/IterableMap$Mapping_Iterator.class */
    public static class Mapping_Iterator<K, V> implements Iterator<V> {
        private final Iterator<K> it;
        private final HashMap<K, V> m;

        public Mapping_Iterator(HashChain<K> c, HashMap<K, V> m) {
            this.it = c.iterator();
            this.m = m;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.it.hasNext();
        }

        @Override // java.util.Iterator
        public V next() throws NoSuchElementException {
            return this.m.get(this.it.next());
        }

        @Override // java.util.Iterator
        public void remove() throws UnsupportedOperationException {
            throw new UnsupportedOperationException("You cannot remove from an Iterator on the values() for an IterableMap.");
        }
    }
}
