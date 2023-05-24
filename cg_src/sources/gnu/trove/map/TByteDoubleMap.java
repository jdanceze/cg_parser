package gnu.trove.map;

import gnu.trove.TDoubleCollection;
import gnu.trove.function.TDoubleFunction;
import gnu.trove.iterator.TByteDoubleIterator;
import gnu.trove.procedure.TByteDoubleProcedure;
import gnu.trove.procedure.TByteProcedure;
import gnu.trove.procedure.TDoubleProcedure;
import gnu.trove.set.TByteSet;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/TByteDoubleMap.class */
public interface TByteDoubleMap {
    byte getNoEntryKey();

    double getNoEntryValue();

    double put(byte b, double d);

    double putIfAbsent(byte b, double d);

    void putAll(Map<? extends Byte, ? extends Double> map);

    void putAll(TByteDoubleMap tByteDoubleMap);

    double get(byte b);

    void clear();

    boolean isEmpty();

    double remove(byte b);

    int size();

    TByteSet keySet();

    byte[] keys();

    byte[] keys(byte[] bArr);

    TDoubleCollection valueCollection();

    double[] values();

    double[] values(double[] dArr);

    boolean containsValue(double d);

    boolean containsKey(byte b);

    TByteDoubleIterator iterator();

    boolean forEachKey(TByteProcedure tByteProcedure);

    boolean forEachValue(TDoubleProcedure tDoubleProcedure);

    boolean forEachEntry(TByteDoubleProcedure tByteDoubleProcedure);

    void transformValues(TDoubleFunction tDoubleFunction);

    boolean retainEntries(TByteDoubleProcedure tByteDoubleProcedure);

    boolean increment(byte b);

    boolean adjustValue(byte b, double d);

    double adjustOrPutValue(byte b, double d, double d2);
}
