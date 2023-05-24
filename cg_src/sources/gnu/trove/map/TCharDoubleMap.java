package gnu.trove.map;

import gnu.trove.TDoubleCollection;
import gnu.trove.function.TDoubleFunction;
import gnu.trove.iterator.TCharDoubleIterator;
import gnu.trove.procedure.TCharDoubleProcedure;
import gnu.trove.procedure.TCharProcedure;
import gnu.trove.procedure.TDoubleProcedure;
import gnu.trove.set.TCharSet;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/TCharDoubleMap.class */
public interface TCharDoubleMap {
    char getNoEntryKey();

    double getNoEntryValue();

    double put(char c, double d);

    double putIfAbsent(char c, double d);

    void putAll(Map<? extends Character, ? extends Double> map);

    void putAll(TCharDoubleMap tCharDoubleMap);

    double get(char c);

    void clear();

    boolean isEmpty();

    double remove(char c);

    int size();

    TCharSet keySet();

    char[] keys();

    char[] keys(char[] cArr);

    TDoubleCollection valueCollection();

    double[] values();

    double[] values(double[] dArr);

    boolean containsValue(double d);

    boolean containsKey(char c);

    TCharDoubleIterator iterator();

    boolean forEachKey(TCharProcedure tCharProcedure);

    boolean forEachValue(TDoubleProcedure tDoubleProcedure);

    boolean forEachEntry(TCharDoubleProcedure tCharDoubleProcedure);

    void transformValues(TDoubleFunction tDoubleFunction);

    boolean retainEntries(TCharDoubleProcedure tCharDoubleProcedure);

    boolean increment(char c);

    boolean adjustValue(char c, double d);

    double adjustOrPutValue(char c, double d, double d2);
}
