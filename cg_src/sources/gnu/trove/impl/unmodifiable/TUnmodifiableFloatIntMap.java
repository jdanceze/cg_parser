package gnu.trove.impl.unmodifiable;

import gnu.trove.TCollections;
import gnu.trove.TIntCollection;
import gnu.trove.function.TIntFunction;
import gnu.trove.iterator.TFloatIntIterator;
import gnu.trove.map.TFloatIntMap;
import gnu.trove.procedure.TFloatIntProcedure;
import gnu.trove.procedure.TFloatProcedure;
import gnu.trove.procedure.TIntProcedure;
import gnu.trove.set.TFloatSet;
import java.io.Serializable;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/impl/unmodifiable/TUnmodifiableFloatIntMap.class */
public class TUnmodifiableFloatIntMap implements TFloatIntMap, Serializable {
    private static final long serialVersionUID = -1034234728574286014L;
    private final TFloatIntMap m;
    private transient TFloatSet keySet = null;
    private transient TIntCollection values = null;

    public TUnmodifiableFloatIntMap(TFloatIntMap m) {
        if (m == null) {
            throw new NullPointerException();
        }
        this.m = m;
    }

    @Override // gnu.trove.map.TFloatIntMap
    public int size() {
        return this.m.size();
    }

    @Override // gnu.trove.map.TFloatIntMap
    public boolean isEmpty() {
        return this.m.isEmpty();
    }

    @Override // gnu.trove.map.TFloatIntMap
    public boolean containsKey(float key) {
        return this.m.containsKey(key);
    }

    @Override // gnu.trove.map.TFloatIntMap
    public boolean containsValue(int val) {
        return this.m.containsValue(val);
    }

    @Override // gnu.trove.map.TFloatIntMap
    public int get(float key) {
        return this.m.get(key);
    }

    @Override // gnu.trove.map.TFloatIntMap
    public int put(float key, int value) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TFloatIntMap
    public int remove(float key) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TFloatIntMap
    public void putAll(TFloatIntMap m) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TFloatIntMap
    public void putAll(Map<? extends Float, ? extends Integer> map) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TFloatIntMap
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TFloatIntMap
    public TFloatSet keySet() {
        if (this.keySet == null) {
            this.keySet = TCollections.unmodifiableSet(this.m.keySet());
        }
        return this.keySet;
    }

    @Override // gnu.trove.map.TFloatIntMap
    public float[] keys() {
        return this.m.keys();
    }

    @Override // gnu.trove.map.TFloatIntMap
    public float[] keys(float[] array) {
        return this.m.keys(array);
    }

    @Override // gnu.trove.map.TFloatIntMap
    public TIntCollection valueCollection() {
        if (this.values == null) {
            this.values = TCollections.unmodifiableCollection(this.m.valueCollection());
        }
        return this.values;
    }

    @Override // gnu.trove.map.TFloatIntMap
    public int[] values() {
        return this.m.values();
    }

    @Override // gnu.trove.map.TFloatIntMap
    public int[] values(int[] array) {
        return this.m.values(array);
    }

    public boolean equals(Object o) {
        return o == this || this.m.equals(o);
    }

    public int hashCode() {
        return this.m.hashCode();
    }

    public String toString() {
        return this.m.toString();
    }

    @Override // gnu.trove.map.TFloatIntMap
    public float getNoEntryKey() {
        return this.m.getNoEntryKey();
    }

    @Override // gnu.trove.map.TFloatIntMap
    public int getNoEntryValue() {
        return this.m.getNoEntryValue();
    }

    @Override // gnu.trove.map.TFloatIntMap
    public boolean forEachKey(TFloatProcedure procedure) {
        return this.m.forEachKey(procedure);
    }

    @Override // gnu.trove.map.TFloatIntMap
    public boolean forEachValue(TIntProcedure procedure) {
        return this.m.forEachValue(procedure);
    }

    @Override // gnu.trove.map.TFloatIntMap
    public boolean forEachEntry(TFloatIntProcedure procedure) {
        return this.m.forEachEntry(procedure);
    }

    @Override // gnu.trove.map.TFloatIntMap
    public TFloatIntIterator iterator() {
        return new TFloatIntIterator() { // from class: gnu.trove.impl.unmodifiable.TUnmodifiableFloatIntMap.1
            TFloatIntIterator iter;

            {
                this.iter = TUnmodifiableFloatIntMap.this.m.iterator();
            }

            @Override // gnu.trove.iterator.TFloatIntIterator
            public float key() {
                return this.iter.key();
            }

            @Override // gnu.trove.iterator.TFloatIntIterator
            public int value() {
                return this.iter.value();
            }

            @Override // gnu.trove.iterator.TAdvancingIterator
            public void advance() {
                this.iter.advance();
            }

            @Override // gnu.trove.iterator.TIterator, java.util.Iterator
            public boolean hasNext() {
                return this.iter.hasNext();
            }

            @Override // gnu.trove.iterator.TFloatIntIterator
            public int setValue(int val) {
                throw new UnsupportedOperationException();
            }

            @Override // gnu.trove.iterator.TIterator, java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override // gnu.trove.map.TFloatIntMap
    public int putIfAbsent(float key, int value) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TFloatIntMap
    public void transformValues(TIntFunction function) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TFloatIntMap
    public boolean retainEntries(TFloatIntProcedure procedure) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TFloatIntMap
    public boolean increment(float key) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TFloatIntMap
    public boolean adjustValue(float key, int amount) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TFloatIntMap
    public int adjustOrPutValue(float key, int adjust_amount, int put_amount) {
        throw new UnsupportedOperationException();
    }
}
