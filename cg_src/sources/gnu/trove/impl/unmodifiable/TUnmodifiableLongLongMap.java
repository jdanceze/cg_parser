package gnu.trove.impl.unmodifiable;

import gnu.trove.TCollections;
import gnu.trove.TLongCollection;
import gnu.trove.function.TLongFunction;
import gnu.trove.iterator.TLongLongIterator;
import gnu.trove.map.TLongLongMap;
import gnu.trove.procedure.TLongLongProcedure;
import gnu.trove.procedure.TLongProcedure;
import gnu.trove.set.TLongSet;
import java.io.Serializable;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/impl/unmodifiable/TUnmodifiableLongLongMap.class */
public class TUnmodifiableLongLongMap implements TLongLongMap, Serializable {
    private static final long serialVersionUID = -1034234728574286014L;
    private final TLongLongMap m;
    private transient TLongSet keySet = null;
    private transient TLongCollection values = null;

    public TUnmodifiableLongLongMap(TLongLongMap m) {
        if (m == null) {
            throw new NullPointerException();
        }
        this.m = m;
    }

    @Override // gnu.trove.map.TLongLongMap
    public int size() {
        return this.m.size();
    }

    @Override // gnu.trove.map.TLongLongMap
    public boolean isEmpty() {
        return this.m.isEmpty();
    }

    @Override // gnu.trove.map.TLongLongMap
    public boolean containsKey(long key) {
        return this.m.containsKey(key);
    }

    @Override // gnu.trove.map.TLongLongMap
    public boolean containsValue(long val) {
        return this.m.containsValue(val);
    }

    @Override // gnu.trove.map.TLongLongMap
    public long get(long key) {
        return this.m.get(key);
    }

    @Override // gnu.trove.map.TLongLongMap
    public long put(long key, long value) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TLongLongMap
    public long remove(long key) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TLongLongMap
    public void putAll(TLongLongMap m) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TLongLongMap
    public void putAll(Map<? extends Long, ? extends Long> map) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TLongLongMap
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TLongLongMap
    public TLongSet keySet() {
        if (this.keySet == null) {
            this.keySet = TCollections.unmodifiableSet(this.m.keySet());
        }
        return this.keySet;
    }

    @Override // gnu.trove.map.TLongLongMap
    public long[] keys() {
        return this.m.keys();
    }

    @Override // gnu.trove.map.TLongLongMap
    public long[] keys(long[] array) {
        return this.m.keys(array);
    }

    @Override // gnu.trove.map.TLongLongMap
    public TLongCollection valueCollection() {
        if (this.values == null) {
            this.values = TCollections.unmodifiableCollection(this.m.valueCollection());
        }
        return this.values;
    }

    @Override // gnu.trove.map.TLongLongMap
    public long[] values() {
        return this.m.values();
    }

    @Override // gnu.trove.map.TLongLongMap
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

    @Override // gnu.trove.map.TLongLongMap
    public long getNoEntryKey() {
        return this.m.getNoEntryKey();
    }

    @Override // gnu.trove.map.TLongLongMap
    public long getNoEntryValue() {
        return this.m.getNoEntryValue();
    }

    @Override // gnu.trove.map.TLongLongMap
    public boolean forEachKey(TLongProcedure procedure) {
        return this.m.forEachKey(procedure);
    }

    @Override // gnu.trove.map.TLongLongMap
    public boolean forEachValue(TLongProcedure procedure) {
        return this.m.forEachValue(procedure);
    }

    @Override // gnu.trove.map.TLongLongMap
    public boolean forEachEntry(TLongLongProcedure procedure) {
        return this.m.forEachEntry(procedure);
    }

    @Override // gnu.trove.map.TLongLongMap
    public TLongLongIterator iterator() {
        return new TLongLongIterator() { // from class: gnu.trove.impl.unmodifiable.TUnmodifiableLongLongMap.1
            TLongLongIterator iter;

            {
                this.iter = TUnmodifiableLongLongMap.this.m.iterator();
            }

            @Override // gnu.trove.iterator.TLongLongIterator
            public long key() {
                return this.iter.key();
            }

            @Override // gnu.trove.iterator.TLongLongIterator
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

            @Override // gnu.trove.iterator.TLongLongIterator
            public long setValue(long val) {
                throw new UnsupportedOperationException();
            }

            @Override // gnu.trove.iterator.TIterator, java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override // gnu.trove.map.TLongLongMap
    public long putIfAbsent(long key, long value) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TLongLongMap
    public void transformValues(TLongFunction function) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TLongLongMap
    public boolean retainEntries(TLongLongProcedure procedure) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TLongLongMap
    public boolean increment(long key) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TLongLongMap
    public boolean adjustValue(long key, long amount) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TLongLongMap
    public long adjustOrPutValue(long key, long adjust_amount, long put_amount) {
        throw new UnsupportedOperationException();
    }
}
