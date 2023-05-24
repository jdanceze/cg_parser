package gnu.trove.impl.unmodifiable;

import gnu.trove.TCollections;
import gnu.trove.TIntCollection;
import gnu.trove.function.TIntFunction;
import gnu.trove.iterator.TLongIntIterator;
import gnu.trove.map.TLongIntMap;
import gnu.trove.procedure.TIntProcedure;
import gnu.trove.procedure.TLongIntProcedure;
import gnu.trove.procedure.TLongProcedure;
import gnu.trove.set.TLongSet;
import java.io.Serializable;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/impl/unmodifiable/TUnmodifiableLongIntMap.class */
public class TUnmodifiableLongIntMap implements TLongIntMap, Serializable {
    private static final long serialVersionUID = -1034234728574286014L;
    private final TLongIntMap m;
    private transient TLongSet keySet = null;
    private transient TIntCollection values = null;

    public TUnmodifiableLongIntMap(TLongIntMap m) {
        if (m == null) {
            throw new NullPointerException();
        }
        this.m = m;
    }

    @Override // gnu.trove.map.TLongIntMap
    public int size() {
        return this.m.size();
    }

    @Override // gnu.trove.map.TLongIntMap
    public boolean isEmpty() {
        return this.m.isEmpty();
    }

    @Override // gnu.trove.map.TLongIntMap
    public boolean containsKey(long key) {
        return this.m.containsKey(key);
    }

    @Override // gnu.trove.map.TLongIntMap
    public boolean containsValue(int val) {
        return this.m.containsValue(val);
    }

    @Override // gnu.trove.map.TLongIntMap
    public int get(long key) {
        return this.m.get(key);
    }

    @Override // gnu.trove.map.TLongIntMap
    public int put(long key, int value) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TLongIntMap
    public int remove(long key) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TLongIntMap
    public void putAll(TLongIntMap m) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TLongIntMap
    public void putAll(Map<? extends Long, ? extends Integer> map) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TLongIntMap
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TLongIntMap
    public TLongSet keySet() {
        if (this.keySet == null) {
            this.keySet = TCollections.unmodifiableSet(this.m.keySet());
        }
        return this.keySet;
    }

    @Override // gnu.trove.map.TLongIntMap
    public long[] keys() {
        return this.m.keys();
    }

    @Override // gnu.trove.map.TLongIntMap
    public long[] keys(long[] array) {
        return this.m.keys(array);
    }

    @Override // gnu.trove.map.TLongIntMap
    public TIntCollection valueCollection() {
        if (this.values == null) {
            this.values = TCollections.unmodifiableCollection(this.m.valueCollection());
        }
        return this.values;
    }

    @Override // gnu.trove.map.TLongIntMap
    public int[] values() {
        return this.m.values();
    }

    @Override // gnu.trove.map.TLongIntMap
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

    @Override // gnu.trove.map.TLongIntMap
    public long getNoEntryKey() {
        return this.m.getNoEntryKey();
    }

    @Override // gnu.trove.map.TLongIntMap
    public int getNoEntryValue() {
        return this.m.getNoEntryValue();
    }

    @Override // gnu.trove.map.TLongIntMap
    public boolean forEachKey(TLongProcedure procedure) {
        return this.m.forEachKey(procedure);
    }

    @Override // gnu.trove.map.TLongIntMap
    public boolean forEachValue(TIntProcedure procedure) {
        return this.m.forEachValue(procedure);
    }

    @Override // gnu.trove.map.TLongIntMap
    public boolean forEachEntry(TLongIntProcedure procedure) {
        return this.m.forEachEntry(procedure);
    }

    @Override // gnu.trove.map.TLongIntMap
    public TLongIntIterator iterator() {
        return new TLongIntIterator() { // from class: gnu.trove.impl.unmodifiable.TUnmodifiableLongIntMap.1
            TLongIntIterator iter;

            {
                this.iter = TUnmodifiableLongIntMap.this.m.iterator();
            }

            @Override // gnu.trove.iterator.TLongIntIterator
            public long key() {
                return this.iter.key();
            }

            @Override // gnu.trove.iterator.TLongIntIterator
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

            @Override // gnu.trove.iterator.TLongIntIterator
            public int setValue(int val) {
                throw new UnsupportedOperationException();
            }

            @Override // gnu.trove.iterator.TIterator, java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override // gnu.trove.map.TLongIntMap
    public int putIfAbsent(long key, int value) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TLongIntMap
    public void transformValues(TIntFunction function) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TLongIntMap
    public boolean retainEntries(TLongIntProcedure procedure) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TLongIntMap
    public boolean increment(long key) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TLongIntMap
    public boolean adjustValue(long key, int amount) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TLongIntMap
    public int adjustOrPutValue(long key, int adjust_amount, int put_amount) {
        throw new UnsupportedOperationException();
    }
}
