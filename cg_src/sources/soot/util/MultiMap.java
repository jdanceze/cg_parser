package soot.util;

import heros.solver.Pair;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
/* loaded from: gencallgraphv3.jar:soot/util/MultiMap.class */
public interface MultiMap<K, V> extends Iterable<Pair<K, V>> {
    boolean isEmpty();

    int numKeys();

    boolean contains(K k, V v);

    boolean containsKey(K k);

    boolean containsValue(V v);

    boolean put(K k, V v);

    boolean putAll(K k, Collection<V> collection);

    boolean putAll(Map<K, Collection<V>> map);

    boolean putMap(Map<K, V> map);

    boolean putAll(MultiMap<K, V> multiMap);

    boolean remove(K k, V v);

    boolean remove(K k);

    boolean removeAll(K k, Collection<V> collection);

    Set<V> get(K k);

    Set<K> keySet();

    Set<V> values();

    int size();

    void clear();
}
