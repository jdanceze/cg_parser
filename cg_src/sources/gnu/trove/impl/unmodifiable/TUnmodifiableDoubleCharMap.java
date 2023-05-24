package gnu.trove.impl.unmodifiable;

import gnu.trove.TCharCollection;
import gnu.trove.TCollections;
import gnu.trove.function.TCharFunction;
import gnu.trove.iterator.TDoubleCharIterator;
import gnu.trove.map.TDoubleCharMap;
import gnu.trove.procedure.TCharProcedure;
import gnu.trove.procedure.TDoubleCharProcedure;
import gnu.trove.procedure.TDoubleProcedure;
import gnu.trove.set.TDoubleSet;
import java.io.Serializable;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/impl/unmodifiable/TUnmodifiableDoubleCharMap.class */
public class TUnmodifiableDoubleCharMap implements TDoubleCharMap, Serializable {
    private static final long serialVersionUID = -1034234728574286014L;
    private final TDoubleCharMap m;
    private transient TDoubleSet keySet = null;
    private transient TCharCollection values = null;

    public TUnmodifiableDoubleCharMap(TDoubleCharMap m) {
        if (m == null) {
            throw new NullPointerException();
        }
        this.m = m;
    }

    @Override // gnu.trove.map.TDoubleCharMap
    public int size() {
        return this.m.size();
    }

    @Override // gnu.trove.map.TDoubleCharMap
    public boolean isEmpty() {
        return this.m.isEmpty();
    }

    @Override // gnu.trove.map.TDoubleCharMap
    public boolean containsKey(double key) {
        return this.m.containsKey(key);
    }

    @Override // gnu.trove.map.TDoubleCharMap
    public boolean containsValue(char val) {
        return this.m.containsValue(val);
    }

    @Override // gnu.trove.map.TDoubleCharMap
    public char get(double key) {
        return this.m.get(key);
    }

    @Override // gnu.trove.map.TDoubleCharMap
    public char put(double key, char value) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TDoubleCharMap
    public char remove(double key) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TDoubleCharMap
    public void putAll(TDoubleCharMap m) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TDoubleCharMap
    public void putAll(Map<? extends Double, ? extends Character> map) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TDoubleCharMap
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TDoubleCharMap
    public TDoubleSet keySet() {
        if (this.keySet == null) {
            this.keySet = TCollections.unmodifiableSet(this.m.keySet());
        }
        return this.keySet;
    }

    @Override // gnu.trove.map.TDoubleCharMap
    public double[] keys() {
        return this.m.keys();
    }

    @Override // gnu.trove.map.TDoubleCharMap
    public double[] keys(double[] array) {
        return this.m.keys(array);
    }

    @Override // gnu.trove.map.TDoubleCharMap
    public TCharCollection valueCollection() {
        if (this.values == null) {
            this.values = TCollections.unmodifiableCollection(this.m.valueCollection());
        }
        return this.values;
    }

    @Override // gnu.trove.map.TDoubleCharMap
    public char[] values() {
        return this.m.values();
    }

    @Override // gnu.trove.map.TDoubleCharMap
    public char[] values(char[] array) {
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

    @Override // gnu.trove.map.TDoubleCharMap
    public double getNoEntryKey() {
        return this.m.getNoEntryKey();
    }

    @Override // gnu.trove.map.TDoubleCharMap
    public char getNoEntryValue() {
        return this.m.getNoEntryValue();
    }

    @Override // gnu.trove.map.TDoubleCharMap
    public boolean forEachKey(TDoubleProcedure procedure) {
        return this.m.forEachKey(procedure);
    }

    @Override // gnu.trove.map.TDoubleCharMap
    public boolean forEachValue(TCharProcedure procedure) {
        return this.m.forEachValue(procedure);
    }

    @Override // gnu.trove.map.TDoubleCharMap
    public boolean forEachEntry(TDoubleCharProcedure procedure) {
        return this.m.forEachEntry(procedure);
    }

    @Override // gnu.trove.map.TDoubleCharMap
    public TDoubleCharIterator iterator() {
        return new TDoubleCharIterator() { // from class: gnu.trove.impl.unmodifiable.TUnmodifiableDoubleCharMap.1
            TDoubleCharIterator iter;

            {
                this.iter = TUnmodifiableDoubleCharMap.this.m.iterator();
            }

            @Override // gnu.trove.iterator.TDoubleCharIterator
            public double key() {
                return this.iter.key();
            }

            @Override // gnu.trove.iterator.TDoubleCharIterator
            public char value() {
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

            @Override // gnu.trove.iterator.TDoubleCharIterator
            public char setValue(char val) {
                throw new UnsupportedOperationException();
            }

            @Override // gnu.trove.iterator.TIterator, java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override // gnu.trove.map.TDoubleCharMap
    public char putIfAbsent(double key, char value) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TDoubleCharMap
    public void transformValues(TCharFunction function) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TDoubleCharMap
    public boolean retainEntries(TDoubleCharProcedure procedure) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TDoubleCharMap
    public boolean increment(double key) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TDoubleCharMap
    public boolean adjustValue(double key, char amount) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TDoubleCharMap
    public char adjustOrPutValue(double key, char adjust_amount, char put_amount) {
        throw new UnsupportedOperationException();
    }
}
