package gnu.trove.map;

import gnu.trove.TCharCollection;
import gnu.trove.function.TCharFunction;
import gnu.trove.iterator.TIntCharIterator;
import gnu.trove.procedure.TCharProcedure;
import gnu.trove.procedure.TIntCharProcedure;
import gnu.trove.procedure.TIntProcedure;
import gnu.trove.set.TIntSet;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/TIntCharMap.class */
public interface TIntCharMap {
    int getNoEntryKey();

    char getNoEntryValue();

    char put(int i, char c);

    char putIfAbsent(int i, char c);

    void putAll(Map<? extends Integer, ? extends Character> map);

    void putAll(TIntCharMap tIntCharMap);

    char get(int i);

    void clear();

    boolean isEmpty();

    char remove(int i);

    int size();

    TIntSet keySet();

    int[] keys();

    int[] keys(int[] iArr);

    TCharCollection valueCollection();

    char[] values();

    char[] values(char[] cArr);

    boolean containsValue(char c);

    boolean containsKey(int i);

    TIntCharIterator iterator();

    boolean forEachKey(TIntProcedure tIntProcedure);

    boolean forEachValue(TCharProcedure tCharProcedure);

    boolean forEachEntry(TIntCharProcedure tIntCharProcedure);

    void transformValues(TCharFunction tCharFunction);

    boolean retainEntries(TIntCharProcedure tIntCharProcedure);

    boolean increment(int i);

    boolean adjustValue(int i, char c);

    char adjustOrPutValue(int i, char c, char c2);
}
