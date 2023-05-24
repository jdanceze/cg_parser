package gnu.trove.map;

import gnu.trove.TCharCollection;
import gnu.trove.function.TCharFunction;
import gnu.trove.iterator.TFloatCharIterator;
import gnu.trove.procedure.TCharProcedure;
import gnu.trove.procedure.TFloatCharProcedure;
import gnu.trove.procedure.TFloatProcedure;
import gnu.trove.set.TFloatSet;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/TFloatCharMap.class */
public interface TFloatCharMap {
    float getNoEntryKey();

    char getNoEntryValue();

    char put(float f, char c);

    char putIfAbsent(float f, char c);

    void putAll(Map<? extends Float, ? extends Character> map);

    void putAll(TFloatCharMap tFloatCharMap);

    char get(float f);

    void clear();

    boolean isEmpty();

    char remove(float f);

    int size();

    TFloatSet keySet();

    float[] keys();

    float[] keys(float[] fArr);

    TCharCollection valueCollection();

    char[] values();

    char[] values(char[] cArr);

    boolean containsValue(char c);

    boolean containsKey(float f);

    TFloatCharIterator iterator();

    boolean forEachKey(TFloatProcedure tFloatProcedure);

    boolean forEachValue(TCharProcedure tCharProcedure);

    boolean forEachEntry(TFloatCharProcedure tFloatCharProcedure);

    void transformValues(TCharFunction tCharFunction);

    boolean retainEntries(TFloatCharProcedure tFloatCharProcedure);

    boolean increment(float f);

    boolean adjustValue(float f, char c);

    char adjustOrPutValue(float f, char c, char c2);
}
