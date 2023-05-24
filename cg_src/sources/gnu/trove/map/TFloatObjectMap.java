package gnu.trove.map;

import gnu.trove.function.TObjectFunction;
import gnu.trove.iterator.TFloatObjectIterator;
import gnu.trove.procedure.TFloatObjectProcedure;
import gnu.trove.procedure.TFloatProcedure;
import gnu.trove.procedure.TObjectProcedure;
import gnu.trove.set.TFloatSet;
import java.util.Collection;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/TFloatObjectMap.class */
public interface TFloatObjectMap<V> {
    float getNoEntryKey();

    int size();

    boolean isEmpty();

    boolean containsKey(float f);

    boolean containsValue(Object obj);

    V get(float f);

    V put(float f, V v);

    V putIfAbsent(float f, V v);

    V remove(float f);

    void putAll(Map<? extends Float, ? extends V> map);

    void putAll(TFloatObjectMap<? extends V> tFloatObjectMap);

    void clear();

    TFloatSet keySet();

    float[] keys();

    float[] keys(float[] fArr);

    Collection<V> valueCollection();

    Object[] values();

    V[] values(V[] vArr);

    TFloatObjectIterator<V> iterator();

    boolean forEachKey(TFloatProcedure tFloatProcedure);

    boolean forEachValue(TObjectProcedure<? super V> tObjectProcedure);

    boolean forEachEntry(TFloatObjectProcedure<? super V> tFloatObjectProcedure);

    void transformValues(TObjectFunction<V, V> tObjectFunction);

    boolean retainEntries(TFloatObjectProcedure<? super V> tFloatObjectProcedure);

    boolean equals(Object obj);

    int hashCode();
}
