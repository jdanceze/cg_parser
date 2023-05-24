package gnu.trove.map;

import gnu.trove.TIntCollection;
import gnu.trove.function.TIntFunction;
import gnu.trove.iterator.TByteIntIterator;
import gnu.trove.procedure.TByteIntProcedure;
import gnu.trove.procedure.TByteProcedure;
import gnu.trove.procedure.TIntProcedure;
import gnu.trove.set.TByteSet;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/TByteIntMap.class */
public interface TByteIntMap {
    byte getNoEntryKey();

    int getNoEntryValue();

    int put(byte b, int i);

    int putIfAbsent(byte b, int i);

    void putAll(Map<? extends Byte, ? extends Integer> map);

    void putAll(TByteIntMap tByteIntMap);

    int get(byte b);

    void clear();

    boolean isEmpty();

    int remove(byte b);

    int size();

    TByteSet keySet();

    byte[] keys();

    byte[] keys(byte[] bArr);

    TIntCollection valueCollection();

    int[] values();

    int[] values(int[] iArr);

    boolean containsValue(int i);

    boolean containsKey(byte b);

    TByteIntIterator iterator();

    boolean forEachKey(TByteProcedure tByteProcedure);

    boolean forEachValue(TIntProcedure tIntProcedure);

    boolean forEachEntry(TByteIntProcedure tByteIntProcedure);

    void transformValues(TIntFunction tIntFunction);

    boolean retainEntries(TByteIntProcedure tByteIntProcedure);

    boolean increment(byte b);

    boolean adjustValue(byte b, int i);

    int adjustOrPutValue(byte b, int i, int i2);
}
