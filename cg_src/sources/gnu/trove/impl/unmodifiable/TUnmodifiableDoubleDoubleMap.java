package gnu.trove.impl.unmodifiable;

import gnu.trove.TCollections;
import gnu.trove.TDoubleCollection;
import gnu.trove.function.TDoubleFunction;
import gnu.trove.iterator.TDoubleDoubleIterator;
import gnu.trove.map.TDoubleDoubleMap;
import gnu.trove.procedure.TDoubleDoubleProcedure;
import gnu.trove.procedure.TDoubleProcedure;
import gnu.trove.set.TDoubleSet;
import java.io.Serializable;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/impl/unmodifiable/TUnmodifiableDoubleDoubleMap.class */
public class TUnmodifiableDoubleDoubleMap implements TDoubleDoubleMap, Serializable {
    private static final long serialVersionUID = -1034234728574286014L;
    private final TDoubleDoubleMap m;
    private transient TDoubleSet keySet = null;
    private transient TDoubleCollection values = null;

    public TUnmodifiableDoubleDoubleMap(TDoubleDoubleMap m) {
        if (m == null) {
            throw new NullPointerException();
        }
        this.m = m;
    }

    @Override // gnu.trove.map.TDoubleDoubleMap
    public int size() {
        return this.m.size();
    }

    @Override // gnu.trove.map.TDoubleDoubleMap
    public boolean isEmpty() {
        return this.m.isEmpty();
    }

    @Override // gnu.trove.map.TDoubleDoubleMap
    public boolean containsKey(double key) {
        return this.m.containsKey(key);
    }

    @Override // gnu.trove.map.TDoubleDoubleMap
    public boolean containsValue(double val) {
        return this.m.containsValue(val);
    }

    @Override // gnu.trove.map.TDoubleDoubleMap
    public double get(double key) {
        return this.m.get(key);
    }

    @Override // gnu.trove.map.TDoubleDoubleMap
    public double put(double key, double value) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TDoubleDoubleMap
    public double remove(double key) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TDoubleDoubleMap
    public void putAll(TDoubleDoubleMap m) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TDoubleDoubleMap
    public void putAll(Map<? extends Double, ? extends Double> map) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TDoubleDoubleMap
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TDoubleDoubleMap
    public TDoubleSet keySet() {
        if (this.keySet == null) {
            this.keySet = TCollections.unmodifiableSet(this.m.keySet());
        }
        return this.keySet;
    }

    @Override // gnu.trove.map.TDoubleDoubleMap
    public double[] keys() {
        return this.m.keys();
    }

    @Override // gnu.trove.map.TDoubleDoubleMap
    public double[] keys(double[] array) {
        return this.m.keys(array);
    }

    @Override // gnu.trove.map.TDoubleDoubleMap
    public TDoubleCollection valueCollection() {
        if (this.values == null) {
            this.values = TCollections.unmodifiableCollection(this.m.valueCollection());
        }
        return this.values;
    }

    @Override // gnu.trove.map.TDoubleDoubleMap
    public double[] values() {
        return this.m.values();
    }

    @Override // gnu.trove.map.TDoubleDoubleMap
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

    @Override // gnu.trove.map.TDoubleDoubleMap
    public double getNoEntryKey() {
        return this.m.getNoEntryKey();
    }

    @Override // gnu.trove.map.TDoubleDoubleMap
    public double getNoEntryValue() {
        return this.m.getNoEntryValue();
    }

    @Override // gnu.trove.map.TDoubleDoubleMap
    public boolean forEachKey(TDoubleProcedure procedure) {
        return this.m.forEachKey(procedure);
    }

    @Override // gnu.trove.map.TDoubleDoubleMap
    public boolean forEachValue(TDoubleProcedure procedure) {
        return this.m.forEachValue(procedure);
    }

    @Override // gnu.trove.map.TDoubleDoubleMap
    public boolean forEachEntry(TDoubleDoubleProcedure procedure) {
        return this.m.forEachEntry(procedure);
    }

    @Override // gnu.trove.map.TDoubleDoubleMap
    public TDoubleDoubleIterator iterator() {
        return new TDoubleDoubleIterator() { // from class: gnu.trove.impl.unmodifiable.TUnmodifiableDoubleDoubleMap.1
            TDoubleDoubleIterator iter;

            {
                this.iter = TUnmodifiableDoubleDoubleMap.this.m.iterator();
            }

            @Override // gnu.trove.iterator.TDoubleDoubleIterator
            public double key() {
                return this.iter.key();
            }

            @Override // gnu.trove.iterator.TDoubleDoubleIterator
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

            @Override // gnu.trove.iterator.TDoubleDoubleIterator
            public double setValue(double val) {
                throw new UnsupportedOperationException();
            }

            @Override // gnu.trove.iterator.TIterator, java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override // gnu.trove.map.TDoubleDoubleMap
    public double putIfAbsent(double key, double value) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TDoubleDoubleMap
    public void transformValues(TDoubleFunction function) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TDoubleDoubleMap
    public boolean retainEntries(TDoubleDoubleProcedure procedure) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TDoubleDoubleMap
    public boolean increment(double key) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TDoubleDoubleMap
    public boolean adjustValue(double key, double amount) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TDoubleDoubleMap
    public double adjustOrPutValue(double key, double adjust_amount, double put_amount) {
        throw new UnsupportedOperationException();
    }
}
