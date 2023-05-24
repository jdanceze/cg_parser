package gnu.trove.impl.unmodifiable;

import gnu.trove.TCollections;
import gnu.trove.TIntCollection;
import gnu.trove.function.TIntFunction;
import gnu.trove.iterator.TObjectIntIterator;
import gnu.trove.map.TObjectIntMap;
import gnu.trove.procedure.TIntProcedure;
import gnu.trove.procedure.TObjectIntProcedure;
import gnu.trove.procedure.TObjectProcedure;
import java.io.Serializable;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/impl/unmodifiable/TUnmodifiableObjectIntMap.class */
public class TUnmodifiableObjectIntMap<K> implements TObjectIntMap<K>, Serializable {
    private static final long serialVersionUID = -1034234728574286014L;
    private final TObjectIntMap<K> m;
    private transient Set<K> keySet = null;
    private transient TIntCollection values = null;

    public TUnmodifiableObjectIntMap(TObjectIntMap<K> m) {
        if (m == null) {
            throw new NullPointerException();
        }
        this.m = m;
    }

    @Override // gnu.trove.map.TObjectIntMap
    public int size() {
        return this.m.size();
    }

    @Override // gnu.trove.map.TObjectIntMap
    public boolean isEmpty() {
        return this.m.isEmpty();
    }

    @Override // gnu.trove.map.TObjectIntMap
    public boolean containsKey(Object key) {
        return this.m.containsKey(key);
    }

    @Override // gnu.trove.map.TObjectIntMap
    public boolean containsValue(int val) {
        return this.m.containsValue(val);
    }

    @Override // gnu.trove.map.TObjectIntMap
    public int get(Object key) {
        return this.m.get(key);
    }

    @Override // gnu.trove.map.TObjectIntMap
    public int put(K key, int value) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TObjectIntMap
    public int remove(Object key) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TObjectIntMap
    public void putAll(TObjectIntMap<? extends K> m) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TObjectIntMap
    public void putAll(Map<? extends K, ? extends Integer> map) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TObjectIntMap
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TObjectIntMap
    public Set<K> keySet() {
        if (this.keySet == null) {
            this.keySet = Collections.unmodifiableSet(this.m.keySet());
        }
        return this.keySet;
    }

    @Override // gnu.trove.map.TObjectIntMap
    public Object[] keys() {
        return this.m.keys();
    }

    @Override // gnu.trove.map.TObjectIntMap
    public K[] keys(K[] array) {
        return this.m.keys(array);
    }

    @Override // gnu.trove.map.TObjectIntMap
    public TIntCollection valueCollection() {
        if (this.values == null) {
            this.values = TCollections.unmodifiableCollection(this.m.valueCollection());
        }
        return this.values;
    }

    @Override // gnu.trove.map.TObjectIntMap
    public int[] values() {
        return this.m.values();
    }

    @Override // gnu.trove.map.TObjectIntMap
    public int[] values(int[] array) {
        return this.m.values(array);
    }

    @Override // gnu.trove.map.TObjectIntMap
    public boolean equals(Object o) {
        return o == this || this.m.equals(o);
    }

    @Override // gnu.trove.map.TObjectIntMap
    public int hashCode() {
        return this.m.hashCode();
    }

    public String toString() {
        return this.m.toString();
    }

    @Override // gnu.trove.map.TObjectIntMap
    public int getNoEntryValue() {
        return this.m.getNoEntryValue();
    }

    @Override // gnu.trove.map.TObjectIntMap
    public boolean forEachKey(TObjectProcedure<? super K> procedure) {
        return this.m.forEachKey(procedure);
    }

    @Override // gnu.trove.map.TObjectIntMap
    public boolean forEachValue(TIntProcedure procedure) {
        return this.m.forEachValue(procedure);
    }

    @Override // gnu.trove.map.TObjectIntMap
    public boolean forEachEntry(TObjectIntProcedure<? super K> procedure) {
        return this.m.forEachEntry(procedure);
    }

    @Override // gnu.trove.map.TObjectIntMap
    public TObjectIntIterator<K> iterator() {
        return new TObjectIntIterator<K>() { // from class: gnu.trove.impl.unmodifiable.TUnmodifiableObjectIntMap.1
            TObjectIntIterator<K> iter;

            {
                this.iter = TUnmodifiableObjectIntMap.this.m.iterator();
            }

            @Override // gnu.trove.iterator.TObjectIntIterator
            public K key() {
                return this.iter.key();
            }

            @Override // gnu.trove.iterator.TObjectIntIterator
            public int value() {
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

            @Override // gnu.trove.iterator.TObjectIntIterator
            public int setValue(int val) {
                throw new UnsupportedOperationException();
            }

            @Override // gnu.trove.iterator.TIterator, java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override // gnu.trove.map.TObjectIntMap
    public int putIfAbsent(K key, int value) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TObjectIntMap
    public void transformValues(TIntFunction function) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TObjectIntMap
    public boolean retainEntries(TObjectIntProcedure<? super K> procedure) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TObjectIntMap
    public boolean increment(K key) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TObjectIntMap
    public boolean adjustValue(K key, int amount) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TObjectIntMap
    public int adjustOrPutValue(K key, int adjust_amount, int put_amount) {
        throw new UnsupportedOperationException();
    }
}
