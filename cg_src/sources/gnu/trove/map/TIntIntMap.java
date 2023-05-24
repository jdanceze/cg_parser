package gnu.trove.map;

import gnu.trove.TIntCollection;
import gnu.trove.function.TIntFunction;
import gnu.trove.iterator.TIntIntIterator;
import gnu.trove.procedure.TIntIntProcedure;
import gnu.trove.procedure.TIntProcedure;
import gnu.trove.set.TIntSet;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/TIntIntMap.class */
public interface TIntIntMap {
    int getNoEntryKey();

    int getNoEntryValue();

    int put(int i, int i2);

    int putIfAbsent(int i, int i2);

    void putAll(Map<? extends Integer, ? extends Integer> map);

    void putAll(TIntIntMap tIntIntMap);

    int get(int i);

    void clear();

    boolean isEmpty();

    int remove(int i);

    int size();

    TIntSet keySet();

    int[] keys();

    int[] keys(int[] iArr);

    TIntCollection valueCollection();

    int[] values();

    int[] values(int[] iArr);

    boolean containsValue(int i);

    boolean containsKey(int i);

    TIntIntIterator iterator();

    boolean forEachKey(TIntProcedure tIntProcedure);

    boolean forEachValue(TIntProcedure tIntProcedure);

    boolean forEachEntry(TIntIntProcedure tIntIntProcedure);

    void transformValues(TIntFunction tIntFunction);

    boolean retainEntries(TIntIntProcedure tIntIntProcedure);

    boolean increment(int i);

    boolean adjustValue(int i, int i2);

    int adjustOrPutValue(int i, int i2, int i3);
}
