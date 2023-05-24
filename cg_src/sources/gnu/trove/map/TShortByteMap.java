package gnu.trove.map;

import gnu.trove.TByteCollection;
import gnu.trove.function.TByteFunction;
import gnu.trove.iterator.TShortByteIterator;
import gnu.trove.procedure.TByteProcedure;
import gnu.trove.procedure.TShortByteProcedure;
import gnu.trove.procedure.TShortProcedure;
import gnu.trove.set.TShortSet;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/TShortByteMap.class */
public interface TShortByteMap {
    short getNoEntryKey();

    byte getNoEntryValue();

    byte put(short s, byte b);

    byte putIfAbsent(short s, byte b);

    void putAll(Map<? extends Short, ? extends Byte> map);

    void putAll(TShortByteMap tShortByteMap);

    byte get(short s);

    void clear();

    boolean isEmpty();

    byte remove(short s);

    int size();

    TShortSet keySet();

    short[] keys();

    short[] keys(short[] sArr);

    TByteCollection valueCollection();

    byte[] values();

    byte[] values(byte[] bArr);

    boolean containsValue(byte b);

    boolean containsKey(short s);

    TShortByteIterator iterator();

    boolean forEachKey(TShortProcedure tShortProcedure);

    boolean forEachValue(TByteProcedure tByteProcedure);

    boolean forEachEntry(TShortByteProcedure tShortByteProcedure);

    void transformValues(TByteFunction tByteFunction);

    boolean retainEntries(TShortByteProcedure tShortByteProcedure);

    boolean increment(short s);

    boolean adjustValue(short s, byte b);

    byte adjustOrPutValue(short s, byte b, byte b2);
}
