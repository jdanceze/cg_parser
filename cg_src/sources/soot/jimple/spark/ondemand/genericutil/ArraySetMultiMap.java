package soot.jimple.spark.ondemand.genericutil;

import java.util.Collection;
import java.util.Set;
/* loaded from: gencallgraphv3.jar:soot/jimple/spark/ondemand/genericutil/ArraySetMultiMap.class */
public class ArraySetMultiMap<K, V> extends AbstractMultiMap<K, V> {
    public static final ArraySetMultiMap EMPTY = new ArraySetMultiMap<Object, Object>() { // from class: soot.jimple.spark.ondemand.genericutil.ArraySetMultiMap.1
        @Override // soot.jimple.spark.ondemand.genericutil.ArraySetMultiMap, soot.jimple.spark.ondemand.genericutil.AbstractMultiMap, soot.jimple.spark.ondemand.genericutil.MultiMap
        public boolean put(Object key, Object val) {
            throw new RuntimeException();
        }

        @Override // soot.jimple.spark.ondemand.genericutil.ArraySetMultiMap, soot.jimple.spark.ondemand.genericutil.AbstractMultiMap, soot.jimple.spark.ondemand.genericutil.MultiMap
        public boolean putAll(Object key, Collection<? extends Object> vals) {
            throw new RuntimeException();
        }
    };

    @Override // soot.jimple.spark.ondemand.genericutil.AbstractMultiMap, soot.jimple.spark.ondemand.genericutil.MultiMap
    public /* bridge */ /* synthetic */ Set keySet() {
        return super.keySet();
    }

    @Override // soot.jimple.spark.ondemand.genericutil.AbstractMultiMap, soot.jimple.spark.ondemand.genericutil.MultiMap
    public /* bridge */ /* synthetic */ void clear() {
        super.clear();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // soot.jimple.spark.ondemand.genericutil.AbstractMultiMap, soot.jimple.spark.ondemand.genericutil.MultiMap
    public /* bridge */ /* synthetic */ boolean putAll(Object obj, Collection collection) {
        return super.putAll(obj, collection);
    }

    @Override // soot.jimple.spark.ondemand.genericutil.AbstractMultiMap, soot.jimple.spark.ondemand.genericutil.MultiMap
    public /* bridge */ /* synthetic */ String toString() {
        return super.toString();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // soot.jimple.spark.ondemand.genericutil.AbstractMultiMap, soot.jimple.spark.ondemand.genericutil.MultiMap
    public /* bridge */ /* synthetic */ Set removeAll(Object obj) {
        return super.removeAll(obj);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // soot.jimple.spark.ondemand.genericutil.AbstractMultiMap, soot.jimple.spark.ondemand.genericutil.MultiMap
    public /* bridge */ /* synthetic */ Set get(Object obj) {
        return get((ArraySetMultiMap<K, V>) obj);
    }

    @Override // soot.jimple.spark.ondemand.genericutil.AbstractMultiMap, soot.jimple.spark.ondemand.genericutil.MultiMap
    public /* bridge */ /* synthetic */ boolean isEmpty() {
        return super.isEmpty();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // soot.jimple.spark.ondemand.genericutil.AbstractMultiMap, soot.jimple.spark.ondemand.genericutil.MultiMap
    public /* bridge */ /* synthetic */ boolean containsKey(Object obj) {
        return super.containsKey(obj);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // soot.jimple.spark.ondemand.genericutil.AbstractMultiMap, soot.jimple.spark.ondemand.genericutil.MultiMap
    public /* bridge */ /* synthetic */ boolean remove(Object obj, Object obj2) {
        return super.remove(obj, obj2);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // soot.jimple.spark.ondemand.genericutil.AbstractMultiMap, soot.jimple.spark.ondemand.genericutil.MultiMap
    public /* bridge */ /* synthetic */ boolean put(Object obj, Object obj2) {
        return super.put(obj, obj2);
    }

    @Override // soot.jimple.spark.ondemand.genericutil.AbstractMultiMap, soot.jimple.spark.ondemand.genericutil.MultiMap
    public /* bridge */ /* synthetic */ int size() {
        return super.size();
    }

    public ArraySetMultiMap() {
        super(false);
    }

    public ArraySetMultiMap(boolean create) {
        super(create);
    }

    @Override // soot.jimple.spark.ondemand.genericutil.AbstractMultiMap
    protected Set<V> createSet() {
        return new ArraySet();
    }

    @Override // soot.jimple.spark.ondemand.genericutil.AbstractMultiMap
    protected Set<V> emptySet() {
        return ArraySet.empty();
    }

    @Override // soot.jimple.spark.ondemand.genericutil.AbstractMultiMap, soot.jimple.spark.ondemand.genericutil.MultiMap
    public ArraySet<V> get(K key) {
        return (ArraySet) super.get((ArraySetMultiMap<K, V>) key);
    }
}
