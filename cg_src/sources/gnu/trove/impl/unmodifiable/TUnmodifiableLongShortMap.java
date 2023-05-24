package gnu.trove.impl.unmodifiable;

import gnu.trove.TCollections;
import gnu.trove.TShortCollection;
import gnu.trove.function.TShortFunction;
import gnu.trove.iterator.TLongShortIterator;
import gnu.trove.map.TLongShortMap;
import gnu.trove.procedure.TLongProcedure;
import gnu.trove.procedure.TLongShortProcedure;
import gnu.trove.procedure.TShortProcedure;
import gnu.trove.set.TLongSet;
import java.io.Serializable;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/impl/unmodifiable/TUnmodifiableLongShortMap.class */
public class TUnmodifiableLongShortMap implements TLongShortMap, Serializable {
    private static final long serialVersionUID = -1034234728574286014L;
    private final TLongShortMap m;
    private transient TLongSet keySet = null;
    private transient TShortCollection values = null;

    public TUnmodifiableLongShortMap(TLongShortMap m) {
        if (m == null) {
            throw new NullPointerException();
        }
        this.m = m;
    }

    @Override // gnu.trove.map.TLongShortMap
    public int size() {
        return this.m.size();
    }

    @Override // gnu.trove.map.TLongShortMap
    public boolean isEmpty() {
        return this.m.isEmpty();
    }

    @Override // gnu.trove.map.TLongShortMap
    public boolean containsKey(long key) {
        return this.m.containsKey(key);
    }

    @Override // gnu.trove.map.TLongShortMap
    public boolean containsValue(short val) {
        return this.m.containsValue(val);
    }

    @Override // gnu.trove.map.TLongShortMap
    public short get(long key) {
        return this.m.get(key);
    }

    @Override // gnu.trove.map.TLongShortMap
    public short put(long key, short value) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TLongShortMap
    public short remove(long key) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TLongShortMap
    public void putAll(TLongShortMap m) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TLongShortMap
    public void putAll(Map<? extends Long, ? extends Short> map) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TLongShortMap
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TLongShortMap
    public TLongSet keySet() {
        if (this.keySet == null) {
            this.keySet = TCollections.unmodifiableSet(this.m.keySet());
        }
        return this.keySet;
    }

    @Override // gnu.trove.map.TLongShortMap
    public long[] keys() {
        return this.m.keys();
    }

    @Override // gnu.trove.map.TLongShortMap
    public long[] keys(long[] array) {
        return this.m.keys(array);
    }

    @Override // gnu.trove.map.TLongShortMap
    public TShortCollection valueCollection() {
        if (this.values == null) {
            this.values = TCollections.unmodifiableCollection(this.m.valueCollection());
        }
        return this.values;
    }

    @Override // gnu.trove.map.TLongShortMap
    public short[] values() {
        return this.m.values();
    }

    @Override // gnu.trove.map.TLongShortMap
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

    @Override // gnu.trove.map.TLongShortMap
    public long getNoEntryKey() {
        return this.m.getNoEntryKey();
    }

    @Override // gnu.trove.map.TLongShortMap
    public short getNoEntryValue() {
        return this.m.getNoEntryValue();
    }

    @Override // gnu.trove.map.TLongShortMap
    public boolean forEachKey(TLongProcedure procedure) {
        return this.m.forEachKey(procedure);
    }

    @Override // gnu.trove.map.TLongShortMap
    public boolean forEachValue(TShortProcedure procedure) {
        return this.m.forEachValue(procedure);
    }

    @Override // gnu.trove.map.TLongShortMap
    public boolean forEachEntry(TLongShortProcedure procedure) {
        return this.m.forEachEntry(procedure);
    }

    @Override // gnu.trove.map.TLongShortMap
    public TLongShortIterator iterator() {
        return new TLongShortIterator() { // from class: gnu.trove.impl.unmodifiable.TUnmodifiableLongShortMap.1
            TLongShortIterator iter;

            {
                this.iter = TUnmodifiableLongShortMap.this.m.iterator();
            }

            @Override // gnu.trove.iterator.TLongShortIterator
            public long key() {
                return this.iter.key();
            }

            @Override // gnu.trove.iterator.TLongShortIterator
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

            @Override // gnu.trove.iterator.TLongShortIterator
            public short setValue(short val) {
                throw new UnsupportedOperationException();
            }

            @Override // gnu.trove.iterator.TIterator, java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override // gnu.trove.map.TLongShortMap
    public short putIfAbsent(long key, short value) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TLongShortMap
    public void transformValues(TShortFunction function) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TLongShortMap
    public boolean retainEntries(TLongShortProcedure procedure) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TLongShortMap
    public boolean increment(long key) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TLongShortMap
    public boolean adjustValue(long key, short amount) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TLongShortMap
    public short adjustOrPutValue(long key, short adjust_amount, short put_amount) {
        throw new UnsupportedOperationException();
    }
}
