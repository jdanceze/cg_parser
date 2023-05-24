package gnu.trove.map;

import gnu.trove.TCharCollection;
import gnu.trove.function.TCharFunction;
import gnu.trove.iterator.TLongCharIterator;
import gnu.trove.procedure.TCharProcedure;
import gnu.trove.procedure.TLongCharProcedure;
import gnu.trove.procedure.TLongProcedure;
import gnu.trove.set.TLongSet;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/TLongCharMap.class */
public interface TLongCharMap {
    long getNoEntryKey();

    char getNoEntryValue();

    char put(long j, char c);

    char putIfAbsent(long j, char c);

    void putAll(Map<? extends Long, ? extends Character> map);

    void putAll(TLongCharMap tLongCharMap);

    char get(long j);

    void clear();

    boolean isEmpty();

    char remove(long j);

    int size();

    TLongSet keySet();

    long[] keys();

    long[] keys(long[] jArr);

    TCharCollection valueCollection();

    char[] values();

    char[] values(char[] cArr);

    boolean containsValue(char c);

    boolean containsKey(long j);

    TLongCharIterator iterator();

    boolean forEachKey(TLongProcedure tLongProcedure);

    boolean forEachValue(TCharProcedure tCharProcedure);

    boolean forEachEntry(TLongCharProcedure tLongCharProcedure);

    void transformValues(TCharFunction tCharFunction);

    boolean retainEntries(TLongCharProcedure tLongCharProcedure);

    boolean increment(long j);

    boolean adjustValue(long j, char c);

    char adjustOrPutValue(long j, char c, char c2);
}
