package gnu.trove.impl.unmodifiable;

import gnu.trove.TCollections;
import gnu.trove.function.TObjectFunction;
import gnu.trove.iterator.TDoubleObjectIterator;
import gnu.trove.map.TDoubleObjectMap;
import gnu.trove.procedure.TDoubleObjectProcedure;
import gnu.trove.procedure.TDoubleProcedure;
import gnu.trove.procedure.TObjectProcedure;
import gnu.trove.set.TDoubleSet;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/impl/unmodifiable/TUnmodifiableDoubleObjectMap.class */
public class TUnmodifiableDoubleObjectMap<V> implements TDoubleObjectMap<V>, Serializable {
    private static final long serialVersionUID = -1034234728574286014L;
    private final TDoubleObjectMap<V> m;
    private transient TDoubleSet keySet = null;
    private transient Collection<V> values = null;

    public TUnmodifiableDoubleObjectMap(TDoubleObjectMap<V> m) {
        if (m == null) {
            throw new NullPointerException();
        }
        this.m = m;
    }

    @Override // gnu.trove.map.TDoubleObjectMap
    public int size() {
        return this.m.size();
    }

    @Override // gnu.trove.map.TDoubleObjectMap
    public boolean isEmpty() {
        return this.m.isEmpty();
    }

    @Override // gnu.trove.map.TDoubleObjectMap
    public boolean containsKey(double key) {
        return this.m.containsKey(key);
    }

    @Override // gnu.trove.map.TDoubleObjectMap
    public boolean containsValue(Object val) {
        return this.m.containsValue(val);
    }

    @Override // gnu.trove.map.TDoubleObjectMap
    public V get(double key) {
        return this.m.get(key);
    }

    @Override // gnu.trove.map.TDoubleObjectMap
    public V put(double key, V value) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TDoubleObjectMap
    public V remove(double key) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TDoubleObjectMap
    public void putAll(TDoubleObjectMap<? extends V> m) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TDoubleObjectMap
    public void putAll(Map<? extends Double, ? extends V> map) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TDoubleObjectMap
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TDoubleObjectMap
    public TDoubleSet keySet() {
        if (this.keySet == null) {
            this.keySet = TCollections.unmodifiableSet(this.m.keySet());
        }
        return this.keySet;
    }

    @Override // gnu.trove.map.TDoubleObjectMap
    public double[] keys() {
        return this.m.keys();
    }

    @Override // gnu.trove.map.TDoubleObjectMap
    public double[] keys(double[] array) {
        return this.m.keys(array);
    }

    @Override // gnu.trove.map.TDoubleObjectMap
    public Collection<V> valueCollection() {
        if (this.values == null) {
            this.values = Collections.unmodifiableCollection(this.m.valueCollection());
        }
        return this.values;
    }

    @Override // gnu.trove.map.TDoubleObjectMap
    public Object[] values() {
        return this.m.values();
    }

    @Override // gnu.trove.map.TDoubleObjectMap
    public V[] values(V[] array) {
        return this.m.values(array);
    }

    @Override // gnu.trove.map.TDoubleObjectMap
    public boolean equals(Object o) {
        return o == this || this.m.equals(o);
    }

    @Override // gnu.trove.map.TDoubleObjectMap
    public int hashCode() {
        return this.m.hashCode();
    }

    public String toString() {
        return this.m.toString();
    }

    @Override // gnu.trove.map.TDoubleObjectMap
    public double getNoEntryKey() {
        return this.m.getNoEntryKey();
    }

    @Override // gnu.trove.map.TDoubleObjectMap
    public boolean forEachKey(TDoubleProcedure procedure) {
        return this.m.forEachKey(procedure);
    }

    @Override // gnu.trove.map.TDoubleObjectMap
    public boolean forEachValue(TObjectProcedure<? super V> procedure) {
        return this.m.forEachValue(procedure);
    }

    @Override // gnu.trove.map.TDoubleObjectMap
    public boolean forEachEntry(TDoubleObjectProcedure<? super V> procedure) {
        return this.m.forEachEntry(procedure);
    }

    @Override // gnu.trove.map.TDoubleObjectMap
    public TDoubleObjectIterator<V> iterator() {
        return new TDoubleObjectIterator<V>() { // from class: gnu.trove.impl.unmodifiable.TUnmodifiableDoubleObjectMap.1
            TDoubleObjectIterator<V> iter;

            {
                this.iter = TUnmodifiableDoubleObjectMap.this.m.iterator();
            }

            @Override // gnu.trove.iterator.TDoubleObjectIterator
            public double key() {
                return this.iter.key();
            }

            @Override // gnu.trove.iterator.TDoubleObjectIterator
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

            @Override // gnu.trove.iterator.TDoubleObjectIterator
            public V setValue(V val) {
                throw new UnsupportedOperationException();
            }

            @Override // gnu.trove.iterator.TIterator, java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override // gnu.trove.map.TDoubleObjectMap
    public V putIfAbsent(double key, V value) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TDoubleObjectMap
    public void transformValues(TObjectFunction<V, V> function) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TDoubleObjectMap
    public boolean retainEntries(TDoubleObjectProcedure<? super V> procedure) {
        throw new UnsupportedOperationException();
    }
}
