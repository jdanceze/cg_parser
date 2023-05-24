package gnu.trove.impl.unmodifiable;

import gnu.trove.TCollections;
import gnu.trove.TDoubleCollection;
import gnu.trove.function.TDoubleFunction;
import gnu.trove.iterator.TObjectDoubleIterator;
import gnu.trove.map.TObjectDoubleMap;
import gnu.trove.procedure.TDoubleProcedure;
import gnu.trove.procedure.TObjectDoubleProcedure;
import gnu.trove.procedure.TObjectProcedure;
import java.io.Serializable;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/impl/unmodifiable/TUnmodifiableObjectDoubleMap.class */
public class TUnmodifiableObjectDoubleMap<K> implements TObjectDoubleMap<K>, Serializable {
    private static final long serialVersionUID = -1034234728574286014L;
    private final TObjectDoubleMap<K> m;
    private transient Set<K> keySet = null;
    private transient TDoubleCollection values = null;

    public TUnmodifiableObjectDoubleMap(TObjectDoubleMap<K> m) {
        if (m == null) {
            throw new NullPointerException();
        }
        this.m = m;
    }

    @Override // gnu.trove.map.TObjectDoubleMap
    public int size() {
        return this.m.size();
    }

    @Override // gnu.trove.map.TObjectDoubleMap
    public boolean isEmpty() {
        return this.m.isEmpty();
    }

    @Override // gnu.trove.map.TObjectDoubleMap
    public boolean containsKey(Object key) {
        return this.m.containsKey(key);
    }

    @Override // gnu.trove.map.TObjectDoubleMap
    public boolean containsValue(double val) {
        return this.m.containsValue(val);
    }

    @Override // gnu.trove.map.TObjectDoubleMap
    public double get(Object key) {
        return this.m.get(key);
    }

    @Override // gnu.trove.map.TObjectDoubleMap
    public double put(K key, double value) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TObjectDoubleMap
    public double remove(Object key) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TObjectDoubleMap
    public void putAll(TObjectDoubleMap<? extends K> m) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TObjectDoubleMap
    public void putAll(Map<? extends K, ? extends Double> map) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TObjectDoubleMap
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TObjectDoubleMap
    public Set<K> keySet() {
        if (this.keySet == null) {
            this.keySet = Collections.unmodifiableSet(this.m.keySet());
        }
        return this.keySet;
    }

    @Override // gnu.trove.map.TObjectDoubleMap
    public Object[] keys() {
        return this.m.keys();
    }

    @Override // gnu.trove.map.TObjectDoubleMap
    public K[] keys(K[] array) {
        return this.m.keys(array);
    }

    @Override // gnu.trove.map.TObjectDoubleMap
    public TDoubleCollection valueCollection() {
        if (this.values == null) {
            this.values = TCollections.unmodifiableCollection(this.m.valueCollection());
        }
        return this.values;
    }

    @Override // gnu.trove.map.TObjectDoubleMap
    public double[] values() {
        return this.m.values();
    }

    @Override // gnu.trove.map.TObjectDoubleMap
    public double[] values(double[] array) {
        return this.m.values(array);
    }

    @Override // gnu.trove.map.TObjectDoubleMap
    public boolean equals(Object o) {
        return o == this || this.m.equals(o);
    }

    @Override // gnu.trove.map.TObjectDoubleMap
    public int hashCode() {
        return this.m.hashCode();
    }

    public String toString() {
        return this.m.toString();
    }

    @Override // gnu.trove.map.TObjectDoubleMap
    public double getNoEntryValue() {
        return this.m.getNoEntryValue();
    }

    @Override // gnu.trove.map.TObjectDoubleMap
    public boolean forEachKey(TObjectProcedure<? super K> procedure) {
        return this.m.forEachKey(procedure);
    }

    @Override // gnu.trove.map.TObjectDoubleMap
    public boolean forEachValue(TDoubleProcedure procedure) {
        return this.m.forEachValue(procedure);
    }

    @Override // gnu.trove.map.TObjectDoubleMap
    public boolean forEachEntry(TObjectDoubleProcedure<? super K> procedure) {
        return this.m.forEachEntry(procedure);
    }

    @Override // gnu.trove.map.TObjectDoubleMap
    public TObjectDoubleIterator<K> iterator() {
        return new TObjectDoubleIterator<K>() { // from class: gnu.trove.impl.unmodifiable.TUnmodifiableObjectDoubleMap.1
            TObjectDoubleIterator<K> iter;

            {
                this.iter = TUnmodifiableObjectDoubleMap.this.m.iterator();
            }

            @Override // gnu.trove.iterator.TObjectDoubleIterator
            public K key() {
                return this.iter.key();
            }

            @Override // gnu.trove.iterator.TObjectDoubleIterator
            public double value() {
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

            @Override // gnu.trove.iterator.TObjectDoubleIterator
            public double setValue(double val) {
                throw new UnsupportedOperationException();
            }

            @Override // gnu.trove.iterator.TIterator, java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override // gnu.trove.map.TObjectDoubleMap
    public double putIfAbsent(K key, double value) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TObjectDoubleMap
    public void transformValues(TDoubleFunction function) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TObjectDoubleMap
    public boolean retainEntries(TObjectDoubleProcedure<? super K> procedure) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TObjectDoubleMap
    public boolean increment(K key) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TObjectDoubleMap
    public boolean adjustValue(K key, double amount) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TObjectDoubleMap
    public double adjustOrPutValue(K key, double adjust_amount, double put_amount) {
        throw new UnsupportedOperationException();
    }
}
