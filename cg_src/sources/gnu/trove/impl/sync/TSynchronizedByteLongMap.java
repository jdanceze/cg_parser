package gnu.trove.impl.sync;

import gnu.trove.TLongCollection;
import gnu.trove.function.TLongFunction;
import gnu.trove.iterator.TByteLongIterator;
import gnu.trove.map.TByteLongMap;
import gnu.trove.procedure.TByteLongProcedure;
import gnu.trove.procedure.TByteProcedure;
import gnu.trove.procedure.TLongProcedure;
import gnu.trove.set.TByteSet;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/impl/sync/TSynchronizedByteLongMap.class */
public class TSynchronizedByteLongMap implements TByteLongMap, Serializable {
    private static final long serialVersionUID = 1978198479659022715L;
    private final TByteLongMap m;
    final Object mutex;
    private transient TByteSet keySet = null;
    private transient TLongCollection values = null;

    public TSynchronizedByteLongMap(TByteLongMap m) {
        if (m == null) {
            throw new NullPointerException();
        }
        this.m = m;
        this.mutex = this;
    }

    public TSynchronizedByteLongMap(TByteLongMap m, Object mutex) {
        this.m = m;
        this.mutex = mutex;
    }

    @Override // gnu.trove.map.TByteLongMap
    public int size() {
        int size;
        synchronized (this.mutex) {
            size = this.m.size();
        }
        return size;
    }

    @Override // gnu.trove.map.TByteLongMap
    public boolean isEmpty() {
        boolean isEmpty;
        synchronized (this.mutex) {
            isEmpty = this.m.isEmpty();
        }
        return isEmpty;
    }

    @Override // gnu.trove.map.TByteLongMap
    public boolean containsKey(byte key) {
        boolean containsKey;
        synchronized (this.mutex) {
            containsKey = this.m.containsKey(key);
        }
        return containsKey;
    }

    @Override // gnu.trove.map.TByteLongMap
    public boolean containsValue(long value) {
        boolean containsValue;
        synchronized (this.mutex) {
            containsValue = this.m.containsValue(value);
        }
        return containsValue;
    }

    @Override // gnu.trove.map.TByteLongMap
    public long get(byte key) {
        long j;
        synchronized (this.mutex) {
            j = this.m.get(key);
        }
        return j;
    }

    @Override // gnu.trove.map.TByteLongMap
    public long put(byte key, long value) {
        long put;
        synchronized (this.mutex) {
            put = this.m.put(key, value);
        }
        return put;
    }

    @Override // gnu.trove.map.TByteLongMap
    public long remove(byte key) {
        long remove;
        synchronized (this.mutex) {
            remove = this.m.remove(key);
        }
        return remove;
    }

    @Override // gnu.trove.map.TByteLongMap
    public void putAll(Map<? extends Byte, ? extends Long> map) {
        synchronized (this.mutex) {
            this.m.putAll(map);
        }
    }

    @Override // gnu.trove.map.TByteLongMap
    public void putAll(TByteLongMap map) {
        synchronized (this.mutex) {
            this.m.putAll(map);
        }
    }

    @Override // gnu.trove.map.TByteLongMap
    public void clear() {
        synchronized (this.mutex) {
            this.m.clear();
        }
    }

    @Override // gnu.trove.map.TByteLongMap
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

    @Override // gnu.trove.map.TByteLongMap
    public byte[] keys() {
        byte[] keys;
        synchronized (this.mutex) {
            keys = this.m.keys();
        }
        return keys;
    }

    @Override // gnu.trove.map.TByteLongMap
    public byte[] keys(byte[] array) {
        byte[] keys;
        synchronized (this.mutex) {
            keys = this.m.keys(array);
        }
        return keys;
    }

    @Override // gnu.trove.map.TByteLongMap
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

    @Override // gnu.trove.map.TByteLongMap
    public long[] values() {
        long[] values;
        synchronized (this.mutex) {
            values = this.m.values();
        }
        return values;
    }

    @Override // gnu.trove.map.TByteLongMap
    public long[] values(long[] array) {
        long[] values;
        synchronized (this.mutex) {
            values = this.m.values(array);
        }
        return values;
    }

    @Override // gnu.trove.map.TByteLongMap
    public TByteLongIterator iterator() {
        return this.m.iterator();
    }

    @Override // gnu.trove.map.TByteLongMap
    public byte getNoEntryKey() {
        return this.m.getNoEntryKey();
    }

    @Override // gnu.trove.map.TByteLongMap
    public long getNoEntryValue() {
        return this.m.getNoEntryValue();
    }

    @Override // gnu.trove.map.TByteLongMap
    public long putIfAbsent(byte key, long value) {
        long putIfAbsent;
        synchronized (this.mutex) {
            putIfAbsent = this.m.putIfAbsent(key, value);
        }
        return putIfAbsent;
    }

    @Override // gnu.trove.map.TByteLongMap
    public boolean forEachKey(TByteProcedure procedure) {
        boolean forEachKey;
        synchronized (this.mutex) {
            forEachKey = this.m.forEachKey(procedure);
        }
        return forEachKey;
    }

    @Override // gnu.trove.map.TByteLongMap
    public boolean forEachValue(TLongProcedure procedure) {
        boolean forEachValue;
        synchronized (this.mutex) {
            forEachValue = this.m.forEachValue(procedure);
        }
        return forEachValue;
    }

    @Override // gnu.trove.map.TByteLongMap
    public boolean forEachEntry(TByteLongProcedure procedure) {
        boolean forEachEntry;
        synchronized (this.mutex) {
            forEachEntry = this.m.forEachEntry(procedure);
        }
        return forEachEntry;
    }

    @Override // gnu.trove.map.TByteLongMap
    public void transformValues(TLongFunction function) {
        synchronized (this.mutex) {
            this.m.transformValues(function);
        }
    }

    @Override // gnu.trove.map.TByteLongMap
    public boolean retainEntries(TByteLongProcedure procedure) {
        boolean retainEntries;
        synchronized (this.mutex) {
            retainEntries = this.m.retainEntries(procedure);
        }
        return retainEntries;
    }

    @Override // gnu.trove.map.TByteLongMap
    public boolean increment(byte key) {
        boolean increment;
        synchronized (this.mutex) {
            increment = this.m.increment(key);
        }
        return increment;
    }

    @Override // gnu.trove.map.TByteLongMap
    public boolean adjustValue(byte key, long amount) {
        boolean adjustValue;
        synchronized (this.mutex) {
            adjustValue = this.m.adjustValue(key, amount);
        }
        return adjustValue;
    }

    @Override // gnu.trove.map.TByteLongMap
    public long adjustOrPutValue(byte key, long adjust_amount, long put_amount) {
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
