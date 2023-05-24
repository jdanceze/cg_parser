package gnu.trove.map;

import gnu.trove.TIntCollection;
import gnu.trove.function.TIntFunction;
import gnu.trove.iterator.TDoubleIntIterator;
import gnu.trove.procedure.TDoubleIntProcedure;
import gnu.trove.procedure.TDoubleProcedure;
import gnu.trove.procedure.TIntProcedure;
import gnu.trove.set.TDoubleSet;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/TDoubleIntMap.class */
public interface TDoubleIntMap {
    double getNoEntryKey();

    int getNoEntryValue();

    int put(double d, int i);

    int putIfAbsent(double d, int i);

    void putAll(Map<? extends Double, ? extends Integer> map);

    void putAll(TDoubleIntMap tDoubleIntMap);

    int get(double d);

    void clear();

    boolean isEmpty();

    int remove(double d);

    int size();

    TDoubleSet keySet();

    double[] keys();

    double[] keys(double[] dArr);

    TIntCollection valueCollection();

    int[] values();

    int[] values(int[] iArr);

    boolean containsValue(int i);

    boolean containsKey(double d);

    TDoubleIntIterator iterator();

    boolean forEachKey(TDoubleProcedure tDoubleProcedure);

    boolean forEachValue(TIntProcedure tIntProcedure);

    boolean forEachEntry(TDoubleIntProcedure tDoubleIntProcedure);

    void transformValues(TIntFunction tIntFunction);

    boolean retainEntries(TDoubleIntProcedure tDoubleIntProcedure);

    boolean increment(double d);

    boolean adjustValue(double d, int i);

    int adjustOrPutValue(double d, int i, int i2);
}
