package gnu.trove.impl.unmodifiable;

import gnu.trove.TCollections;
import gnu.trove.TDoubleCollection;
import gnu.trove.function.TDoubleFunction;
import gnu.trove.iterator.TShortDoubleIterator;
import gnu.trove.map.TShortDoubleMap;
import gnu.trove.procedure.TDoubleProcedure;
import gnu.trove.procedure.TShortDoubleProcedure;
import gnu.trove.procedure.TShortProcedure;
import gnu.trove.set.TShortSet;
import java.io.Serializable;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/impl/unmodifiable/TUnmodifiableShortDoubleMap.class */
public class TUnmodifiableShortDoubleMap implements TShortDoubleMap, Serializable {
    private static final long serialVersionUID = -1034234728574286014L;
    private final TShortDoubleMap m;
    private transient TShortSet keySet = null;
    private transient TDoubleCollection values = null;

    public TUnmodifiableShortDoubleMap(TShortDoubleMap m) {
        if (m == null) {
            throw new NullPointerException();
        }
        this.m = m;
    }

    @Override // gnu.trove.map.TShortDoubleMap
    public int size() {
        return this.m.size();
    }

    @Override // gnu.trove.map.TShortDoubleMap
    public boolean isEmpty() {
        return this.m.isEmpty();
    }

    @Override // gnu.trove.map.TShortDoubleMap
    public boolean containsKey(short key) {
        return this.m.containsKey(key);
    }

    @Override // gnu.trove.map.TShortDoubleMap
    public boolean containsValue(double val) {
        return this.m.containsValue(val);
    }

    @Override // gnu.trove.map.TShortDoubleMap
    public double get(short key) {
        return this.m.get(key);
    }

    @Override // gnu.trove.map.TShortDoubleMap
    public double put(short key, double value) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TShortDoubleMap
    public double remove(short key) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TShortDoubleMap
    public void putAll(TShortDoubleMap m) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TShortDoubleMap
    public void putAll(Map<? extends Short, ? extends Double> map) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TShortDoubleMap
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TShortDoubleMap
    public TShortSet keySet() {
        if (this.keySet == null) {
            this.keySet = TCollections.unmodifiableSet(this.m.keySet());
        }
        return this.keySet;
    }

    @Override // gnu.trove.map.TShortDoubleMap
    public short[] keys() {
        return this.m.keys();
    }

    @Override // gnu.trove.map.TShortDoubleMap
    public short[] keys(short[] array) {
        return this.m.keys(array);
    }

    @Override // gnu.trove.map.TShortDoubleMap
    public TDoubleCollection valueCollection() {
        if (this.values == null) {
            this.values = TCollections.unmodifiableCollection(this.m.valueCollection());
        }
        return this.values;
    }

    @Override // gnu.trove.map.TShortDoubleMap
    public double[] values() {
        return this.m.values();
    }

    @Override // gnu.trove.map.TShortDoubleMap
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

    @Override // gnu.trove.map.TShortDoubleMap
    public short getNoEntryKey() {
        return this.m.getNoEntryKey();
    }

    @Override // gnu.trove.map.TShortDoubleMap
    public double getNoEntryValue() {
        return this.m.getNoEntryValue();
    }

    @Override // gnu.trove.map.TShortDoubleMap
    public boolean forEachKey(TShortProcedure procedure) {
        return this.m.forEachKey(procedure);
    }

    @Override // gnu.trove.map.TShortDoubleMap
    public boolean forEachValue(TDoubleProcedure procedure) {
        return this.m.forEachValue(procedure);
    }

    @Override // gnu.trove.map.TShortDoubleMap
    public boolean forEachEntry(TShortDoubleProcedure procedure) {
        return this.m.forEachEntry(procedure);
    }

    @Override // gnu.trove.map.TShortDoubleMap
    public TShortDoubleIterator iterator() {
        return new TShortDoubleIterator() { // from class: gnu.trove.impl.unmodifiable.TUnmodifiableShortDoubleMap.1
            TShortDoubleIterator iter;

            {
                this.iter = TUnmodifiableShortDoubleMap.this.m.iterator();
            }

            @Override // gnu.trove.iterator.TShortDoubleIterator
            public short key() {
                return this.iter.key();
            }

            @Override // gnu.trove.iterator.TShortDoubleIterator
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

            @Override // gnu.trove.iterator.TShortDoubleIterator
            public double setValue(double val) {
                throw new UnsupportedOperationException();
            }

            @Override // gnu.trove.iterator.TIterator, java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override // gnu.trove.map.TShortDoubleMap
    public double putIfAbsent(short key, double value) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TShortDoubleMap
    public void transformValues(TDoubleFunction function) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TShortDoubleMap
    public boolean retainEntries(TShortDoubleProcedure procedure) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TShortDoubleMap
    public boolean increment(short key) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TShortDoubleMap
    public boolean adjustValue(short key, double amount) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TShortDoubleMap
    public double adjustOrPutValue(short key, double adjust_amount, double put_amount) {
        throw new UnsupportedOperationException();
    }
}
