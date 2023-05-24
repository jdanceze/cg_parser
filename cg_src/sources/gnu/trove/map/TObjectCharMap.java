package gnu.trove.map;

import gnu.trove.TCharCollection;
import gnu.trove.function.TCharFunction;
import gnu.trove.iterator.TObjectCharIterator;
import gnu.trove.procedure.TCharProcedure;
import gnu.trove.procedure.TObjectCharProcedure;
import gnu.trove.procedure.TObjectProcedure;
import java.util.Map;
import java.util.Set;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/TObjectCharMap.class */
public interface TObjectCharMap<K> {
    char getNoEntryValue();

    int size();

    boolean isEmpty();

    boolean containsKey(Object obj);

    boolean containsValue(char c);

    char get(Object obj);

    char put(K k, char c);

    char putIfAbsent(K k, char c);

    char remove(Object obj);

    void putAll(Map<? extends K, ? extends Character> map);

    void putAll(TObjectCharMap<? extends K> tObjectCharMap);

    void clear();

    Set<K> keySet();

    Object[] keys();

    K[] keys(K[] kArr);

    TCharCollection valueCollection();

    char[] values();

    char[] values(char[] cArr);

    TObjectCharIterator<K> iterator();

    boolean increment(K k);

    boolean adjustValue(K k, char c);

    char adjustOrPutValue(K k, char c, char c2);

    boolean forEachKey(TObjectProcedure<? super K> tObjectProcedure);

    boolean forEachValue(TCharProcedure tCharProcedure);

    boolean forEachEntry(TObjectCharProcedure<? super K> tObjectCharProcedure);

    void transformValues(TCharFunction tCharFunction);

    boolean retainEntries(TObjectCharProcedure<? super K> tObjectCharProcedure);

    boolean equals(Object obj);

    int hashCode();
}
