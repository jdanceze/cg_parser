package gnu.trove.map;

import gnu.trove.TFloatCollection;
import gnu.trove.function.TFloatFunction;
import gnu.trove.iterator.TShortFloatIterator;
import gnu.trove.procedure.TFloatProcedure;
import gnu.trove.procedure.TShortFloatProcedure;
import gnu.trove.procedure.TShortProcedure;
import gnu.trove.set.TShortSet;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/TShortFloatMap.class */
public interface TShortFloatMap {
    short getNoEntryKey();

    float getNoEntryValue();

    float put(short s, float f);

    float putIfAbsent(short s, float f);

    void putAll(Map<? extends Short, ? extends Float> map);

    void putAll(TShortFloatMap tShortFloatMap);

    float get(short s);

    void clear();

    boolean isEmpty();

    float remove(short s);

    int size();

    TShortSet keySet();

    short[] keys();

    short[] keys(short[] sArr);

    TFloatCollection valueCollection();

    float[] values();

    float[] values(float[] fArr);

    boolean containsValue(float f);

    boolean containsKey(short s);

    TShortFloatIterator iterator();

    boolean forEachKey(TShortProcedure tShortProcedure);

    boolean forEachValue(TFloatProcedure tFloatProcedure);

    boolean forEachEntry(TShortFloatProcedure tShortFloatProcedure);

    void transformValues(TFloatFunction tFloatFunction);

    boolean retainEntries(TShortFloatProcedure tShortFloatProcedure);

    boolean increment(short s);

    boolean adjustValue(short s, float f);

    float adjustOrPutValue(short s, float f, float f2);
}
