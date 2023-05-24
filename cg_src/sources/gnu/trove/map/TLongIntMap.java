package gnu.trove.map;

import gnu.trove.TIntCollection;
import gnu.trove.function.TIntFunction;
import gnu.trove.iterator.TLongIntIterator;
import gnu.trove.procedure.TIntProcedure;
import gnu.trove.procedure.TLongIntProcedure;
import gnu.trove.procedure.TLongProcedure;
import gnu.trove.set.TLongSet;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/TLongIntMap.class */
public interface TLongIntMap {
    long getNoEntryKey();

    int getNoEntryValue();

    int put(long j, int i);

    int putIfAbsent(long j, int i);

    void putAll(Map<? extends Long, ? extends Integer> map);

    void putAll(TLongIntMap tLongIntMap);

    int get(long j);

    void clear();

    boolean isEmpty();

    int remove(long j);

    int size();

    TLongSet keySet();

    long[] keys();

    long[] keys(long[] jArr);

    TIntCollection valueCollection();

    int[] values();

    int[] values(int[] iArr);

    boolean containsValue(int i);

    boolean containsKey(long j);

    TLongIntIterator iterator();

    boolean forEachKey(TLongProcedure tLongProcedure);

    boolean forEachValue(TIntProcedure tIntProcedure);

    boolean forEachEntry(TLongIntProcedure tLongIntProcedure);

    void transformValues(TIntFunction tIntFunction);

    boolean retainEntries(TLongIntProcedure tLongIntProcedure);

    boolean increment(long j);

    boolean adjustValue(long j, int i);

    int adjustOrPutValue(long j, int i, int i2);
}
