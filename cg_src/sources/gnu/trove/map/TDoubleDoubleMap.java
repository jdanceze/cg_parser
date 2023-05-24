package gnu.trove.map;

import gnu.trove.TDoubleCollection;
import gnu.trove.function.TDoubleFunction;
import gnu.trove.iterator.TDoubleDoubleIterator;
import gnu.trove.procedure.TDoubleDoubleProcedure;
import gnu.trove.procedure.TDoubleProcedure;
import gnu.trove.set.TDoubleSet;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/TDoubleDoubleMap.class */
public interface TDoubleDoubleMap {
    double getNoEntryKey();

    double getNoEntryValue();

    double put(double d, double d2);

    double putIfAbsent(double d, double d2);

    void putAll(Map<? extends Double, ? extends Double> map);

    void putAll(TDoubleDoubleMap tDoubleDoubleMap);

    double get(double d);

    void clear();

    boolean isEmpty();

    double remove(double d);

    int size();

    TDoubleSet keySet();

    double[] keys();

    double[] keys(double[] dArr);

    TDoubleCollection valueCollection();

    double[] values();

    double[] values(double[] dArr);

    boolean containsValue(double d);

    boolean containsKey(double d);

    TDoubleDoubleIterator iterator();

    boolean forEachKey(TDoubleProcedure tDoubleProcedure);

    boolean forEachValue(TDoubleProcedure tDoubleProcedure);

    boolean forEachEntry(TDoubleDoubleProcedure tDoubleDoubleProcedure);

    void transformValues(TDoubleFunction tDoubleFunction);

    boolean retainEntries(TDoubleDoubleProcedure tDoubleDoubleProcedure);

    boolean increment(double d);

    boolean adjustValue(double d, double d2);

    double adjustOrPutValue(double d, double d2, double d3);
}
