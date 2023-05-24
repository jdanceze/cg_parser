package gnu.trove.map;

import gnu.trove.TByteCollection;
import gnu.trove.function.TByteFunction;
import gnu.trove.iterator.TByteByteIterator;
import gnu.trove.procedure.TByteByteProcedure;
import gnu.trove.procedure.TByteProcedure;
import gnu.trove.set.TByteSet;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/TByteByteMap.class */
public interface TByteByteMap {
    byte getNoEntryKey();

    byte getNoEntryValue();

    byte put(byte b, byte b2);

    byte putIfAbsent(byte b, byte b2);

    void putAll(Map<? extends Byte, ? extends Byte> map);

    void putAll(TByteByteMap tByteByteMap);

    byte get(byte b);

    void clear();

    boolean isEmpty();

    byte remove(byte b);

    int size();

    TByteSet keySet();

    byte[] keys();

    byte[] keys(byte[] bArr);

    TByteCollection valueCollection();

    byte[] values();

    byte[] values(byte[] bArr);

    boolean containsValue(byte b);

    boolean containsKey(byte b);

    TByteByteIterator iterator();

    boolean forEachKey(TByteProcedure tByteProcedure);

    boolean forEachValue(TByteProcedure tByteProcedure);

    boolean forEachEntry(TByteByteProcedure tByteByteProcedure);

    void transformValues(TByteFunction tByteFunction);

    boolean retainEntries(TByteByteProcedure tByteByteProcedure);

    boolean increment(byte b);

    boolean adjustValue(byte b, byte b2);

    byte adjustOrPutValue(byte b, byte b2, byte b3);
}
