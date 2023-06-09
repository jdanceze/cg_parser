package gnu.trove.impl.unmodifiable;

import gnu.trove.TByteCollection;
import gnu.trove.TCollections;
import gnu.trove.function.TByteFunction;
import gnu.trove.iterator.TFloatByteIterator;
import gnu.trove.map.TFloatByteMap;
import gnu.trove.procedure.TByteProcedure;
import gnu.trove.procedure.TFloatByteProcedure;
import gnu.trove.procedure.TFloatProcedure;
import gnu.trove.set.TFloatSet;
import java.io.Serializable;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/impl/unmodifiable/TUnmodifiableFloatByteMap.class */
public class TUnmodifiableFloatByteMap implements TFloatByteMap, Serializable {
    private static final long serialVersionUID = -1034234728574286014L;
    private final TFloatByteMap m;
    private transient TFloatSet keySet = null;
    private transient TByteCollection values = null;

    public TUnmodifiableFloatByteMap(TFloatByteMap m) {
        if (m == null) {
            throw new NullPointerException();
        }
        this.m = m;
    }

    @Override // gnu.trove.map.TFloatByteMap
    public int size() {
        return this.m.size();
    }

    @Override // gnu.trove.map.TFloatByteMap
    public boolean isEmpty() {
        return this.m.isEmpty();
    }

    @Override // gnu.trove.map.TFloatByteMap
    public boolean containsKey(float key) {
        return this.m.containsKey(key);
    }

    @Override // gnu.trove.map.TFloatByteMap
    public boolean containsValue(byte val) {
        return this.m.containsValue(val);
    }

    @Override // gnu.trove.map.TFloatByteMap
    public byte get(float key) {
        return this.m.get(key);
    }

    @Override // gnu.trove.map.TFloatByteMap
    public byte put(float key, byte value) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TFloatByteMap
    public byte remove(float key) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TFloatByteMap
    public void putAll(TFloatByteMap m) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TFloatByteMap
    public void putAll(Map<? extends Float, ? extends Byte> map) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TFloatByteMap
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TFloatByteMap
    public TFloatSet keySet() {
        if (this.keySet == null) {
            this.keySet = TCollections.unmodifiableSet(this.m.keySet());
        }
        return this.keySet;
    }

    @Override // gnu.trove.map.TFloatByteMap
    public float[] keys() {
        return this.m.keys();
    }

    @Override // gnu.trove.map.TFloatByteMap
    public float[] keys(float[] array) {
        return this.m.keys(array);
    }

    @Override // gnu.trove.map.TFloatByteMap
    public TByteCollection valueCollection() {
        if (this.values == null) {
            this.values = TCollections.unmodifiableCollection(this.m.valueCollection());
        }
        return this.values;
    }

    @Override // gnu.trove.map.TFloatByteMap
    public byte[] values() {
        return this.m.values();
    }

    @Override // gnu.trove.map.TFloatByteMap
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

    @Override // gnu.trove.map.TFloatByteMap
    public float getNoEntryKey() {
        return this.m.getNoEntryKey();
    }

    @Override // gnu.trove.map.TFloatByteMap
    public byte getNoEntryValue() {
        return this.m.getNoEntryValue();
    }

    @Override // gnu.trove.map.TFloatByteMap
    public boolean forEachKey(TFloatProcedure procedure) {
        return this.m.forEachKey(procedure);
    }

    @Override // gnu.trove.map.TFloatByteMap
    public boolean forEachValue(TByteProcedure procedure) {
        return this.m.forEachValue(procedure);
    }

    @Override // gnu.trove.map.TFloatByteMap
    public boolean forEachEntry(TFloatByteProcedure procedure) {
        return this.m.forEachEntry(procedure);
    }

    @Override // gnu.trove.map.TFloatByteMap
    public TFloatByteIterator iterator() {
        return new TFloatByteIterator() { // from class: gnu.trove.impl.unmodifiable.TUnmodifiableFloatByteMap.1
            TFloatByteIterator iter;

            {
                this.iter = TUnmodifiableFloatByteMap.this.m.iterator();
            }

            @Override // gnu.trove.iterator.TFloatByteIterator
            public float key() {
                return this.iter.key();
            }

            @Override // gnu.trove.iterator.TFloatByteIterator
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

            @Override // gnu.trove.iterator.TFloatByteIterator
            public byte setValue(byte val) {
                throw new UnsupportedOperationException();
            }

            @Override // gnu.trove.iterator.TIterator, java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override // gnu.trove.map.TFloatByteMap
    public byte putIfAbsent(float key, byte value) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TFloatByteMap
    public void transformValues(TByteFunction function) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TFloatByteMap
    public boolean retainEntries(TFloatByteProcedure procedure) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TFloatByteMap
    public boolean increment(float key) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TFloatByteMap
    public boolean adjustValue(float key, byte amount) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TFloatByteMap
    public byte adjustOrPutValue(float key, byte adjust_amount, byte put_amount) {
        throw new UnsupportedOperationException();
    }
}
