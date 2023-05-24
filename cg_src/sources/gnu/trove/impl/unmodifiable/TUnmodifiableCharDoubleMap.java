package gnu.trove.impl.unmodifiable;

import gnu.trove.TCollections;
import gnu.trove.TDoubleCollection;
import gnu.trove.function.TDoubleFunction;
import gnu.trove.iterator.TCharDoubleIterator;
import gnu.trove.map.TCharDoubleMap;
import gnu.trove.procedure.TCharDoubleProcedure;
import gnu.trove.procedure.TCharProcedure;
import gnu.trove.procedure.TDoubleProcedure;
import gnu.trove.set.TCharSet;
import java.io.Serializable;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/impl/unmodifiable/TUnmodifiableCharDoubleMap.class */
public class TUnmodifiableCharDoubleMap implements TCharDoubleMap, Serializable {
    private static final long serialVersionUID = -1034234728574286014L;
    private final TCharDoubleMap m;
    private transient TCharSet keySet = null;
    private transient TDoubleCollection values = null;

    public TUnmodifiableCharDoubleMap(TCharDoubleMap m) {
        if (m == null) {
            throw new NullPointerException();
        }
        this.m = m;
    }

    @Override // gnu.trove.map.TCharDoubleMap
    public int size() {
        return this.m.size();
    }

    @Override // gnu.trove.map.TCharDoubleMap
    public boolean isEmpty() {
        return this.m.isEmpty();
    }

    @Override // gnu.trove.map.TCharDoubleMap
    public boolean containsKey(char key) {
        return this.m.containsKey(key);
    }

    @Override // gnu.trove.map.TCharDoubleMap
    public boolean containsValue(double val) {
        return this.m.containsValue(val);
    }

    @Override // gnu.trove.map.TCharDoubleMap
    public double get(char key) {
        return this.m.get(key);
    }

    @Override // gnu.trove.map.TCharDoubleMap
    public double put(char key, double value) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TCharDoubleMap
    public double remove(char key) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TCharDoubleMap
    public void putAll(TCharDoubleMap m) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TCharDoubleMap
    public void putAll(Map<? extends Character, ? extends Double> map) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TCharDoubleMap
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TCharDoubleMap
    public TCharSet keySet() {
        if (this.keySet == null) {
            this.keySet = TCollections.unmodifiableSet(this.m.keySet());
        }
        return this.keySet;
    }

    @Override // gnu.trove.map.TCharDoubleMap
    public char[] keys() {
        return this.m.keys();
    }

    @Override // gnu.trove.map.TCharDoubleMap
    public char[] keys(char[] array) {
        return this.m.keys(array);
    }

    @Override // gnu.trove.map.TCharDoubleMap
    public TDoubleCollection valueCollection() {
        if (this.values == null) {
            this.values = TCollections.unmodifiableCollection(this.m.valueCollection());
        }
        return this.values;
    }

    @Override // gnu.trove.map.TCharDoubleMap
    public double[] values() {
        return this.m.values();
    }

    @Override // gnu.trove.map.TCharDoubleMap
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

    @Override // gnu.trove.map.TCharDoubleMap
    public char getNoEntryKey() {
        return this.m.getNoEntryKey();
    }

    @Override // gnu.trove.map.TCharDoubleMap
    public double getNoEntryValue() {
        return this.m.getNoEntryValue();
    }

    @Override // gnu.trove.map.TCharDoubleMap
    public boolean forEachKey(TCharProcedure procedure) {
        return this.m.forEachKey(procedure);
    }

    @Override // gnu.trove.map.TCharDoubleMap
    public boolean forEachValue(TDoubleProcedure procedure) {
        return this.m.forEachValue(procedure);
    }

    @Override // gnu.trove.map.TCharDoubleMap
    public boolean forEachEntry(TCharDoubleProcedure procedure) {
        return this.m.forEachEntry(procedure);
    }

    @Override // gnu.trove.map.TCharDoubleMap
    public TCharDoubleIterator iterator() {
        return new TCharDoubleIterator() { // from class: gnu.trove.impl.unmodifiable.TUnmodifiableCharDoubleMap.1
            TCharDoubleIterator iter;

            {
                this.iter = TUnmodifiableCharDoubleMap.this.m.iterator();
            }

            @Override // gnu.trove.iterator.TCharDoubleIterator
            public char key() {
                return this.iter.key();
            }

            @Override // gnu.trove.iterator.TCharDoubleIterator
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

            @Override // gnu.trove.iterator.TCharDoubleIterator
            public double setValue(double val) {
                throw new UnsupportedOperationException();
            }

            @Override // gnu.trove.iterator.TIterator, java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override // gnu.trove.map.TCharDoubleMap
    public double putIfAbsent(char key, double value) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TCharDoubleMap
    public void transformValues(TDoubleFunction function) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TCharDoubleMap
    public boolean retainEntries(TCharDoubleProcedure procedure) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TCharDoubleMap
    public boolean increment(char key) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TCharDoubleMap
    public boolean adjustValue(char key, double amount) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TCharDoubleMap
    public double adjustOrPutValue(char key, double adjust_amount, double put_amount) {
        throw new UnsupportedOperationException();
    }
}
