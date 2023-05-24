package gnu.trove.map;

import gnu.trove.function.TObjectFunction;
import gnu.trove.iterator.TDoubleObjectIterator;
import gnu.trove.procedure.TDoubleObjectProcedure;
import gnu.trove.procedure.TDoubleProcedure;
import gnu.trove.procedure.TObjectProcedure;
import gnu.trove.set.TDoubleSet;
import java.util.Collection;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/TDoubleObjectMap.class */
public interface TDoubleObjectMap<V> {
    double getNoEntryKey();

    int size();

    boolean isEmpty();

    boolean containsKey(double d);

    boolean containsValue(Object obj);

    V get(double d);

    V put(double d, V v);

    V putIfAbsent(double d, V v);

    V remove(double d);

    void putAll(Map<? extends Double, ? extends V> map);

    void putAll(TDoubleObjectMap<? extends V> tDoubleObjectMap);

    void clear();

    TDoubleSet keySet();

    double[] keys();

    double[] keys(double[] dArr);

    Collection<V> valueCollection();

    Object[] values();

    V[] values(V[] vArr);

    TDoubleObjectIterator<V> iterator();

    boolean forEachKey(TDoubleProcedure tDoubleProcedure);

    boolean forEachValue(TObjectProcedure<? super V> tObjectProcedure);

    boolean forEachEntry(TDoubleObjectProcedure<? super V> tDoubleObjectProcedure);

    void transformValues(TObjectFunction<V, V> tObjectFunction);

    boolean retainEntries(TDoubleObjectProcedure<? super V> tDoubleObjectProcedure);

    boolean equals(Object obj);

    int hashCode();
}
