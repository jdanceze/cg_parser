package gnu.trove.impl.unmodifiable;

import gnu.trove.TCollections;
import gnu.trove.TFloatCollection;
import gnu.trove.function.TFloatFunction;
import gnu.trove.iterator.TIntFloatIterator;
import gnu.trove.map.TIntFloatMap;
import gnu.trove.procedure.TFloatProcedure;
import gnu.trove.procedure.TIntFloatProcedure;
import gnu.trove.procedure.TIntProcedure;
import gnu.trove.set.TIntSet;
import java.io.Serializable;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/impl/unmodifiable/TUnmodifiableIntFloatMap.class */
public class TUnmodifiableIntFloatMap implements TIntFloatMap, Serializable {
    private static final long serialVersionUID = -1034234728574286014L;
    private final TIntFloatMap m;
    private transient TIntSet keySet = null;
    private transient TFloatCollection values = null;

    public TUnmodifiableIntFloatMap(TIntFloatMap m) {
        if (m == null) {
            throw new NullPointerException();
        }
        this.m = m;
    }

    @Override // gnu.trove.map.TIntFloatMap
    public int size() {
        return this.m.size();
    }

    @Override // gnu.trove.map.TIntFloatMap
    public boolean isEmpty() {
        return this.m.isEmpty();
    }

    @Override // gnu.trove.map.TIntFloatMap
    public boolean containsKey(int key) {
        return this.m.containsKey(key);
    }

    @Override // gnu.trove.map.TIntFloatMap
    public boolean containsValue(float val) {
        return this.m.containsValue(val);
    }

    @Override // gnu.trove.map.TIntFloatMap
    public float get(int key) {
        return this.m.get(key);
    }

    @Override // gnu.trove.map.TIntFloatMap
    public float put(int key, float value) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TIntFloatMap
    public float remove(int key) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TIntFloatMap
    public void putAll(TIntFloatMap m) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TIntFloatMap
    public void putAll(Map<? extends Integer, ? extends Float> map) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TIntFloatMap
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TIntFloatMap
    public TIntSet keySet() {
        if (this.keySet == null) {
            this.keySet = TCollections.unmodifiableSet(this.m.keySet());
        }
        return this.keySet;
    }

    @Override // gnu.trove.map.TIntFloatMap
    public int[] keys() {
        return this.m.keys();
    }

    @Override // gnu.trove.map.TIntFloatMap
    public int[] keys(int[] array) {
        return this.m.keys(array);
    }

    @Override // gnu.trove.map.TIntFloatMap
    public TFloatCollection valueCollection() {
        if (this.values == null) {
            this.values = TCollections.unmodifiableCollection(this.m.valueCollection());
        }
        return this.values;
    }

    @Override // gnu.trove.map.TIntFloatMap
    public float[] values() {
        return this.m.values();
    }

    @Override // gnu.trove.map.TIntFloatMap
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

    @Override // gnu.trove.map.TIntFloatMap
    public int getNoEntryKey() {
        return this.m.getNoEntryKey();
    }

    @Override // gnu.trove.map.TIntFloatMap
    public float getNoEntryValue() {
        return this.m.getNoEntryValue();
    }

    @Override // gnu.trove.map.TIntFloatMap
    public boolean forEachKey(TIntProcedure procedure) {
        return this.m.forEachKey(procedure);
    }

    @Override // gnu.trove.map.TIntFloatMap
    public boolean forEachValue(TFloatProcedure procedure) {
        return this.m.forEachValue(procedure);
    }

    @Override // gnu.trove.map.TIntFloatMap
    public boolean forEachEntry(TIntFloatProcedure procedure) {
        return this.m.forEachEntry(procedure);
    }

    @Override // gnu.trove.map.TIntFloatMap
    public TIntFloatIterator iterator() {
        return new TIntFloatIterator() { // from class: gnu.trove.impl.unmodifiable.TUnmodifiableIntFloatMap.1
            TIntFloatIterator iter;

            {
                this.iter = TUnmodifiableIntFloatMap.this.m.iterator();
            }

            @Override // gnu.trove.iterator.TIntFloatIterator
            public int key() {
                return this.iter.key();
            }

            @Override // gnu.trove.iterator.TIntFloatIterator
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

            @Override // gnu.trove.iterator.TIntFloatIterator
            public float setValue(float val) {
                throw new UnsupportedOperationException();
            }

            @Override // gnu.trove.iterator.TIterator, java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override // gnu.trove.map.TIntFloatMap
    public float putIfAbsent(int key, float value) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TIntFloatMap
    public void transformValues(TFloatFunction function) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TIntFloatMap
    public boolean retainEntries(TIntFloatProcedure procedure) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TIntFloatMap
    public boolean increment(int key) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TIntFloatMap
    public boolean adjustValue(int key, float amount) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TIntFloatMap
    public float adjustOrPutValue(int key, float adjust_amount, float put_amount) {
        throw new UnsupportedOperationException();
    }
}
