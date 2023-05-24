package gnu.trove.map;

import gnu.trove.TShortCollection;
import gnu.trove.function.TShortFunction;
import gnu.trove.iterator.TDoubleShortIterator;
import gnu.trove.procedure.TDoubleProcedure;
import gnu.trove.procedure.TDoubleShortProcedure;
import gnu.trove.procedure.TShortProcedure;
import gnu.trove.set.TDoubleSet;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/TDoubleShortMap.class */
public interface TDoubleShortMap {
    double getNoEntryKey();

    short getNoEntryValue();

    short put(double d, short s);

    short putIfAbsent(double d, short s);

    void putAll(Map<? extends Double, ? extends Short> map);

    void putAll(TDoubleShortMap tDoubleShortMap);

    short get(double d);

    void clear();

    boolean isEmpty();

    short remove(double d);

    int size();

    TDoubleSet keySet();

    double[] keys();

    double[] keys(double[] dArr);

    TShortCollection valueCollection();

    short[] values();

    short[] values(short[] sArr);

    boolean containsValue(short s);

    boolean containsKey(double d);

    TDoubleShortIterator iterator();

    boolean forEachKey(TDoubleProcedure tDoubleProcedure);

    boolean forEachValue(TShortProcedure tShortProcedure);

    boolean forEachEntry(TDoubleShortProcedure tDoubleShortProcedure);

    void transformValues(TShortFunction tShortFunction);

    boolean retainEntries(TDoubleShortProcedure tDoubleShortProcedure);

    boolean increment(double d);

    boolean adjustValue(double d, short s);

    short adjustOrPutValue(double d, short s, short s2);
}
