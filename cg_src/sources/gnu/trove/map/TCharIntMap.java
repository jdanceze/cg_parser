package gnu.trove.map;

import gnu.trove.TIntCollection;
import gnu.trove.function.TIntFunction;
import gnu.trove.iterator.TCharIntIterator;
import gnu.trove.procedure.TCharIntProcedure;
import gnu.trove.procedure.TCharProcedure;
import gnu.trove.procedure.TIntProcedure;
import gnu.trove.set.TCharSet;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/TCharIntMap.class */
public interface TCharIntMap {
    char getNoEntryKey();

    int getNoEntryValue();

    int put(char c, int i);

    int putIfAbsent(char c, int i);

    void putAll(Map<? extends Character, ? extends Integer> map);

    void putAll(TCharIntMap tCharIntMap);

    int get(char c);

    void clear();

    boolean isEmpty();

    int remove(char c);

    int size();

    TCharSet keySet();

    char[] keys();

    char[] keys(char[] cArr);

    TIntCollection valueCollection();

    int[] values();

    int[] values(int[] iArr);

    boolean containsValue(int i);

    boolean containsKey(char c);

    TCharIntIterator iterator();

    boolean forEachKey(TCharProcedure tCharProcedure);

    boolean forEachValue(TIntProcedure tIntProcedure);

    boolean forEachEntry(TCharIntProcedure tCharIntProcedure);

    void transformValues(TIntFunction tIntFunction);

    boolean retainEntries(TCharIntProcedure tCharIntProcedure);

    boolean increment(char c);

    boolean adjustValue(char c, int i);

    int adjustOrPutValue(char c, int i, int i2);
}
