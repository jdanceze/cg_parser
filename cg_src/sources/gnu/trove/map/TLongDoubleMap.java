package gnu.trove.map;

import gnu.trove.TDoubleCollection;
import gnu.trove.function.TDoubleFunction;
import gnu.trove.iterator.TLongDoubleIterator;
import gnu.trove.procedure.TDoubleProcedure;
import gnu.trove.procedure.TLongDoubleProcedure;
import gnu.trove.procedure.TLongProcedure;
import gnu.trove.set.TLongSet;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/TLongDoubleMap.class */
public interface TLongDoubleMap {
    long getNoEntryKey();

    double getNoEntryValue();

    double put(long j, double d);

    double putIfAbsent(long j, double d);

    void putAll(Map<? extends Long, ? extends Double> map);

    void putAll(TLongDoubleMap tLongDoubleMap);

    double get(long j);

    void clear();

    boolean isEmpty();

    double remove(long j);

    int size();

    TLongSet keySet();

    long[] keys();

    long[] keys(long[] jArr);

    TDoubleCollection valueCollection();

    double[] values();

    double[] values(double[] dArr);

    boolean containsValue(double d);

    boolean containsKey(long j);

    TLongDoubleIterator iterator();

    boolean forEachKey(TLongProcedure tLongProcedure);

    boolean forEachValue(TDoubleProcedure tDoubleProcedure);

    boolean forEachEntry(TLongDoubleProcedure tLongDoubleProcedure);

    void transformValues(TDoubleFunction tDoubleFunction);

    boolean retainEntries(TLongDoubleProcedure tLongDoubleProcedure);

    boolean increment(long j);

    boolean adjustValue(long j, double d);

    double adjustOrPutValue(long j, double d, double d2);
}
