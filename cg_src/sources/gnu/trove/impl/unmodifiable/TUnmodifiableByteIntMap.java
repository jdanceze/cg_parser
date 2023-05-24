package gnu.trove.impl.unmodifiable;

import gnu.trove.TCollections;
import gnu.trove.TIntCollection;
import gnu.trove.function.TIntFunction;
import gnu.trove.iterator.TByteIntIterator;
import gnu.trove.map.TByteIntMap;
import gnu.trove.procedure.TByteIntProcedure;
import gnu.trove.procedure.TByteProcedure;
import gnu.trove.procedure.TIntProcedure;
import gnu.trove.set.TByteSet;
import java.io.Serializable;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/impl/unmodifiable/TUnmodifiableByteIntMap.class */
public class TUnmodifiableByteIntMap implements TByteIntMap, Serializable {
    private static final long serialVersionUID = -1034234728574286014L;
    private final TByteIntMap m;
    private transient TByteSet keySet = null;
    private transient TIntCollection values = null;

    public TUnmodifiableByteIntMap(TByteIntMap m) {
        if (m == null) {
            throw new NullPointerException();
        }
        this.m = m;
    }

    @Override // gnu.trove.map.TByteIntMap
    public int size() {
        return this.m.size();
    }

    @Override // gnu.trove.map.TByteIntMap
    public boolean isEmpty() {
        return this.m.isEmpty();
    }

    @Override // gnu.trove.map.TByteIntMap
    public boolean containsKey(byte key) {
        return this.m.containsKey(key);
    }

    @Override // gnu.trove.map.TByteIntMap
    public boolean containsValue(int val) {
        return this.m.containsValue(val);
    }

    @Override // gnu.trove.map.TByteIntMap
    public int get(byte key) {
        return this.m.get(key);
    }

    @Override // gnu.trove.map.TByteIntMap
    public int put(byte key, int value) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TByteIntMap
    public int remove(byte key) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TByteIntMap
    public void putAll(TByteIntMap m) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TByteIntMap
    public void putAll(Map<? extends Byte, ? extends Integer> map) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TByteIntMap
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TByteIntMap
    public TByteSet keySet() {
        if (this.keySet == null) {
            this.keySet = TCollections.unmodifiableSet(this.m.keySet());
        }
        return this.keySet;
    }

    @Override // gnu.trove.map.TByteIntMap
    public byte[] keys() {
        return this.m.keys();
    }

    @Override // gnu.trove.map.TByteIntMap
    public byte[] keys(byte[] array) {
        return this.m.keys(array);
    }

    @Override // gnu.trove.map.TByteIntMap
    public TIntCollection valueCollection() {
        if (this.values == null) {
            this.values = TCollections.unmodifiableCollection(this.m.valueCollection());
        }
        return this.values;
    }

    @Override // gnu.trove.map.TByteIntMap
    public int[] values() {
        return this.m.values();
    }

    @Override // gnu.trove.map.TByteIntMap
    public int[] values(int[] array) {
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

    @Override // gnu.trove.map.TByteIntMap
    public byte getNoEntryKey() {
        return this.m.getNoEntryKey();
    }

    @Override // gnu.trove.map.TByteIntMap
    public int getNoEntryValue() {
        return this.m.getNoEntryValue();
    }

    @Override // gnu.trove.map.TByteIntMap
    public boolean forEachKey(TByteProcedure procedure) {
        return this.m.forEachKey(procedure);
    }

    @Override // gnu.trove.map.TByteIntMap
    public boolean forEachValue(TIntProcedure procedure) {
        return this.m.forEachValue(procedure);
    }

    @Override // gnu.trove.map.TByteIntMap
    public boolean forEachEntry(TByteIntProcedure procedure) {
        return this.m.forEachEntry(procedure);
    }

    @Override // gnu.trove.map.TByteIntMap
    public TByteIntIterator iterator() {
        return new TByteIntIterator() { // from class: gnu.trove.impl.unmodifiable.TUnmodifiableByteIntMap.1
            TByteIntIterator iter;

            {
                this.iter = TUnmodifiableByteIntMap.this.m.iterator();
            }

            @Override // gnu.trove.iterator.TByteIntIterator
            public byte key() {
                return this.iter.key();
            }

            @Override // gnu.trove.iterator.TByteIntIterator
            public int value() {
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

            @Override // gnu.trove.iterator.TByteIntIterator
            public int setValue(int val) {
                throw new UnsupportedOperationException();
            }

            @Override // gnu.trove.iterator.TIterator, java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override // gnu.trove.map.TByteIntMap
    public int putIfAbsent(byte key, int value) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TByteIntMap
    public void transformValues(TIntFunction function) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TByteIntMap
    public boolean retainEntries(TByteIntProcedure procedure) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TByteIntMap
    public boolean increment(byte key) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TByteIntMap
    public boolean adjustValue(byte key, int amount) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TByteIntMap
    public int adjustOrPutValue(byte key, int adjust_amount, int put_amount) {
        throw new UnsupportedOperationException();
    }
}
