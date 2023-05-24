package gnu.trove.impl.unmodifiable;

import gnu.trove.TCollections;
import gnu.trove.TFloatCollection;
import gnu.trove.function.TFloatFunction;
import gnu.trove.iterator.TShortFloatIterator;
import gnu.trove.map.TShortFloatMap;
import gnu.trove.procedure.TFloatProcedure;
import gnu.trove.procedure.TShortFloatProcedure;
import gnu.trove.procedure.TShortProcedure;
import gnu.trove.set.TShortSet;
import java.io.Serializable;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/impl/unmodifiable/TUnmodifiableShortFloatMap.class */
public class TUnmodifiableShortFloatMap implements TShortFloatMap, Serializable {
    private static final long serialVersionUID = -1034234728574286014L;
    private final TShortFloatMap m;
    private transient TShortSet keySet = null;
    private transient TFloatCollection values = null;

    public TUnmodifiableShortFloatMap(TShortFloatMap m) {
        if (m == null) {
            throw new NullPointerException();
        }
        this.m = m;
    }

    @Override // gnu.trove.map.TShortFloatMap
    public int size() {
        return this.m.size();
    }

    @Override // gnu.trove.map.TShortFloatMap
    public boolean isEmpty() {
        return this.m.isEmpty();
    }

    @Override // gnu.trove.map.TShortFloatMap
    public boolean containsKey(short key) {
        return this.m.containsKey(key);
    }

    @Override // gnu.trove.map.TShortFloatMap
    public boolean containsValue(float val) {
        return this.m.containsValue(val);
    }

    @Override // gnu.trove.map.TShortFloatMap
    public float get(short key) {
        return this.m.get(key);
    }

    @Override // gnu.trove.map.TShortFloatMap
    public float put(short key, float value) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TShortFloatMap
    public float remove(short key) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TShortFloatMap
    public void putAll(TShortFloatMap m) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TShortFloatMap
    public void putAll(Map<? extends Short, ? extends Float> map) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TShortFloatMap
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TShortFloatMap
    public TShortSet keySet() {
        if (this.keySet == null) {
            this.keySet = TCollections.unmodifiableSet(this.m.keySet());
        }
        return this.keySet;
    }

    @Override // gnu.trove.map.TShortFloatMap
    public short[] keys() {
        return this.m.keys();
    }

    @Override // gnu.trove.map.TShortFloatMap
    public short[] keys(short[] array) {
        return this.m.keys(array);
    }

    @Override // gnu.trove.map.TShortFloatMap
    public TFloatCollection valueCollection() {
        if (this.values == null) {
            this.values = TCollections.unmodifiableCollection(this.m.valueCollection());
        }
        return this.values;
    }

    @Override // gnu.trove.map.TShortFloatMap
    public float[] values() {
        return this.m.values();
    }

    @Override // gnu.trove.map.TShortFloatMap
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

    @Override // gnu.trove.map.TShortFloatMap
    public short getNoEntryKey() {
        return this.m.getNoEntryKey();
    }

    @Override // gnu.trove.map.TShortFloatMap
    public float getNoEntryValue() {
        return this.m.getNoEntryValue();
    }

    @Override // gnu.trove.map.TShortFloatMap
    public boolean forEachKey(TShortProcedure procedure) {
        return this.m.forEachKey(procedure);
    }

    @Override // gnu.trove.map.TShortFloatMap
    public boolean forEachValue(TFloatProcedure procedure) {
        return this.m.forEachValue(procedure);
    }

    @Override // gnu.trove.map.TShortFloatMap
    public boolean forEachEntry(TShortFloatProcedure procedure) {
        return this.m.forEachEntry(procedure);
    }

    @Override // gnu.trove.map.TShortFloatMap
    public TShortFloatIterator iterator() {
        return new TShortFloatIterator() { // from class: gnu.trove.impl.unmodifiable.TUnmodifiableShortFloatMap.1
            TShortFloatIterator iter;

            {
                this.iter = TUnmodifiableShortFloatMap.this.m.iterator();
            }

            @Override // gnu.trove.iterator.TShortFloatIterator
            public short key() {
                return this.iter.key();
            }

            @Override // gnu.trove.iterator.TShortFloatIterator
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

            @Override // gnu.trove.iterator.TShortFloatIterator
            public float setValue(float val) {
                throw new UnsupportedOperationException();
            }

            @Override // gnu.trove.iterator.TIterator, java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override // gnu.trove.map.TShortFloatMap
    public float putIfAbsent(short key, float value) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TShortFloatMap
    public void transformValues(TFloatFunction function) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TShortFloatMap
    public boolean retainEntries(TShortFloatProcedure procedure) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TShortFloatMap
    public boolean increment(short key) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TShortFloatMap
    public boolean adjustValue(short key, float amount) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TShortFloatMap
    public float adjustOrPutValue(short key, float adjust_amount, float put_amount) {
        throw new UnsupportedOperationException();
    }
}
