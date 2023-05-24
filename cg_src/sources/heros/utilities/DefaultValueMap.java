package heros.utilities;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
/* loaded from: gencallgraphv3.jar:heros-1.2.3-SNAPSHOT.jar:heros/utilities/DefaultValueMap.class */
public abstract class DefaultValueMap<K, V> implements Map<K, V> {
    private HashMap<K, V> map = new HashMap<>();

    protected abstract V createItem(K k);

    @Override // java.util.Map
    public int size() {
        return this.map.size();
    }

    @Override // java.util.Map
    public boolean isEmpty() {
        return this.map.isEmpty();
    }

    @Override // java.util.Map
    public boolean containsKey(Object key) {
        return this.map.containsKey(key);
    }

    @Override // java.util.Map
    public boolean containsValue(Object value) {
        return this.map.containsValue(value);
    }

    public V getOrCreate(K key) {
        if (!this.map.containsKey(key)) {
            V value = createItem(key);
            this.map.put(key, value);
            return value;
        }
        return this.map.get(key);
    }

    @Override // java.util.Map
    public V get(Object key) {
        return this.map.get(key);
    }

    @Override // java.util.Map
    public V put(K key, V value) {
        return this.map.put(key, value);
    }

    @Override // java.util.Map
    public V remove(Object key) {
        return this.map.remove(key);
    }

    @Override // java.util.Map
    public void putAll(Map<? extends K, ? extends V> m) {
        this.map.putAll(m);
    }

    @Override // java.util.Map
    public void clear() {
        this.map.clear();
    }

    @Override // java.util.Map
    public Set<K> keySet() {
        return this.map.keySet();
    }

    @Override // java.util.Map
    public Collection<V> values() {
        return this.map.values();
    }

    @Override // java.util.Map
    public Set<Map.Entry<K, V>> entrySet() {
        return this.map.entrySet();
    }
}
