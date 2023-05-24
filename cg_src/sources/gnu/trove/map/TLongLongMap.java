package gnu.trove.map;

import gnu.trove.TLongCollection;
import gnu.trove.function.TLongFunction;
import gnu.trove.iterator.TLongLongIterator;
import gnu.trove.procedure.TLongLongProcedure;
import gnu.trove.procedure.TLongProcedure;
import gnu.trove.set.TLongSet;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/TLongLongMap.class */
public interface TLongLongMap {
    long getNoEntryKey();

    long getNoEntryValue();

    long put(long j, long j2);

    long putIfAbsent(long j, long j2);

    void putAll(Map<? extends Long, ? extends Long> map);

    void putAll(TLongLongMap tLongLongMap);

    long get(long j);

    void clear();

    boolean isEmpty();

    long remove(long j);

    int size();

    TLongSet keySet();

    long[] keys();

    long[] keys(long[] jArr);

    TLongCollection valueCollection();

    long[] values();

    long[] values(long[] jArr);

    boolean containsValue(long j);

    boolean containsKey(long j);

    TLongLongIterator iterator();

    boolean forEachKey(TLongProcedure tLongProcedure);

    boolean forEachValue(TLongProcedure tLongProcedure);

    boolean forEachEntry(TLongLongProcedure tLongLongProcedure);

    void transformValues(TLongFunction tLongFunction);

    boolean retainEntries(TLongLongProcedure tLongLongProcedure);

    boolean increment(long j);

    boolean adjustValue(long j, long j2);

    long adjustOrPutValue(long j, long j2, long j3);
}
