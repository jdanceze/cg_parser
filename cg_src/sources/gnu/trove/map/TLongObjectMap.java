package gnu.trove.map;

import gnu.trove.function.TObjectFunction;
import gnu.trove.iterator.TLongObjectIterator;
import gnu.trove.procedure.TLongObjectProcedure;
import gnu.trove.procedure.TLongProcedure;
import gnu.trove.procedure.TObjectProcedure;
import gnu.trove.set.TLongSet;
import java.util.Collection;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/TLongObjectMap.class */
public interface TLongObjectMap<V> {
    long getNoEntryKey();

    int size();

    boolean isEmpty();

    boolean containsKey(long j);

    boolean containsValue(Object obj);

    V get(long j);

    V put(long j, V v);

    V putIfAbsent(long j, V v);

    V remove(long j);

    void putAll(Map<? extends Long, ? extends V> map);

    void putAll(TLongObjectMap<? extends V> tLongObjectMap);

    void clear();

    TLongSet keySet();

    long[] keys();

    long[] keys(long[] jArr);

    Collection<V> valueCollection();

    Object[] values();

    V[] values(V[] vArr);

    TLongObjectIterator<V> iterator();

    boolean forEachKey(TLongProcedure tLongProcedure);

    boolean forEachValue(TObjectProcedure<? super V> tObjectProcedure);

    boolean forEachEntry(TLongObjectProcedure<? super V> tLongObjectProcedure);

    void transformValues(TObjectFunction<V, V> tObjectFunction);

    boolean retainEntries(TLongObjectProcedure<? super V> tLongObjectProcedure);

    boolean equals(Object obj);

    int hashCode();
}
