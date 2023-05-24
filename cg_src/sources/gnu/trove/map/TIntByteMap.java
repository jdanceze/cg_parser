package gnu.trove.map;

import gnu.trove.TByteCollection;
import gnu.trove.function.TByteFunction;
import gnu.trove.iterator.TIntByteIterator;
import gnu.trove.procedure.TByteProcedure;
import gnu.trove.procedure.TIntByteProcedure;
import gnu.trove.procedure.TIntProcedure;
import gnu.trove.set.TIntSet;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/TIntByteMap.class */
public interface TIntByteMap {
    int getNoEntryKey();

    byte getNoEntryValue();

    byte put(int i, byte b);

    byte putIfAbsent(int i, byte b);

    void putAll(Map<? extends Integer, ? extends Byte> map);

    void putAll(TIntByteMap tIntByteMap);

    byte get(int i);

    void clear();

    boolean isEmpty();

    byte remove(int i);

    int size();

    TIntSet keySet();

    int[] keys();

    int[] keys(int[] iArr);

    TByteCollection valueCollection();

    byte[] values();

    byte[] values(byte[] bArr);

    boolean containsValue(byte b);

    boolean containsKey(int i);

    TIntByteIterator iterator();

    boolean forEachKey(TIntProcedure tIntProcedure);

    boolean forEachValue(TByteProcedure tByteProcedure);

    boolean forEachEntry(TIntByteProcedure tIntByteProcedure);

    void transformValues(TByteFunction tByteFunction);

    boolean retainEntries(TIntByteProcedure tIntByteProcedure);

    boolean increment(int i);

    boolean adjustValue(int i, byte b);

    byte adjustOrPutValue(int i, byte b, byte b2);
}
