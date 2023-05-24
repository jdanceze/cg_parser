package gnu.trove.map;

import gnu.trove.TShortCollection;
import gnu.trove.function.TShortFunction;
import gnu.trove.iterator.TCharShortIterator;
import gnu.trove.procedure.TCharProcedure;
import gnu.trove.procedure.TCharShortProcedure;
import gnu.trove.procedure.TShortProcedure;
import gnu.trove.set.TCharSet;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/TCharShortMap.class */
public interface TCharShortMap {
    char getNoEntryKey();

    short getNoEntryValue();

    short put(char c, short s);

    short putIfAbsent(char c, short s);

    void putAll(Map<? extends Character, ? extends Short> map);

    void putAll(TCharShortMap tCharShortMap);

    short get(char c);

    void clear();

    boolean isEmpty();

    short remove(char c);

    int size();

    TCharSet keySet();

    char[] keys();

    char[] keys(char[] cArr);

    TShortCollection valueCollection();

    short[] values();

    short[] values(short[] sArr);

    boolean containsValue(short s);

    boolean containsKey(char c);

    TCharShortIterator iterator();

    boolean forEachKey(TCharProcedure tCharProcedure);

    boolean forEachValue(TShortProcedure tShortProcedure);

    boolean forEachEntry(TCharShortProcedure tCharShortProcedure);

    void transformValues(TShortFunction tShortFunction);

    boolean retainEntries(TCharShortProcedure tCharShortProcedure);

    boolean increment(char c);

    boolean adjustValue(char c, short s);

    short adjustOrPutValue(char c, short s, short s2);
}
