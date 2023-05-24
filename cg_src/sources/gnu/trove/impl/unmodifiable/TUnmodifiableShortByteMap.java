package gnu.trove.impl.unmodifiable;

import gnu.trove.TByteCollection;
import gnu.trove.TCollections;
import gnu.trove.function.TByteFunction;
import gnu.trove.iterator.TShortByteIterator;
import gnu.trove.map.TShortByteMap;
import gnu.trove.procedure.TByteProcedure;
import gnu.trove.procedure.TShortByteProcedure;
import gnu.trove.procedure.TShortProcedure;
import gnu.trove.set.TShortSet;
import java.io.Serializable;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/impl/unmodifiable/TUnmodifiableShortByteMap.class */
public class TUnmodifiableShortByteMap implements TShortByteMap, Serializable {
    private static final long serialVersionUID = -1034234728574286014L;
    private final TShortByteMap m;
    private transient TShortSet keySet = null;
    private transient TByteCollection values = null;

    public TUnmodifiableShortByteMap(TShortByteMap m) {
        if (m == null) {
            throw new NullPointerException();
        }
        this.m = m;
    }

    @Override // gnu.trove.map.TShortByteMap
    public int size() {
        return this.m.size();
    }

    @Override // gnu.trove.map.TShortByteMap
    public boolean isEmpty() {
        return this.m.isEmpty();
    }

    @Override // gnu.trove.map.TShortByteMap
    public boolean containsKey(short key) {
        return this.m.containsKey(key);
    }

    @Override // gnu.trove.map.TShortByteMap
    public boolean containsValue(byte val) {
        return this.m.containsValue(val);
    }

    @Override // gnu.trove.map.TShortByteMap
    public byte get(short key) {
        return this.m.get(key);
    }

    @Override // gnu.trove.map.TShortByteMap
    public byte put(short key, byte value) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TShortByteMap
    public byte remove(short key) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TShortByteMap
    public void putAll(TShortByteMap m) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TShortByteMap
    public void putAll(Map<? extends Short, ? extends Byte> map) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TShortByteMap
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TShortByteMap
    public TShortSet keySet() {
        if (this.keySet == null) {
            this.keySet = TCollections.unmodifiableSet(this.m.keySet());
        }
        return this.keySet;
    }

    @Override // gnu.trove.map.TShortByteMap
    public short[] keys() {
        return this.m.keys();
    }

    @Override // gnu.trove.map.TShortByteMap
    public short[] keys(short[] array) {
        return this.m.keys(array);
    }

    @Override // gnu.trove.map.TShortByteMap
    public TByteCollection valueCollection() {
        if (this.values == null) {
            this.values = TCollections.unmodifiableCollection(this.m.valueCollection());
        }
        return this.values;
    }

    @Override // gnu.trove.map.TShortByteMap
    public byte[] values() {
        return this.m.values();
    }

    @Override // gnu.trove.map.TShortByteMap
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

    @Override // gnu.trove.map.TShortByteMap
    public short getNoEntryKey() {
        return this.m.getNoEntryKey();
    }

    @Override // gnu.trove.map.TShortByteMap
    public byte getNoEntryValue() {
        return this.m.getNoEntryValue();
    }

    @Override // gnu.trove.map.TShortByteMap
    public boolean forEachKey(TShortProcedure procedure) {
        return this.m.forEachKey(procedure);
    }

    @Override // gnu.trove.map.TShortByteMap
    public boolean forEachValue(TByteProcedure procedure) {
        return this.m.forEachValue(procedure);
    }

    @Override // gnu.trove.map.TShortByteMap
    public boolean forEachEntry(TShortByteProcedure procedure) {
        return this.m.forEachEntry(procedure);
    }

    @Override // gnu.trove.map.TShortByteMap
    public TShortByteIterator iterator() {
        return new TShortByteIterator() { // from class: gnu.trove.impl.unmodifiable.TUnmodifiableShortByteMap.1
            TShortByteIterator iter;

            {
                this.iter = TUnmodifiableShortByteMap.this.m.iterator();
            }

            @Override // gnu.trove.iterator.TShortByteIterator
            public short key() {
                return this.iter.key();
            }

            @Override // gnu.trove.iterator.TShortByteIterator
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

            @Override // gnu.trove.iterator.TShortByteIterator
            public byte setValue(byte val) {
                throw new UnsupportedOperationException();
            }

            @Override // gnu.trove.iterator.TIterator, java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override // gnu.trove.map.TShortByteMap
    public byte putIfAbsent(short key, byte value) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TShortByteMap
    public void transformValues(TByteFunction function) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TShortByteMap
    public boolean retainEntries(TShortByteProcedure procedure) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TShortByteMap
    public boolean increment(short key) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TShortByteMap
    public boolean adjustValue(short key, byte amount) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TShortByteMap
    public byte adjustOrPutValue(short key, byte adjust_amount, byte put_amount) {
        throw new UnsupportedOperationException();
    }
}
