package gnu.trove.map;

import gnu.trove.TByteCollection;
import gnu.trove.function.TByteFunction;
import gnu.trove.iterator.TFloatByteIterator;
import gnu.trove.procedure.TByteProcedure;
import gnu.trove.procedure.TFloatByteProcedure;
import gnu.trove.procedure.TFloatProcedure;
import gnu.trove.set.TFloatSet;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/TFloatByteMap.class */
public interface TFloatByteMap {
    float getNoEntryKey();

    byte getNoEntryValue();

    byte put(float f, byte b);

    byte putIfAbsent(float f, byte b);

    void putAll(Map<? extends Float, ? extends Byte> map);

    void putAll(TFloatByteMap tFloatByteMap);

    byte get(float f);

    void clear();

    boolean isEmpty();

    byte remove(float f);

    int size();

    TFloatSet keySet();

    float[] keys();

    float[] keys(float[] fArr);

    TByteCollection valueCollection();

    byte[] values();

    byte[] values(byte[] bArr);

    boolean containsValue(byte b);

    boolean containsKey(float f);

    TFloatByteIterator iterator();

    boolean forEachKey(TFloatProcedure tFloatProcedure);

    boolean forEachValue(TByteProcedure tByteProcedure);

    boolean forEachEntry(TFloatByteProcedure tFloatByteProcedure);

    void transformValues(TByteFunction tByteFunction);

    boolean retainEntries(TFloatByteProcedure tFloatByteProcedure);

    boolean increment(float f);

    boolean adjustValue(float f, byte b);

    byte adjustOrPutValue(float f, byte b, byte b2);
}
