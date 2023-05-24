package gnu.trove.impl.unmodifiable;

import gnu.trove.TByteCollection;
import gnu.trove.TCollections;
import gnu.trove.function.TByteFunction;
import gnu.trove.iterator.TByteByteIterator;
import gnu.trove.map.TByteByteMap;
import gnu.trove.procedure.TByteByteProcedure;
import gnu.trove.procedure.TByteProcedure;
import gnu.trove.set.TByteSet;
import java.io.Serializable;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/impl/unmodifiable/TUnmodifiableByteByteMap.class */
public class TUnmodifiableByteByteMap implements TByteByteMap, Serializable {
    private static final long serialVersionUID = -1034234728574286014L;
    private final TByteByteMap m;
    private transient TByteSet keySet = null;
    private transient TByteCollection values = null;

    public TUnmodifiableByteByteMap(TByteByteMap m) {
        if (m == null) {
            throw new NullPointerException();
        }
        this.m = m;
    }

    @Override // gnu.trove.map.TByteByteMap
    public int size() {
        return this.m.size();
    }

    @Override // gnu.trove.map.TByteByteMap
    public boolean isEmpty() {
        return this.m.isEmpty();
    }

    @Override // gnu.trove.map.TByteByteMap
    public boolean containsKey(byte key) {
        return this.m.containsKey(key);
    }

    @Override // gnu.trove.map.TByteByteMap
    public boolean containsValue(byte val) {
        return this.m.containsValue(val);
    }

    @Override // gnu.trove.map.TByteByteMap
    public byte get(byte key) {
        return this.m.get(key);
    }

    @Override // gnu.trove.map.TByteByteMap
    public byte put(byte key, byte value) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TByteByteMap
    public byte remove(byte key) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TByteByteMap
    public void putAll(TByteByteMap m) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TByteByteMap
    public void putAll(Map<? extends Byte, ? extends Byte> map) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TByteByteMap
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TByteByteMap
    public TByteSet keySet() {
        if (this.keySet == null) {
            this.keySet = TCollections.unmodifiableSet(this.m.keySet());
        }
        return this.keySet;
    }

    @Override // gnu.trove.map.TByteByteMap
    public byte[] keys() {
        return this.m.keys();
    }

    @Override // gnu.trove.map.TByteByteMap
    public byte[] keys(byte[] array) {
        return this.m.keys(array);
    }

    @Override // gnu.trove.map.TByteByteMap
    public TByteCollection valueCollection() {
        if (this.values == null) {
            this.values = TCollections.unmodifiableCollection(this.m.valueCollection());
        }
        return this.values;
    }

    @Override // gnu.trove.map.TByteByteMap
    public byte[] values() {
        return this.m.values();
    }

    @Override // gnu.trove.map.TByteByteMap
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

    @Override // gnu.trove.map.TByteByteMap
    public byte getNoEntryKey() {
        return this.m.getNoEntryKey();
    }

    @Override // gnu.trove.map.TByteByteMap
    public byte getNoEntryValue() {
        return this.m.getNoEntryValue();
    }

    @Override // gnu.trove.map.TByteByteMap
    public boolean forEachKey(TByteProcedure procedure) {
        return this.m.forEachKey(procedure);
    }

    @Override // gnu.trove.map.TByteByteMap
    public boolean forEachValue(TByteProcedure procedure) {
        return this.m.forEachValue(procedure);
    }

    @Override // gnu.trove.map.TByteByteMap
    public boolean forEachEntry(TByteByteProcedure procedure) {
        return this.m.forEachEntry(procedure);
    }

    @Override // gnu.trove.map.TByteByteMap
    public TByteByteIterator iterator() {
        return new TByteByteIterator() { // from class: gnu.trove.impl.unmodifiable.TUnmodifiableByteByteMap.1
            TByteByteIterator iter;

            {
                this.iter = TUnmodifiableByteByteMap.this.m.iterator();
            }

            @Override // gnu.trove.iterator.TByteByteIterator
            public byte key() {
                return this.iter.key();
            }

            @Override // gnu.trove.iterator.TByteByteIterator
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

            @Override // gnu.trove.iterator.TByteByteIterator
            public byte setValue(byte val) {
                throw new UnsupportedOperationException();
            }

            @Override // gnu.trove.iterator.TIterator, java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override // gnu.trove.map.TByteByteMap
    public byte putIfAbsent(byte key, byte value) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TByteByteMap
    public void transformValues(TByteFunction function) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TByteByteMap
    public boolean retainEntries(TByteByteProcedure procedure) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TByteByteMap
    public boolean increment(byte key) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TByteByteMap
    public boolean adjustValue(byte key, byte amount) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TByteByteMap
    public byte adjustOrPutValue(byte key, byte adjust_amount, byte put_amount) {
        throw new UnsupportedOperationException();
    }
}
