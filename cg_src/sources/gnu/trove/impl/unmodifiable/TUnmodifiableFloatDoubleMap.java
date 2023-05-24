package gnu.trove.impl.unmodifiable;

import gnu.trove.TCollections;
import gnu.trove.TDoubleCollection;
import gnu.trove.function.TDoubleFunction;
import gnu.trove.iterator.TFloatDoubleIterator;
import gnu.trove.map.TFloatDoubleMap;
import gnu.trove.procedure.TDoubleProcedure;
import gnu.trove.procedure.TFloatDoubleProcedure;
import gnu.trove.procedure.TFloatProcedure;
import gnu.trove.set.TFloatSet;
import java.io.Serializable;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/impl/unmodifiable/TUnmodifiableFloatDoubleMap.class */
public class TUnmodifiableFloatDoubleMap implements TFloatDoubleMap, Serializable {
    private static final long serialVersionUID = -1034234728574286014L;
    private final TFloatDoubleMap m;
    private transient TFloatSet keySet = null;
    private transient TDoubleCollection values = null;

    public TUnmodifiableFloatDoubleMap(TFloatDoubleMap m) {
        if (m == null) {
            throw new NullPointerException();
        }
        this.m = m;
    }

    @Override // gnu.trove.map.TFloatDoubleMap
    public int size() {
        return this.m.size();
    }

    @Override // gnu.trove.map.TFloatDoubleMap
    public boolean isEmpty() {
        return this.m.isEmpty();
    }

    @Override // gnu.trove.map.TFloatDoubleMap
    public boolean containsKey(float key) {
        return this.m.containsKey(key);
    }

    @Override // gnu.trove.map.TFloatDoubleMap
    public boolean containsValue(double val) {
        return this.m.containsValue(val);
    }

    @Override // gnu.trove.map.TFloatDoubleMap
    public double get(float key) {
        return this.m.get(key);
    }

    @Override // gnu.trove.map.TFloatDoubleMap
    public double put(float key, double value) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TFloatDoubleMap
    public double remove(float key) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TFloatDoubleMap
    public void putAll(TFloatDoubleMap m) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TFloatDoubleMap
    public void putAll(Map<? extends Float, ? extends Double> map) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TFloatDoubleMap
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TFloatDoubleMap
    public TFloatSet keySet() {
        if (this.keySet == null) {
            this.keySet = TCollections.unmodifiableSet(this.m.keySet());
        }
        return this.keySet;
    }

    @Override // gnu.trove.map.TFloatDoubleMap
    public float[] keys() {
        return this.m.keys();
    }

    @Override // gnu.trove.map.TFloatDoubleMap
    public float[] keys(float[] array) {
        return this.m.keys(array);
    }

    @Override // gnu.trove.map.TFloatDoubleMap
    public TDoubleCollection valueCollection() {
        if (this.values == null) {
            this.values = TCollections.unmodifiableCollection(this.m.valueCollection());
        }
        return this.values;
    }

    @Override // gnu.trove.map.TFloatDoubleMap
    public double[] values() {
        return this.m.values();
    }

    @Override // gnu.trove.map.TFloatDoubleMap
    public double[] values(double[] array) {
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

    @Override // gnu.trove.map.TFloatDoubleMap
    public float getNoEntryKey() {
        return this.m.getNoEntryKey();
    }

    @Override // gnu.trove.map.TFloatDoubleMap
    public double getNoEntryValue() {
        return this.m.getNoEntryValue();
    }

    @Override // gnu.trove.map.TFloatDoubleMap
    public boolean forEachKey(TFloatProcedure procedure) {
        return this.m.forEachKey(procedure);
    }

    @Override // gnu.trove.map.TFloatDoubleMap
    public boolean forEachValue(TDoubleProcedure procedure) {
        return this.m.forEachValue(procedure);
    }

    @Override // gnu.trove.map.TFloatDoubleMap
    public boolean forEachEntry(TFloatDoubleProcedure procedure) {
        return this.m.forEachEntry(procedure);
    }

    @Override // gnu.trove.map.TFloatDoubleMap
    public TFloatDoubleIterator iterator() {
        return new TFloatDoubleIterator() { // from class: gnu.trove.impl.unmodifiable.TUnmodifiableFloatDoubleMap.1
            TFloatDoubleIterator iter;

            {
                this.iter = TUnmodifiableFloatDoubleMap.this.m.iterator();
            }

            @Override // gnu.trove.iterator.TFloatDoubleIterator
            public float key() {
                return this.iter.key();
            }

            @Override // gnu.trove.iterator.TFloatDoubleIterator
            public double value() {
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

            @Override // gnu.trove.iterator.TFloatDoubleIterator
            public double setValue(double val) {
                throw new UnsupportedOperationException();
            }

            @Override // gnu.trove.iterator.TIterator, java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override // gnu.trove.map.TFloatDoubleMap
    public double putIfAbsent(float key, double value) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TFloatDoubleMap
    public void transformValues(TDoubleFunction function) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TFloatDoubleMap
    public boolean retainEntries(TFloatDoubleProcedure procedure) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TFloatDoubleMap
    public boolean increment(float key) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TFloatDoubleMap
    public boolean adjustValue(float key, double amount) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TFloatDoubleMap
    public double adjustOrPutValue(float key, double adjust_amount, double put_amount) {
        throw new UnsupportedOperationException();
    }
}
