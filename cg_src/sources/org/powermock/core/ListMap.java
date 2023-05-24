package org.powermock.core;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
/* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/core/ListMap.class */
public class ListMap<K, V> implements Map<K, V> {
    private List<Map.Entry<K, V>> entries = new LinkedList();

    /* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/core/ListMap$SimpleEntry.class */
    private static class SimpleEntry<K, V> implements Map.Entry<K, V> {
        private K key;
        private V value;

        public SimpleEntry(K key, V value) {
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
            V old = this.value;
            this.value = value;
            return old;
        }
    }

    @Override // java.util.Map
    public V remove(Object key) {
        Iterator<Map.Entry<K, V>> i = this.entries.iterator();
        while (i.hasNext()) {
            Map.Entry<K, V> entry = i.next();
            if (entry.getKey() == key) {
                i.remove();
                return entry.getValue();
            }
        }
        return null;
    }

    @Override // java.util.Map
    public void clear() {
        this.entries.clear();
    }

    @Override // java.util.Map
    public V get(Object key) {
        for (Map.Entry<K, V> entry : this.entries) {
            if (entry.getKey() == key) {
                return entry.getValue();
            }
        }
        return null;
    }

    @Override // java.util.Map
    public V put(K key, V value) {
        for (Map.Entry<K, V> entry : this.entries) {
            if (entry.getKey() == key) {
                return entry.setValue(value);
            }
        }
        this.entries.add(new SimpleEntry<>(key, value));
        return null;
    }

    @Override // java.util.Map
    public boolean containsKey(Object key) {
        return get(key) != null;
    }

    @Override // java.util.Map
    public boolean containsValue(Object value) {
        for (Map.Entry<K, V> entry : this.entries) {
            if (entry.getValue() == value) {
                return true;
            }
        }
        return false;
    }

    @Override // java.util.Map
    public Set<Map.Entry<K, V>> entrySet() {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.Map
    public boolean isEmpty() {
        return this.entries.isEmpty();
    }

    @Override // java.util.Map
    public Set<K> keySet() {
        Set<K> identityHashSet = new HashSet<>();
        for (Map.Entry<K, V> entry : this.entries) {
            identityHashSet.add(entry.getKey());
        }
        return identityHashSet;
    }

    @Override // java.util.Map
    public void putAll(Map<? extends K, ? extends V> t) {
        Set<?> entrySet = t.entrySet();
        for (Object object : entrySet) {
            this.entries.add((Map.Entry) object);
        }
    }

    @Override // java.util.Map
    public int size() {
        return this.entries.size();
    }

    @Override // java.util.Map
    public Collection<V> values() {
        Set<V> hashSet = new HashSet<>();
        for (Map.Entry<K, V> entry : this.entries) {
            hashSet.add(entry.getValue());
        }
        return hashSet;
    }
}
