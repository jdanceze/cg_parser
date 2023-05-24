package gnu.trove.map;

import gnu.trove.TFloatCollection;
import gnu.trove.function.TFloatFunction;
import gnu.trove.iterator.TLongFloatIterator;
import gnu.trove.procedure.TFloatProcedure;
import gnu.trove.procedure.TLongFloatProcedure;
import gnu.trove.procedure.TLongProcedure;
import gnu.trove.set.TLongSet;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/TLongFloatMap.class */
public interface TLongFloatMap {
    long getNoEntryKey();

    float getNoEntryValue();

    float put(long j, float f);

    float putIfAbsent(long j, float f);

    void putAll(Map<? extends Long, ? extends Float> map);

    void putAll(TLongFloatMap tLongFloatMap);

    float get(long j);

    void clear();

    boolean isEmpty();

    float remove(long j);

    int size();

    TLongSet keySet();

    long[] keys();

    long[] keys(long[] jArr);

    TFloatCollection valueCollection();

    float[] values();

    float[] values(float[] fArr);

    boolean containsValue(float f);

    boolean containsKey(long j);

    TLongFloatIterator iterator();

    boolean forEachKey(TLongProcedure tLongProcedure);

    boolean forEachValue(TFloatProcedure tFloatProcedure);

    boolean forEachEntry(TLongFloatProcedure tLongFloatProcedure);

    void transformValues(TFloatFunction tFloatFunction);

    boolean retainEntries(TLongFloatProcedure tLongFloatProcedure);

    boolean increment(long j);

    boolean adjustValue(long j, float f);

    float adjustOrPutValue(long j, float f, float f2);
}
