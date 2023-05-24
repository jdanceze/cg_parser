package gnu.trove.impl.unmodifiable;

import gnu.trove.TCollections;
import gnu.trove.TLongCollection;
import gnu.trove.function.TLongFunction;
import gnu.trove.iterator.TFloatLongIterator;
import gnu.trove.map.TFloatLongMap;
import gnu.trove.procedure.TFloatLongProcedure;
import gnu.trove.procedure.TFloatProcedure;
import gnu.trove.procedure.TLongProcedure;
import gnu.trove.set.TFloatSet;
import java.io.Serializable;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/impl/unmodifiable/TUnmodifiableFloatLongMap.class */
public class TUnmodifiableFloatLongMap implements TFloatLongMap, Serializable {
    private static final long serialVersionUID = -1034234728574286014L;
    private final TFloatLongMap m;
    private transient TFloatSet keySet = null;
    private transient TLongCollection values = null;

    public TUnmodifiableFloatLongMap(TFloatLongMap m) {
        if (m == null) {
            throw new NullPointerException();
        }
        this.m = m;
    }

    @Override // gnu.trove.map.TFloatLongMap
    public int size() {
        return this.m.size();
    }

    @Override // gnu.trove.map.TFloatLongMap
    public boolean isEmpty() {
        return this.m.isEmpty();
    }

    @Override // gnu.trove.map.TFloatLongMap
    public boolean containsKey(float key) {
        return this.m.containsKey(key);
    }

    @Override // gnu.trove.map.TFloatLongMap
    public boolean containsValue(long val) {
        return this.m.containsValue(val);
    }

    @Override // gnu.trove.map.TFloatLongMap
    public long get(float key) {
        return this.m.get(key);
    }

    @Override // gnu.trove.map.TFloatLongMap
    public long put(float key, long value) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TFloatLongMap
    public long remove(float key) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TFloatLongMap
    public void putAll(TFloatLongMap m) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TFloatLongMap
    public void putAll(Map<? extends Float, ? extends Long> map) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TFloatLongMap
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TFloatLongMap
    public TFloatSet keySet() {
        if (this.keySet == null) {
            this.keySet = TCollections.unmodifiableSet(this.m.keySet());
        }
        return this.keySet;
    }

    @Override // gnu.trove.map.TFloatLongMap
    public float[] keys() {
        return this.m.keys();
    }

    @Override // gnu.trove.map.TFloatLongMap
    public float[] keys(float[] array) {
        return this.m.keys(array);
    }

    @Override // gnu.trove.map.TFloatLongMap
    public TLongCollection valueCollection() {
        if (this.values == null) {
            this.values = TCollections.unmodifiableCollection(this.m.valueCollection());
        }
        return this.values;
    }

    @Override // gnu.trove.map.TFloatLongMap
    public long[] values() {
        return this.m.values();
    }

    @Override // gnu.trove.map.TFloatLongMap
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

    @Override // gnu.trove.map.TFloatLongMap
    public float getNoEntryKey() {
        return this.m.getNoEntryKey();
    }

    @Override // gnu.trove.map.TFloatLongMap
    public long getNoEntryValue() {
        return this.m.getNoEntryValue();
    }

    @Override // gnu.trove.map.TFloatLongMap
    public boolean forEachKey(TFloatProcedure procedure) {
        return this.m.forEachKey(procedure);
    }

    @Override // gnu.trove.map.TFloatLongMap
    public boolean forEachValue(TLongProcedure procedure) {
        return this.m.forEachValue(procedure);
    }

    @Override // gnu.trove.map.TFloatLongMap
    public boolean forEachEntry(TFloatLongProcedure procedure) {
        return this.m.forEachEntry(procedure);
    }

    @Override // gnu.trove.map.TFloatLongMap
    public TFloatLongIterator iterator() {
        return new TFloatLongIterator() { // from class: gnu.trove.impl.unmodifiable.TUnmodifiableFloatLongMap.1
            TFloatLongIterator iter;

            {
                this.iter = TUnmodifiableFloatLongMap.this.m.iterator();
            }

            @Override // gnu.trove.iterator.TFloatLongIterator
            public float key() {
                return this.iter.key();
            }

            @Override // gnu.trove.iterator.TFloatLongIterator
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

            @Override // gnu.trove.iterator.TFloatLongIterator
            public long setValue(long val) {
                throw new UnsupportedOperationException();
            }

            @Override // gnu.trove.iterator.TIterator, java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override // gnu.trove.map.TFloatLongMap
    public long putIfAbsent(float key, long value) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TFloatLongMap
    public void transformValues(TLongFunction function) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TFloatLongMap
    public boolean retainEntries(TFloatLongProcedure procedure) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TFloatLongMap
    public boolean increment(float key) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TFloatLongMap
    public boolean adjustValue(float key, long amount) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TFloatLongMap
    public long adjustOrPutValue(float key, long adjust_amount, long put_amount) {
        throw new UnsupportedOperationException();
    }
}
