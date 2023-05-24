package soot.util;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
/* loaded from: gencallgraphv3.jar:soot/util/ConcurrentHashMultiMap.class */
public class ConcurrentHashMultiMap<K, V> extends AbstractMultiMap<K, V> {
    private static final long serialVersionUID = -3182515910302586044L;
    private final Map<K, ConcurrentMap<V, V>> m = new ConcurrentHashMap(0);

    public ConcurrentHashMultiMap() {
    }

    public ConcurrentHashMultiMap(MultiMap<K, V> m) {
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
        for (Map<V, V> s : this.m.values()) {
            if (s.containsKey(value)) {
                return true;
            }
        }
        return false;
    }

    protected ConcurrentMap<V, V> newSet() {
        return new ConcurrentHashMap();
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v13 */
    /* JADX WARN: Type inference failed for: r0v6 */
    /* JADX WARN: Type inference failed for: r0v7, types: [java.lang.Throwable] */
    private ConcurrentMap<V, V> findSet(K key) {
        ConcurrentMap<V, V> s = this.m.get(key);
        if (s == null) {
            ?? r0 = this;
            synchronized (r0) {
                s = this.m.get(key);
                if (s == null) {
                    s = newSet();
                    this.m.put(key, s);
                }
                r0 = r0;
            }
        }
        return s;
    }

    @Override // soot.util.MultiMap
    public boolean put(K key, V value) {
        return findSet(key).put(value, value) == null;
    }

    public V putIfAbsent(K key, V value) {
        return findSet(key).putIfAbsent(value, value);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // soot.util.MultiMap
    public boolean putAll(K key, Collection<V> values) {
        if (values == null || values.isEmpty()) {
            return false;
        }
        ConcurrentMap<V, V> s = this.m.get(key);
        if (s == null) {
            synchronized (this) {
                s = this.m.get(key);
                if (s == null) {
                    ConcurrentMap<V, V> newSet = newSet();
                    for (V v : values) {
                        newSet.put(v, v);
                    }
                    this.m.put(key, newSet);
                    return true;
                }
            }
        }
        boolean ok = false;
        for (V v2 : values) {
            if (s.put(v2, v2) == null) {
                ok = true;
            }
        }
        return ok;
    }

    @Override // soot.util.MultiMap
    public boolean remove(K key, V value) {
        Map<V, V> s = this.m.get(key);
        if (s == null) {
            return false;
        }
        boolean ret = s.remove(value) != null;
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
        Map<V, V> s = this.m.get(key);
        if (s == null) {
            return false;
        }
        boolean ret = false;
        for (V v : values) {
            if (s.remove(v) != null) {
                ret = true;
            }
        }
        if (s.isEmpty()) {
            this.m.remove(key);
        }
        return ret;
    }

    @Override // soot.util.MultiMap
    public Set<V> get(K o) {
        Map<V, V> ret = this.m.get(o);
        if (ret == null) {
            return Collections.emptySet();
        }
        return Collections.unmodifiableSet(ret.keySet());
    }

    @Override // soot.util.MultiMap
    public Set<K> keySet() {
        return this.m.keySet();
    }

    @Override // soot.util.MultiMap
    public Set<V> values() {
        Set<V> ret = new HashSet<>(this.m.size());
        for (Map<V, V> s : this.m.values()) {
            ret.addAll(s.keySet());
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
        for (Map.Entry<K, ConcurrentMap<V, V>> e : this.m.entrySet()) {
            Map<V, V> s = e.getValue();
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
        return this.m.toString();
    }
}
