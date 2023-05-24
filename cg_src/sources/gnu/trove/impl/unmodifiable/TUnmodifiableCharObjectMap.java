package gnu.trove.impl.unmodifiable;

import gnu.trove.TCollections;
import gnu.trove.function.TObjectFunction;
import gnu.trove.iterator.TCharObjectIterator;
import gnu.trove.map.TCharObjectMap;
import gnu.trove.procedure.TCharObjectProcedure;
import gnu.trove.procedure.TCharProcedure;
import gnu.trove.procedure.TObjectProcedure;
import gnu.trove.set.TCharSet;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/impl/unmodifiable/TUnmodifiableCharObjectMap.class */
public class TUnmodifiableCharObjectMap<V> implements TCharObjectMap<V>, Serializable {
    private static final long serialVersionUID = -1034234728574286014L;
    private final TCharObjectMap<V> m;
    private transient TCharSet keySet = null;
    private transient Collection<V> values = null;

    public TUnmodifiableCharObjectMap(TCharObjectMap<V> m) {
        if (m == null) {
            throw new NullPointerException();
        }
        this.m = m;
    }

    @Override // gnu.trove.map.TCharObjectMap
    public int size() {
        return this.m.size();
    }

    @Override // gnu.trove.map.TCharObjectMap
    public boolean isEmpty() {
        return this.m.isEmpty();
    }

    @Override // gnu.trove.map.TCharObjectMap
    public boolean containsKey(char key) {
        return this.m.containsKey(key);
    }

    @Override // gnu.trove.map.TCharObjectMap
    public boolean containsValue(Object val) {
        return this.m.containsValue(val);
    }

    @Override // gnu.trove.map.TCharObjectMap
    public V get(char key) {
        return this.m.get(key);
    }

    @Override // gnu.trove.map.TCharObjectMap
    public V put(char key, V value) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TCharObjectMap
    public V remove(char key) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TCharObjectMap
    public void putAll(TCharObjectMap<? extends V> m) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TCharObjectMap
    public void putAll(Map<? extends Character, ? extends V> map) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TCharObjectMap
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TCharObjectMap
    public TCharSet keySet() {
        if (this.keySet == null) {
            this.keySet = TCollections.unmodifiableSet(this.m.keySet());
        }
        return this.keySet;
    }

    @Override // gnu.trove.map.TCharObjectMap
    public char[] keys() {
        return this.m.keys();
    }

    @Override // gnu.trove.map.TCharObjectMap
    public char[] keys(char[] array) {
        return this.m.keys(array);
    }

    @Override // gnu.trove.map.TCharObjectMap
    public Collection<V> valueCollection() {
        if (this.values == null) {
            this.values = Collections.unmodifiableCollection(this.m.valueCollection());
        }
        return this.values;
    }

    @Override // gnu.trove.map.TCharObjectMap
    public Object[] values() {
        return this.m.values();
    }

    @Override // gnu.trove.map.TCharObjectMap
    public V[] values(V[] array) {
        return this.m.values(array);
    }

    @Override // gnu.trove.map.TCharObjectMap
    public boolean equals(Object o) {
        return o == this || this.m.equals(o);
    }

    @Override // gnu.trove.map.TCharObjectMap
    public int hashCode() {
        return this.m.hashCode();
    }

    public String toString() {
        return this.m.toString();
    }

    @Override // gnu.trove.map.TCharObjectMap
    public char getNoEntryKey() {
        return this.m.getNoEntryKey();
    }

    @Override // gnu.trove.map.TCharObjectMap
    public boolean forEachKey(TCharProcedure procedure) {
        return this.m.forEachKey(procedure);
    }

    @Override // gnu.trove.map.TCharObjectMap
    public boolean forEachValue(TObjectProcedure<? super V> procedure) {
        return this.m.forEachValue(procedure);
    }

    @Override // gnu.trove.map.TCharObjectMap
    public boolean forEachEntry(TCharObjectProcedure<? super V> procedure) {
        return this.m.forEachEntry(procedure);
    }

    @Override // gnu.trove.map.TCharObjectMap
    public TCharObjectIterator<V> iterator() {
        return new TCharObjectIterator<V>() { // from class: gnu.trove.impl.unmodifiable.TUnmodifiableCharObjectMap.1
            TCharObjectIterator<V> iter;

            {
                this.iter = TUnmodifiableCharObjectMap.this.m.iterator();
            }

            @Override // gnu.trove.iterator.TCharObjectIterator
            public char key() {
                return this.iter.key();
            }

            @Override // gnu.trove.iterator.TCharObjectIterator
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

            @Override // gnu.trove.iterator.TCharObjectIterator
            public V setValue(V val) {
                throw new UnsupportedOperationException();
            }

            @Override // gnu.trove.iterator.TIterator, java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override // gnu.trove.map.TCharObjectMap
    public V putIfAbsent(char key, V value) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TCharObjectMap
    public void transformValues(TObjectFunction<V, V> function) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TCharObjectMap
    public boolean retainEntries(TCharObjectProcedure<? super V> procedure) {
        throw new UnsupportedOperationException();
    }
}
