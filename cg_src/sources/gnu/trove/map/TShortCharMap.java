package gnu.trove.map;

import gnu.trove.TCharCollection;
import gnu.trove.function.TCharFunction;
import gnu.trove.iterator.TShortCharIterator;
import gnu.trove.procedure.TCharProcedure;
import gnu.trove.procedure.TShortCharProcedure;
import gnu.trove.procedure.TShortProcedure;
import gnu.trove.set.TShortSet;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/TShortCharMap.class */
public interface TShortCharMap {
    short getNoEntryKey();

    char getNoEntryValue();

    char put(short s, char c);

    char putIfAbsent(short s, char c);

    void putAll(Map<? extends Short, ? extends Character> map);

    void putAll(TShortCharMap tShortCharMap);

    char get(short s);

    void clear();

    boolean isEmpty();

    char remove(short s);

    int size();

    TShortSet keySet();

    short[] keys();

    short[] keys(short[] sArr);

    TCharCollection valueCollection();

    char[] values();

    char[] values(char[] cArr);

    boolean containsValue(char c);

    boolean containsKey(short s);

    TShortCharIterator iterator();

    boolean forEachKey(TShortProcedure tShortProcedure);

    boolean forEachValue(TCharProcedure tCharProcedure);

    boolean forEachEntry(TShortCharProcedure tShortCharProcedure);

    void transformValues(TCharFunction tCharFunction);

    boolean retainEntries(TShortCharProcedure tShortCharProcedure);

    boolean increment(short s);

    boolean adjustValue(short s, char c);

    char adjustOrPutValue(short s, char c, char c2);
}
