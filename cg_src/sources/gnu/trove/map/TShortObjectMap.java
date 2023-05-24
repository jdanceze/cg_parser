package gnu.trove.map;

import gnu.trove.function.TObjectFunction;
import gnu.trove.iterator.TShortObjectIterator;
import gnu.trove.procedure.TObjectProcedure;
import gnu.trove.procedure.TShortObjectProcedure;
import gnu.trove.procedure.TShortProcedure;
import gnu.trove.set.TShortSet;
import java.util.Collection;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/TShortObjectMap.class */
public interface TShortObjectMap<V> {
    short getNoEntryKey();

    int size();

    boolean isEmpty();

    boolean containsKey(short s);

    boolean containsValue(Object obj);

    V get(short s);

    V put(short s, V v);

    V putIfAbsent(short s, V v);

    V remove(short s);

    void putAll(Map<? extends Short, ? extends V> map);

    void putAll(TShortObjectMap<? extends V> tShortObjectMap);

    void clear();

    TShortSet keySet();

    short[] keys();

    short[] keys(short[] sArr);

    Collection<V> valueCollection();

    Object[] values();

    V[] values(V[] vArr);

    TShortObjectIterator<V> iterator();

    boolean forEachKey(TShortProcedure tShortProcedure);

    boolean forEachValue(TObjectProcedure<? super V> tObjectProcedure);

    boolean forEachEntry(TShortObjectProcedure<? super V> tShortObjectProcedure);

    void transformValues(TObjectFunction<V, V> tObjectFunction);

    boolean retainEntries(TShortObjectProcedure<? super V> tShortObjectProcedure);

    boolean equals(Object obj);

    int hashCode();
}
