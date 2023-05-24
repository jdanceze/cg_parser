package gnu.trove.map;

import gnu.trove.function.TObjectFunction;
import gnu.trove.iterator.TByteObjectIterator;
import gnu.trove.procedure.TByteObjectProcedure;
import gnu.trove.procedure.TByteProcedure;
import gnu.trove.procedure.TObjectProcedure;
import gnu.trove.set.TByteSet;
import java.util.Collection;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/TByteObjectMap.class */
public interface TByteObjectMap<V> {
    byte getNoEntryKey();

    int size();

    boolean isEmpty();

    boolean containsKey(byte b);

    boolean containsValue(Object obj);

    V get(byte b);

    V put(byte b, V v);

    V putIfAbsent(byte b, V v);

    V remove(byte b);

    void putAll(Map<? extends Byte, ? extends V> map);

    void putAll(TByteObjectMap<? extends V> tByteObjectMap);

    void clear();

    TByteSet keySet();

    byte[] keys();

    byte[] keys(byte[] bArr);

    Collection<V> valueCollection();

    Object[] values();

    V[] values(V[] vArr);

    TByteObjectIterator<V> iterator();

    boolean forEachKey(TByteProcedure tByteProcedure);

    boolean forEachValue(TObjectProcedure<? super V> tObjectProcedure);

    boolean forEachEntry(TByteObjectProcedure<? super V> tByteObjectProcedure);

    void transformValues(TObjectFunction<V, V> tObjectFunction);

    boolean retainEntries(TByteObjectProcedure<? super V> tByteObjectProcedure);

    boolean equals(Object obj);

    int hashCode();
}
