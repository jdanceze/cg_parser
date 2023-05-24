package gnu.trove.map;

import gnu.trove.TCharCollection;
import gnu.trove.function.TCharFunction;
import gnu.trove.iterator.TDoubleCharIterator;
import gnu.trove.procedure.TCharProcedure;
import gnu.trove.procedure.TDoubleCharProcedure;
import gnu.trove.procedure.TDoubleProcedure;
import gnu.trove.set.TDoubleSet;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/TDoubleCharMap.class */
public interface TDoubleCharMap {
    double getNoEntryKey();

    char getNoEntryValue();

    char put(double d, char c);

    char putIfAbsent(double d, char c);

    void putAll(Map<? extends Double, ? extends Character> map);

    void putAll(TDoubleCharMap tDoubleCharMap);

    char get(double d);

    void clear();

    boolean isEmpty();

    char remove(double d);

    int size();

    TDoubleSet keySet();

    double[] keys();

    double[] keys(double[] dArr);

    TCharCollection valueCollection();

    char[] values();

    char[] values(char[] cArr);

    boolean containsValue(char c);

    boolean containsKey(double d);

    TDoubleCharIterator iterator();

    boolean forEachKey(TDoubleProcedure tDoubleProcedure);

    boolean forEachValue(TCharProcedure tCharProcedure);

    boolean forEachEntry(TDoubleCharProcedure tDoubleCharProcedure);

    void transformValues(TCharFunction tCharFunction);

    boolean retainEntries(TDoubleCharProcedure tDoubleCharProcedure);

    boolean increment(double d);

    boolean adjustValue(double d, char c);

    char adjustOrPutValue(double d, char c, char c2);
}
