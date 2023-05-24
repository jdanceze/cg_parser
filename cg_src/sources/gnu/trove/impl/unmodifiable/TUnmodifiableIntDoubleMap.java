package gnu.trove.impl.unmodifiable;

import gnu.trove.TCollections;
import gnu.trove.TDoubleCollection;
import gnu.trove.function.TDoubleFunction;
import gnu.trove.iterator.TIntDoubleIterator;
import gnu.trove.map.TIntDoubleMap;
import gnu.trove.procedure.TDoubleProcedure;
import gnu.trove.procedure.TIntDoubleProcedure;
import gnu.trove.procedure.TIntProcedure;
import gnu.trove.set.TIntSet;
import java.io.Serializable;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/impl/unmodifiable/TUnmodifiableIntDoubleMap.class */
public class TUnmodifiableIntDoubleMap implements TIntDoubleMap, Serializable {
    private static final long serialVersionUID = -1034234728574286014L;
    private final TIntDoubleMap m;
    private transient TIntSet keySet = null;
    private transient TDoubleCollection values = null;

    public TUnmodifiableIntDoubleMap(TIntDoubleMap m) {
        if (m == null) {
            throw new NullPointerException();
        }
        this.m = m;
    }

    @Override // gnu.trove.map.TIntDoubleMap
    public int size() {
        return this.m.size();
    }

    @Override // gnu.trove.map.TIntDoubleMap
    public boolean isEmpty() {
        return this.m.isEmpty();
    }

    @Override // gnu.trove.map.TIntDoubleMap
    public boolean containsKey(int key) {
        return this.m.containsKey(key);
    }

    @Override // gnu.trove.map.TIntDoubleMap
    public boolean containsValue(double val) {
        return this.m.containsValue(val);
    }

    @Override // gnu.trove.map.TIntDoubleMap
    public double get(int key) {
        return this.m.get(key);
    }

    @Override // gnu.trove.map.TIntDoubleMap
    public double put(int key, double value) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TIntDoubleMap
    public double remove(int key) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TIntDoubleMap
    public void putAll(TIntDoubleMap m) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TIntDoubleMap
    public void putAll(Map<? extends Integer, ? extends Double> map) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TIntDoubleMap
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TIntDoubleMap
    public TIntSet keySet() {
        if (this.keySet == null) {
            this.keySet = TCollections.unmodifiableSet(this.m.keySet());
        }
        return this.keySet;
    }

    @Override // gnu.trove.map.TIntDoubleMap
    public int[] keys() {
        return this.m.keys();
    }

    @Override // gnu.trove.map.TIntDoubleMap
    public int[] keys(int[] array) {
        return this.m.keys(array);
    }

    @Override // gnu.trove.map.TIntDoubleMap
    public TDoubleCollection valueCollection() {
        if (this.values == null) {
            this.values = TCollections.unmodifiableCollection(this.m.valueCollection());
        }
        return this.values;
    }

    @Override // gnu.trove.map.TIntDoubleMap
    public double[] values() {
        return this.m.values();
    }

    @Override // gnu.trove.map.TIntDoubleMap
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

    @Override // gnu.trove.map.TIntDoubleMap
    public int getNoEntryKey() {
        return this.m.getNoEntryKey();
    }

    @Override // gnu.trove.map.TIntDoubleMap
    public double getNoEntryValue() {
        return this.m.getNoEntryValue();
    }

    @Override // gnu.trove.map.TIntDoubleMap
    public boolean forEachKey(TIntProcedure procedure) {
        return this.m.forEachKey(procedure);
    }

    @Override // gnu.trove.map.TIntDoubleMap
    public boolean forEachValue(TDoubleProcedure procedure) {
        return this.m.forEachValue(procedure);
    }

    @Override // gnu.trove.map.TIntDoubleMap
    public boolean forEachEntry(TIntDoubleProcedure procedure) {
        return this.m.forEachEntry(procedure);
    }

    @Override // gnu.trove.map.TIntDoubleMap
    public TIntDoubleIterator iterator() {
        return new TIntDoubleIterator() { // from class: gnu.trove.impl.unmodifiable.TUnmodifiableIntDoubleMap.1
            TIntDoubleIterator iter;

            {
                this.iter = TUnmodifiableIntDoubleMap.this.m.iterator();
            }

            @Override // gnu.trove.iterator.TIntDoubleIterator
            public int key() {
                return this.iter.key();
            }

            @Override // gnu.trove.iterator.TIntDoubleIterator
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

            @Override // gnu.trove.iterator.TIntDoubleIterator
            public double setValue(double val) {
                throw new UnsupportedOperationException();
            }

            @Override // gnu.trove.iterator.TIterator, java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override // gnu.trove.map.TIntDoubleMap
    public double putIfAbsent(int key, double value) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TIntDoubleMap
    public void transformValues(TDoubleFunction function) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TIntDoubleMap
    public boolean retainEntries(TIntDoubleProcedure procedure) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TIntDoubleMap
    public boolean increment(int key) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TIntDoubleMap
    public boolean adjustValue(int key, double amount) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TIntDoubleMap
    public double adjustOrPutValue(int key, double adjust_amount, double put_amount) {
        throw new UnsupportedOperationException();
    }
}
