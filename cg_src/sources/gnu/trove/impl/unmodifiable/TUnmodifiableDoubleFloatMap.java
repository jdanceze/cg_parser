package gnu.trove.impl.unmodifiable;

import gnu.trove.TCollections;
import gnu.trove.TFloatCollection;
import gnu.trove.function.TFloatFunction;
import gnu.trove.iterator.TDoubleFloatIterator;
import gnu.trove.map.TDoubleFloatMap;
import gnu.trove.procedure.TDoubleFloatProcedure;
import gnu.trove.procedure.TDoubleProcedure;
import gnu.trove.procedure.TFloatProcedure;
import gnu.trove.set.TDoubleSet;
import java.io.Serializable;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/impl/unmodifiable/TUnmodifiableDoubleFloatMap.class */
public class TUnmodifiableDoubleFloatMap implements TDoubleFloatMap, Serializable {
    private static final long serialVersionUID = -1034234728574286014L;
    private final TDoubleFloatMap m;
    private transient TDoubleSet keySet = null;
    private transient TFloatCollection values = null;

    public TUnmodifiableDoubleFloatMap(TDoubleFloatMap m) {
        if (m == null) {
            throw new NullPointerException();
        }
        this.m = m;
    }

    @Override // gnu.trove.map.TDoubleFloatMap
    public int size() {
        return this.m.size();
    }

    @Override // gnu.trove.map.TDoubleFloatMap
    public boolean isEmpty() {
        return this.m.isEmpty();
    }

    @Override // gnu.trove.map.TDoubleFloatMap
    public boolean containsKey(double key) {
        return this.m.containsKey(key);
    }

    @Override // gnu.trove.map.TDoubleFloatMap
    public boolean containsValue(float val) {
        return this.m.containsValue(val);
    }

    @Override // gnu.trove.map.TDoubleFloatMap
    public float get(double key) {
        return this.m.get(key);
    }

    @Override // gnu.trove.map.TDoubleFloatMap
    public float put(double key, float value) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TDoubleFloatMap
    public float remove(double key) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TDoubleFloatMap
    public void putAll(TDoubleFloatMap m) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TDoubleFloatMap
    public void putAll(Map<? extends Double, ? extends Float> map) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TDoubleFloatMap
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TDoubleFloatMap
    public TDoubleSet keySet() {
        if (this.keySet == null) {
            this.keySet = TCollections.unmodifiableSet(this.m.keySet());
        }
        return this.keySet;
    }

    @Override // gnu.trove.map.TDoubleFloatMap
    public double[] keys() {
        return this.m.keys();
    }

    @Override // gnu.trove.map.TDoubleFloatMap
    public double[] keys(double[] array) {
        return this.m.keys(array);
    }

    @Override // gnu.trove.map.TDoubleFloatMap
    public TFloatCollection valueCollection() {
        if (this.values == null) {
            this.values = TCollections.unmodifiableCollection(this.m.valueCollection());
        }
        return this.values;
    }

    @Override // gnu.trove.map.TDoubleFloatMap
    public float[] values() {
        return this.m.values();
    }

    @Override // gnu.trove.map.TDoubleFloatMap
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

    @Override // gnu.trove.map.TDoubleFloatMap
    public double getNoEntryKey() {
        return this.m.getNoEntryKey();
    }

    @Override // gnu.trove.map.TDoubleFloatMap
    public float getNoEntryValue() {
        return this.m.getNoEntryValue();
    }

    @Override // gnu.trove.map.TDoubleFloatMap
    public boolean forEachKey(TDoubleProcedure procedure) {
        return this.m.forEachKey(procedure);
    }

    @Override // gnu.trove.map.TDoubleFloatMap
    public boolean forEachValue(TFloatProcedure procedure) {
        return this.m.forEachValue(procedure);
    }

    @Override // gnu.trove.map.TDoubleFloatMap
    public boolean forEachEntry(TDoubleFloatProcedure procedure) {
        return this.m.forEachEntry(procedure);
    }

    @Override // gnu.trove.map.TDoubleFloatMap
    public TDoubleFloatIterator iterator() {
        return new TDoubleFloatIterator() { // from class: gnu.trove.impl.unmodifiable.TUnmodifiableDoubleFloatMap.1
            TDoubleFloatIterator iter;

            {
                this.iter = TUnmodifiableDoubleFloatMap.this.m.iterator();
            }

            @Override // gnu.trove.iterator.TDoubleFloatIterator
            public double key() {
                return this.iter.key();
            }

            @Override // gnu.trove.iterator.TDoubleFloatIterator
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

            @Override // gnu.trove.iterator.TDoubleFloatIterator
            public float setValue(float val) {
                throw new UnsupportedOperationException();
            }

            @Override // gnu.trove.iterator.TIterator, java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override // gnu.trove.map.TDoubleFloatMap
    public float putIfAbsent(double key, float value) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TDoubleFloatMap
    public void transformValues(TFloatFunction function) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TDoubleFloatMap
    public boolean retainEntries(TDoubleFloatProcedure procedure) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TDoubleFloatMap
    public boolean increment(double key) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TDoubleFloatMap
    public boolean adjustValue(double key, float amount) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TDoubleFloatMap
    public float adjustOrPutValue(double key, float adjust_amount, float put_amount) {
        throw new UnsupportedOperationException();
    }
}
