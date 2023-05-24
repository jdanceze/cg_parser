package gnu.trove.impl.unmodifiable;

import gnu.trove.TCollections;
import gnu.trove.function.TObjectFunction;
import gnu.trove.iterator.TIntObjectIterator;
import gnu.trove.map.TIntObjectMap;
import gnu.trove.procedure.TIntObjectProcedure;
import gnu.trove.procedure.TIntProcedure;
import gnu.trove.procedure.TObjectProcedure;
import gnu.trove.set.TIntSet;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/impl/unmodifiable/TUnmodifiableIntObjectMap.class */
public class TUnmodifiableIntObjectMap<V> implements TIntObjectMap<V>, Serializable {
    private static final long serialVersionUID = -1034234728574286014L;
    private final TIntObjectMap<V> m;
    private transient TIntSet keySet = null;
    private transient Collection<V> values = null;

    public TUnmodifiableIntObjectMap(TIntObjectMap<V> m) {
        if (m == null) {
            throw new NullPointerException();
        }
        this.m = m;
    }

    @Override // gnu.trove.map.TIntObjectMap
    public int size() {
        return this.m.size();
    }

    @Override // gnu.trove.map.TIntObjectMap
    public boolean isEmpty() {
        return this.m.isEmpty();
    }

    @Override // gnu.trove.map.TIntObjectMap
    public boolean containsKey(int key) {
        return this.m.containsKey(key);
    }

    @Override // gnu.trove.map.TIntObjectMap
    public boolean containsValue(Object val) {
        return this.m.containsValue(val);
    }

    @Override // gnu.trove.map.TIntObjectMap
    public V get(int key) {
        return this.m.get(key);
    }

    @Override // gnu.trove.map.TIntObjectMap
    public V put(int key, V value) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TIntObjectMap
    public V remove(int key) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TIntObjectMap
    public void putAll(TIntObjectMap<? extends V> m) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TIntObjectMap
    public void putAll(Map<? extends Integer, ? extends V> map) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TIntObjectMap
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TIntObjectMap
    public TIntSet keySet() {
        if (this.keySet == null) {
            this.keySet = TCollections.unmodifiableSet(this.m.keySet());
        }
        return this.keySet;
    }

    @Override // gnu.trove.map.TIntObjectMap
    public int[] keys() {
        return this.m.keys();
    }

    @Override // gnu.trove.map.TIntObjectMap
    public int[] keys(int[] array) {
        return this.m.keys(array);
    }

    @Override // gnu.trove.map.TIntObjectMap
    public Collection<V> valueCollection() {
        if (this.values == null) {
            this.values = Collections.unmodifiableCollection(this.m.valueCollection());
        }
        return this.values;
    }

    @Override // gnu.trove.map.TIntObjectMap
    public Object[] values() {
        return this.m.values();
    }

    @Override // gnu.trove.map.TIntObjectMap
    public V[] values(V[] array) {
        return this.m.values(array);
    }

    @Override // gnu.trove.map.TIntObjectMap
    public boolean equals(Object o) {
        return o == this || this.m.equals(o);
    }

    @Override // gnu.trove.map.TIntObjectMap
    public int hashCode() {
        return this.m.hashCode();
    }

    public String toString() {
        return this.m.toString();
    }

    @Override // gnu.trove.map.TIntObjectMap
    public int getNoEntryKey() {
        return this.m.getNoEntryKey();
    }

    @Override // gnu.trove.map.TIntObjectMap
    public boolean forEachKey(TIntProcedure procedure) {
        return this.m.forEachKey(procedure);
    }

    @Override // gnu.trove.map.TIntObjectMap
    public boolean forEachValue(TObjectProcedure<? super V> procedure) {
        return this.m.forEachValue(procedure);
    }

    @Override // gnu.trove.map.TIntObjectMap
    public boolean forEachEntry(TIntObjectProcedure<? super V> procedure) {
        return this.m.forEachEntry(procedure);
    }

    @Override // gnu.trove.map.TIntObjectMap
    public TIntObjectIterator<V> iterator() {
        return new TIntObjectIterator<V>() { // from class: gnu.trove.impl.unmodifiable.TUnmodifiableIntObjectMap.1
            TIntObjectIterator<V> iter;

            {
                this.iter = TUnmodifiableIntObjectMap.this.m.iterator();
            }

            @Override // gnu.trove.iterator.TIntObjectIterator
            public int key() {
                return this.iter.key();
            }

            @Override // gnu.trove.iterator.TIntObjectIterator
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

            @Override // gnu.trove.iterator.TIntObjectIterator
            public V setValue(V val) {
                throw new UnsupportedOperationException();
            }

            @Override // gnu.trove.iterator.TIterator, java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override // gnu.trove.map.TIntObjectMap
    public V putIfAbsent(int key, V value) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TIntObjectMap
    public void transformValues(TObjectFunction<V, V> function) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TIntObjectMap
    public boolean retainEntries(TIntObjectProcedure<? super V> procedure) {
        throw new UnsupportedOperationException();
    }
}
