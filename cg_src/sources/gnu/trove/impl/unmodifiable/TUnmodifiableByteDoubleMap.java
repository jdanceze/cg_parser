package gnu.trove.impl.unmodifiable;

import gnu.trove.TCollections;
import gnu.trove.TDoubleCollection;
import gnu.trove.function.TDoubleFunction;
import gnu.trove.iterator.TByteDoubleIterator;
import gnu.trove.map.TByteDoubleMap;
import gnu.trove.procedure.TByteDoubleProcedure;
import gnu.trove.procedure.TByteProcedure;
import gnu.trove.procedure.TDoubleProcedure;
import gnu.trove.set.TByteSet;
import java.io.Serializable;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/impl/unmodifiable/TUnmodifiableByteDoubleMap.class */
public class TUnmodifiableByteDoubleMap implements TByteDoubleMap, Serializable {
    private static final long serialVersionUID = -1034234728574286014L;
    private final TByteDoubleMap m;
    private transient TByteSet keySet = null;
    private transient TDoubleCollection values = null;

    public TUnmodifiableByteDoubleMap(TByteDoubleMap m) {
        if (m == null) {
            throw new NullPointerException();
        }
        this.m = m;
    }

    @Override // gnu.trove.map.TByteDoubleMap
    public int size() {
        return this.m.size();
    }

    @Override // gnu.trove.map.TByteDoubleMap
    public boolean isEmpty() {
        return this.m.isEmpty();
    }

    @Override // gnu.trove.map.TByteDoubleMap
    public boolean containsKey(byte key) {
        return this.m.containsKey(key);
    }

    @Override // gnu.trove.map.TByteDoubleMap
    public boolean containsValue(double val) {
        return this.m.containsValue(val);
    }

    @Override // gnu.trove.map.TByteDoubleMap
    public double get(byte key) {
        return this.m.get(key);
    }

    @Override // gnu.trove.map.TByteDoubleMap
    public double put(byte key, double value) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TByteDoubleMap
    public double remove(byte key) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TByteDoubleMap
    public void putAll(TByteDoubleMap m) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TByteDoubleMap
    public void putAll(Map<? extends Byte, ? extends Double> map) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TByteDoubleMap
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TByteDoubleMap
    public TByteSet keySet() {
        if (this.keySet == null) {
            this.keySet = TCollections.unmodifiableSet(this.m.keySet());
        }
        return this.keySet;
    }

    @Override // gnu.trove.map.TByteDoubleMap
    public byte[] keys() {
        return this.m.keys();
    }

    @Override // gnu.trove.map.TByteDoubleMap
    public byte[] keys(byte[] array) {
        return this.m.keys(array);
    }

    @Override // gnu.trove.map.TByteDoubleMap
    public TDoubleCollection valueCollection() {
        if (this.values == null) {
            this.values = TCollections.unmodifiableCollection(this.m.valueCollection());
        }
        return this.values;
    }

    @Override // gnu.trove.map.TByteDoubleMap
    public double[] values() {
        return this.m.values();
    }

    @Override // gnu.trove.map.TByteDoubleMap
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

    @Override // gnu.trove.map.TByteDoubleMap
    public byte getNoEntryKey() {
        return this.m.getNoEntryKey();
    }

    @Override // gnu.trove.map.TByteDoubleMap
    public double getNoEntryValue() {
        return this.m.getNoEntryValue();
    }

    @Override // gnu.trove.map.TByteDoubleMap
    public boolean forEachKey(TByteProcedure procedure) {
        return this.m.forEachKey(procedure);
    }

    @Override // gnu.trove.map.TByteDoubleMap
    public boolean forEachValue(TDoubleProcedure procedure) {
        return this.m.forEachValue(procedure);
    }

    @Override // gnu.trove.map.TByteDoubleMap
    public boolean forEachEntry(TByteDoubleProcedure procedure) {
        return this.m.forEachEntry(procedure);
    }

    @Override // gnu.trove.map.TByteDoubleMap
    public TByteDoubleIterator iterator() {
        return new TByteDoubleIterator() { // from class: gnu.trove.impl.unmodifiable.TUnmodifiableByteDoubleMap.1
            TByteDoubleIterator iter;

            {
                this.iter = TUnmodifiableByteDoubleMap.this.m.iterator();
            }

            @Override // gnu.trove.iterator.TByteDoubleIterator
            public byte key() {
                return this.iter.key();
            }

            @Override // gnu.trove.iterator.TByteDoubleIterator
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

            @Override // gnu.trove.iterator.TByteDoubleIterator
            public double setValue(double val) {
                throw new UnsupportedOperationException();
            }

            @Override // gnu.trove.iterator.TIterator, java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override // gnu.trove.map.TByteDoubleMap
    public double putIfAbsent(byte key, double value) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TByteDoubleMap
    public void transformValues(TDoubleFunction function) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TByteDoubleMap
    public boolean retainEntries(TByteDoubleProcedure procedure) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TByteDoubleMap
    public boolean increment(byte key) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TByteDoubleMap
    public boolean adjustValue(byte key, double amount) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TByteDoubleMap
    public double adjustOrPutValue(byte key, double adjust_amount, double put_amount) {
        throw new UnsupportedOperationException();
    }
}
