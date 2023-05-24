package gnu.trove.map;

import gnu.trove.TShortCollection;
import gnu.trove.function.TShortFunction;
import gnu.trove.iterator.TShortShortIterator;
import gnu.trove.procedure.TShortProcedure;
import gnu.trove.procedure.TShortShortProcedure;
import gnu.trove.set.TShortSet;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/TShortShortMap.class */
public interface TShortShortMap {
    short getNoEntryKey();

    short getNoEntryValue();

    short put(short s, short s2);

    short putIfAbsent(short s, short s2);

    void putAll(Map<? extends Short, ? extends Short> map);

    void putAll(TShortShortMap tShortShortMap);

    short get(short s);

    void clear();

    boolean isEmpty();

    short remove(short s);

    int size();

    TShortSet keySet();

    short[] keys();

    short[] keys(short[] sArr);

    TShortCollection valueCollection();

    short[] values();

    short[] values(short[] sArr);

    boolean containsValue(short s);

    boolean containsKey(short s);

    TShortShortIterator iterator();

    boolean forEachKey(TShortProcedure tShortProcedure);

    boolean forEachValue(TShortProcedure tShortProcedure);

    boolean forEachEntry(TShortShortProcedure tShortShortProcedure);

    void transformValues(TShortFunction tShortFunction);

    boolean retainEntries(TShortShortProcedure tShortShortProcedure);

    boolean increment(short s);

    boolean adjustValue(short s, short s2);

    short adjustOrPutValue(short s, short s2, short s3);
}
