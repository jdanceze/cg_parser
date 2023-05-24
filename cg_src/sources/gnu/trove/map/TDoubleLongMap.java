package gnu.trove.map;

import gnu.trove.TLongCollection;
import gnu.trove.function.TLongFunction;
import gnu.trove.iterator.TDoubleLongIterator;
import gnu.trove.procedure.TDoubleLongProcedure;
import gnu.trove.procedure.TDoubleProcedure;
import gnu.trove.procedure.TLongProcedure;
import gnu.trove.set.TDoubleSet;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/TDoubleLongMap.class */
public interface TDoubleLongMap {
    double getNoEntryKey();

    long getNoEntryValue();

    long put(double d, long j);

    long putIfAbsent(double d, long j);

    void putAll(Map<? extends Double, ? extends Long> map);

    void putAll(TDoubleLongMap tDoubleLongMap);

    long get(double d);

    void clear();

    boolean isEmpty();

    long remove(double d);

    int size();

    TDoubleSet keySet();

    double[] keys();

    double[] keys(double[] dArr);

    TLongCollection valueCollection();

    long[] values();

    long[] values(long[] jArr);

    boolean containsValue(long j);

    boolean containsKey(double d);

    TDoubleLongIterator iterator();

    boolean forEachKey(TDoubleProcedure tDoubleProcedure);

    boolean forEachValue(TLongProcedure tLongProcedure);

    boolean forEachEntry(TDoubleLongProcedure tDoubleLongProcedure);

    void transformValues(TLongFunction tLongFunction);

    boolean retainEntries(TDoubleLongProcedure tDoubleLongProcedure);

    boolean increment(double d);

    boolean adjustValue(double d, long j);

    long adjustOrPutValue(double d, long j, long j2);
}
