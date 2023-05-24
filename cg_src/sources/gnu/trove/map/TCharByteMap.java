package gnu.trove.map;

import gnu.trove.TByteCollection;
import gnu.trove.function.TByteFunction;
import gnu.trove.iterator.TCharByteIterator;
import gnu.trove.procedure.TByteProcedure;
import gnu.trove.procedure.TCharByteProcedure;
import gnu.trove.procedure.TCharProcedure;
import gnu.trove.set.TCharSet;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/TCharByteMap.class */
public interface TCharByteMap {
    char getNoEntryKey();

    byte getNoEntryValue();

    byte put(char c, byte b);

    byte putIfAbsent(char c, byte b);

    void putAll(Map<? extends Character, ? extends Byte> map);

    void putAll(TCharByteMap tCharByteMap);

    byte get(char c);

    void clear();

    boolean isEmpty();

    byte remove(char c);

    int size();

    TCharSet keySet();

    char[] keys();

    char[] keys(char[] cArr);

    TByteCollection valueCollection();

    byte[] values();

    byte[] values(byte[] bArr);

    boolean containsValue(byte b);

    boolean containsKey(char c);

    TCharByteIterator iterator();

    boolean forEachKey(TCharProcedure tCharProcedure);

    boolean forEachValue(TByteProcedure tByteProcedure);

    boolean forEachEntry(TCharByteProcedure tCharByteProcedure);

    void transformValues(TByteFunction tByteFunction);

    boolean retainEntries(TCharByteProcedure tCharByteProcedure);

    boolean increment(char c);

    boolean adjustValue(char c, byte b);

    byte adjustOrPutValue(char c, byte b, byte b2);
}
