package gnu.trove.impl.unmodifiable;

import gnu.trove.TByteCollection;
import gnu.trove.TCollections;
import gnu.trove.function.TByteFunction;
import gnu.trove.iterator.TIntByteIterator;
import gnu.trove.map.TIntByteMap;
import gnu.trove.procedure.TByteProcedure;
import gnu.trove.procedure.TIntByteProcedure;
import gnu.trove.procedure.TIntProcedure;
import gnu.trove.set.TIntSet;
import java.io.Serializable;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/impl/unmodifiable/TUnmodifiableIntByteMap.class */
public class TUnmodifiableIntByteMap implements TIntByteMap, Serializable {
    private static final long serialVersionUID = -1034234728574286014L;
    private final TIntByteMap m;
    private transient TIntSet keySet = null;
    private transient TByteCollection values = null;

    public TUnmodifiableIntByteMap(TIntByteMap m) {
        if (m == null) {
            throw new NullPointerException();
        }
        this.m = m;
    }

    @Override // gnu.trove.map.TIntByteMap
    public int size() {
        return this.m.size();
    }

    @Override // gnu.trove.map.TIntByteMap
    public boolean isEmpty() {
        return this.m.isEmpty();
    }

    @Override // gnu.trove.map.TIntByteMap
    public boolean containsKey(int key) {
        return this.m.containsKey(key);
    }

    @Override // gnu.trove.map.TIntByteMap
    public boolean containsValue(byte val) {
        return this.m.containsValue(val);
    }

    @Override // gnu.trove.map.TIntByteMap
    public byte get(int key) {
        return this.m.get(key);
    }

    @Override // gnu.trove.map.TIntByteMap
    public byte put(int key, byte value) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TIntByteMap
    public byte remove(int key) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TIntByteMap
    public void putAll(TIntByteMap m) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TIntByteMap
    public void putAll(Map<? extends Integer, ? extends Byte> map) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TIntByteMap
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TIntByteMap
    public TIntSet keySet() {
        if (this.keySet == null) {
            this.keySet = TCollections.unmodifiableSet(this.m.keySet());
        }
        return this.keySet;
    }

    @Override // gnu.trove.map.TIntByteMap
    public int[] keys() {
        return this.m.keys();
    }

    @Override // gnu.trove.map.TIntByteMap
    public int[] keys(int[] array) {
        return this.m.keys(array);
    }

    @Override // gnu.trove.map.TIntByteMap
    public TByteCollection valueCollection() {
        if (this.values == null) {
            this.values = TCollections.unmodifiableCollection(this.m.valueCollection());
        }
        return this.values;
    }

    @Override // gnu.trove.map.TIntByteMap
    public byte[] values() {
        return this.m.values();
    }

    @Override // gnu.trove.map.TIntByteMap
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

    @Override // gnu.trove.map.TIntByteMap
    public int getNoEntryKey() {
        return this.m.getNoEntryKey();
    }

    @Override // gnu.trove.map.TIntByteMap
    public byte getNoEntryValue() {
        return this.m.getNoEntryValue();
    }

    @Override // gnu.trove.map.TIntByteMap
    public boolean forEachKey(TIntProcedure procedure) {
        return this.m.forEachKey(procedure);
    }

    @Override // gnu.trove.map.TIntByteMap
    public boolean forEachValue(TByteProcedure procedure) {
        return this.m.forEachValue(procedure);
    }

    @Override // gnu.trove.map.TIntByteMap
    public boolean forEachEntry(TIntByteProcedure procedure) {
        return this.m.forEachEntry(procedure);
    }

    @Override // gnu.trove.map.TIntByteMap
    public TIntByteIterator iterator() {
        return new TIntByteIterator() { // from class: gnu.trove.impl.unmodifiable.TUnmodifiableIntByteMap.1
            TIntByteIterator iter;

            {
                this.iter = TUnmodifiableIntByteMap.this.m.iterator();
            }

            @Override // gnu.trove.iterator.TIntByteIterator
            public int key() {
                return this.iter.key();
            }

            @Override // gnu.trove.iterator.TIntByteIterator
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

            @Override // gnu.trove.iterator.TIntByteIterator
            public byte setValue(byte val) {
                throw new UnsupportedOperationException();
            }

            @Override // gnu.trove.iterator.TIterator, java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override // gnu.trove.map.TIntByteMap
    public byte putIfAbsent(int key, byte value) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TIntByteMap
    public void transformValues(TByteFunction function) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TIntByteMap
    public boolean retainEntries(TIntByteProcedure procedure) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TIntByteMap
    public boolean increment(int key) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TIntByteMap
    public boolean adjustValue(int key, byte amount) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TIntByteMap
    public byte adjustOrPutValue(int key, byte adjust_amount, byte put_amount) {
        throw new UnsupportedOperationException();
    }
}
