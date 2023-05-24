package gnu.trove.impl.unmodifiable;

import gnu.trove.TCollections;
import gnu.trove.TFloatCollection;
import gnu.trove.function.TFloatFunction;
import gnu.trove.iterator.TObjectFloatIterator;
import gnu.trove.map.TObjectFloatMap;
import gnu.trove.procedure.TFloatProcedure;
import gnu.trove.procedure.TObjectFloatProcedure;
import gnu.trove.procedure.TObjectProcedure;
import java.io.Serializable;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/impl/unmodifiable/TUnmodifiableObjectFloatMap.class */
public class TUnmodifiableObjectFloatMap<K> implements TObjectFloatMap<K>, Serializable {
    private static final long serialVersionUID = -1034234728574286014L;
    private final TObjectFloatMap<K> m;
    private transient Set<K> keySet = null;
    private transient TFloatCollection values = null;

    public TUnmodifiableObjectFloatMap(TObjectFloatMap<K> m) {
        if (m == null) {
            throw new NullPointerException();
        }
        this.m = m;
    }

    @Override // gnu.trove.map.TObjectFloatMap
    public int size() {
        return this.m.size();
    }

    @Override // gnu.trove.map.TObjectFloatMap
    public boolean isEmpty() {
        return this.m.isEmpty();
    }

    @Override // gnu.trove.map.TObjectFloatMap
    public boolean containsKey(Object key) {
        return this.m.containsKey(key);
    }

    @Override // gnu.trove.map.TObjectFloatMap
    public boolean containsValue(float val) {
        return this.m.containsValue(val);
    }

    @Override // gnu.trove.map.TObjectFloatMap
    public float get(Object key) {
        return this.m.get(key);
    }

    @Override // gnu.trove.map.TObjectFloatMap
    public float put(K key, float value) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TObjectFloatMap
    public float remove(Object key) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TObjectFloatMap
    public void putAll(TObjectFloatMap<? extends K> m) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TObjectFloatMap
    public void putAll(Map<? extends K, ? extends Float> map) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TObjectFloatMap
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TObjectFloatMap
    public Set<K> keySet() {
        if (this.keySet == null) {
            this.keySet = Collections.unmodifiableSet(this.m.keySet());
        }
        return this.keySet;
    }

    @Override // gnu.trove.map.TObjectFloatMap
    public Object[] keys() {
        return this.m.keys();
    }

    @Override // gnu.trove.map.TObjectFloatMap
    public K[] keys(K[] array) {
        return this.m.keys(array);
    }

    @Override // gnu.trove.map.TObjectFloatMap
    public TFloatCollection valueCollection() {
        if (this.values == null) {
            this.values = TCollections.unmodifiableCollection(this.m.valueCollection());
        }
        return this.values;
    }

    @Override // gnu.trove.map.TObjectFloatMap
    public float[] values() {
        return this.m.values();
    }

    @Override // gnu.trove.map.TObjectFloatMap
    public float[] values(float[] array) {
        return this.m.values(array);
    }

    @Override // gnu.trove.map.TObjectFloatMap
    public boolean equals(Object o) {
        return o == this || this.m.equals(o);
    }

    @Override // gnu.trove.map.TObjectFloatMap
    public int hashCode() {
        return this.m.hashCode();
    }

    public String toString() {
        return this.m.toString();
    }

    @Override // gnu.trove.map.TObjectFloatMap
    public float getNoEntryValue() {
        return this.m.getNoEntryValue();
    }

    @Override // gnu.trove.map.TObjectFloatMap
    public boolean forEachKey(TObjectProcedure<? super K> procedure) {
        return this.m.forEachKey(procedure);
    }

    @Override // gnu.trove.map.TObjectFloatMap
    public boolean forEachValue(TFloatProcedure procedure) {
        return this.m.forEachValue(procedure);
    }

    @Override // gnu.trove.map.TObjectFloatMap
    public boolean forEachEntry(TObjectFloatProcedure<? super K> procedure) {
        return this.m.forEachEntry(procedure);
    }

    @Override // gnu.trove.map.TObjectFloatMap
    public TObjectFloatIterator<K> iterator() {
        return new TObjectFloatIterator<K>() { // from class: gnu.trove.impl.unmodifiable.TUnmodifiableObjectFloatMap.1
            TObjectFloatIterator<K> iter;

            {
                this.iter = TUnmodifiableObjectFloatMap.this.m.iterator();
            }

            @Override // gnu.trove.iterator.TObjectFloatIterator
            public K key() {
                return this.iter.key();
            }

            @Override // gnu.trove.iterator.TObjectFloatIterator
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

            @Override // gnu.trove.iterator.TObjectFloatIterator
            public float setValue(float val) {
                throw new UnsupportedOperationException();
            }

            @Override // gnu.trove.iterator.TIterator, java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override // gnu.trove.map.TObjectFloatMap
    public float putIfAbsent(K key, float value) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TObjectFloatMap
    public void transformValues(TFloatFunction function) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TObjectFloatMap
    public boolean retainEntries(TObjectFloatProcedure<? super K> procedure) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TObjectFloatMap
    public boolean increment(K key) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TObjectFloatMap
    public boolean adjustValue(K key, float amount) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TObjectFloatMap
    public float adjustOrPutValue(K key, float adjust_amount, float put_amount) {
        throw new UnsupportedOperationException();
    }
}
