package gnu.trove.map;

import gnu.trove.TLongCollection;
import gnu.trove.function.TLongFunction;
import gnu.trove.iterator.TIntLongIterator;
import gnu.trove.procedure.TIntLongProcedure;
import gnu.trove.procedure.TIntProcedure;
import gnu.trove.procedure.TLongProcedure;
import gnu.trove.set.TIntSet;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/TIntLongMap.class */
public interface TIntLongMap {
    int getNoEntryKey();

    long getNoEntryValue();

    long put(int i, long j);

    long putIfAbsent(int i, long j);

    void putAll(Map<? extends Integer, ? extends Long> map);

    void putAll(TIntLongMap tIntLongMap);

    long get(int i);

    void clear();

    boolean isEmpty();

    long remove(int i);

    int size();

    TIntSet keySet();

    int[] keys();

    int[] keys(int[] iArr);

    TLongCollection valueCollection();

    long[] values();

    long[] values(long[] jArr);

    boolean containsValue(long j);

    boolean containsKey(int i);

    TIntLongIterator iterator();

    boolean forEachKey(TIntProcedure tIntProcedure);

    boolean forEachValue(TLongProcedure tLongProcedure);

    boolean forEachEntry(TIntLongProcedure tIntLongProcedure);

    void transformValues(TLongFunction tLongFunction);

    boolean retainEntries(TIntLongProcedure tIntLongProcedure);

    boolean increment(int i);

    boolean adjustValue(int i, long j);

    long adjustOrPutValue(int i, long j, long j2);
}
