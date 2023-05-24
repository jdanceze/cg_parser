package gnu.trove.map;

import gnu.trove.TDoubleCollection;
import gnu.trove.function.TDoubleFunction;
import gnu.trove.iterator.TObjectDoubleIterator;
import gnu.trove.procedure.TDoubleProcedure;
import gnu.trove.procedure.TObjectDoubleProcedure;
import gnu.trove.procedure.TObjectProcedure;
import java.util.Map;
import java.util.Set;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/TObjectDoubleMap.class */
public interface TObjectDoubleMap<K> {
    double getNoEntryValue();

    int size();

    boolean isEmpty();

    boolean containsKey(Object obj);

    boolean containsValue(double d);

    double get(Object obj);

    double put(K k, double d);

    double putIfAbsent(K k, double d);

    double remove(Object obj);

    void putAll(Map<? extends K, ? extends Double> map);

    void putAll(TObjectDoubleMap<? extends K> tObjectDoubleMap);

    void clear();

    Set<K> keySet();

    Object[] keys();

    K[] keys(K[] kArr);

    TDoubleCollection valueCollection();

    double[] values();

    double[] values(double[] dArr);

    TObjectDoubleIterator<K> iterator();

    boolean increment(K k);

    boolean adjustValue(K k, double d);

    double adjustOrPutValue(K k, double d, double d2);

    boolean forEachKey(TObjectProcedure<? super K> tObjectProcedure);

    boolean forEachValue(TDoubleProcedure tDoubleProcedure);

    boolean forEachEntry(TObjectDoubleProcedure<? super K> tObjectDoubleProcedure);

    void transformValues(TDoubleFunction tDoubleFunction);

    boolean retainEntries(TObjectDoubleProcedure<? super K> tObjectDoubleProcedure);

    boolean equals(Object obj);

    int hashCode();
}
