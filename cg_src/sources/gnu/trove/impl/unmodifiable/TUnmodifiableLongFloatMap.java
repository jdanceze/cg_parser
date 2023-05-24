package gnu.trove.impl.unmodifiable;

import gnu.trove.TCollections;
import gnu.trove.TFloatCollection;
import gnu.trove.function.TFloatFunction;
import gnu.trove.iterator.TLongFloatIterator;
import gnu.trove.map.TLongFloatMap;
import gnu.trove.procedure.TFloatProcedure;
import gnu.trove.procedure.TLongFloatProcedure;
import gnu.trove.procedure.TLongProcedure;
import gnu.trove.set.TLongSet;
import java.io.Serializable;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/impl/unmodifiable/TUnmodifiableLongFloatMap.class */
public class TUnmodifiableLongFloatMap implements TLongFloatMap, Serializable {
    private static final long serialVersionUID = -1034234728574286014L;
    private final TLongFloatMap m;
    private transient TLongSet keySet = null;
    private transient TFloatCollection values = null;

    public TUnmodifiableLongFloatMap(TLongFloatMap m) {
        if (m == null) {
            throw new NullPointerException();
        }
        this.m = m;
    }

    @Override // gnu.trove.map.TLongFloatMap
    public int size() {
        return this.m.size();
    }

    @Override // gnu.trove.map.TLongFloatMap
    public boolean isEmpty() {
        return this.m.isEmpty();
    }

    @Override // gnu.trove.map.TLongFloatMap
    public boolean containsKey(long key) {
        return this.m.containsKey(key);
    }

    @Override // gnu.trove.map.TLongFloatMap
    public boolean containsValue(float val) {
        return this.m.containsValue(val);
    }

    @Override // gnu.trove.map.TLongFloatMap
    public float get(long key) {
        return this.m.get(key);
    }

    @Override // gnu.trove.map.TLongFloatMap
    public float put(long key, float value) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TLongFloatMap
    public float remove(long key) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TLongFloatMap
    public void putAll(TLongFloatMap m) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TLongFloatMap
    public void putAll(Map<? extends Long, ? extends Float> map) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TLongFloatMap
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TLongFloatMap
    public TLongSet keySet() {
        if (this.keySet == null) {
            this.keySet = TCollections.unmodifiableSet(this.m.keySet());
        }
        return this.keySet;
    }

    @Override // gnu.trove.map.TLongFloatMap
    public long[] keys() {
        return this.m.keys();
    }

    @Override // gnu.trove.map.TLongFloatMap
    public long[] keys(long[] array) {
        return this.m.keys(array);
    }

    @Override // gnu.trove.map.TLongFloatMap
    public TFloatCollection valueCollection() {
        if (this.values == null) {
            this.values = TCollections.unmodifiableCollection(this.m.valueCollection());
        }
        return this.values;
    }

    @Override // gnu.trove.map.TLongFloatMap
    public float[] values() {
        return this.m.values();
    }

    @Override // gnu.trove.map.TLongFloatMap
    public float[] values(float[] array) {
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

    @Override // gnu.trove.map.TLongFloatMap
    public long getNoEntryKey() {
        return this.m.getNoEntryKey();
    }

    @Override // gnu.trove.map.TLongFloatMap
    public float getNoEntryValue() {
        return this.m.getNoEntryValue();
    }

    @Override // gnu.trove.map.TLongFloatMap
    public boolean forEachKey(TLongProcedure procedure) {
        return this.m.forEachKey(procedure);
    }

    @Override // gnu.trove.map.TLongFloatMap
    public boolean forEachValue(TFloatProcedure procedure) {
        return this.m.forEachValue(procedure);
    }

    @Override // gnu.trove.map.TLongFloatMap
    public boolean forEachEntry(TLongFloatProcedure procedure) {
        return this.m.forEachEntry(procedure);
    }

    @Override // gnu.trove.map.TLongFloatMap
    public TLongFloatIterator iterator() {
        return new TLongFloatIterator() { // from class: gnu.trove.impl.unmodifiable.TUnmodifiableLongFloatMap.1
            TLongFloatIterator iter;

            {
                this.iter = TUnmodifiableLongFloatMap.this.m.iterator();
            }

            @Override // gnu.trove.iterator.TLongFloatIterator
            public long key() {
                return this.iter.key();
            }

            @Override // gnu.trove.iterator.TLongFloatIterator
            public float value() {
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

            @Override // gnu.trove.iterator.TLongFloatIterator
            public float setValue(float val) {
                throw new UnsupportedOperationException();
            }

            @Override // gnu.trove.iterator.TIterator, java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override // gnu.trove.map.TLongFloatMap
    public float putIfAbsent(long key, float value) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TLongFloatMap
    public void transformValues(TFloatFunction function) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TLongFloatMap
    public boolean retainEntries(TLongFloatProcedure procedure) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TLongFloatMap
    public boolean increment(long key) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TLongFloatMap
    public boolean adjustValue(long key, float amount) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TLongFloatMap
    public float adjustOrPutValue(long key, float adjust_amount, float put_amount) {
        throw new UnsupportedOperationException();
    }
}
