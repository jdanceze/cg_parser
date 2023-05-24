package gnu.trove.impl.unmodifiable;

import gnu.trove.TCollections;
import gnu.trove.TShortCollection;
import gnu.trove.function.TShortFunction;
import gnu.trove.iterator.TFloatShortIterator;
import gnu.trove.map.TFloatShortMap;
import gnu.trove.procedure.TFloatProcedure;
import gnu.trove.procedure.TFloatShortProcedure;
import gnu.trove.procedure.TShortProcedure;
import gnu.trove.set.TFloatSet;
import java.io.Serializable;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/impl/unmodifiable/TUnmodifiableFloatShortMap.class */
public class TUnmodifiableFloatShortMap implements TFloatShortMap, Serializable {
    private static final long serialVersionUID = -1034234728574286014L;
    private final TFloatShortMap m;
    private transient TFloatSet keySet = null;
    private transient TShortCollection values = null;

    public TUnmodifiableFloatShortMap(TFloatShortMap m) {
        if (m == null) {
            throw new NullPointerException();
        }
        this.m = m;
    }

    @Override // gnu.trove.map.TFloatShortMap
    public int size() {
        return this.m.size();
    }

    @Override // gnu.trove.map.TFloatShortMap
    public boolean isEmpty() {
        return this.m.isEmpty();
    }

    @Override // gnu.trove.map.TFloatShortMap
    public boolean containsKey(float key) {
        return this.m.containsKey(key);
    }

    @Override // gnu.trove.map.TFloatShortMap
    public boolean containsValue(short val) {
        return this.m.containsValue(val);
    }

    @Override // gnu.trove.map.TFloatShortMap
    public short get(float key) {
        return this.m.get(key);
    }

    @Override // gnu.trove.map.TFloatShortMap
    public short put(float key, short value) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TFloatShortMap
    public short remove(float key) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TFloatShortMap
    public void putAll(TFloatShortMap m) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TFloatShortMap
    public void putAll(Map<? extends Float, ? extends Short> map) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TFloatShortMap
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TFloatShortMap
    public TFloatSet keySet() {
        if (this.keySet == null) {
            this.keySet = TCollections.unmodifiableSet(this.m.keySet());
        }
        return this.keySet;
    }

    @Override // gnu.trove.map.TFloatShortMap
    public float[] keys() {
        return this.m.keys();
    }

    @Override // gnu.trove.map.TFloatShortMap
    public float[] keys(float[] array) {
        return this.m.keys(array);
    }

    @Override // gnu.trove.map.TFloatShortMap
    public TShortCollection valueCollection() {
        if (this.values == null) {
            this.values = TCollections.unmodifiableCollection(this.m.valueCollection());
        }
        return this.values;
    }

    @Override // gnu.trove.map.TFloatShortMap
    public short[] values() {
        return this.m.values();
    }

    @Override // gnu.trove.map.TFloatShortMap
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

    @Override // gnu.trove.map.TFloatShortMap
    public float getNoEntryKey() {
        return this.m.getNoEntryKey();
    }

    @Override // gnu.trove.map.TFloatShortMap
    public short getNoEntryValue() {
        return this.m.getNoEntryValue();
    }

    @Override // gnu.trove.map.TFloatShortMap
    public boolean forEachKey(TFloatProcedure procedure) {
        return this.m.forEachKey(procedure);
    }

    @Override // gnu.trove.map.TFloatShortMap
    public boolean forEachValue(TShortProcedure procedure) {
        return this.m.forEachValue(procedure);
    }

    @Override // gnu.trove.map.TFloatShortMap
    public boolean forEachEntry(TFloatShortProcedure procedure) {
        return this.m.forEachEntry(procedure);
    }

    @Override // gnu.trove.map.TFloatShortMap
    public TFloatShortIterator iterator() {
        return new TFloatShortIterator() { // from class: gnu.trove.impl.unmodifiable.TUnmodifiableFloatShortMap.1
            TFloatShortIterator iter;

            {
                this.iter = TUnmodifiableFloatShortMap.this.m.iterator();
            }

            @Override // gnu.trove.iterator.TFloatShortIterator
            public float key() {
                return this.iter.key();
            }

            @Override // gnu.trove.iterator.TFloatShortIterator
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

            @Override // gnu.trove.iterator.TFloatShortIterator
            public short setValue(short val) {
                throw new UnsupportedOperationException();
            }

            @Override // gnu.trove.iterator.TIterator, java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override // gnu.trove.map.TFloatShortMap
    public short putIfAbsent(float key, short value) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TFloatShortMap
    public void transformValues(TShortFunction function) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TFloatShortMap
    public boolean retainEntries(TFloatShortProcedure procedure) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TFloatShortMap
    public boolean increment(float key) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TFloatShortMap
    public boolean adjustValue(float key, short amount) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TFloatShortMap
    public short adjustOrPutValue(float key, short adjust_amount, short put_amount) {
        throw new UnsupportedOperationException();
    }
}
