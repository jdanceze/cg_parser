package gnu.trove.map;

import gnu.trove.function.TObjectFunction;
import gnu.trove.iterator.TIntObjectIterator;
import gnu.trove.procedure.TIntObjectProcedure;
import gnu.trove.procedure.TIntProcedure;
import gnu.trove.procedure.TObjectProcedure;
import gnu.trove.set.TIntSet;
import java.util.Collection;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/TIntObjectMap.class */
public interface TIntObjectMap<V> {
    int getNoEntryKey();

    int size();

    boolean isEmpty();

    boolean containsKey(int i);

    boolean containsValue(Object obj);

    V get(int i);

    V put(int i, V v);

    V putIfAbsent(int i, V v);

    V remove(int i);

    void putAll(Map<? extends Integer, ? extends V> map);

    void putAll(TIntObjectMap<? extends V> tIntObjectMap);

    void clear();

    TIntSet keySet();

    int[] keys();

    int[] keys(int[] iArr);

    Collection<V> valueCollection();

    Object[] values();

    V[] values(V[] vArr);

    TIntObjectIterator<V> iterator();

    boolean forEachKey(TIntProcedure tIntProcedure);

    boolean forEachValue(TObjectProcedure<? super V> tObjectProcedure);

    boolean forEachEntry(TIntObjectProcedure<? super V> tIntObjectProcedure);

    void transformValues(TObjectFunction<V, V> tObjectFunction);

    boolean retainEntries(TIntObjectProcedure<? super V> tIntObjectProcedure);

    boolean equals(Object obj);

    int hashCode();
}
