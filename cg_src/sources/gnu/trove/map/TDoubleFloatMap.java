package gnu.trove.map;

import gnu.trove.TFloatCollection;
import gnu.trove.function.TFloatFunction;
import gnu.trove.iterator.TDoubleFloatIterator;
import gnu.trove.procedure.TDoubleFloatProcedure;
import gnu.trove.procedure.TDoubleProcedure;
import gnu.trove.procedure.TFloatProcedure;
import gnu.trove.set.TDoubleSet;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/TDoubleFloatMap.class */
public interface TDoubleFloatMap {
    double getNoEntryKey();

    float getNoEntryValue();

    float put(double d, float f);

    float putIfAbsent(double d, float f);

    void putAll(Map<? extends Double, ? extends Float> map);

    void putAll(TDoubleFloatMap tDoubleFloatMap);

    float get(double d);

    void clear();

    boolean isEmpty();

    float remove(double d);

    int size();

    TDoubleSet keySet();

    double[] keys();

    double[] keys(double[] dArr);

    TFloatCollection valueCollection();

    float[] values();

    float[] values(float[] fArr);

    boolean containsValue(float f);

    boolean containsKey(double d);

    TDoubleFloatIterator iterator();

    boolean forEachKey(TDoubleProcedure tDoubleProcedure);

    boolean forEachValue(TFloatProcedure tFloatProcedure);

    boolean forEachEntry(TDoubleFloatProcedure tDoubleFloatProcedure);

    void transformValues(TFloatFunction tFloatFunction);

    boolean retainEntries(TDoubleFloatProcedure tDoubleFloatProcedure);

    boolean increment(double d);

    boolean adjustValue(double d, float f);

    float adjustOrPutValue(double d, float f, float f2);
}
