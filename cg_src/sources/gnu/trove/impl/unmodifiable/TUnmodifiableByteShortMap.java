package gnu.trove.impl.unmodifiable;

import gnu.trove.TCollections;
import gnu.trove.TShortCollection;
import gnu.trove.function.TShortFunction;
import gnu.trove.iterator.TByteShortIterator;
import gnu.trove.map.TByteShortMap;
import gnu.trove.procedure.TByteProcedure;
import gnu.trove.procedure.TByteShortProcedure;
import gnu.trove.procedure.TShortProcedure;
import gnu.trove.set.TByteSet;
import java.io.Serializable;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/impl/unmodifiable/TUnmodifiableByteShortMap.class */
public class TUnmodifiableByteShortMap implements TByteShortMap, Serializable {
    private static final long serialVersionUID = -1034234728574286014L;
    private final TByteShortMap m;
    private transient TByteSet keySet = null;
    private transient TShortCollection values = null;

    public TUnmodifiableByteShortMap(TByteShortMap m) {
        if (m == null) {
            throw new NullPointerException();
        }
        this.m = m;
    }

    @Override // gnu.trove.map.TByteShortMap
    public int size() {
        return this.m.size();
    }

    @Override // gnu.trove.map.TByteShortMap
    public boolean isEmpty() {
        return this.m.isEmpty();
    }

    @Override // gnu.trove.map.TByteShortMap
    public boolean containsKey(byte key) {
        return this.m.containsKey(key);
    }

    @Override // gnu.trove.map.TByteShortMap
    public boolean containsValue(short val) {
        return this.m.containsValue(val);
    }

    @Override // gnu.trove.map.TByteShortMap
    public short get(byte key) {
        return this.m.get(key);
    }

    @Override // gnu.trove.map.TByteShortMap
    public short put(byte key, short value) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TByteShortMap
    public short remove(byte key) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TByteShortMap
    public void putAll(TByteShortMap m) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TByteShortMap
    public void putAll(Map<? extends Byte, ? extends Short> map) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TByteShortMap
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TByteShortMap
    public TByteSet keySet() {
        if (this.keySet == null) {
            this.keySet = TCollections.unmodifiableSet(this.m.keySet());
        }
        return this.keySet;
    }

    @Override // gnu.trove.map.TByteShortMap
    public byte[] keys() {
        return this.m.keys();
    }

    @Override // gnu.trove.map.TByteShortMap
    public byte[] keys(byte[] array) {
        return this.m.keys(array);
    }

    @Override // gnu.trove.map.TByteShortMap
    public TShortCollection valueCollection() {
        if (this.values == null) {
            this.values = TCollections.unmodifiableCollection(this.m.valueCollection());
        }
        return this.values;
    }

    @Override // gnu.trove.map.TByteShortMap
    public short[] values() {
        return this.m.values();
    }

    @Override // gnu.trove.map.TByteShortMap
    public short[] values(short[] array) {
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

    @Override // gnu.trove.map.TByteShortMap
    public byte getNoEntryKey() {
        return this.m.getNoEntryKey();
    }

    @Override // gnu.trove.map.TByteShortMap
    public short getNoEntryValue() {
        return this.m.getNoEntryValue();
    }

    @Override // gnu.trove.map.TByteShortMap
    public boolean forEachKey(TByteProcedure procedure) {
        return this.m.forEachKey(procedure);
    }

    @Override // gnu.trove.map.TByteShortMap
    public boolean forEachValue(TShortProcedure procedure) {
        return this.m.forEachValue(procedure);
    }

    @Override // gnu.trove.map.TByteShortMap
    public boolean forEachEntry(TByteShortProcedure procedure) {
        return this.m.forEachEntry(procedure);
    }

    @Override // gnu.trove.map.TByteShortMap
    public TByteShortIterator iterator() {
        return new TByteShortIterator() { // from class: gnu.trove.impl.unmodifiable.TUnmodifiableByteShortMap.1
            TByteShortIterator iter;

            {
                this.iter = TUnmodifiableByteShortMap.this.m.iterator();
            }

            @Override // gnu.trove.iterator.TByteShortIterator
            public byte key() {
                return this.iter.key();
            }

            @Override // gnu.trove.iterator.TByteShortIterator
            public short value() {
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

            @Override // gnu.trove.iterator.TByteShortIterator
            public short setValue(short val) {
                throw new UnsupportedOperationException();
            }

            @Override // gnu.trove.iterator.TIterator, java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override // gnu.trove.map.TByteShortMap
    public short putIfAbsent(byte key, short value) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TByteShortMap
    public void transformValues(TShortFunction function) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TByteShortMap
    public boolean retainEntries(TByteShortProcedure procedure) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TByteShortMap
    public boolean increment(byte key) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TByteShortMap
    public boolean adjustValue(byte key, short amount) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TByteShortMap
    public short adjustOrPutValue(byte key, short adjust_amount, short put_amount) {
        throw new UnsupportedOperationException();
    }
}
