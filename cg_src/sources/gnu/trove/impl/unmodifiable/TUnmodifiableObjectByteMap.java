package gnu.trove.impl.unmodifiable;

import gnu.trove.TByteCollection;
import gnu.trove.TCollections;
import gnu.trove.function.TByteFunction;
import gnu.trove.iterator.TObjectByteIterator;
import gnu.trove.map.TObjectByteMap;
import gnu.trove.procedure.TByteProcedure;
import gnu.trove.procedure.TObjectByteProcedure;
import gnu.trove.procedure.TObjectProcedure;
import java.io.Serializable;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/impl/unmodifiable/TUnmodifiableObjectByteMap.class */
public class TUnmodifiableObjectByteMap<K> implements TObjectByteMap<K>, Serializable {
    private static final long serialVersionUID = -1034234728574286014L;
    private final TObjectByteMap<K> m;
    private transient Set<K> keySet = null;
    private transient TByteCollection values = null;

    public TUnmodifiableObjectByteMap(TObjectByteMap<K> m) {
        if (m == null) {
            throw new NullPointerException();
        }
        this.m = m;
    }

    @Override // gnu.trove.map.TObjectByteMap
    public int size() {
        return this.m.size();
    }

    @Override // gnu.trove.map.TObjectByteMap
    public boolean isEmpty() {
        return this.m.isEmpty();
    }

    @Override // gnu.trove.map.TObjectByteMap
    public boolean containsKey(Object key) {
        return this.m.containsKey(key);
    }

    @Override // gnu.trove.map.TObjectByteMap
    public boolean containsValue(byte val) {
        return this.m.containsValue(val);
    }

    @Override // gnu.trove.map.TObjectByteMap
    public byte get(Object key) {
        return this.m.get(key);
    }

    @Override // gnu.trove.map.TObjectByteMap
    public byte put(K key, byte value) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TObjectByteMap
    public byte remove(Object key) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TObjectByteMap
    public void putAll(TObjectByteMap<? extends K> m) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TObjectByteMap
    public void putAll(Map<? extends K, ? extends Byte> map) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TObjectByteMap
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TObjectByteMap
    public Set<K> keySet() {
        if (this.keySet == null) {
            this.keySet = Collections.unmodifiableSet(this.m.keySet());
        }
        return this.keySet;
    }

    @Override // gnu.trove.map.TObjectByteMap
    public Object[] keys() {
        return this.m.keys();
    }

    @Override // gnu.trove.map.TObjectByteMap
    public K[] keys(K[] array) {
        return this.m.keys(array);
    }

    @Override // gnu.trove.map.TObjectByteMap
    public TByteCollection valueCollection() {
        if (this.values == null) {
            this.values = TCollections.unmodifiableCollection(this.m.valueCollection());
        }
        return this.values;
    }

    @Override // gnu.trove.map.TObjectByteMap
    public byte[] values() {
        return this.m.values();
    }

    @Override // gnu.trove.map.TObjectByteMap
    public byte[] values(byte[] array) {
        return this.m.values(array);
    }

    @Override // gnu.trove.map.TObjectByteMap
    public boolean equals(Object o) {
        return o == this || this.m.equals(o);
    }

    @Override // gnu.trove.map.TObjectByteMap
    public int hashCode() {
        return this.m.hashCode();
    }

    public String toString() {
        return this.m.toString();
    }

    @Override // gnu.trove.map.TObjectByteMap
    public byte getNoEntryValue() {
        return this.m.getNoEntryValue();
    }

    @Override // gnu.trove.map.TObjectByteMap
    public boolean forEachKey(TObjectProcedure<? super K> procedure) {
        return this.m.forEachKey(procedure);
    }

    @Override // gnu.trove.map.TObjectByteMap
    public boolean forEachValue(TByteProcedure procedure) {
        return this.m.forEachValue(procedure);
    }

    @Override // gnu.trove.map.TObjectByteMap
    public boolean forEachEntry(TObjectByteProcedure<? super K> procedure) {
        return this.m.forEachEntry(procedure);
    }

    @Override // gnu.trove.map.TObjectByteMap
    public TObjectByteIterator<K> iterator() {
        return new TObjectByteIterator<K>() { // from class: gnu.trove.impl.unmodifiable.TUnmodifiableObjectByteMap.1
            TObjectByteIterator<K> iter;

            {
                this.iter = TUnmodifiableObjectByteMap.this.m.iterator();
            }

            @Override // gnu.trove.iterator.TObjectByteIterator
            public K key() {
                return this.iter.key();
            }

            @Override // gnu.trove.iterator.TObjectByteIterator
            public byte value() {
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

            @Override // gnu.trove.iterator.TObjectByteIterator
            public byte setValue(byte val) {
                throw new UnsupportedOperationException();
            }

            @Override // gnu.trove.iterator.TIterator, java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override // gnu.trove.map.TObjectByteMap
    public byte putIfAbsent(K key, byte value) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TObjectByteMap
    public void transformValues(TByteFunction function) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TObjectByteMap
    public boolean retainEntries(TObjectByteProcedure<? super K> procedure) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TObjectByteMap
    public boolean increment(K key) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TObjectByteMap
    public boolean adjustValue(K key, byte amount) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TObjectByteMap
    public byte adjustOrPutValue(K key, byte adjust_amount, byte put_amount) {
        throw new UnsupportedOperationException();
    }
}
