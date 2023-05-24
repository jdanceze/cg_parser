package gnu.trove.impl.unmodifiable;

import gnu.trove.TCollections;
import gnu.trove.TLongCollection;
import gnu.trove.function.TLongFunction;
import gnu.trove.iterator.TShortLongIterator;
import gnu.trove.map.TShortLongMap;
import gnu.trove.procedure.TLongProcedure;
import gnu.trove.procedure.TShortLongProcedure;
import gnu.trove.procedure.TShortProcedure;
import gnu.trove.set.TShortSet;
import java.io.Serializable;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/impl/unmodifiable/TUnmodifiableShortLongMap.class */
public class TUnmodifiableShortLongMap implements TShortLongMap, Serializable {
    private static final long serialVersionUID = -1034234728574286014L;
    private final TShortLongMap m;
    private transient TShortSet keySet = null;
    private transient TLongCollection values = null;

    public TUnmodifiableShortLongMap(TShortLongMap m) {
        if (m == null) {
            throw new NullPointerException();
        }
        this.m = m;
    }

    @Override // gnu.trove.map.TShortLongMap
    public int size() {
        return this.m.size();
    }

    @Override // gnu.trove.map.TShortLongMap
    public boolean isEmpty() {
        return this.m.isEmpty();
    }

    @Override // gnu.trove.map.TShortLongMap
    public boolean containsKey(short key) {
        return this.m.containsKey(key);
    }

    @Override // gnu.trove.map.TShortLongMap
    public boolean containsValue(long val) {
        return this.m.containsValue(val);
    }

    @Override // gnu.trove.map.TShortLongMap
    public long get(short key) {
        return this.m.get(key);
    }

    @Override // gnu.trove.map.TShortLongMap
    public long put(short key, long value) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TShortLongMap
    public long remove(short key) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TShortLongMap
    public void putAll(TShortLongMap m) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TShortLongMap
    public void putAll(Map<? extends Short, ? extends Long> map) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TShortLongMap
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TShortLongMap
    public TShortSet keySet() {
        if (this.keySet == null) {
            this.keySet = TCollections.unmodifiableSet(this.m.keySet());
        }
        return this.keySet;
    }

    @Override // gnu.trove.map.TShortLongMap
    public short[] keys() {
        return this.m.keys();
    }

    @Override // gnu.trove.map.TShortLongMap
    public short[] keys(short[] array) {
        return this.m.keys(array);
    }

    @Override // gnu.trove.map.TShortLongMap
    public TLongCollection valueCollection() {
        if (this.values == null) {
            this.values = TCollections.unmodifiableCollection(this.m.valueCollection());
        }
        return this.values;
    }

    @Override // gnu.trove.map.TShortLongMap
    public long[] values() {
        return this.m.values();
    }

    @Override // gnu.trove.map.TShortLongMap
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

    @Override // gnu.trove.map.TShortLongMap
    public short getNoEntryKey() {
        return this.m.getNoEntryKey();
    }

    @Override // gnu.trove.map.TShortLongMap
    public long getNoEntryValue() {
        return this.m.getNoEntryValue();
    }

    @Override // gnu.trove.map.TShortLongMap
    public boolean forEachKey(TShortProcedure procedure) {
        return this.m.forEachKey(procedure);
    }

    @Override // gnu.trove.map.TShortLongMap
    public boolean forEachValue(TLongProcedure procedure) {
        return this.m.forEachValue(procedure);
    }

    @Override // gnu.trove.map.TShortLongMap
    public boolean forEachEntry(TShortLongProcedure procedure) {
        return this.m.forEachEntry(procedure);
    }

    @Override // gnu.trove.map.TShortLongMap
    public TShortLongIterator iterator() {
        return new TShortLongIterator() { // from class: gnu.trove.impl.unmodifiable.TUnmodifiableShortLongMap.1
            TShortLongIterator iter;

            {
                this.iter = TUnmodifiableShortLongMap.this.m.iterator();
            }

            @Override // gnu.trove.iterator.TShortLongIterator
            public short key() {
                return this.iter.key();
            }

            @Override // gnu.trove.iterator.TShortLongIterator
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

            @Override // gnu.trove.iterator.TShortLongIterator
            public long setValue(long val) {
                throw new UnsupportedOperationException();
            }

            @Override // gnu.trove.iterator.TIterator, java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override // gnu.trove.map.TShortLongMap
    public long putIfAbsent(short key, long value) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TShortLongMap
    public void transformValues(TLongFunction function) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TShortLongMap
    public boolean retainEntries(TShortLongProcedure procedure) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TShortLongMap
    public boolean increment(short key) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TShortLongMap
    public boolean adjustValue(short key, long amount) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TShortLongMap
    public long adjustOrPutValue(short key, long adjust_amount, long put_amount) {
        throw new UnsupportedOperationException();
    }
}
