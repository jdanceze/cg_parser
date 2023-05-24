package gnu.trove.map;

import gnu.trove.TShortCollection;
import gnu.trove.function.TShortFunction;
import gnu.trove.iterator.TByteShortIterator;
import gnu.trove.procedure.TByteProcedure;
import gnu.trove.procedure.TByteShortProcedure;
import gnu.trove.procedure.TShortProcedure;
import gnu.trove.set.TByteSet;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/TByteShortMap.class */
public interface TByteShortMap {
    byte getNoEntryKey();

    short getNoEntryValue();

    short put(byte b, short s);

    short putIfAbsent(byte b, short s);

    void putAll(Map<? extends Byte, ? extends Short> map);

    void putAll(TByteShortMap tByteShortMap);

    short get(byte b);

    void clear();

    boolean isEmpty();

    short remove(byte b);

    int size();

    TByteSet keySet();

    byte[] keys();

    byte[] keys(byte[] bArr);

    TShortCollection valueCollection();

    short[] values();

    short[] values(short[] sArr);

    boolean containsValue(short s);

    boolean containsKey(byte b);

    TByteShortIterator iterator();

    boolean forEachKey(TByteProcedure tByteProcedure);

    boolean forEachValue(TShortProcedure tShortProcedure);

    boolean forEachEntry(TByteShortProcedure tByteShortProcedure);

    void transformValues(TShortFunction tShortFunction);

    boolean retainEntries(TByteShortProcedure tByteShortProcedure);

    boolean increment(byte b);

    boolean adjustValue(byte b, short s);

    short adjustOrPutValue(byte b, short s, short s2);
}
