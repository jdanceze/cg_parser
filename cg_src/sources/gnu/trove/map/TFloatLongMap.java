package gnu.trove.map;

import gnu.trove.TLongCollection;
import gnu.trove.function.TLongFunction;
import gnu.trove.iterator.TFloatLongIterator;
import gnu.trove.procedure.TFloatLongProcedure;
import gnu.trove.procedure.TFloatProcedure;
import gnu.trove.procedure.TLongProcedure;
import gnu.trove.set.TFloatSet;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/TFloatLongMap.class */
public interface TFloatLongMap {
    float getNoEntryKey();

    long getNoEntryValue();

    long put(float f, long j);

    long putIfAbsent(float f, long j);

    void putAll(Map<? extends Float, ? extends Long> map);

    void putAll(TFloatLongMap tFloatLongMap);

    long get(float f);

    void clear();

    boolean isEmpty();

    long remove(float f);

    int size();

    TFloatSet keySet();

    float[] keys();

    float[] keys(float[] fArr);

    TLongCollection valueCollection();

    long[] values();

    long[] values(long[] jArr);

    boolean containsValue(long j);

    boolean containsKey(float f);

    TFloatLongIterator iterator();

    boolean forEachKey(TFloatProcedure tFloatProcedure);

    boolean forEachValue(TLongProcedure tLongProcedure);

    boolean forEachEntry(TFloatLongProcedure tFloatLongProcedure);

    void transformValues(TLongFunction tLongFunction);

    boolean retainEntries(TFloatLongProcedure tFloatLongProcedure);

    boolean increment(float f);

    boolean adjustValue(float f, long j);

    long adjustOrPutValue(float f, long j, long j2);
}
