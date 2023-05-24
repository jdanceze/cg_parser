package gnu.trove.impl.unmodifiable;

import gnu.trove.TCharCollection;
import gnu.trove.TCollections;
import gnu.trove.function.TCharFunction;
import gnu.trove.iterator.TObjectCharIterator;
import gnu.trove.map.TObjectCharMap;
import gnu.trove.procedure.TCharProcedure;
import gnu.trove.procedure.TObjectCharProcedure;
import gnu.trove.procedure.TObjectProcedure;
import java.io.Serializable;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/impl/unmodifiable/TUnmodifiableObjectCharMap.class */
public class TUnmodifiableObjectCharMap<K> implements TObjectCharMap<K>, Serializable {
    private static final long serialVersionUID = -1034234728574286014L;
    private final TObjectCharMap<K> m;
    private transient Set<K> keySet = null;
    private transient TCharCollection values = null;

    public TUnmodifiableObjectCharMap(TObjectCharMap<K> m) {
        if (m == null) {
            throw new NullPointerException();
        }
        this.m = m;
    }

    @Override // gnu.trove.map.TObjectCharMap
    public int size() {
        return this.m.size();
    }

    @Override // gnu.trove.map.TObjectCharMap
    public boolean isEmpty() {
        return this.m.isEmpty();
    }

    @Override // gnu.trove.map.TObjectCharMap
    public boolean containsKey(Object key) {
        return this.m.containsKey(key);
    }

    @Override // gnu.trove.map.TObjectCharMap
    public boolean containsValue(char val) {
        return this.m.containsValue(val);
    }

    @Override // gnu.trove.map.TObjectCharMap
    public char get(Object key) {
        return this.m.get(key);
    }

    @Override // gnu.trove.map.TObjectCharMap
    public char put(K key, char value) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TObjectCharMap
    public char remove(Object key) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TObjectCharMap
    public void putAll(TObjectCharMap<? extends K> m) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TObjectCharMap
    public void putAll(Map<? extends K, ? extends Character> map) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TObjectCharMap
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TObjectCharMap
    public Set<K> keySet() {
        if (this.keySet == null) {
            this.keySet = Collections.unmodifiableSet(this.m.keySet());
        }
        return this.keySet;
    }

    @Override // gnu.trove.map.TObjectCharMap
    public Object[] keys() {
        return this.m.keys();
    }

    @Override // gnu.trove.map.TObjectCharMap
    public K[] keys(K[] array) {
        return this.m.keys(array);
    }

    @Override // gnu.trove.map.TObjectCharMap
    public TCharCollection valueCollection() {
        if (this.values == null) {
            this.values = TCollections.unmodifiableCollection(this.m.valueCollection());
        }
        return this.values;
    }

    @Override // gnu.trove.map.TObjectCharMap
    public char[] values() {
        return this.m.values();
    }

    @Override // gnu.trove.map.TObjectCharMap
    public char[] values(char[] array) {
        return this.m.values(array);
    }

    @Override // gnu.trove.map.TObjectCharMap
    public boolean equals(Object o) {
        return o == this || this.m.equals(o);
    }

    @Override // gnu.trove.map.TObjectCharMap
    public int hashCode() {
        return this.m.hashCode();
    }

    public String toString() {
        return this.m.toString();
    }

    @Override // gnu.trove.map.TObjectCharMap
    public char getNoEntryValue() {
        return this.m.getNoEntryValue();
    }

    @Override // gnu.trove.map.TObjectCharMap
    public boolean forEachKey(TObjectProcedure<? super K> procedure) {
        return this.m.forEachKey(procedure);
    }

    @Override // gnu.trove.map.TObjectCharMap
    public boolean forEachValue(TCharProcedure procedure) {
        return this.m.forEachValue(procedure);
    }

    @Override // gnu.trove.map.TObjectCharMap
    public boolean forEachEntry(TObjectCharProcedure<? super K> procedure) {
        return this.m.forEachEntry(procedure);
    }

    @Override // gnu.trove.map.TObjectCharMap
    public TObjectCharIterator<K> iterator() {
        return new TObjectCharIterator<K>() { // from class: gnu.trove.impl.unmodifiable.TUnmodifiableObjectCharMap.1
            TObjectCharIterator<K> iter;

            {
                this.iter = TUnmodifiableObjectCharMap.this.m.iterator();
            }

            @Override // gnu.trove.iterator.TObjectCharIterator
            public K key() {
                return this.iter.key();
            }

            @Override // gnu.trove.iterator.TObjectCharIterator
            public char value() {
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

            @Override // gnu.trove.iterator.TObjectCharIterator
            public char setValue(char val) {
                throw new UnsupportedOperationException();
            }

            @Override // gnu.trove.iterator.TIterator, java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override // gnu.trove.map.TObjectCharMap
    public char putIfAbsent(K key, char value) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TObjectCharMap
    public void transformValues(TCharFunction function) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TObjectCharMap
    public boolean retainEntries(TObjectCharProcedure<? super K> procedure) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TObjectCharMap
    public boolean increment(K key) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TObjectCharMap
    public boolean adjustValue(K key, char amount) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TObjectCharMap
    public char adjustOrPutValue(K key, char adjust_amount, char put_amount) {
        throw new UnsupportedOperationException();
    }
}
