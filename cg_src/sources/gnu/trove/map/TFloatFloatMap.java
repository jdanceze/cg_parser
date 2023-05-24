package gnu.trove.map;

import gnu.trove.TFloatCollection;
import gnu.trove.function.TFloatFunction;
import gnu.trove.iterator.TFloatFloatIterator;
import gnu.trove.procedure.TFloatFloatProcedure;
import gnu.trove.procedure.TFloatProcedure;
import gnu.trove.set.TFloatSet;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/TFloatFloatMap.class */
public interface TFloatFloatMap {
    float getNoEntryKey();

    float getNoEntryValue();

    float put(float f, float f2);

    float putIfAbsent(float f, float f2);

    void putAll(Map<? extends Float, ? extends Float> map);

    void putAll(TFloatFloatMap tFloatFloatMap);

    float get(float f);

    void clear();

    boolean isEmpty();

    float remove(float f);

    int size();

    TFloatSet keySet();

    float[] keys();

    float[] keys(float[] fArr);

    TFloatCollection valueCollection();

    float[] values();

    float[] values(float[] fArr);

    boolean containsValue(float f);

    boolean containsKey(float f);

    TFloatFloatIterator iterator();

    boolean forEachKey(TFloatProcedure tFloatProcedure);

    boolean forEachValue(TFloatProcedure tFloatProcedure);

    boolean forEachEntry(TFloatFloatProcedure tFloatFloatProcedure);

    void transformValues(TFloatFunction tFloatFunction);

    boolean retainEntries(TFloatFloatProcedure tFloatFloatProcedure);

    boolean increment(float f);

    boolean adjustValue(float f, float f2);

    float adjustOrPutValue(float f, float f2, float f3);
}
