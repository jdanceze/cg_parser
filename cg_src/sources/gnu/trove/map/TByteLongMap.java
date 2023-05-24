package gnu.trove.map;

import gnu.trove.TLongCollection;
import gnu.trove.function.TLongFunction;
import gnu.trove.iterator.TByteLongIterator;
import gnu.trove.procedure.TByteLongProcedure;
import gnu.trove.procedure.TByteProcedure;
import gnu.trove.procedure.TLongProcedure;
import gnu.trove.set.TByteSet;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/TByteLongMap.class */
public interface TByteLongMap {
    byte getNoEntryKey();

    long getNoEntryValue();

    long put(byte b, long j);

    long putIfAbsent(byte b, long j);

    void putAll(Map<? extends Byte, ? extends Long> map);

    void putAll(TByteLongMap tByteLongMap);

    long get(byte b);

    void clear();

    boolean isEmpty();

    long remove(byte b);

    int size();

    TByteSet keySet();

    byte[] keys();

    byte[] keys(byte[] bArr);

    TLongCollection valueCollection();

    long[] values();

    long[] values(long[] jArr);

    boolean containsValue(long j);

    boolean containsKey(byte b);

    TByteLongIterator iterator();

    boolean forEachKey(TByteProcedure tByteProcedure);

    boolean forEachValue(TLongProcedure tLongProcedure);

    boolean forEachEntry(TByteLongProcedure tByteLongProcedure);

    void transformValues(TLongFunction tLongFunction);

    boolean retainEntries(TByteLongProcedure tByteLongProcedure);

    boolean increment(byte b);

    boolean adjustValue(byte b, long j);

    long adjustOrPutValue(byte b, long j, long j2);
}
