package gnu.trove.map;

import gnu.trove.function.TObjectFunction;
import gnu.trove.iterator.TCharObjectIterator;
import gnu.trove.procedure.TCharObjectProcedure;
import gnu.trove.procedure.TCharProcedure;
import gnu.trove.procedure.TObjectProcedure;
import gnu.trove.set.TCharSet;
import java.util.Collection;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/TCharObjectMap.class */
public interface TCharObjectMap<V> {
    char getNoEntryKey();

    int size();

    boolean isEmpty();

    boolean containsKey(char c);

    boolean containsValue(Object obj);

    V get(char c);

    V put(char c, V v);

    V putIfAbsent(char c, V v);

    V remove(char c);

    void putAll(Map<? extends Character, ? extends V> map);

    void putAll(TCharObjectMap<? extends V> tCharObjectMap);

    void clear();

    TCharSet keySet();

    char[] keys();

    char[] keys(char[] cArr);

    Collection<V> valueCollection();

    Object[] values();

    V[] values(V[] vArr);

    TCharObjectIterator<V> iterator();

    boolean forEachKey(TCharProcedure tCharProcedure);

    boolean forEachValue(TObjectProcedure<? super V> tObjectProcedure);

    boolean forEachEntry(TCharObjectProcedure<? super V> tCharObjectProcedure);

    void transformValues(TObjectFunction<V, V> tObjectFunction);

    boolean retainEntries(TCharObjectProcedure<? super V> tCharObjectProcedure);

    boolean equals(Object obj);

    int hashCode();
}
