package gnu.trove.impl.unmodifiable;

import gnu.trove.TCollections;
import gnu.trove.TShortCollection;
import gnu.trove.function.TShortFunction;
import gnu.trove.iterator.TShortShortIterator;
import gnu.trove.map.TShortShortMap;
import gnu.trove.procedure.TShortProcedure;
import gnu.trove.procedure.TShortShortProcedure;
import gnu.trove.set.TShortSet;
import java.io.Serializable;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/impl/unmodifiable/TUnmodifiableShortShortMap.class */
public class TUnmodifiableShortShortMap implements TShortShortMap, Serializable {
    private static final long serialVersionUID = -1034234728574286014L;
    private final TShortShortMap m;
    private transient TShortSet keySet = null;
    private transient TShortCollection values = null;

    public TUnmodifiableShortShortMap(TShortShortMap m) {
        if (m == null) {
            throw new NullPointerException();
        }
        this.m = m;
    }

    @Override // gnu.trove.map.TShortShortMap
    public int size() {
        return this.m.size();
    }

    @Override // gnu.trove.map.TShortShortMap
    public boolean isEmpty() {
        return this.m.isEmpty();
    }

    @Override // gnu.trove.map.TShortShortMap
    public boolean containsKey(short key) {
        return this.m.containsKey(key);
    }

    @Override // gnu.trove.map.TShortShortMap
    public boolean containsValue(short val) {
        return this.m.containsValue(val);
    }

    @Override // gnu.trove.map.TShortShortMap
    public short get(short key) {
        return this.m.get(key);
    }

    @Override // gnu.trove.map.TShortShortMap
    public short put(short key, short value) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TShortShortMap
    public short remove(short key) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TShortShortMap
    public void putAll(TShortShortMap m) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TShortShortMap
    public void putAll(Map<? extends Short, ? extends Short> map) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TShortShortMap
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TShortShortMap
    public TShortSet keySet() {
        if (this.keySet == null) {
            this.keySet = TCollections.unmodifiableSet(this.m.keySet());
        }
        return this.keySet;
    }

    @Override // gnu.trove.map.TShortShortMap
    public short[] keys() {
        return this.m.keys();
    }

    @Override // gnu.trove.map.TShortShortMap
    public short[] keys(short[] array) {
        return this.m.keys(array);
    }

    @Override // gnu.trove.map.TShortShortMap
    public TShortCollection valueCollection() {
        if (this.values == null) {
            this.values = TCollections.unmodifiableCollection(this.m.valueCollection());
        }
        return this.values;
    }

    @Override // gnu.trove.map.TShortShortMap
    public short[] values() {
        return this.m.values();
    }

    @Override // gnu.trove.map.TShortShortMap
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

    @Override // gnu.trove.map.TShortShortMap
    public short getNoEntryKey() {
        return this.m.getNoEntryKey();
    }

    @Override // gnu.trove.map.TShortShortMap
    public short getNoEntryValue() {
        return this.m.getNoEntryValue();
    }

    @Override // gnu.trove.map.TShortShortMap
    public boolean forEachKey(TShortProcedure procedure) {
        return this.m.forEachKey(procedure);
    }

    @Override // gnu.trove.map.TShortShortMap
    public boolean forEachValue(TShortProcedure procedure) {
        return this.m.forEachValue(procedure);
    }

    @Override // gnu.trove.map.TShortShortMap
    public boolean forEachEntry(TShortShortProcedure procedure) {
        return this.m.forEachEntry(procedure);
    }

    @Override // gnu.trove.map.TShortShortMap
    public TShortShortIterator iterator() {
        return new TShortShortIterator() { // from class: gnu.trove.impl.unmodifiable.TUnmodifiableShortShortMap.1
            TShortShortIterator iter;

            {
                this.iter = TUnmodifiableShortShortMap.this.m.iterator();
            }

            @Override // gnu.trove.iterator.TShortShortIterator
            public short key() {
                return this.iter.key();
            }

            @Override // gnu.trove.iterator.TShortShortIterator
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

            @Override // gnu.trove.iterator.TShortShortIterator
            public short setValue(short val) {
                throw new UnsupportedOperationException();
            }

            @Override // gnu.trove.iterator.TIterator, java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override // gnu.trove.map.TShortShortMap
    public short putIfAbsent(short key, short value) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TShortShortMap
    public void transformValues(TShortFunction function) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TShortShortMap
    public boolean retainEntries(TShortShortProcedure procedure) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TShortShortMap
    public boolean increment(short key) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TShortShortMap
    public boolean adjustValue(short key, short amount) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TShortShortMap
    public short adjustOrPutValue(short key, short adjust_amount, short put_amount) {
        throw new UnsupportedOperationException();
    }
}
