package gnu.trove.map;

import gnu.trove.TByteCollection;
import gnu.trove.function.TByteFunction;
import gnu.trove.iterator.TLongByteIterator;
import gnu.trove.procedure.TByteProcedure;
import gnu.trove.procedure.TLongByteProcedure;
import gnu.trove.procedure.TLongProcedure;
import gnu.trove.set.TLongSet;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/TLongByteMap.class */
public interface TLongByteMap {
    long getNoEntryKey();

    byte getNoEntryValue();

    byte put(long j, byte b);

    byte putIfAbsent(long j, byte b);

    void putAll(Map<? extends Long, ? extends Byte> map);

    void putAll(TLongByteMap tLongByteMap);

    byte get(long j);

    void clear();

    boolean isEmpty();

    byte remove(long j);

    int size();

    TLongSet keySet();

    long[] keys();

    long[] keys(long[] jArr);

    TByteCollection valueCollection();

    byte[] values();

    byte[] values(byte[] bArr);

    boolean containsValue(byte b);

    boolean containsKey(long j);

    TLongByteIterator iterator();

    boolean forEachKey(TLongProcedure tLongProcedure);

    boolean forEachValue(TByteProcedure tByteProcedure);

    boolean forEachEntry(TLongByteProcedure tLongByteProcedure);

    void transformValues(TByteFunction tByteFunction);

    boolean retainEntries(TLongByteProcedure tLongByteProcedure);

    boolean increment(long j);

    boolean adjustValue(long j, byte b);

    byte adjustOrPutValue(long j, byte b, byte b2);
}
