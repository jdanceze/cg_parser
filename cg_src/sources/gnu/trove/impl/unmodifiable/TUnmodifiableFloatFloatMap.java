package gnu.trove.impl.unmodifiable;

import gnu.trove.TCollections;
import gnu.trove.TFloatCollection;
import gnu.trove.function.TFloatFunction;
import gnu.trove.iterator.TFloatFloatIterator;
import gnu.trove.map.TFloatFloatMap;
import gnu.trove.procedure.TFloatFloatProcedure;
import gnu.trove.procedure.TFloatProcedure;
import gnu.trove.set.TFloatSet;
import java.io.Serializable;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/impl/unmodifiable/TUnmodifiableFloatFloatMap.class */
public class TUnmodifiableFloatFloatMap implements TFloatFloatMap, Serializable {
    private static final long serialVersionUID = -1034234728574286014L;
    private final TFloatFloatMap m;
    private transient TFloatSet keySet = null;
    private transient TFloatCollection values = null;

    public TUnmodifiableFloatFloatMap(TFloatFloatMap m) {
        if (m == null) {
            throw new NullPointerException();
        }
        this.m = m;
    }

    @Override // gnu.trove.map.TFloatFloatMap
    public int size() {
        return this.m.size();
    }

    @Override // gnu.trove.map.TFloatFloatMap
    public boolean isEmpty() {
        return this.m.isEmpty();
    }

    @Override // gnu.trove.map.TFloatFloatMap
    public boolean containsKey(float key) {
        return this.m.containsKey(key);
    }

    @Override // gnu.trove.map.TFloatFloatMap
    public boolean containsValue(float val) {
        return this.m.containsValue(val);
    }

    @Override // gnu.trove.map.TFloatFloatMap
    public float get(float key) {
        return this.m.get(key);
    }

    @Override // gnu.trove.map.TFloatFloatMap
    public float put(float key, float value) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TFloatFloatMap
    public float remove(float key) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TFloatFloatMap
    public void putAll(TFloatFloatMap m) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TFloatFloatMap
    public void putAll(Map<? extends Float, ? extends Float> map) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TFloatFloatMap
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TFloatFloatMap
    public TFloatSet keySet() {
        if (this.keySet == null) {
            this.keySet = TCollections.unmodifiableSet(this.m.keySet());
        }
        return this.keySet;
    }

    @Override // gnu.trove.map.TFloatFloatMap
    public float[] keys() {
        return this.m.keys();
    }

    @Override // gnu.trove.map.TFloatFloatMap
    public float[] keys(float[] array) {
        return this.m.keys(array);
    }

    @Override // gnu.trove.map.TFloatFloatMap
    public TFloatCollection valueCollection() {
        if (this.values == null) {
            this.values = TCollections.unmodifiableCollection(this.m.valueCollection());
        }
        return this.values;
    }

    @Override // gnu.trove.map.TFloatFloatMap
    public float[] values() {
        return this.m.values();
    }

    @Override // gnu.trove.map.TFloatFloatMap
    public float[] values(float[] array) {
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

    @Override // gnu.trove.map.TFloatFloatMap
    public float getNoEntryKey() {
        return this.m.getNoEntryKey();
    }

    @Override // gnu.trove.map.TFloatFloatMap
    public float getNoEntryValue() {
        return this.m.getNoEntryValue();
    }

    @Override // gnu.trove.map.TFloatFloatMap
    public boolean forEachKey(TFloatProcedure procedure) {
        return this.m.forEachKey(procedure);
    }

    @Override // gnu.trove.map.TFloatFloatMap
    public boolean forEachValue(TFloatProcedure procedure) {
        return this.m.forEachValue(procedure);
    }

    @Override // gnu.trove.map.TFloatFloatMap
    public boolean forEachEntry(TFloatFloatProcedure procedure) {
        return this.m.forEachEntry(procedure);
    }

    @Override // gnu.trove.map.TFloatFloatMap
    public TFloatFloatIterator iterator() {
        return new TFloatFloatIterator() { // from class: gnu.trove.impl.unmodifiable.TUnmodifiableFloatFloatMap.1
            TFloatFloatIterator iter;

            {
                this.iter = TUnmodifiableFloatFloatMap.this.m.iterator();
            }

            @Override // gnu.trove.iterator.TFloatFloatIterator
            public float key() {
                return this.iter.key();
            }

            @Override // gnu.trove.iterator.TFloatFloatIterator
            public float value() {
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

            @Override // gnu.trove.iterator.TFloatFloatIterator
            public float setValue(float val) {
                throw new UnsupportedOperationException();
            }

            @Override // gnu.trove.iterator.TIterator, java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override // gnu.trove.map.TFloatFloatMap
    public float putIfAbsent(float key, float value) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TFloatFloatMap
    public void transformValues(TFloatFunction function) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TFloatFloatMap
    public boolean retainEntries(TFloatFloatProcedure procedure) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TFloatFloatMap
    public boolean increment(float key) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TFloatFloatMap
    public boolean adjustValue(float key, float amount) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TFloatFloatMap
    public float adjustOrPutValue(float key, float adjust_amount, float put_amount) {
        throw new UnsupportedOperationException();
    }
}
