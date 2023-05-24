package gnu.trove.impl.unmodifiable;

import gnu.trove.TByteCollection;
import gnu.trove.TCollections;
import gnu.trove.function.TByteFunction;
import gnu.trove.iterator.TDoubleByteIterator;
import gnu.trove.map.TDoubleByteMap;
import gnu.trove.procedure.TByteProcedure;
import gnu.trove.procedure.TDoubleByteProcedure;
import gnu.trove.procedure.TDoubleProcedure;
import gnu.trove.set.TDoubleSet;
import java.io.Serializable;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/impl/unmodifiable/TUnmodifiableDoubleByteMap.class */
public class TUnmodifiableDoubleByteMap implements TDoubleByteMap, Serializable {
    private static final long serialVersionUID = -1034234728574286014L;
    private final TDoubleByteMap m;
    private transient TDoubleSet keySet = null;
    private transient TByteCollection values = null;

    public TUnmodifiableDoubleByteMap(TDoubleByteMap m) {
        if (m == null) {
            throw new NullPointerException();
        }
        this.m = m;
    }

    @Override // gnu.trove.map.TDoubleByteMap
    public int size() {
        return this.m.size();
    }

    @Override // gnu.trove.map.TDoubleByteMap
    public boolean isEmpty() {
        return this.m.isEmpty();
    }

    @Override // gnu.trove.map.TDoubleByteMap
    public boolean containsKey(double key) {
        return this.m.containsKey(key);
    }

    @Override // gnu.trove.map.TDoubleByteMap
    public boolean containsValue(byte val) {
        return this.m.containsValue(val);
    }

    @Override // gnu.trove.map.TDoubleByteMap
    public byte get(double key) {
        return this.m.get(key);
    }

    @Override // gnu.trove.map.TDoubleByteMap
    public byte put(double key, byte value) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TDoubleByteMap
    public byte remove(double key) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TDoubleByteMap
    public void putAll(TDoubleByteMap m) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TDoubleByteMap
    public void putAll(Map<? extends Double, ? extends Byte> map) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TDoubleByteMap
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TDoubleByteMap
    public TDoubleSet keySet() {
        if (this.keySet == null) {
            this.keySet = TCollections.unmodifiableSet(this.m.keySet());
        }
        return this.keySet;
    }

    @Override // gnu.trove.map.TDoubleByteMap
    public double[] keys() {
        return this.m.keys();
    }

    @Override // gnu.trove.map.TDoubleByteMap
    public double[] keys(double[] array) {
        return this.m.keys(array);
    }

    @Override // gnu.trove.map.TDoubleByteMap
    public TByteCollection valueCollection() {
        if (this.values == null) {
            this.values = TCollections.unmodifiableCollection(this.m.valueCollection());
        }
        return this.values;
    }

    @Override // gnu.trove.map.TDoubleByteMap
    public byte[] values() {
        return this.m.values();
    }

    @Override // gnu.trove.map.TDoubleByteMap
    public byte[] values(byte[] array) {
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

    @Override // gnu.trove.map.TDoubleByteMap
    public double getNoEntryKey() {
        return this.m.getNoEntryKey();
    }

    @Override // gnu.trove.map.TDoubleByteMap
    public byte getNoEntryValue() {
        return this.m.getNoEntryValue();
    }

    @Override // gnu.trove.map.TDoubleByteMap
    public boolean forEachKey(TDoubleProcedure procedure) {
        return this.m.forEachKey(procedure);
    }

    @Override // gnu.trove.map.TDoubleByteMap
    public boolean forEachValue(TByteProcedure procedure) {
        return this.m.forEachValue(procedure);
    }

    @Override // gnu.trove.map.TDoubleByteMap
    public boolean forEachEntry(TDoubleByteProcedure procedure) {
        return this.m.forEachEntry(procedure);
    }

    @Override // gnu.trove.map.TDoubleByteMap
    public TDoubleByteIterator iterator() {
        return new TDoubleByteIterator() { // from class: gnu.trove.impl.unmodifiable.TUnmodifiableDoubleByteMap.1
            TDoubleByteIterator iter;

            {
                this.iter = TUnmodifiableDoubleByteMap.this.m.iterator();
            }

            @Override // gnu.trove.iterator.TDoubleByteIterator
            public double key() {
                return this.iter.key();
            }

            @Override // gnu.trove.iterator.TDoubleByteIterator
            public byte value() {
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

            @Override // gnu.trove.iterator.TDoubleByteIterator
            public byte setValue(byte val) {
                throw new UnsupportedOperationException();
            }

            @Override // gnu.trove.iterator.TIterator, java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override // gnu.trove.map.TDoubleByteMap
    public byte putIfAbsent(double key, byte value) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TDoubleByteMap
    public void transformValues(TByteFunction function) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TDoubleByteMap
    public boolean retainEntries(TDoubleByteProcedure procedure) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TDoubleByteMap
    public boolean increment(double key) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TDoubleByteMap
    public boolean adjustValue(double key, byte amount) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TDoubleByteMap
    public byte adjustOrPutValue(double key, byte adjust_amount, byte put_amount) {
        throw new UnsupportedOperationException();
    }
}
