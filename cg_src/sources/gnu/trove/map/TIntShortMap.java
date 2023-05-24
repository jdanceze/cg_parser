package gnu.trove.map;

import gnu.trove.TShortCollection;
import gnu.trove.function.TShortFunction;
import gnu.trove.iterator.TIntShortIterator;
import gnu.trove.procedure.TIntProcedure;
import gnu.trove.procedure.TIntShortProcedure;
import gnu.trove.procedure.TShortProcedure;
import gnu.trove.set.TIntSet;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/TIntShortMap.class */
public interface TIntShortMap {
    int getNoEntryKey();

    short getNoEntryValue();

    short put(int i, short s);

    short putIfAbsent(int i, short s);

    void putAll(Map<? extends Integer, ? extends Short> map);

    void putAll(TIntShortMap tIntShortMap);

    short get(int i);

    void clear();

    boolean isEmpty();

    short remove(int i);

    int size();

    TIntSet keySet();

    int[] keys();

    int[] keys(int[] iArr);

    TShortCollection valueCollection();

    short[] values();

    short[] values(short[] sArr);

    boolean containsValue(short s);

    boolean containsKey(int i);

    TIntShortIterator iterator();

    boolean forEachKey(TIntProcedure tIntProcedure);

    boolean forEachValue(TShortProcedure tShortProcedure);

    boolean forEachEntry(TIntShortProcedure tIntShortProcedure);

    void transformValues(TShortFunction tShortFunction);

    boolean retainEntries(TIntShortProcedure tIntShortProcedure);

    boolean increment(int i);

    boolean adjustValue(int i, short s);

    short adjustOrPutValue(int i, short s, short s2);
}
