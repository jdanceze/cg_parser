package soot.jimple.spark.ondemand.genericutil;

import java.util.Collection;
import java.util.Set;
/* loaded from: gencallgraphv3.jar:soot/jimple/spark/ondemand/genericutil/MultiMap.class */
public interface MultiMap<K, V> {
    Set<V> get(K k);

    boolean put(K k, V v);

    boolean remove(K k, V v);

    Set<K> keySet();

    boolean containsKey(K k);

    int size();

    String toString();

    boolean putAll(K k, Collection<? extends V> collection);

    Set<V> removeAll(K k);

    void clear();

    boolean isEmpty();
}
