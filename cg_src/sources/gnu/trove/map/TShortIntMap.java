package gnu.trove.map;

import gnu.trove.TIntCollection;
import gnu.trove.function.TIntFunction;
import gnu.trove.iterator.TShortIntIterator;
import gnu.trove.procedure.TIntProcedure;
import gnu.trove.procedure.TShortIntProcedure;
import gnu.trove.procedure.TShortProcedure;
import gnu.trove.set.TShortSet;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/TShortIntMap.class */
public interface TShortIntMap {
    short getNoEntryKey();

    int getNoEntryValue();

    int put(short s, int i);

    int putIfAbsent(short s, int i);

    void putAll(Map<? extends Short, ? extends Integer> map);

    void putAll(TShortIntMap tShortIntMap);

    int get(short s);

    void clear();

    boolean isEmpty();

    int remove(short s);

    int size();

    TShortSet keySet();

    short[] keys();

    short[] keys(short[] sArr);

    TIntCollection valueCollection();

    int[] values();

    int[] values(int[] iArr);

    boolean containsValue(int i);

    boolean containsKey(short s);

    TShortIntIterator iterator();

    boolean forEachKey(TShortProcedure tShortProcedure);

    boolean forEachValue(TIntProcedure tIntProcedure);

    boolean forEachEntry(TShortIntProcedure tShortIntProcedure);

    void transformValues(TIntFunction tIntFunction);

    boolean retainEntries(TShortIntProcedure tShortIntProcedure);

    boolean increment(short s);

    boolean adjustValue(short s, int i);

    int adjustOrPutValue(short s, int i, int i2);
}
