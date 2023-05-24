package soot.jimple.spark.ondemand.genericutil;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
/* loaded from: gencallgraphv3.jar:soot/jimple/spark/ondemand/genericutil/AbstractMultiMap.class */
abstract class AbstractMultiMap<K, V> implements MultiMap<K, V> {
    protected final Map<K, Set<V>> map = new HashMap();
    protected final boolean create;

    protected abstract Set<V> createSet();

    /* JADX INFO: Access modifiers changed from: protected */
    public AbstractMultiMap(boolean create) {
        this.create = create;
    }

    protected Set<V> emptySet() {
        return Collections.emptySet();
    }

    @Override // soot.jimple.spark.ondemand.genericutil.MultiMap
    public Set<V> get(K key) {
        Set<V> ret = this.map.get(key);
        if (ret == null) {
            if (this.create) {
                ret = createSet();
                this.map.put(key, ret);
            } else {
                ret = emptySet();
            }
        }
        return ret;
    }

    @Override // soot.jimple.spark.ondemand.genericutil.MultiMap
    public boolean put(K key, V val) {
        Set<V> vals = this.map.get(key);
        if (vals == null) {
            vals = createSet();
            this.map.put(key, vals);
        }
        return vals.add(val);
    }

    @Override // soot.jimple.spark.ondemand.genericutil.MultiMap
    public boolean remove(K key, V val) {
        Set<V> elems = this.map.get(key);
        if (elems == null) {
            return false;
        }
        boolean ret = elems.remove(val);
        if (elems.isEmpty()) {
            this.map.remove(key);
        }
        return ret;
    }

    @Override // soot.jimple.spark.ondemand.genericutil.MultiMap
    public Set<V> removeAll(K key) {
        return this.map.remove(key);
    }

    @Override // soot.jimple.spark.ondemand.genericutil.MultiMap
    public Set<K> keySet() {
        return this.map.keySet();
    }

    @Override // soot.jimple.spark.ondemand.genericutil.MultiMap
    public boolean containsKey(K key) {
        return this.map.containsKey(key);
    }

    @Override // soot.jimple.spark.ondemand.genericutil.MultiMap
    public int size() {
        int ret = 0;
        for (K key : keySet()) {
            ret += get(key).size();
        }
        return ret;
    }

    @Override // soot.jimple.spark.ondemand.genericutil.MultiMap
    public String toString() {
        return this.map.toString();
    }

    @Override // soot.jimple.spark.ondemand.genericutil.MultiMap
    public boolean putAll(K key, Collection<? extends V> vals) {
        Set<V> edges = this.map.get(key);
        if (edges == null) {
            edges = createSet();
            this.map.put(key, edges);
        }
        return edges.addAll(vals);
    }

    @Override // soot.jimple.spark.ondemand.genericutil.MultiMap
    public void clear() {
        this.map.clear();
    }

    @Override // soot.jimple.spark.ondemand.genericutil.MultiMap
    public boolean isEmpty() {
        return this.map.isEmpty();
    }
}
