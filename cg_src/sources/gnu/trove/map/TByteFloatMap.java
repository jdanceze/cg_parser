package gnu.trove.map;

import gnu.trove.TFloatCollection;
import gnu.trove.function.TFloatFunction;
import gnu.trove.iterator.TByteFloatIterator;
import gnu.trove.procedure.TByteFloatProcedure;
import gnu.trove.procedure.TByteProcedure;
import gnu.trove.procedure.TFloatProcedure;
import gnu.trove.set.TByteSet;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/TByteFloatMap.class */
public interface TByteFloatMap {
    byte getNoEntryKey();

    float getNoEntryValue();

    float put(byte b, float f);

    float putIfAbsent(byte b, float f);

    void putAll(Map<? extends Byte, ? extends Float> map);

    void putAll(TByteFloatMap tByteFloatMap);

    float get(byte b);

    void clear();

    boolean isEmpty();

    float remove(byte b);

    int size();

    TByteSet keySet();

    byte[] keys();

    byte[] keys(byte[] bArr);

    TFloatCollection valueCollection();

    float[] values();

    float[] values(float[] fArr);

    boolean containsValue(float f);

    boolean containsKey(byte b);

    TByteFloatIterator iterator();

    boolean forEachKey(TByteProcedure tByteProcedure);

    boolean forEachValue(TFloatProcedure tFloatProcedure);

    boolean forEachEntry(TByteFloatProcedure tByteFloatProcedure);

    void transformValues(TFloatFunction tFloatFunction);

    boolean retainEntries(TByteFloatProcedure tByteFloatProcedure);

    boolean increment(byte b);

    boolean adjustValue(byte b, float f);

    float adjustOrPutValue(byte b, float f, float f2);
}
