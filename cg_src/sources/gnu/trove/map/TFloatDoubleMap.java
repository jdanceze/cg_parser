package gnu.trove.map;

import gnu.trove.TDoubleCollection;
import gnu.trove.function.TDoubleFunction;
import gnu.trove.iterator.TFloatDoubleIterator;
import gnu.trove.procedure.TDoubleProcedure;
import gnu.trove.procedure.TFloatDoubleProcedure;
import gnu.trove.procedure.TFloatProcedure;
import gnu.trove.set.TFloatSet;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/TFloatDoubleMap.class */
public interface TFloatDoubleMap {
    float getNoEntryKey();

    double getNoEntryValue();

    double put(float f, double d);

    double putIfAbsent(float f, double d);

    void putAll(Map<? extends Float, ? extends Double> map);

    void putAll(TFloatDoubleMap tFloatDoubleMap);

    double get(float f);

    void clear();

    boolean isEmpty();

    double remove(float f);

    int size();

    TFloatSet keySet();

    float[] keys();

    float[] keys(float[] fArr);

    TDoubleCollection valueCollection();

    double[] values();

    double[] values(double[] dArr);

    boolean containsValue(double d);

    boolean containsKey(float f);

    TFloatDoubleIterator iterator();

    boolean forEachKey(TFloatProcedure tFloatProcedure);

    boolean forEachValue(TDoubleProcedure tDoubleProcedure);

    boolean forEachEntry(TFloatDoubleProcedure tFloatDoubleProcedure);

    void transformValues(TDoubleFunction tDoubleFunction);

    boolean retainEntries(TFloatDoubleProcedure tFloatDoubleProcedure);

    boolean increment(float f);

    boolean adjustValue(float f, double d);

    double adjustOrPutValue(float f, double d, double d2);
}
