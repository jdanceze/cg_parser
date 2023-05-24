package gnu.trove.map;

import gnu.trove.function.TObjectFunction;
import gnu.trove.procedure.TObjectObjectProcedure;
import gnu.trove.procedure.TObjectProcedure;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/TMap.class */
public interface TMap<K, V> extends Map<K, V> {
    @Override // java.util.Map
    V putIfAbsent(K k, V v);

    boolean forEachKey(TObjectProcedure<? super K> tObjectProcedure);

    boolean forEachValue(TObjectProcedure<? super V> tObjectProcedure);

    boolean forEachEntry(TObjectObjectProcedure<? super K, ? super V> tObjectObjectProcedure);

    boolean retainEntries(TObjectObjectProcedure<? super K, ? super V> tObjectObjectProcedure);

    void transformValues(TObjectFunction<V, V> tObjectFunction);
}
