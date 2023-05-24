package gnu.trove.map;

import gnu.trove.TDoubleCollection;
import gnu.trove.function.TDoubleFunction;
import gnu.trove.iterator.TIntDoubleIterator;
import gnu.trove.procedure.TDoubleProcedure;
import gnu.trove.procedure.TIntDoubleProcedure;
import gnu.trove.procedure.TIntProcedure;
import gnu.trove.set.TIntSet;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/TIntDoubleMap.class */
public interface TIntDoubleMap {
    int getNoEntryKey();

    double getNoEntryValue();

    double put(int i, double d);

    double putIfAbsent(int i, double d);

    void putAll(Map<? extends Integer, ? extends Double> map);

    void putAll(TIntDoubleMap tIntDoubleMap);

    double get(int i);

    void clear();

    boolean isEmpty();

    double remove(int i);

    int size();

    TIntSet keySet();

    int[] keys();

    int[] keys(int[] iArr);

    TDoubleCollection valueCollection();

    double[] values();

    double[] values(double[] dArr);

    boolean containsValue(double d);

    boolean containsKey(int i);

    TIntDoubleIterator iterator();

    boolean forEachKey(TIntProcedure tIntProcedure);

    boolean forEachValue(TDoubleProcedure tDoubleProcedure);

    boolean forEachEntry(TIntDoubleProcedure tIntDoubleProcedure);

    void transformValues(TDoubleFunction tDoubleFunction);

    boolean retainEntries(TIntDoubleProcedure tIntDoubleProcedure);

    boolean increment(int i);

    boolean adjustValue(int i, double d);

    double adjustOrPutValue(int i, double d, double d2);
}
