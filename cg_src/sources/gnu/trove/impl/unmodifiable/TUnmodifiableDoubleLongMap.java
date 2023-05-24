package gnu.trove.impl.unmodifiable;

import gnu.trove.TCollections;
import gnu.trove.TLongCollection;
import gnu.trove.function.TLongFunction;
import gnu.trove.iterator.TDoubleLongIterator;
import gnu.trove.map.TDoubleLongMap;
import gnu.trove.procedure.TDoubleLongProcedure;
import gnu.trove.procedure.TDoubleProcedure;
import gnu.trove.procedure.TLongProcedure;
import gnu.trove.set.TDoubleSet;
import java.io.Serializable;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/impl/unmodifiable/TUnmodifiableDoubleLongMap.class */
public class TUnmodifiableDoubleLongMap implements TDoubleLongMap, Serializable {
    private static final long serialVersionUID = -1034234728574286014L;
    private final TDoubleLongMap m;
    private transient TDoubleSet keySet = null;
    private transient TLongCollection values = null;

    public TUnmodifiableDoubleLongMap(TDoubleLongMap m) {
        if (m == null) {
            throw new NullPointerException();
        }
        this.m = m;
    }

    @Override // gnu.trove.map.TDoubleLongMap
    public int size() {
        return this.m.size();
    }

    @Override // gnu.trove.map.TDoubleLongMap
    public boolean isEmpty() {
        return this.m.isEmpty();
    }

    @Override // gnu.trove.map.TDoubleLongMap
    public boolean containsKey(double key) {
        return this.m.containsKey(key);
    }

    @Override // gnu.trove.map.TDoubleLongMap
    public boolean containsValue(long val) {
        return this.m.containsValue(val);
    }

    @Override // gnu.trove.map.TDoubleLongMap
    public long get(double key) {
        return this.m.get(key);
    }

    @Override // gnu.trove.map.TDoubleLongMap
    public long put(double key, long value) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TDoubleLongMap
    public long remove(double key) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TDoubleLongMap
    public void putAll(TDoubleLongMap m) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TDoubleLongMap
    public void putAll(Map<? extends Double, ? extends Long> map) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TDoubleLongMap
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TDoubleLongMap
    public TDoubleSet keySet() {
        if (this.keySet == null) {
            this.keySet = TCollections.unmodifiableSet(this.m.keySet());
        }
        return this.keySet;
    }

    @Override // gnu.trove.map.TDoubleLongMap
    public double[] keys() {
        return this.m.keys();
    }

    @Override // gnu.trove.map.TDoubleLongMap
    public double[] keys(double[] array) {
        return this.m.keys(array);
    }

    @Override // gnu.trove.map.TDoubleLongMap
    public TLongCollection valueCollection() {
        if (this.values == null) {
            this.values = TCollections.unmodifiableCollection(this.m.valueCollection());
        }
        return this.values;
    }

    @Override // gnu.trove.map.TDoubleLongMap
    public long[] values() {
        return this.m.values();
    }

    @Override // gnu.trove.map.TDoubleLongMap
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

    @Override // gnu.trove.map.TDoubleLongMap
    public double getNoEntryKey() {
        return this.m.getNoEntryKey();
    }

    @Override // gnu.trove.map.TDoubleLongMap
    public long getNoEntryValue() {
        return this.m.getNoEntryValue();
    }

    @Override // gnu.trove.map.TDoubleLongMap
    public boolean forEachKey(TDoubleProcedure procedure) {
        return this.m.forEachKey(procedure);
    }

    @Override // gnu.trove.map.TDoubleLongMap
    public boolean forEachValue(TLongProcedure procedure) {
        return this.m.forEachValue(procedure);
    }

    @Override // gnu.trove.map.TDoubleLongMap
    public boolean forEachEntry(TDoubleLongProcedure procedure) {
        return this.m.forEachEntry(procedure);
    }

    @Override // gnu.trove.map.TDoubleLongMap
    public TDoubleLongIterator iterator() {
        return new TDoubleLongIterator() { // from class: gnu.trove.impl.unmodifiable.TUnmodifiableDoubleLongMap.1
            TDoubleLongIterator iter;

            {
                this.iter = TUnmodifiableDoubleLongMap.this.m.iterator();
            }

            @Override // gnu.trove.iterator.TDoubleLongIterator
            public double key() {
                return this.iter.key();
            }

            @Override // gnu.trove.iterator.TDoubleLongIterator
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

            @Override // gnu.trove.iterator.TDoubleLongIterator
            public long setValue(long val) {
                throw new UnsupportedOperationException();
            }

            @Override // gnu.trove.iterator.TIterator, java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override // gnu.trove.map.TDoubleLongMap
    public long putIfAbsent(double key, long value) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TDoubleLongMap
    public void transformValues(TLongFunction function) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TDoubleLongMap
    public boolean retainEntries(TDoubleLongProcedure procedure) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TDoubleLongMap
    public boolean increment(double key) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TDoubleLongMap
    public boolean adjustValue(double key, long amount) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TDoubleLongMap
    public long adjustOrPutValue(double key, long adjust_amount, long put_amount) {
        throw new UnsupportedOperationException();
    }
}
