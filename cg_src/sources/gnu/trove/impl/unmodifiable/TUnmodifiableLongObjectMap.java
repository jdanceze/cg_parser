package gnu.trove.impl.unmodifiable;

import gnu.trove.TCollections;
import gnu.trove.function.TObjectFunction;
import gnu.trove.iterator.TLongObjectIterator;
import gnu.trove.map.TLongObjectMap;
import gnu.trove.procedure.TLongObjectProcedure;
import gnu.trove.procedure.TLongProcedure;
import gnu.trove.procedure.TObjectProcedure;
import gnu.trove.set.TLongSet;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/impl/unmodifiable/TUnmodifiableLongObjectMap.class */
public class TUnmodifiableLongObjectMap<V> implements TLongObjectMap<V>, Serializable {
    private static final long serialVersionUID = -1034234728574286014L;
    private final TLongObjectMap<V> m;
    private transient TLongSet keySet = null;
    private transient Collection<V> values = null;

    public TUnmodifiableLongObjectMap(TLongObjectMap<V> m) {
        if (m == null) {
            throw new NullPointerException();
        }
        this.m = m;
    }

    @Override // gnu.trove.map.TLongObjectMap
    public int size() {
        return this.m.size();
    }

    @Override // gnu.trove.map.TLongObjectMap
    public boolean isEmpty() {
        return this.m.isEmpty();
    }

    @Override // gnu.trove.map.TLongObjectMap
    public boolean containsKey(long key) {
        return this.m.containsKey(key);
    }

    @Override // gnu.trove.map.TLongObjectMap
    public boolean containsValue(Object val) {
        return this.m.containsValue(val);
    }

    @Override // gnu.trove.map.TLongObjectMap
    public V get(long key) {
        return this.m.get(key);
    }

    @Override // gnu.trove.map.TLongObjectMap
    public V put(long key, V value) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TLongObjectMap
    public V remove(long key) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TLongObjectMap
    public void putAll(TLongObjectMap<? extends V> m) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TLongObjectMap
    public void putAll(Map<? extends Long, ? extends V> map) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TLongObjectMap
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TLongObjectMap
    public TLongSet keySet() {
        if (this.keySet == null) {
            this.keySet = TCollections.unmodifiableSet(this.m.keySet());
        }
        return this.keySet;
    }

    @Override // gnu.trove.map.TLongObjectMap
    public long[] keys() {
        return this.m.keys();
    }

    @Override // gnu.trove.map.TLongObjectMap
    public long[] keys(long[] array) {
        return this.m.keys(array);
    }

    @Override // gnu.trove.map.TLongObjectMap
    public Collection<V> valueCollection() {
        if (this.values == null) {
            this.values = Collections.unmodifiableCollection(this.m.valueCollection());
        }
        return this.values;
    }

    @Override // gnu.trove.map.TLongObjectMap
    public Object[] values() {
        return this.m.values();
    }

    @Override // gnu.trove.map.TLongObjectMap
    public V[] values(V[] array) {
        return this.m.values(array);
    }

    @Override // gnu.trove.map.TLongObjectMap
    public boolean equals(Object o) {
        return o == this || this.m.equals(o);
    }

    @Override // gnu.trove.map.TLongObjectMap
    public int hashCode() {
        return this.m.hashCode();
    }

    public String toString() {
        return this.m.toString();
    }

    @Override // gnu.trove.map.TLongObjectMap
    public long getNoEntryKey() {
        return this.m.getNoEntryKey();
    }

    @Override // gnu.trove.map.TLongObjectMap
    public boolean forEachKey(TLongProcedure procedure) {
        return this.m.forEachKey(procedure);
    }

    @Override // gnu.trove.map.TLongObjectMap
    public boolean forEachValue(TObjectProcedure<? super V> procedure) {
        return this.m.forEachValue(procedure);
    }

    @Override // gnu.trove.map.TLongObjectMap
    public boolean forEachEntry(TLongObjectProcedure<? super V> procedure) {
        return this.m.forEachEntry(procedure);
    }

    @Override // gnu.trove.map.TLongObjectMap
    public TLongObjectIterator<V> iterator() {
        return new TLongObjectIterator<V>() { // from class: gnu.trove.impl.unmodifiable.TUnmodifiableLongObjectMap.1
            TLongObjectIterator<V> iter;

            {
                this.iter = TUnmodifiableLongObjectMap.this.m.iterator();
            }

            @Override // gnu.trove.iterator.TLongObjectIterator
            public long key() {
                return this.iter.key();
            }

            @Override // gnu.trove.iterator.TLongObjectIterator
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

            @Override // gnu.trove.iterator.TLongObjectIterator
            public V setValue(V val) {
                throw new UnsupportedOperationException();
            }

            @Override // gnu.trove.iterator.TIterator, java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override // gnu.trove.map.TLongObjectMap
    public V putIfAbsent(long key, V value) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TLongObjectMap
    public void transformValues(TObjectFunction<V, V> function) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TLongObjectMap
    public boolean retainEntries(TLongObjectProcedure<? super V> procedure) {
        throw new UnsupportedOperationException();
    }
}
