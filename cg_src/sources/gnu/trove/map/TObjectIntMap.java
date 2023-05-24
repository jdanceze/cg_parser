package gnu.trove.map;

import gnu.trove.TIntCollection;
import gnu.trove.function.TIntFunction;
import gnu.trove.iterator.TObjectIntIterator;
import gnu.trove.procedure.TIntProcedure;
import gnu.trove.procedure.TObjectIntProcedure;
import gnu.trove.procedure.TObjectProcedure;
import java.util.Map;
import java.util.Set;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/TObjectIntMap.class */
public interface TObjectIntMap<K> {
    int getNoEntryValue();

    int size();

    boolean isEmpty();

    boolean containsKey(Object obj);

    boolean containsValue(int i);

    int get(Object obj);

    int put(K k, int i);

    int putIfAbsent(K k, int i);

    int remove(Object obj);

    void putAll(Map<? extends K, ? extends Integer> map);

    void putAll(TObjectIntMap<? extends K> tObjectIntMap);

    void clear();

    Set<K> keySet();

    Object[] keys();

    K[] keys(K[] kArr);

    TIntCollection valueCollection();

    int[] values();

    int[] values(int[] iArr);

    TObjectIntIterator<K> iterator();

    boolean increment(K k);

    boolean adjustValue(K k, int i);

    int adjustOrPutValue(K k, int i, int i2);

    boolean forEachKey(TObjectProcedure<? super K> tObjectProcedure);

    boolean forEachValue(TIntProcedure tIntProcedure);

    boolean forEachEntry(TObjectIntProcedure<? super K> tObjectIntProcedure);

    void transformValues(TIntFunction tIntFunction);

    boolean retainEntries(TObjectIntProcedure<? super K> tObjectIntProcedure);

    boolean equals(Object obj);

    int hashCode();
}
