package gnu.trove.map;

import gnu.trove.TShortCollection;
import gnu.trove.function.TShortFunction;
import gnu.trove.iterator.TLongShortIterator;
import gnu.trove.procedure.TLongProcedure;
import gnu.trove.procedure.TLongShortProcedure;
import gnu.trove.procedure.TShortProcedure;
import gnu.trove.set.TLongSet;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/TLongShortMap.class */
public interface TLongShortMap {
    long getNoEntryKey();

    short getNoEntryValue();

    short put(long j, short s);

    short putIfAbsent(long j, short s);

    void putAll(Map<? extends Long, ? extends Short> map);

    void putAll(TLongShortMap tLongShortMap);

    short get(long j);

    void clear();

    boolean isEmpty();

    short remove(long j);

    int size();

    TLongSet keySet();

    long[] keys();

    long[] keys(long[] jArr);

    TShortCollection valueCollection();

    short[] values();

    short[] values(short[] sArr);

    boolean containsValue(short s);

    boolean containsKey(long j);

    TLongShortIterator iterator();

    boolean forEachKey(TLongProcedure tLongProcedure);

    boolean forEachValue(TShortProcedure tShortProcedure);

    boolean forEachEntry(TLongShortProcedure tLongShortProcedure);

    void transformValues(TShortFunction tShortFunction);

    boolean retainEntries(TLongShortProcedure tLongShortProcedure);

    boolean increment(long j);

    boolean adjustValue(long j, short s);

    short adjustOrPutValue(long j, short s, short s2);
}
