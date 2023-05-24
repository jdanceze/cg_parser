package gnu.trove.impl.sync;

import gnu.trove.TLongCollection;
import gnu.trove.function.TLongFunction;
import gnu.trove.iterator.TLongLongIterator;
import gnu.trove.map.TLongLongMap;
import gnu.trove.procedure.TLongLongProcedure;
import gnu.trove.procedure.TLongProcedure;
import gnu.trove.set.TLongSet;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/impl/sync/TSynchronizedLongLongMap.class */
public class TSynchronizedLongLongMap implements TLongLongMap, Serializable {
    private static final long serialVersionUID = 1978198479659022715L;
    private final TLongLongMap m;
    final Object mutex;
    private transient TLongSet keySet = null;
    private transient TLongCollection values = null;

    public TSynchronizedLongLongMap(TLongLongMap m) {
        if (m == null) {
            throw new NullPointerException();
        }
        this.m = m;
        this.mutex = this;
    }

    public TSynchronizedLongLongMap(TLongLongMap m, Object mutex) {
        this.m = m;
        this.mutex = mutex;
    }

    @Override // gnu.trove.map.TLongLongMap
    public int size() {
        int size;
        synchronized (this.mutex) {
            size = this.m.size();
        }
        return size;
    }

    @Override // gnu.trove.map.TLongLongMap
    public boolean isEmpty() {
        boolean isEmpty;
        synchronized (this.mutex) {
            isEmpty = this.m.isEmpty();
        }
        return isEmpty;
    }

    @Override // gnu.trove.map.TLongLongMap
    public boolean containsKey(long key) {
        boolean containsKey;
        synchronized (this.mutex) {
            containsKey = this.m.containsKey(key);
        }
        return containsKey;
    }

    @Override // gnu.trove.map.TLongLongMap
    public boolean containsValue(long value) {
        boolean containsValue;
        synchronized (this.mutex) {
            containsValue = this.m.containsValue(value);
        }
        return containsValue;
    }

    @Override // gnu.trove.map.TLongLongMap
    public long get(long key) {
        long j;
        synchronized (this.mutex) {
            j = this.m.get(key);
        }
        return j;
    }

    @Override // gnu.trove.map.TLongLongMap
    public long put(long key, long value) {
        long put;
        synchronized (this.mutex) {
            put = this.m.put(key, value);
        }
        return put;
    }

    @Override // gnu.trove.map.TLongLongMap
    public long remove(long key) {
        long remove;
        synchronized (this.mutex) {
            remove = this.m.remove(key);
        }
        return remove;
    }

    @Override // gnu.trove.map.TLongLongMap
    public void putAll(Map<? extends Long, ? extends Long> map) {
        synchronized (this.mutex) {
            this.m.putAll(map);
        }
    }

    @Override // gnu.trove.map.TLongLongMap
    public void putAll(TLongLongMap map) {
        synchronized (this.mutex) {
            this.m.putAll(map);
        }
    }

    @Override // gnu.trove.map.TLongLongMap
    public void clear() {
        synchronized (this.mutex) {
            this.m.clear();
        }
    }

    @Override // gnu.trove.map.TLongLongMap
    public TLongSet keySet() {
        TLongSet tLongSet;
        synchronized (this.mutex) {
            if (this.keySet == null) {
                this.keySet = new TSynchronizedLongSet(this.m.keySet(), this.mutex);
            }
            tLongSet = this.keySet;
        }
        return tLongSet;
    }

    @Override // gnu.trove.map.TLongLongMap
    public long[] keys() {
        long[] keys;
        synchronized (this.mutex) {
            keys = this.m.keys();
        }
        return keys;
    }

    @Override // gnu.trove.map.TLongLongMap
    public long[] keys(long[] array) {
        long[] keys;
        synchronized (this.mutex) {
            keys = this.m.keys(array);
        }
        return keys;
    }

    @Override // gnu.trove.map.TLongLongMap
    public TLongCollection valueCollection() {
        TLongCollection tLongCollection;
        synchronized (this.mutex) {
            if (this.values == null) {
                this.values = new TSynchronizedLongCollection(this.m.valueCollection(), this.mutex);
            }
            tLongCollection = this.values;
        }
        return tLongCollection;
    }

    @Override // gnu.trove.map.TLongLongMap
    public long[] values() {
        long[] values;
        synchronized (this.mutex) {
            values = this.m.values();
        }
        return values;
    }

    @Override // gnu.trove.map.TLongLongMap
    public long[] values(long[] array) {
        long[] values;
        synchronized (this.mutex) {
            values = this.m.values(array);
        }
        return values;
    }

    @Override // gnu.trove.map.TLongLongMap
    public TLongLongIterator iterator() {
        return this.m.iterator();
    }

    @Override // gnu.trove.map.TLongLongMap
    public long getNoEntryKey() {
        return this.m.getNoEntryKey();
    }

    @Override // gnu.trove.map.TLongLongMap
    public long getNoEntryValue() {
        return this.m.getNoEntryValue();
    }

    @Override // gnu.trove.map.TLongLongMap
    public long putIfAbsent(long key, long value) {
        long putIfAbsent;
        synchronized (this.mutex) {
            putIfAbsent = this.m.putIfAbsent(key, value);
        }
        return putIfAbsent;
    }

    @Override // gnu.trove.map.TLongLongMap
    public boolean forEachKey(TLongProcedure procedure) {
        boolean forEachKey;
        synchronized (this.mutex) {
            forEachKey = this.m.forEachKey(procedure);
        }
        return forEachKey;
    }

    @Override // gnu.trove.map.TLongLongMap
    public boolean forEachValue(TLongProcedure procedure) {
        boolean forEachValue;
        synchronized (this.mutex) {
            forEachValue = this.m.forEachValue(procedure);
        }
        return forEachValue;
    }

    @Override // gnu.trove.map.TLongLongMap
    public boolean forEachEntry(TLongLongProcedure procedure) {
        boolean forEachEntry;
        synchronized (this.mutex) {
            forEachEntry = this.m.forEachEntry(procedure);
        }
        return forEachEntry;
    }

    @Override // gnu.trove.map.TLongLongMap
    public void transformValues(TLongFunction function) {
        synchronized (this.mutex) {
            this.m.transformValues(function);
        }
    }

    @Override // gnu.trove.map.TLongLongMap
    public boolean retainEntries(TLongLongProcedure procedure) {
        boolean retainEntries;
        synchronized (this.mutex) {
            retainEntries = this.m.retainEntries(procedure);
        }
        return retainEntries;
    }

    @Override // gnu.trove.map.TLongLongMap
    public boolean increment(long key) {
        boolean increment;
        synchronized (this.mutex) {
            increment = this.m.increment(key);
        }
        return increment;
    }

    @Override // gnu.trove.map.TLongLongMap
    public boolean adjustValue(long key, long amount) {
        boolean adjustValue;
        synchronized (this.mutex) {
            adjustValue = this.m.adjustValue(key, amount);
        }
        return adjustValue;
    }

    @Override // gnu.trove.map.TLongLongMap
    public long adjustOrPutValue(long key, long adjust_amount, long put_amount) {
        long adjustOrPutValue;
        synchronized (this.mutex) {
            adjustOrPutValue = this.m.adjustOrPutValue(key, adjust_amount, put_amount);
        }
        return adjustOrPutValue;
    }

    public boolean equals(Object o) {
        boolean equals;
        synchronized (this.mutex) {
            equals = this.m.equals(o);
        }
        return equals;
    }

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
