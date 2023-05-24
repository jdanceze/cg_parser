package gnu.trove.impl.sync;

import gnu.trove.function.TObjectFunction;
import gnu.trove.iterator.TByteObjectIterator;
import gnu.trove.map.TByteObjectMap;
import gnu.trove.procedure.TByteObjectProcedure;
import gnu.trove.procedure.TByteProcedure;
import gnu.trove.procedure.TObjectProcedure;
import gnu.trove.set.TByteSet;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/impl/sync/TSynchronizedByteObjectMap.class */
public class TSynchronizedByteObjectMap<V> implements TByteObjectMap<V>, Serializable {
    private static final long serialVersionUID = 1978198479659022715L;
    private final TByteObjectMap<V> m;
    final Object mutex;
    private transient TByteSet keySet = null;
    private transient Collection<V> values = null;

    public TSynchronizedByteObjectMap(TByteObjectMap<V> m) {
        if (m == null) {
            throw new NullPointerException();
        }
        this.m = m;
        this.mutex = this;
    }

    public TSynchronizedByteObjectMap(TByteObjectMap<V> m, Object mutex) {
        this.m = m;
        this.mutex = mutex;
    }

    @Override // gnu.trove.map.TByteObjectMap
    public int size() {
        int size;
        synchronized (this.mutex) {
            size = this.m.size();
        }
        return size;
    }

    @Override // gnu.trove.map.TByteObjectMap
    public boolean isEmpty() {
        boolean isEmpty;
        synchronized (this.mutex) {
            isEmpty = this.m.isEmpty();
        }
        return isEmpty;
    }

    @Override // gnu.trove.map.TByteObjectMap
    public boolean containsKey(byte key) {
        boolean containsKey;
        synchronized (this.mutex) {
            containsKey = this.m.containsKey(key);
        }
        return containsKey;
    }

    @Override // gnu.trove.map.TByteObjectMap
    public boolean containsValue(Object value) {
        boolean containsValue;
        synchronized (this.mutex) {
            containsValue = this.m.containsValue(value);
        }
        return containsValue;
    }

    @Override // gnu.trove.map.TByteObjectMap
    public V get(byte key) {
        V v;
        synchronized (this.mutex) {
            v = this.m.get(key);
        }
        return v;
    }

    @Override // gnu.trove.map.TByteObjectMap
    public V put(byte key, V value) {
        V put;
        synchronized (this.mutex) {
            put = this.m.put(key, value);
        }
        return put;
    }

    @Override // gnu.trove.map.TByteObjectMap
    public V remove(byte key) {
        V remove;
        synchronized (this.mutex) {
            remove = this.m.remove(key);
        }
        return remove;
    }

    @Override // gnu.trove.map.TByteObjectMap
    public void putAll(Map<? extends Byte, ? extends V> map) {
        synchronized (this.mutex) {
            this.m.putAll(map);
        }
    }

    @Override // gnu.trove.map.TByteObjectMap
    public void putAll(TByteObjectMap<? extends V> map) {
        synchronized (this.mutex) {
            this.m.putAll(map);
        }
    }

    @Override // gnu.trove.map.TByteObjectMap
    public void clear() {
        synchronized (this.mutex) {
            this.m.clear();
        }
    }

    @Override // gnu.trove.map.TByteObjectMap
    public TByteSet keySet() {
        TByteSet tByteSet;
        synchronized (this.mutex) {
            if (this.keySet == null) {
                this.keySet = new TSynchronizedByteSet(this.m.keySet(), this.mutex);
            }
            tByteSet = this.keySet;
        }
        return tByteSet;
    }

    @Override // gnu.trove.map.TByteObjectMap
    public byte[] keys() {
        byte[] keys;
        synchronized (this.mutex) {
            keys = this.m.keys();
        }
        return keys;
    }

    @Override // gnu.trove.map.TByteObjectMap
    public byte[] keys(byte[] array) {
        byte[] keys;
        synchronized (this.mutex) {
            keys = this.m.keys(array);
        }
        return keys;
    }

    @Override // gnu.trove.map.TByteObjectMap
    public Collection<V> valueCollection() {
        Collection<V> collection;
        synchronized (this.mutex) {
            if (this.values == null) {
                this.values = new SynchronizedCollection(this.m.valueCollection(), this.mutex);
            }
            collection = this.values;
        }
        return collection;
    }

    @Override // gnu.trove.map.TByteObjectMap
    public Object[] values() {
        Object[] values;
        synchronized (this.mutex) {
            values = this.m.values();
        }
        return values;
    }

    @Override // gnu.trove.map.TByteObjectMap
    public V[] values(V[] array) {
        V[] values;
        synchronized (this.mutex) {
            values = this.m.values(array);
        }
        return values;
    }

    @Override // gnu.trove.map.TByteObjectMap
    public TByteObjectIterator<V> iterator() {
        return this.m.iterator();
    }

    @Override // gnu.trove.map.TByteObjectMap
    public byte getNoEntryKey() {
        return this.m.getNoEntryKey();
    }

    @Override // gnu.trove.map.TByteObjectMap
    public V putIfAbsent(byte key, V value) {
        V putIfAbsent;
        synchronized (this.mutex) {
            putIfAbsent = this.m.putIfAbsent(key, value);
        }
        return putIfAbsent;
    }

    @Override // gnu.trove.map.TByteObjectMap
    public boolean forEachKey(TByteProcedure procedure) {
        boolean forEachKey;
        synchronized (this.mutex) {
            forEachKey = this.m.forEachKey(procedure);
        }
        return forEachKey;
    }

    @Override // gnu.trove.map.TByteObjectMap
    public boolean forEachValue(TObjectProcedure<? super V> procedure) {
        boolean forEachValue;
        synchronized (this.mutex) {
            forEachValue = this.m.forEachValue(procedure);
        }
        return forEachValue;
    }

    @Override // gnu.trove.map.TByteObjectMap
    public boolean forEachEntry(TByteObjectProcedure<? super V> procedure) {
        boolean forEachEntry;
        synchronized (this.mutex) {
            forEachEntry = this.m.forEachEntry(procedure);
        }
        return forEachEntry;
    }

    @Override // gnu.trove.map.TByteObjectMap
    public void transformValues(TObjectFunction<V, V> function) {
        synchronized (this.mutex) {
            this.m.transformValues(function);
        }
    }

    @Override // gnu.trove.map.TByteObjectMap
    public boolean retainEntries(TByteObjectProcedure<? super V> procedure) {
        boolean retainEntries;
        synchronized (this.mutex) {
            retainEntries = this.m.retainEntries(procedure);
        }
        return retainEntries;
    }

    @Override // gnu.trove.map.TByteObjectMap
    public boolean equals(Object o) {
        boolean equals;
        synchronized (this.mutex) {
            equals = this.m.equals(o);
        }
        return equals;
    }

    @Override // gnu.trove.map.TByteObjectMap
    public int hashCode() {
        int hashCode;
        synchronized (this.mutex) {
            hashCode = this.m.hashCode();
        }
        return hashCode;
    }

    public String toString() {
        String obj;
        synchronized (this.mutex) {
            obj = this.m.toString();
        }
        return obj;
    }

    private void writeObject(ObjectOutputStream s) throws IOException {
        synchronized (this.mutex) {
            s.defaultWriteObject();
        }
    }
}
