package gnu.trove.map;

import gnu.trove.TLongCollection;
import gnu.trove.function.TLongFunction;
import gnu.trove.iterator.TCharLongIterator;
import gnu.trove.procedure.TCharLongProcedure;
import gnu.trove.procedure.TCharProcedure;
import gnu.trove.procedure.TLongProcedure;
import gnu.trove.set.TCharSet;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/TCharLongMap.class */
public interface TCharLongMap {
    char getNoEntryKey();

    long getNoEntryValue();

    long put(char c, long j);

    long putIfAbsent(char c, long j);

    void putAll(Map<? extends Character, ? extends Long> map);

    void putAll(TCharLongMap tCharLongMap);

    long get(char c);

    void clear();

    boolean isEmpty();

    long remove(char c);

    int size();

    TCharSet keySet();

    char[] keys();

    char[] keys(char[] cArr);

    TLongCollection valueCollection();

    long[] values();

    long[] values(long[] jArr);

    boolean containsValue(long j);

    boolean containsKey(char c);

    TCharLongIterator iterator();

    boolean forEachKey(TCharProcedure tCharProcedure);

    boolean forEachValue(TLongProcedure tLongProcedure);

    boolean forEachEntry(TCharLongProcedure tCharLongProcedure);

    void transformValues(TLongFunction tLongFunction);

    boolean retainEntries(TCharLongProcedure tCharLongProcedure);

    boolean increment(char c);

    boolean adjustValue(char c, long j);

    long adjustOrPutValue(char c, long j, long j2);
}
