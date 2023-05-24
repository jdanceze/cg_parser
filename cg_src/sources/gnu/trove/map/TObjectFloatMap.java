package gnu.trove.map;

import gnu.trove.TFloatCollection;
import gnu.trove.function.TFloatFunction;
import gnu.trove.iterator.TObjectFloatIterator;
import gnu.trove.procedure.TFloatProcedure;
import gnu.trove.procedure.TObjectFloatProcedure;
import gnu.trove.procedure.TObjectProcedure;
import java.util.Map;
import java.util.Set;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/TObjectFloatMap.class */
public interface TObjectFloatMap<K> {
    float getNoEntryValue();

    int size();

    boolean isEmpty();

    boolean containsKey(Object obj);

    boolean containsValue(float f);

    float get(Object obj);

    float put(K k, float f);

    float putIfAbsent(K k, float f);

    float remove(Object obj);

    void putAll(Map<? extends K, ? extends Float> map);

    void putAll(TObjectFloatMap<? extends K> tObjectFloatMap);

    void clear();

    Set<K> keySet();

    Object[] keys();

    K[] keys(K[] kArr);

    TFloatCollection valueCollection();

    float[] values();

    float[] values(float[] fArr);

    TObjectFloatIterator<K> iterator();

    boolean increment(K k);

    boolean adjustValue(K k, float f);

    float adjustOrPutValue(K k, float f, float f2);

    boolean forEachKey(TObjectProcedure<? super K> tObjectProcedure);

    boolean forEachValue(TFloatProcedure tFloatProcedure);

    boolean forEachEntry(TObjectFloatProcedure<? super K> tObjectFloatProcedure);

    void transformValues(TFloatFunction tFloatFunction);

    boolean retainEntries(TObjectFloatProcedure<? super K> tObjectFloatProcedure);

    boolean equals(Object obj);

    int hashCode();
}
