package gnu.trove.map;

import gnu.trove.TIntCollection;
import gnu.trove.function.TIntFunction;
import gnu.trove.iterator.TFloatIntIterator;
import gnu.trove.procedure.TFloatIntProcedure;
import gnu.trove.procedure.TFloatProcedure;
import gnu.trove.procedure.TIntProcedure;
import gnu.trove.set.TFloatSet;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/TFloatIntMap.class */
public interface TFloatIntMap {
    float getNoEntryKey();

    int getNoEntryValue();

    int put(float f, int i);

    int putIfAbsent(float f, int i);

    void putAll(Map<? extends Float, ? extends Integer> map);

    void putAll(TFloatIntMap tFloatIntMap);

    int get(float f);

    void clear();

    boolean isEmpty();

    int remove(float f);

    int size();

    TFloatSet keySet();

    float[] keys();

    float[] keys(float[] fArr);

    TIntCollection valueCollection();

    int[] values();

    int[] values(int[] iArr);

    boolean containsValue(int i);

    boolean containsKey(float f);

    TFloatIntIterator iterator();

    boolean forEachKey(TFloatProcedure tFloatProcedure);

    boolean forEachValue(TIntProcedure tIntProcedure);

    boolean forEachEntry(TFloatIntProcedure tFloatIntProcedure);

    void transformValues(TIntFunction tIntFunction);

    boolean retainEntries(TFloatIntProcedure tFloatIntProcedure);

    boolean increment(float f);

    boolean adjustValue(float f, int i);

    int adjustOrPutValue(float f, int i, int i2);
}
