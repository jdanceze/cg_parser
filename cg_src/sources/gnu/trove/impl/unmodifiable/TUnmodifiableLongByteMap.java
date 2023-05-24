package gnu.trove.impl.unmodifiable;

import gnu.trove.TByteCollection;
import gnu.trove.TCollections;
import gnu.trove.function.TByteFunction;
import gnu.trove.iterator.TLongByteIterator;
import gnu.trove.map.TLongByteMap;
import gnu.trove.procedure.TByteProcedure;
import gnu.trove.procedure.TLongByteProcedure;
import gnu.trove.procedure.TLongProcedure;
import gnu.trove.set.TLongSet;
import java.io.Serializable;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/impl/unmodifiable/TUnmodifiableLongByteMap.class */
public class TUnmodifiableLongByteMap implements TLongByteMap, Serializable {
    private static final long serialVersionUID = -1034234728574286014L;
    private final TLongByteMap m;
    private transient TLongSet keySet = null;
    private transient TByteCollection values = null;

    public TUnmodifiableLongByteMap(TLongByteMap m) {
        if (m == null) {
            throw new NullPointerException();
        }
        this.m = m;
    }

    @Override // gnu.trove.map.TLongByteMap
    public int size() {
        return this.m.size();
    }

    @Override // gnu.trove.map.TLongByteMap
    public boolean isEmpty() {
        return this.m.isEmpty();
    }

    @Override // gnu.trove.map.TLongByteMap
    public boolean containsKey(long key) {
        return this.m.containsKey(key);
    }

    @Override // gnu.trove.map.TLongByteMap
    public boolean containsValue(byte val) {
        return this.m.containsValue(val);
    }

    @Override // gnu.trove.map.TLongByteMap
    public byte get(long key) {
        return this.m.get(key);
    }

    @Override // gnu.trove.map.TLongByteMap
    public byte put(long key, byte value) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TLongByteMap
    public byte remove(long key) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TLongByteMap
    public void putAll(TLongByteMap m) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TLongByteMap
    public void putAll(Map<? extends Long, ? extends Byte> map) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TLongByteMap
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TLongByteMap
    public TLongSet keySet() {
        if (this.keySet == null) {
            this.keySet = TCollections.unmodifiableSet(this.m.keySet());
        }
        return this.keySet;
    }

    @Override // gnu.trove.map.TLongByteMap
    public long[] keys() {
        return this.m.keys();
    }

    @Override // gnu.trove.map.TLongByteMap
    public long[] keys(long[] array) {
        return this.m.keys(array);
    }

    @Override // gnu.trove.map.TLongByteMap
    public TByteCollection valueCollection() {
        if (this.values == null) {
            this.values = TCollections.unmodifiableCollection(this.m.valueCollection());
        }
        return this.values;
    }

    @Override // gnu.trove.map.TLongByteMap
    public byte[] values() {
        return this.m.values();
    }

    @Override // gnu.trove.map.TLongByteMap
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

    @Override // gnu.trove.map.TLongByteMap
    public long getNoEntryKey() {
        return this.m.getNoEntryKey();
    }

    @Override // gnu.trove.map.TLongByteMap
    public byte getNoEntryValue() {
        return this.m.getNoEntryValue();
    }

    @Override // gnu.trove.map.TLongByteMap
    public boolean forEachKey(TLongProcedure procedure) {
        return this.m.forEachKey(procedure);
    }

    @Override // gnu.trove.map.TLongByteMap
    public boolean forEachValue(TByteProcedure procedure) {
        return this.m.forEachValue(procedure);
    }

    @Override // gnu.trove.map.TLongByteMap
    public boolean forEachEntry(TLongByteProcedure procedure) {
        return this.m.forEachEntry(procedure);
    }

    @Override // gnu.trove.map.TLongByteMap
    public TLongByteIterator iterator() {
        return new TLongByteIterator() { // from class: gnu.trove.impl.unmodifiable.TUnmodifiableLongByteMap.1
            TLongByteIterator iter;

            {
                this.iter = TUnmodifiableLongByteMap.this.m.iterator();
            }

            @Override // gnu.trove.iterator.TLongByteIterator
            public long key() {
                return this.iter.key();
            }

            @Override // gnu.trove.iterator.TLongByteIterator
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

            @Override // gnu.trove.iterator.TLongByteIterator
            public byte setValue(byte val) {
                throw new UnsupportedOperationException();
            }

            @Override // gnu.trove.iterator.TIterator, java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override // gnu.trove.map.TLongByteMap
    public byte putIfAbsent(long key, byte value) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TLongByteMap
    public void transformValues(TByteFunction function) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TLongByteMap
    public boolean retainEntries(TLongByteProcedure procedure) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TLongByteMap
    public boolean increment(long key) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TLongByteMap
    public boolean adjustValue(long key, byte amount) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TLongByteMap
    public byte adjustOrPutValue(long key, byte adjust_amount, byte put_amount) {
        throw new UnsupportedOperationException();
    }
}
