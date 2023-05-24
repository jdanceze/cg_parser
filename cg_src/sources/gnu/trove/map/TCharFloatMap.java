package gnu.trove.map;

import gnu.trove.TFloatCollection;
import gnu.trove.function.TFloatFunction;
import gnu.trove.iterator.TCharFloatIterator;
import gnu.trove.procedure.TCharFloatProcedure;
import gnu.trove.procedure.TCharProcedure;
import gnu.trove.procedure.TFloatProcedure;
import gnu.trove.set.TCharSet;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/TCharFloatMap.class */
public interface TCharFloatMap {
    char getNoEntryKey();

    float getNoEntryValue();

    float put(char c, float f);

    float putIfAbsent(char c, float f);

    void putAll(Map<? extends Character, ? extends Float> map);

    void putAll(TCharFloatMap tCharFloatMap);

    float get(char c);

    void clear();

    boolean isEmpty();

    float remove(char c);

    int size();

    TCharSet keySet();

    char[] keys();

    char[] keys(char[] cArr);

    TFloatCollection valueCollection();

    float[] values();

    float[] values(float[] fArr);

    boolean containsValue(float f);

    boolean containsKey(char c);

    TCharFloatIterator iterator();

    boolean forEachKey(TCharProcedure tCharProcedure);

    boolean forEachValue(TFloatProcedure tFloatProcedure);

    boolean forEachEntry(TCharFloatProcedure tCharFloatProcedure);

    void transformValues(TFloatFunction tFloatFunction);

    boolean retainEntries(TCharFloatProcedure tCharFloatProcedure);

    boolean increment(char c);

    boolean adjustValue(char c, float f);

    float adjustOrPutValue(char c, float f, float f2);
}
