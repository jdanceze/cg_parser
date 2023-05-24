package gnu.trove.map;

import gnu.trove.TLongCollection;
import gnu.trove.function.TLongFunction;
import gnu.trove.iterator.TObjectLongIterator;
import gnu.trove.procedure.TLongProcedure;
import gnu.trove.procedure.TObjectLongProcedure;
import gnu.trove.procedure.TObjectProcedure;
import java.util.Map;
import java.util.Set;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/TObjectLongMap.class */
public interface TObjectLongMap<K> {
    long getNoEntryValue();

    int size();

    boolean isEmpty();

    boolean containsKey(Object obj);

    boolean containsValue(long j);

    long get(Object obj);

    long put(K k, long j);

    long putIfAbsent(K k, long j);

    long remove(Object obj);

    void putAll(Map<? extends K, ? extends Long> map);

    void putAll(TObjectLongMap<? extends K> tObjectLongMap);

    void clear();

    Set<K> keySet();

    Object[] keys();

    K[] keys(K[] kArr);

    TLongCollection valueCollection();

    long[] values();

    long[] values(long[] jArr);

    TObjectLongIterator<K> iterator();

    boolean increment(K k);

    boolean adjustValue(K k, long j);

    long adjustOrPutValue(K k, long j, long j2);

    boolean forEachKey(TObjectProcedure<? super K> tObjectProcedure);

    boolean forEachValue(TLongProcedure tLongProcedure);

    boolean forEachEntry(TObjectLongProcedure<? super K> tObjectLongProcedure);

    void transformValues(TLongFunction tLongFunction);

    boolean retainEntries(TObjectLongProcedure<? super K> tObjectLongProcedure);

    boolean equals(Object obj);

    int hashCode();
}
