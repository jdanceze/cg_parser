package gnu.trove.impl.unmodifiable;

import gnu.trove.TCollections;
import gnu.trove.TLongCollection;
import gnu.trove.function.TLongFunction;
import gnu.trove.iterator.TIntLongIterator;
import gnu.trove.map.TIntLongMap;
import gnu.trove.procedure.TIntLongProcedure;
import gnu.trove.procedure.TIntProcedure;
import gnu.trove.procedure.TLongProcedure;
import gnu.trove.set.TIntSet;
import java.io.Serializable;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/impl/unmodifiable/TUnmodifiableIntLongMap.class */
public class TUnmodifiableIntLongMap implements TIntLongMap, Serializable {
    private static final long serialVersionUID = -1034234728574286014L;
    private final TIntLongMap m;
    private transient TIntSet keySet = null;
    private transient TLongCollection values = null;

    public TUnmodifiableIntLongMap(TIntLongMap m) {
        if (m == null) {
            throw new NullPointerException();
        }
        this.m = m;
    }

    @Override // gnu.trove.map.TIntLongMap
    public int size() {
        return this.m.size();
    }

    @Override // gnu.trove.map.TIntLongMap
    public boolean isEmpty() {
        return this.m.isEmpty();
    }

    @Override // gnu.trove.map.TIntLongMap
    public boolean containsKey(int key) {
        return this.m.containsKey(key);
    }

    @Override // gnu.trove.map.TIntLongMap
    public boolean containsValue(long val) {
        return this.m.containsValue(val);
    }

    @Override // gnu.trove.map.TIntLongMap
    public long get(int key) {
        return this.m.get(key);
    }

    @Override // gnu.trove.map.TIntLongMap
    public long put(int key, long value) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TIntLongMap
    public long remove(int key) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TIntLongMap
    public void putAll(TIntLongMap m) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TIntLongMap
    public void putAll(Map<? extends Integer, ? extends Long> map) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TIntLongMap
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TIntLongMap
    public TIntSet keySet() {
        if (this.keySet == null) {
            this.keySet = TCollections.unmodifiableSet(this.m.keySet());
        }
        return this.keySet;
    }

    @Override // gnu.trove.map.TIntLongMap
    public int[] keys() {
        return this.m.keys();
    }

    @Override // gnu.trove.map.TIntLongMap
    public int[] keys(int[] array) {
        return this.m.keys(array);
    }

    @Override // gnu.trove.map.TIntLongMap
    public TLongCollection valueCollection() {
        if (this.values == null) {
            this.values = TCollections.unmodifiableCollection(this.m.valueCollection());
        }
        return this.values;
    }

    @Override // gnu.trove.map.TIntLongMap
    public long[] values() {
        return this.m.values();
    }

    @Override // gnu.trove.map.TIntLongMap
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

    @Override // gnu.trove.map.TIntLongMap
    public int getNoEntryKey() {
        return this.m.getNoEntryKey();
    }

    @Override // gnu.trove.map.TIntLongMap
    public long getNoEntryValue() {
        return this.m.getNoEntryValue();
    }

    @Override // gnu.trove.map.TIntLongMap
    public boolean forEachKey(TIntProcedure procedure) {
        return this.m.forEachKey(procedure);
    }

    @Override // gnu.trove.map.TIntLongMap
    public boolean forEachValue(TLongProcedure procedure) {
        return this.m.forEachValue(procedure);
    }

    @Override // gnu.trove.map.TIntLongMap
    public boolean forEachEntry(TIntLongProcedure procedure) {
        return this.m.forEachEntry(procedure);
    }

    @Override // gnu.trove.map.TIntLongMap
    public TIntLongIterator iterator() {
        return new TIntLongIterator() { // from class: gnu.trove.impl.unmodifiable.TUnmodifiableIntLongMap.1
            TIntLongIterator iter;

            {
                this.iter = TUnmodifiableIntLongMap.this.m.iterator();
            }

            @Override // gnu.trove.iterator.TIntLongIterator
            public int key() {
                return this.iter.key();
            }

            @Override // gnu.trove.iterator.TIntLongIterator
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

            @Override // gnu.trove.iterator.TIntLongIterator
            public long setValue(long val) {
                throw new UnsupportedOperationException();
            }

            @Override // gnu.trove.iterator.TIterator, java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override // gnu.trove.map.TIntLongMap
    public long putIfAbsent(int key, long value) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TIntLongMap
    public void transformValues(TLongFunction function) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TIntLongMap
    public boolean retainEntries(TIntLongProcedure procedure) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TIntLongMap
    public boolean increment(int key) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TIntLongMap
    public boolean adjustValue(int key, long amount) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TIntLongMap
    public long adjustOrPutValue(int key, long adjust_amount, long put_amount) {
        throw new UnsupportedOperationException();
    }
}
