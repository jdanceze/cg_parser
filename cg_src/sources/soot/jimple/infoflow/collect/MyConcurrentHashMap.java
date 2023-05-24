package soot.jimple.infoflow.collect;

import java.util.concurrent.ConcurrentHashMap;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/collect/MyConcurrentHashMap.class */
public class MyConcurrentHashMap<K, V> extends ConcurrentHashMap<K, V> {
    private static final long serialVersionUID = 6591113627062569214L;

    @FunctionalInterface
    /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/collect/MyConcurrentHashMap$IValueFactory.class */
    public interface IValueFactory<V> {
        V createValue();
    }

    public V putIfAbsentElseGet(K key, V value) {
        V oldVal = putIfAbsent(key, value);
        return oldVal == null ? value : oldVal;
    }

    public V putIfAbsentElseGet(K key, IValueFactory<V> valueFactory) {
        V oldVal = get(key);
        if (oldVal != null) {
            return oldVal;
        }
        V value = valueFactory.createValue();
        V oldVal2 = putIfAbsent(key, value);
        return oldVal2 == null ? value : oldVal2;
    }
}
