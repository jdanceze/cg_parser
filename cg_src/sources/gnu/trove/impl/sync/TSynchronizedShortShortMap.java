package gnu.trove.impl.sync;

import gnu.trove.TShortCollection;
import gnu.trove.function.TShortFunction;
import gnu.trove.iterator.TShortShortIterator;
import gnu.trove.map.TShortShortMap;
import gnu.trove.procedure.TShortProcedure;
import gnu.trove.procedure.TShortShortProcedure;
import gnu.trove.set.TShortSet;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/impl/sync/TSynchronizedShortShortMap.class */
public class TSynchronizedShortShortMap implements TShortShortMap, Serializable {
    private static final long serialVersionUID = 1978198479659022715L;
    private final TShortShortMap m;
    final Object mutex;
    private transient TShortSet keySet = null;
    private transient TShortCollection values = null;

    public TSynchronizedShortShortMap(TShortShortMap m) {
        if (m == null) {
            throw new NullPointerException();
        }
        this.m = m;
        this.mutex = this;
    }

    public TSynchronizedShortShortMap(TShortShortMap m, Object mutex) {
        this.m = m;
        this.mutex = mutex;
    }

    @Override // gnu.trove.map.TShortShortMap
    public int size() {
        int size;
        synchronized (this.mutex) {
            size = this.m.size();
        }
        return size;
    }

    @Override // gnu.trove.map.TShortShortMap
    public boolean isEmpty() {
        boolean isEmpty;
        synchronized (this.mutex) {
            isEmpty = this.m.isEmpty();
        }
        return isEmpty;
    }

    @Override // gnu.trove.map.TShortShortMap
    public boolean containsKey(short key) {
        boolean containsKey;
        synchronized (this.mutex) {
            containsKey = this.m.containsKey(key);
        }
        return containsKey;
    }

    @Override // gnu.trove.map.TShortShortMap
    public boolean containsValue(short value) {
        boolean containsValue;
        synchronized (this.mutex) {
            containsValue = this.m.containsValue(value);
        }
        return containsValue;
    }

    @Override // gnu.trove.map.TShortShortMap
    public short get(short key) {
        short s;
        synchronized (this.mutex) {
            s = this.m.get(key);
        }
        return s;
    }

    @Override // gnu.trove.map.TShortShortMap
    public short put(short key, short value) {
        short put;
        synchronized (this.mutex) {
            put = this.m.put(key, value);
        }
        return put;
    }

    @Override // gnu.trove.map.TShortShortMap
    public short remove(short key) {
        short remove;
        synchronized (this.mutex) {
            remove = this.m.remove(key);
        }
        return remove;
    }

    @Override // gnu.trove.map.TShortShortMap
    public void putAll(Map<? extends Short, ? extends Short> map) {
        synchronized (this.mutex) {
            this.m.putAll(map);
        }
    }

    @Override // gnu.trove.map.TShortShortMap
    public void putAll(TShortShortMap map) {
        synchronized (this.mutex) {
            this.m.putAll(map);
        }
    }

    @Override // gnu.trove.map.TShortShortMap
    public void clear() {
        synchronized (this.mutex) {
            this.m.clear();
        }
    }

    @Override // gnu.trove.map.TShortShortMap
    public TShortSet keySet() {
        TShortSet tShortSet;
        synchronized (this.mutex) {
            if (this.keySet == null) {
                this.keySet = new TSynchronizedShortSet(this.m.keySet(), this.mutex);
            }
            tShortSet = this.keySet;
        }
        return tShortSet;
    }

    @Override // gnu.trove.map.TShortShortMap
    public short[] keys() {
        short[] keys;
        synchronized (this.mutex) {
            keys = this.m.keys();
        }
        return keys;
    }

    @Override // gnu.trove.map.TShortShortMap
    public short[] keys(short[] array) {
        short[] keys;
        synchronized (this.mutex) {
            keys = this.m.keys(array);
        }
        return keys;
    }

    @Override // gnu.trove.map.TShortShortMap
    public TShortCollection valueCollection() {
        TShortCollection tShortCollection;
        synchronized (this.mutex) {
            if (this.values == null) {
                this.values = new TSynchronizedShortCollection(this.m.valueCollection(), this.mutex);
            }
            tShortCollection = this.values;
        }
        return tShortCollection;
    }

    @Override // gnu.trove.map.TShortShortMap
    public short[] values() {
        short[] values;
        synchronized (this.mutex) {
            values = this.m.values();
        }
        return values;
    }

    @Override // gnu.trove.map.TShortShortMap
    public short[] values(short[] array) {
        short[] values;
        synchronized (this.mutex) {
            values = this.m.values(array);
        }
        return values;
    }

    @Override // gnu.trove.map.TShortShortMap
    public TShortShortIterator iterator() {
        return this.m.iterator();
    }

    @Override // gnu.trove.map.TShortShortMap
    public short getNoEntryKey() {
        return this.m.getNoEntryKey();
    }

    @Override // gnu.trove.map.TShortShortMap
    public short getNoEntryValue() {
        return this.m.getNoEntryValue();
    }

    @Override // gnu.trove.map.TShortShortMap
    public short putIfAbsent(short key, short value) {
        short putIfAbsent;
        synchronized (this.mutex) {
            putIfAbsent = this.m.putIfAbsent(key, value);
        }
        return putIfAbsent;
    }

    @Override // gnu.trove.map.TShortShortMap
    public boolean forEachKey(TShortProcedure procedure) {
        boolean forEachKey;
        synchronized (this.mutex) {
            forEachKey = this.m.forEachKey(procedure);
        }
        return forEachKey;
    }

    @Override // gnu.trove.map.TShortShortMap
    public boolean forEachValue(TShortProcedure procedure) {
        boolean forEachValue;
        synchronized (this.mutex) {
            forEachValue = this.m.forEachValue(procedure);
        }
        return forEachValue;
    }

    @Override // gnu.trove.map.TShortShortMap
    public boolean forEachEntry(TShortShortProcedure procedure) {
        boolean forEachEntry;
        synchronized (this.mutex) {
            forEachEntry = this.m.forEachEntry(procedure);
        }
        return forEachEntry;
    }

    @Override // gnu.trove.map.TShortShortMap
    public void transformValues(TShortFunction function) {
        synchronized (this.mutex) {
            this.m.transformValues(function);
        }
    }

    @Override // gnu.trove.map.TShortShortMap
    public boolean retainEntries(TShortShortProcedure procedure) {
        boolean retainEntries;
        synchronized (this.mutex) {
            retainEntries = this.m.retainEntries(procedure);
        }
        return retainEntries;
    }

    @Override // gnu.trove.map.TShortShortMap
    public boolean increment(short key) {
        boolean increment;
        synchronized (this.mutex) {
            increment = this.m.increment(key);
        }
        return increment;
    }

    @Override // gnu.trove.map.TShortShortMap
    public boolean adjustValue(short key, short amount) {
        boolean adjustValue;
        synchronized (this.mutex) {
            adjustValue = this.m.adjustValue(key, amount);
        }
        return adjustValue;
    }

    @Override // gnu.trove.map.TShortShortMap
    public short adjustOrPutValue(short key, short adjust_amount, short put_amount) {
        short adjustOrPutValue;
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
