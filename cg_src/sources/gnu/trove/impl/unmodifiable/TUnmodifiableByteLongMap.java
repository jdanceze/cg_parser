package gnu.trove.impl.unmodifiable;

import gnu.trove.TCollections;
import gnu.trove.TLongCollection;
import gnu.trove.function.TLongFunction;
import gnu.trove.iterator.TByteLongIterator;
import gnu.trove.map.TByteLongMap;
import gnu.trove.procedure.TByteLongProcedure;
import gnu.trove.procedure.TByteProcedure;
import gnu.trove.procedure.TLongProcedure;
import gnu.trove.set.TByteSet;
import java.io.Serializable;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/impl/unmodifiable/TUnmodifiableByteLongMap.class */
public class TUnmodifiableByteLongMap implements TByteLongMap, Serializable {
    private static final long serialVersionUID = -1034234728574286014L;
    private final TByteLongMap m;
    private transient TByteSet keySet = null;
    private transient TLongCollection values = null;

    public TUnmodifiableByteLongMap(TByteLongMap m) {
        if (m == null) {
            throw new NullPointerException();
        }
        this.m = m;
    }

    @Override // gnu.trove.map.TByteLongMap
    public int size() {
        return this.m.size();
    }

    @Override // gnu.trove.map.TByteLongMap
    public boolean isEmpty() {
        return this.m.isEmpty();
    }

    @Override // gnu.trove.map.TByteLongMap
    public boolean containsKey(byte key) {
        return this.m.containsKey(key);
    }

    @Override // gnu.trove.map.TByteLongMap
    public boolean containsValue(long val) {
        return this.m.containsValue(val);
    }

    @Override // gnu.trove.map.TByteLongMap
    public long get(byte key) {
        return this.m.get(key);
    }

    @Override // gnu.trove.map.TByteLongMap
    public long put(byte key, long value) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TByteLongMap
    public long remove(byte key) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TByteLongMap
    public void putAll(TByteLongMap m) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TByteLongMap
    public void putAll(Map<? extends Byte, ? extends Long> map) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TByteLongMap
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TByteLongMap
    public TByteSet keySet() {
        if (this.keySet == null) {
            this.keySet = TCollections.unmodifiableSet(this.m.keySet());
        }
        return this.keySet;
    }

    @Override // gnu.trove.map.TByteLongMap
    public byte[] keys() {
        return this.m.keys();
    }

    @Override // gnu.trove.map.TByteLongMap
    public byte[] keys(byte[] array) {
        return this.m.keys(array);
    }

    @Override // gnu.trove.map.TByteLongMap
    public TLongCollection valueCollection() {
        if (this.values == null) {
            this.values = TCollections.unmodifiableCollection(this.m.valueCollection());
        }
        return this.values;
    }

    @Override // gnu.trove.map.TByteLongMap
    public long[] values() {
        return this.m.values();
    }

    @Override // gnu.trove.map.TByteLongMap
    public long[] values(long[] array) {
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

    @Override // gnu.trove.map.TByteLongMap
    public byte getNoEntryKey() {
        return this.m.getNoEntryKey();
    }

    @Override // gnu.trove.map.TByteLongMap
    public long getNoEntryValue() {
        return this.m.getNoEntryValue();
    }

    @Override // gnu.trove.map.TByteLongMap
    public boolean forEachKey(TByteProcedure procedure) {
        return this.m.forEachKey(procedure);
    }

    @Override // gnu.trove.map.TByteLongMap
    public boolean forEachValue(TLongProcedure procedure) {
        return this.m.forEachValue(procedure);
    }

    @Override // gnu.trove.map.TByteLongMap
    public boolean forEachEntry(TByteLongProcedure procedure) {
        return this.m.forEachEntry(procedure);
    }

    @Override // gnu.trove.map.TByteLongMap
    public TByteLongIterator iterator() {
        return new TByteLongIterator() { // from class: gnu.trove.impl.unmodifiable.TUnmodifiableByteLongMap.1
            TByteLongIterator iter;

            {
                this.iter = TUnmodifiableByteLongMap.this.m.iterator();
            }

            @Override // gnu.trove.iterator.TByteLongIterator
            public byte key() {
                return this.iter.key();
            }

            @Override // gnu.trove.iterator.TByteLongIterator
            public long value() {
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

            @Override // gnu.trove.iterator.TByteLongIterator
            public long setValue(long val) {
                throw new UnsupportedOperationException();
            }

            @Override // gnu.trove.iterator.TIterator, java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override // gnu.trove.map.TByteLongMap
    public long putIfAbsent(byte key, long value) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TByteLongMap
    public void transformValues(TLongFunction function) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TByteLongMap
    public boolean retainEntries(TByteLongProcedure procedure) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TByteLongMap
    public boolean increment(byte key) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TByteLongMap
    public boolean adjustValue(byte key, long amount) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TByteLongMap
    public long adjustOrPutValue(byte key, long adjust_amount, long put_amount) {
        throw new UnsupportedOperationException();
    }
}
