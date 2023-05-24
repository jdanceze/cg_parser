package gnu.trove.map;

import gnu.trove.TShortCollection;
import gnu.trove.function.TShortFunction;
import gnu.trove.iterator.TObjectShortIterator;
import gnu.trove.procedure.TObjectProcedure;
import gnu.trove.procedure.TObjectShortProcedure;
import gnu.trove.procedure.TShortProcedure;
import java.util.Map;
import java.util.Set;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/TObjectShortMap.class */
public interface TObjectShortMap<K> {
    short getNoEntryValue();

    int size();

    boolean isEmpty();

    boolean containsKey(Object obj);

    boolean containsValue(short s);

    short get(Object obj);

    short put(K k, short s);

    short putIfAbsent(K k, short s);

    short remove(Object obj);

    void putAll(Map<? extends K, ? extends Short> map);

    void putAll(TObjectShortMap<? extends K> tObjectShortMap);

    void clear();

    Set<K> keySet();

    Object[] keys();

    K[] keys(K[] kArr);

    TShortCollection valueCollection();

    short[] values();

    short[] values(short[] sArr);

    TObjectShortIterator<K> iterator();

    boolean increment(K k);

    boolean adjustValue(K k, short s);

    short adjustOrPutValue(K k, short s, short s2);

    boolean forEachKey(TObjectProcedure<? super K> tObjectProcedure);

    boolean forEachValue(TShortProcedure tShortProcedure);

    boolean forEachEntry(TObjectShortProcedure<? super K> tObjectShortProcedure);

    void transformValues(TShortFunction tShortFunction);

    boolean retainEntries(TObjectShortProcedure<? super K> tObjectShortProcedure);

    boolean equals(Object obj);

    int hashCode();
}
