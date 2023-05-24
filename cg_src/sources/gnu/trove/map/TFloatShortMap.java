package gnu.trove.map;

import gnu.trove.TShortCollection;
import gnu.trove.function.TShortFunction;
import gnu.trove.iterator.TFloatShortIterator;
import gnu.trove.procedure.TFloatProcedure;
import gnu.trove.procedure.TFloatShortProcedure;
import gnu.trove.procedure.TShortProcedure;
import gnu.trove.set.TFloatSet;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/TFloatShortMap.class */
public interface TFloatShortMap {
    float getNoEntryKey();

    short getNoEntryValue();

    short put(float f, short s);

    short putIfAbsent(float f, short s);

    void putAll(Map<? extends Float, ? extends Short> map);

    void putAll(TFloatShortMap tFloatShortMap);

    short get(float f);

    void clear();

    boolean isEmpty();

    short remove(float f);

    int size();

    TFloatSet keySet();

    float[] keys();

    float[] keys(float[] fArr);

    TShortCollection valueCollection();

    short[] values();

    short[] values(short[] sArr);

    boolean containsValue(short s);

    boolean containsKey(float f);

    TFloatShortIterator iterator();

    boolean forEachKey(TFloatProcedure tFloatProcedure);

    boolean forEachValue(TShortProcedure tShortProcedure);

    boolean forEachEntry(TFloatShortProcedure tFloatShortProcedure);

    void transformValues(TShortFunction tShortFunction);

    boolean retainEntries(TFloatShortProcedure tFloatShortProcedure);

    boolean increment(float f);

    boolean adjustValue(float f, short s);

    short adjustOrPutValue(float f, short s, short s2);
}
