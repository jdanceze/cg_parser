package soot.jimple.infoflow.collect;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/collect/ConcurrentIdentityHashMap.class */
public class ConcurrentIdentityHashMap<K, V> implements ConcurrentMap<K, V> {
    private final ConcurrentMap<IdentityWrapper<K>, V> innerMap = new ConcurrentHashMap();

    @Override // java.util.Map
    public int size() {
        return this.innerMap.size();
    }

    @Override // java.util.Map
    public boolean isEmpty() {
        return this.innerMap.isEmpty();
    }

    @Override // java.util.Map
    public boolean containsKey(Object key) {
        return this.innerMap.containsKey(new IdentityWrapper(key));
    }

    @Override // java.util.Map
    public boolean containsValue(Object value) {
        return this.innerMap.containsValue(value);
    }

    @Override // java.util.Map
    public V get(Object key) {
        return this.innerMap.get(new IdentityWrapper(key));
    }

    @Override // java.util.Map
    public V put(K key, V value) {
        return this.innerMap.put(new IdentityWrapper<>(key), value);
    }

    @Override // java.util.Map
    public V remove(Object key) {
        return this.innerMap.remove(new IdentityWrapper(key));
    }

    @Override // java.util.Map
    public void putAll(Map<? extends K, ? extends V> m) {
        for (Map.Entry<? extends K, ? extends V> entry : m.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override // java.util.Map
    public void clear() {
        this.innerMap.clear();
    }

    @Override // java.util.Map
    public Set<K> keySet() {
        Set<K> set = Collections.newSetFromMap(new IdentityHashMap());
        for (IdentityWrapper<K> k : this.innerMap.keySet()) {
            set.add(k.getContents());
        }
        return set;
    }

    @Override // java.util.Map
    public Collection<V> values() {
        return this.innerMap.values();
    }

    /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/collect/ConcurrentIdentityHashMap$MapEntry.class */
    public class MapEntry implements Map.Entry<K, V> {
        private final K key;
        private final V value;

        public MapEntry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override // java.util.Map.Entry
        public K getKey() {
            return this.key;
        }

        @Override // java.util.Map.Entry
        public V getValue() {
            return this.value;
        }

        @Override // java.util.Map.Entry
        public V setValue(V value) {
            throw new RuntimeException("Unsupported operation");
        }

        @Override // java.util.Map.Entry
        public boolean equals(Object other) {
            if (other == null || getClass() != other.getClass()) {
                return false;
            }
            ConcurrentIdentityHashMap<K, V>.MapEntry me = (MapEntry) other;
            return this.key == me.key && this.value.equals(me.value);
        }

        @Override // java.util.Map.Entry
        public int hashCode() {
            return System.identityHashCode(this.key) + this.value.hashCode();
        }
    }

    @Override // java.util.Map
    public Set<Map.Entry<K, V>> entrySet() {
        Set<Map.Entry<K, V>> set = new HashSet<>();
        for (Map.Entry<IdentityWrapper<K>, V> entry : this.innerMap.entrySet()) {
            set.add(new MapEntry(entry.getKey().getContents(), entry.getValue()));
        }
        return set;
    }

    public String toString() {
        return this.innerMap.toString();
    }

    @Override // java.util.concurrent.ConcurrentMap, java.util.Map
    public V putIfAbsent(K key, V value) {
        return this.innerMap.putIfAbsent(new IdentityWrapper<>(key), value);
    }

    @Override // java.util.concurrent.ConcurrentMap, java.util.Map
    public boolean remove(Object key, Object value) {
        return this.innerMap.remove(new IdentityWrapper(key), value);
    }

    @Override // java.util.concurrent.ConcurrentMap, java.util.Map
    public boolean replace(K key, V oldValue, V newValue) {
        return this.innerMap.replace(new IdentityWrapper<>(key), oldValue, newValue);
    }

    @Override // java.util.concurrent.ConcurrentMap, java.util.Map
    public V replace(K key, V value) {
        return this.innerMap.replace(new IdentityWrapper<>(key), value);
    }
}
