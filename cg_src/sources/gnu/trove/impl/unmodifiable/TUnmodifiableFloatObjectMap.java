package gnu.trove.impl.unmodifiable;

import gnu.trove.TCollections;
import gnu.trove.function.TObjectFunction;
import gnu.trove.iterator.TFloatObjectIterator;
import gnu.trove.map.TFloatObjectMap;
import gnu.trove.procedure.TFloatObjectProcedure;
import gnu.trove.procedure.TFloatProcedure;
import gnu.trove.procedure.TObjectProcedure;
import gnu.trove.set.TFloatSet;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/impl/unmodifiable/TUnmodifiableFloatObjectMap.class */
public class TUnmodifiableFloatObjectMap<V> implements TFloatObjectMap<V>, Serializable {
    private static final long serialVersionUID = -1034234728574286014L;
    private final TFloatObjectMap<V> m;
    private transient TFloatSet keySet = null;
    private transient Collection<V> values = null;

    public TUnmodifiableFloatObjectMap(TFloatObjectMap<V> m) {
        if (m == null) {
            throw new NullPointerException();
        }
        this.m = m;
    }

    @Override // gnu.trove.map.TFloatObjectMap
    public int size() {
        return this.m.size();
    }

    @Override // gnu.trove.map.TFloatObjectMap
    public boolean isEmpty() {
        return this.m.isEmpty();
    }

    @Override // gnu.trove.map.TFloatObjectMap
    public boolean containsKey(float key) {
        return this.m.containsKey(key);
    }

    @Override // gnu.trove.map.TFloatObjectMap
    public boolean containsValue(Object val) {
        return this.m.containsValue(val);
    }

    @Override // gnu.trove.map.TFloatObjectMap
    public V get(float key) {
        return this.m.get(key);
    }

    @Override // gnu.trove.map.TFloatObjectMap
    public V put(float key, V value) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TFloatObjectMap
    public V remove(float key) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TFloatObjectMap
    public void putAll(TFloatObjectMap<? extends V> m) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TFloatObjectMap
    public void putAll(Map<? extends Float, ? extends V> map) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TFloatObjectMap
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TFloatObjectMap
    public TFloatSet keySet() {
        if (this.keySet == null) {
            this.keySet = TCollections.unmodifiableSet(this.m.keySet());
        }
        return this.keySet;
    }

    @Override // gnu.trove.map.TFloatObjectMap
    public float[] keys() {
        return this.m.keys();
    }

    @Override // gnu.trove.map.TFloatObjectMap
    public float[] keys(float[] array) {
        return this.m.keys(array);
    }

    @Override // gnu.trove.map.TFloatObjectMap
    public Collection<V> valueCollection() {
        if (this.values == null) {
            this.values = Collections.unmodifiableCollection(this.m.valueCollection());
        }
        return this.values;
    }

    @Override // gnu.trove.map.TFloatObjectMap
    public Object[] values() {
        return this.m.values();
    }

    @Override // gnu.trove.map.TFloatObjectMap
    public V[] values(V[] array) {
        return this.m.values(array);
    }

    @Override // gnu.trove.map.TFloatObjectMap
    public boolean equals(Object o) {
        return o == this || this.m.equals(o);
    }

    @Override // gnu.trove.map.TFloatObjectMap
    public int hashCode() {
        return this.m.hashCode();
    }

    public String toString() {
        return this.m.toString();
    }

    @Override // gnu.trove.map.TFloatObjectMap
    public float getNoEntryKey() {
        return this.m.getNoEntryKey();
    }

    @Override // gnu.trove.map.TFloatObjectMap
    public boolean forEachKey(TFloatProcedure procedure) {
        return this.m.forEachKey(procedure);
    }

    @Override // gnu.trove.map.TFloatObjectMap
    public boolean forEachValue(TObjectProcedure<? super V> procedure) {
        return this.m.forEachValue(procedure);
    }

    @Override // gnu.trove.map.TFloatObjectMap
    public boolean forEachEntry(TFloatObjectProcedure<? super V> procedure) {
        return this.m.forEachEntry(procedure);
    }

    @Override // gnu.trove.map.TFloatObjectMap
    public TFloatObjectIterator<V> iterator() {
        return new TFloatObjectIterator<V>() { // from class: gnu.trove.impl.unmodifiable.TUnmodifiableFloatObjectMap.1
            TFloatObjectIterator<V> iter;

            {
                this.iter = TUnmodifiableFloatObjectMap.this.m.iterator();
            }

            @Override // gnu.trove.iterator.TFloatObjectIterator
            public float key() {
                return this.iter.key();
            }

            @Override // gnu.trove.iterator.TFloatObjectIterator
            public V value() {
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

            @Override // gnu.trove.iterator.TFloatObjectIterator
            public V setValue(V val) {
                throw new UnsupportedOperationException();
            }

            @Override // gnu.trove.iterator.TIterator, java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override // gnu.trove.map.TFloatObjectMap
    public V putIfAbsent(float key, V value) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TFloatObjectMap
    public void transformValues(TObjectFunction<V, V> function) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TFloatObjectMap
    public boolean retainEntries(TFloatObjectProcedure<? super V> procedure) {
        throw new UnsupportedOperationException();
    }
}
