package gnu.trove.map;

import gnu.trove.TByteCollection;
import gnu.trove.function.TByteFunction;
import gnu.trove.iterator.TDoubleByteIterator;
import gnu.trove.procedure.TByteProcedure;
import gnu.trove.procedure.TDoubleByteProcedure;
import gnu.trove.procedure.TDoubleProcedure;
import gnu.trove.set.TDoubleSet;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/TDoubleByteMap.class */
public interface TDoubleByteMap {
    double getNoEntryKey();

    byte getNoEntryValue();

    byte put(double d, byte b);

    byte putIfAbsent(double d, byte b);

    void putAll(Map<? extends Double, ? extends Byte> map);

    void putAll(TDoubleByteMap tDoubleByteMap);

    byte get(double d);

    void clear();

    boolean isEmpty();

    byte remove(double d);

    int size();

    TDoubleSet keySet();

    double[] keys();

    double[] keys(double[] dArr);

    TByteCollection valueCollection();

    byte[] values();

    byte[] values(byte[] bArr);

    boolean containsValue(byte b);

    boolean containsKey(double d);

    TDoubleByteIterator iterator();

    boolean forEachKey(TDoubleProcedure tDoubleProcedure);

    boolean forEachValue(TByteProcedure tByteProcedure);

    boolean forEachEntry(TDoubleByteProcedure tDoubleByteProcedure);

    void transformValues(TByteFunction tByteFunction);

    boolean retainEntries(TDoubleByteProcedure tDoubleByteProcedure);

    boolean increment(double d);

    boolean adjustValue(double d, byte b);

    byte adjustOrPutValue(double d, byte b, byte b2);
}
