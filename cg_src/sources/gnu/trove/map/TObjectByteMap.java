package gnu.trove.map;

import gnu.trove.TByteCollection;
import gnu.trove.function.TByteFunction;
import gnu.trove.iterator.TObjectByteIterator;
import gnu.trove.procedure.TByteProcedure;
import gnu.trove.procedure.TObjectByteProcedure;
import gnu.trove.procedure.TObjectProcedure;
import java.util.Map;
import java.util.Set;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/TObjectByteMap.class */
public interface TObjectByteMap<K> {
    byte getNoEntryValue();

    int size();

    boolean isEmpty();

    boolean containsKey(Object obj);

    boolean containsValue(byte b);

    byte get(Object obj);

    byte put(K k, byte b);

    byte putIfAbsent(K k, byte b);

    byte remove(Object obj);

    void putAll(Map<? extends K, ? extends Byte> map);

    void putAll(TObjectByteMap<? extends K> tObjectByteMap);

    void clear();

    Set<K> keySet();

    Object[] keys();

    K[] keys(K[] kArr);

    TByteCollection valueCollection();

    byte[] values();

    byte[] values(byte[] bArr);

    TObjectByteIterator<K> iterator();

    boolean increment(K k);

    boolean adjustValue(K k, byte b);

    byte adjustOrPutValue(K k, byte b, byte b2);

    boolean forEachKey(TObjectProcedure<? super K> tObjectProcedure);

    boolean forEachValue(TByteProcedure tByteProcedure);

    boolean forEachEntry(TObjectByteProcedure<? super K> tObjectByteProcedure);

    void transformValues(TByteFunction tByteFunction);

    boolean retainEntries(TObjectByteProcedure<? super K> tObjectByteProcedure);

    boolean equals(Object obj);

    int hashCode();
}
