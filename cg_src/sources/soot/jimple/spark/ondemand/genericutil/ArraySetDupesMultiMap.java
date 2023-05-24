package soot.jimple.spark.ondemand.genericutil;

import java.util.Collection;
import java.util.Set;
/* loaded from: gencallgraphv3.jar:soot/jimple/spark/ondemand/genericutil/ArraySetDupesMultiMap.class */
public class ArraySetDupesMultiMap<K, V> extends AbstractMultiMap<K, V> {
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
        return super.get(obj);
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

    public ArraySetDupesMultiMap(boolean create) {
        super(create);
    }

    public ArraySetDupesMultiMap() {
        this(false);
    }

    @Override // soot.jimple.spark.ondemand.genericutil.AbstractMultiMap
    protected Set<V> createSet() {
        return new ArraySet(1, false);
    }
}
