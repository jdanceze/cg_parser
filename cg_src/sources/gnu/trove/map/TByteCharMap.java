package gnu.trove.map;

import gnu.trove.TCharCollection;
import gnu.trove.function.TCharFunction;
import gnu.trove.iterator.TByteCharIterator;
import gnu.trove.procedure.TByteCharProcedure;
import gnu.trove.procedure.TByteProcedure;
import gnu.trove.procedure.TCharProcedure;
import gnu.trove.set.TByteSet;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/TByteCharMap.class */
public interface TByteCharMap {
    byte getNoEntryKey();

    char getNoEntryValue();

    char put(byte b, char c);

    char putIfAbsent(byte b, char c);

    void putAll(Map<? extends Byte, ? extends Character> map);

    void putAll(TByteCharMap tByteCharMap);

    char get(byte b);

    void clear();

    boolean isEmpty();

    char remove(byte b);

    int size();

    TByteSet keySet();

    byte[] keys();

    byte[] keys(byte[] bArr);

    TCharCollection valueCollection();

    char[] values();

    char[] values(char[] cArr);

    boolean containsValue(char c);

    boolean containsKey(byte b);

    TByteCharIterator iterator();

    boolean forEachKey(TByteProcedure tByteProcedure);

    boolean forEachValue(TCharProcedure tCharProcedure);

    boolean forEachEntry(TByteCharProcedure tByteCharProcedure);

    void transformValues(TCharFunction tCharFunction);

    boolean retainEntries(TByteCharProcedure tByteCharProcedure);

    boolean increment(byte b);

    boolean adjustValue(byte b, char c);

    char adjustOrPutValue(byte b, char c, char c2);
}
