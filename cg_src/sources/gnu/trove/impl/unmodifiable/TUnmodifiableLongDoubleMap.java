package gnu.trove.impl.unmodifiable;

import gnu.trove.TCollections;
import gnu.trove.TDoubleCollection;
import gnu.trove.function.TDoubleFunction;
import gnu.trove.iterator.TLongDoubleIterator;
import gnu.trove.map.TLongDoubleMap;
import gnu.trove.procedure.TDoubleProcedure;
import gnu.trove.procedure.TLongDoubleProcedure;
import gnu.trove.procedure.TLongProcedure;
import gnu.trove.set.TLongSet;
import java.io.Serializable;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/impl/unmodifiable/TUnmodifiableLongDoubleMap.class */
public class TUnmodifiableLongDoubleMap implements TLongDoubleMap, Serializable {
    private static final long serialVersionUID = -1034234728574286014L;
    private final TLongDoubleMap m;
    private transient TLongSet keySet = null;
    private transient TDoubleCollection values = null;

    public TUnmodifiableLongDoubleMap(TLongDoubleMap m) {
        if (m == null) {
            throw new NullPointerException();
        }
        this.m = m;
    }

    @Override // gnu.trove.map.TLongDoubleMap
    public int size() {
        return this.m.size();
    }

    @Override // gnu.trove.map.TLongDoubleMap
    public boolean isEmpty() {
        return this.m.isEmpty();
    }

    @Override // gnu.trove.map.TLongDoubleMap
    public boolean containsKey(long key) {
        return this.m.containsKey(key);
    }

    @Override // gnu.trove.map.TLongDoubleMap
    public boolean containsValue(double val) {
        return this.m.containsValue(val);
    }

    @Override // gnu.trove.map.TLongDoubleMap
    public double get(long key) {
        return this.m.get(key);
    }

    @Override // gnu.trove.map.TLongDoubleMap
    public double put(long key, double value) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TLongDoubleMap
    public double remove(long key) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TLongDoubleMap
    public void putAll(TLongDoubleMap m) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TLongDoubleMap
    public void putAll(Map<? extends Long, ? extends Double> map) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TLongDoubleMap
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TLongDoubleMap
    public TLongSet keySet() {
        if (this.keySet == null) {
            this.keySet = TCollections.unmodifiableSet(this.m.keySet());
        }
        return this.keySet;
    }

    @Override // gnu.trove.map.TLongDoubleMap
    public long[] keys() {
        return this.m.keys();
    }

    @Override // gnu.trove.map.TLongDoubleMap
    public long[] keys(long[] array) {
        return this.m.keys(array);
    }

    @Override // gnu.trove.map.TLongDoubleMap
    public TDoubleCollection valueCollection() {
        if (this.values == null) {
            this.values = TCollections.unmodifiableCollection(this.m.valueCollection());
        }
        return this.values;
    }

    @Override // gnu.trove.map.TLongDoubleMap
    public double[] values() {
        return this.m.values();
    }

    @Override // gnu.trove.map.TLongDoubleMap
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

    @Override // gnu.trove.map.TLongDoubleMap
    public long getNoEntryKey() {
        return this.m.getNoEntryKey();
    }

    @Override // gnu.trove.map.TLongDoubleMap
    public double getNoEntryValue() {
        return this.m.getNoEntryValue();
    }

    @Override // gnu.trove.map.TLongDoubleMap
    public boolean forEachKey(TLongProcedure procedure) {
        return this.m.forEachKey(procedure);
    }

    @Override // gnu.trove.map.TLongDoubleMap
    public boolean forEachValue(TDoubleProcedure procedure) {
        return this.m.forEachValue(procedure);
    }

    @Override // gnu.trove.map.TLongDoubleMap
    public boolean forEachEntry(TLongDoubleProcedure procedure) {
        return this.m.forEachEntry(procedure);
    }

    @Override // gnu.trove.map.TLongDoubleMap
    public TLongDoubleIterator iterator() {
        return new TLongDoubleIterator() { // from class: gnu.trove.impl.unmodifiable.TUnmodifiableLongDoubleMap.1
            TLongDoubleIterator iter;

            {
                this.iter = TUnmodifiableLongDoubleMap.this.m.iterator();
            }

            @Override // gnu.trove.iterator.TLongDoubleIterator
            public long key() {
                return this.iter.key();
            }

            @Override // gnu.trove.iterator.TLongDoubleIterator
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

            @Override // gnu.trove.iterator.TLongDoubleIterator
            public double setValue(double val) {
                throw new UnsupportedOperationException();
            }

            @Override // gnu.trove.iterator.TIterator, java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override // gnu.trove.map.TLongDoubleMap
    public double putIfAbsent(long key, double value) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TLongDoubleMap
    public void transformValues(TDoubleFunction function) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TLongDoubleMap
    public boolean retainEntries(TLongDoubleProcedure procedure) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TLongDoubleMap
    public boolean increment(long key) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TLongDoubleMap
    public boolean adjustValue(long key, double amount) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TLongDoubleMap
    public double adjustOrPutValue(long key, double adjust_amount, double put_amount) {
        throw new UnsupportedOperationException();
    }
}
