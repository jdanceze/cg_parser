package gnu.trove.map;

import gnu.trove.TCharCollection;
import gnu.trove.function.TCharFunction;
import gnu.trove.iterator.TCharCharIterator;
import gnu.trove.procedure.TCharCharProcedure;
import gnu.trove.procedure.TCharProcedure;
import gnu.trove.set.TCharSet;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/TCharCharMap.class */
public interface TCharCharMap {
    char getNoEntryKey();

    char getNoEntryValue();

    char put(char c, char c2);

    char putIfAbsent(char c, char c2);

    void putAll(Map<? extends Character, ? extends Character> map);

    void putAll(TCharCharMap tCharCharMap);

    char get(char c);

    void clear();

    boolean isEmpty();

    char remove(char c);

    int size();

    TCharSet keySet();

    char[] keys();

    char[] keys(char[] cArr);

    TCharCollection valueCollection();

    char[] values();

    char[] values(char[] cArr);

    boolean containsValue(char c);

    boolean containsKey(char c);

    TCharCharIterator iterator();

    boolean forEachKey(TCharProcedure tCharProcedure);

    boolean forEachValue(TCharProcedure tCharProcedure);

    boolean forEachEntry(TCharCharProcedure tCharCharProcedure);

    void transformValues(TCharFunction tCharFunction);

    boolean retainEntries(TCharCharProcedure tCharCharProcedure);

    boolean increment(char c);

    boolean adjustValue(char c, char c2);

    char adjustOrPutValue(char c, char c2, char c3);
}
