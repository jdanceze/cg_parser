package gnu.trove.map;

import gnu.trove.TDoubleCollection;
import gnu.trove.function.TDoubleFunction;
import gnu.trove.iterator.TShortDoubleIterator;
import gnu.trove.procedure.TDoubleProcedure;
import gnu.trove.procedure.TShortDoubleProcedure;
import gnu.trove.procedure.TShortProcedure;
import gnu.trove.set.TShortSet;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/TShortDoubleMap.class */
public interface TShortDoubleMap {
    short getNoEntryKey();

    double getNoEntryValue();

    double put(short s, double d);

    double putIfAbsent(short s, double d);

    void putAll(Map<? extends Short, ? extends Double> map);

    void putAll(TShortDoubleMap tShortDoubleMap);

    double get(short s);

    void clear();

    boolean isEmpty();

    double remove(short s);

    int size();

    TShortSet keySet();

    short[] keys();

    short[] keys(short[] sArr);

    TDoubleCollection valueCollection();

    double[] values();

    double[] values(double[] dArr);

    boolean containsValue(double d);

    boolean containsKey(short s);

    TShortDoubleIterator iterator();

    boolean forEachKey(TShortProcedure tShortProcedure);

    boolean forEachValue(TDoubleProcedure tDoubleProcedure);

    boolean forEachEntry(TShortDoubleProcedure tShortDoubleProcedure);

    void transformValues(TDoubleFunction tDoubleFunction);

    boolean retainEntries(TShortDoubleProcedure tShortDoubleProcedure);

    boolean increment(short s);

    boolean adjustValue(short s, double d);

    double adjustOrPutValue(short s, double d, double d2);
}
