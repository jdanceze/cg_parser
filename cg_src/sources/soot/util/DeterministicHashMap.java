package soot.util;

import java.util.HashMap;
import java.util.Set;
/* loaded from: gencallgraphv3.jar:soot/util/DeterministicHashMap.class */
public class DeterministicHashMap<K, V> extends HashMap<K, V> {
    Set<K> keys;

    public DeterministicHashMap(int initialCapacity) {
        super(initialCapacity);
        this.keys = new TrustingMonotonicArraySet();
    }

    public DeterministicHashMap(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
        this.keys = new TrustingMonotonicArraySet();
    }

    @Override // java.util.HashMap, java.util.AbstractMap, java.util.Map
    public V put(K key, V value) {
        if (!containsKey(key)) {
            this.keys.add(key);
        }
        return (V) super.put(key, value);
    }

    @Override // java.util.HashMap, java.util.AbstractMap, java.util.Map
    public V remove(Object obj) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.HashMap, java.util.AbstractMap, java.util.Map
    public Set<K> keySet() {
        return this.keys;
    }
}
