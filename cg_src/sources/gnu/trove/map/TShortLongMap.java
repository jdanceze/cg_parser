package gnu.trove.map;

import gnu.trove.TLongCollection;
import gnu.trove.function.TLongFunction;
import gnu.trove.iterator.TShortLongIterator;
import gnu.trove.procedure.TLongProcedure;
import gnu.trove.procedure.TShortLongProcedure;
import gnu.trove.procedure.TShortProcedure;
import gnu.trove.set.TShortSet;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/TShortLongMap.class */
public interface TShortLongMap {
    short getNoEntryKey();

    long getNoEntryValue();

    long put(short s, long j);

    long putIfAbsent(short s, long j);

    void putAll(Map<? extends Short, ? extends Long> map);

    void putAll(TShortLongMap tShortLongMap);

    long get(short s);

    void clear();

    boolean isEmpty();

    long remove(short s);

    int size();

    TShortSet keySet();

    short[] keys();

    short[] keys(short[] sArr);

    TLongCollection valueCollection();

    long[] values();

    long[] values(long[] jArr);

    boolean containsValue(long j);

    boolean containsKey(short s);

    TShortLongIterator iterator();

    boolean forEachKey(TShortProcedure tShortProcedure);

    boolean forEachValue(TLongProcedure tLongProcedure);

    boolean forEachEntry(TShortLongProcedure tShortLongProcedure);

    void transformValues(TLongFunction tLongFunction);

    boolean retainEntries(TShortLongProcedure tShortLongProcedure);

    boolean increment(short s);

    boolean adjustValue(short s, long j);

    long adjustOrPutValue(short s, long j, long j2);
}
