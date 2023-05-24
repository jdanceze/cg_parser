package soot.util;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
/* loaded from: gencallgraphv3.jar:soot/util/HashMultiMap.class */
public class HashMultiMap<K, V> extends AbstractMultiMap<K, V> {
    private static final long serialVersionUID = -1928446853508616896L;
    private static final float DEFAULT_LOAD_FACTOR = 0.7f;
    protected final Map<K, Set<V>> m;
    protected final float loadFactor;

    protected Map<K, Set<V>> createMap() {
        return createMap(0);
    }

    protected Map<K, Set<V>> createMap(int initialSize) {
        return new HashMap(initialSize, this.loadFactor);
    }

    public HashMultiMap() {
        this.loadFactor = DEFAULT_LOAD_FACTOR;
        this.m = createMap();
    }

    public HashMultiMap(int initialSize) {
        this.loadFactor = DEFAULT_LOAD_FACTOR;
        this.m = createMap(initialSize);
    }

    public HashMultiMap(int initialSize, float loadFactor) {
        this.loadFactor = loadFactor;
        this.m = createMap(initialSize);
    }

    public HashMultiMap(MultiMap<K, V> m) {
        this.loadFactor = DEFAULT_LOAD_FACTOR;
        this.m = createMap();
        putAll(m);
    }

    public HashMultiMap(Map<K, Collection<V>> m) {
        this.loadFactor = DEFAULT_LOAD_FACTOR;
        this.m = createMap();
        putAll(m);
    }

    @Override // soot.util.MultiMap
    public int numKeys() {
        return this.m.size();
    }

    @Override // soot.util.MultiMap
    public boolean containsKey(Object key) {
        return this.m.containsKey(key);
    }

    @Override // soot.util.MultiMap
    public boolean containsValue(V value) {
        for (Set<V> s : this.m.values()) {
            if (s.contains(value)) {
                return true;
            }
        }
        return false;
    }

    protected Set<V> newSet() {
        return new HashSet(4);
    }

    private Set<V> findSet(K key) {
        Set<V> s = this.m.get(key);
        if (s == null) {
            s = newSet();
            this.m.put(key, s);
        }
        return s;
    }

    @Override // soot.util.MultiMap
    public boolean put(K key, V value) {
        return findSet(key).add(value);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // soot.util.MultiMap
    public boolean putAll(K key, Collection<V> values) {
        if (values.isEmpty()) {
            return false;
        }
        return findSet(key).addAll(values);
    }

    @Override // soot.util.MultiMap
    public boolean remove(K key, V value) {
        Set<V> s = this.m.get(key);
        if (s == null) {
            return false;
        }
        boolean ret = s.remove(value);
        if (s.isEmpty()) {
            this.m.remove(key);
        }
        return ret;
    }

    @Override // soot.util.MultiMap
    public boolean remove(K key) {
        return this.m.remove(key) != null;
    }

    @Override // soot.util.MultiMap
    public boolean removeAll(K key, Collection<V> values) {
        Set<V> s = this.m.get(key);
        if (s == null) {
            return false;
        }
        boolean ret = s.removeAll(values);
        if (s.isEmpty()) {
            this.m.remove(key);
        }
        return ret;
    }

    @Override // soot.util.MultiMap
    public Set<V> get(K o) {
        Set<V> ret = this.m.get(o);
        return ret == null ? Collections.emptySet() : ret;
    }

    @Override // soot.util.MultiMap
    public Set<K> keySet() {
        return this.m.keySet();
    }

    @Override // soot.util.MultiMap
    public Set<V> values() {
        Set<V> ret = new HashSet<>(this.m.size());
        for (Set<V> s : this.m.values()) {
            ret.addAll(s);
        }
        return ret;
    }

    public boolean equals(Object o) {
        if (!(o instanceof MultiMap)) {
            return false;
        }
        MultiMap<K, V> mm = (MultiMap) o;
        if (!keySet().equals(mm.keySet())) {
            return false;
        }
        for (Map.Entry<K, Set<V>> e : this.m.entrySet()) {
            Set<V> s = e.getValue();
            if (!s.equals(mm.get(e.getKey()))) {
                return false;
            }
        }
        return true;
    }

    public int hashCode() {
        return this.m.hashCode();
    }

    @Override // soot.util.MultiMap
    public int size() {
        return this.m.size();
    }

    @Override // soot.util.MultiMap
    public void clear() {
        this.m.clear();
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<K, Set<V>> entry : this.m.entrySet()) {
            builder.append(entry.getKey()).append(":\n").append(entry.getValue().toString()).append("\n\n");
        }
        if (builder.length() > 2) {
            builder.delete(builder.length() - 2, builder.length());
        }
        return builder.toString();
    }
}
