package gnu.trove.impl.sync;

import gnu.trove.TFloatCollection;
import gnu.trove.function.TFloatFunction;
import gnu.trove.iterator.TDoubleFloatIterator;
import gnu.trove.map.TDoubleFloatMap;
import gnu.trove.procedure.TDoubleFloatProcedure;
import gnu.trove.procedure.TDoubleProcedure;
import gnu.trove.procedure.TFloatProcedure;
import gnu.trove.set.TDoubleSet;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/impl/sync/TSynchronizedDoubleFloatMap.class */
public class TSynchronizedDoubleFloatMap implements TDoubleFloatMap, Serializable {
    private static final long serialVersionUID = 1978198479659022715L;
    private final TDoubleFloatMap m;
    final Object mutex;
    private transient TDoubleSet keySet = null;
    private transient TFloatCollection values = null;

    public TSynchronizedDoubleFloatMap(TDoubleFloatMap m) {
        if (m == null) {
            throw new NullPointerException();
        }
        this.m = m;
        this.mutex = this;
    }

    public TSynchronizedDoubleFloatMap(TDoubleFloatMap m, Object mutex) {
        this.m = m;
        this.mutex = mutex;
    }

    @Override // gnu.trove.map.TDoubleFloatMap
    public int size() {
        int size;
        synchronized (this.mutex) {
            size = this.m.size();
        }
        return size;
    }

    @Override // gnu.trove.map.TDoubleFloatMap
    public boolean isEmpty() {
        boolean isEmpty;
        synchronized (this.mutex) {
            isEmpty = this.m.isEmpty();
        }
        return isEmpty;
    }

    @Override // gnu.trove.map.TDoubleFloatMap
    public boolean containsKey(double key) {
        boolean containsKey;
        synchronized (this.mutex) {
            containsKey = this.m.containsKey(key);
        }
        return containsKey;
    }

    @Override // gnu.trove.map.TDoubleFloatMap
    public boolean containsValue(float value) {
        boolean containsValue;
        synchronized (this.mutex) {
            containsValue = this.m.containsValue(value);
        }
        return containsValue;
    }

    @Override // gnu.trove.map.TDoubleFloatMap
    public float get(double key) {
        float f;
        synchronized (this.mutex) {
            f = this.m.get(key);
        }
        return f;
    }

    @Override // gnu.trove.map.TDoubleFloatMap
    public float put(double key, float value) {
        float put;
        synchronized (this.mutex) {
            put = this.m.put(key, value);
        }
        return put;
    }

    @Override // gnu.trove.map.TDoubleFloatMap
    public float remove(double key) {
        float remove;
        synchronized (this.mutex) {
            remove = this.m.remove(key);
        }
        return remove;
    }

    @Override // gnu.trove.map.TDoubleFloatMap
    public void putAll(Map<? extends Double, ? extends Float> map) {
        synchronized (this.mutex) {
            this.m.putAll(map);
        }
    }

    @Override // gnu.trove.map.TDoubleFloatMap
    public void putAll(TDoubleFloatMap map) {
        synchronized (this.mutex) {
            this.m.putAll(map);
        }
    }

    @Override // gnu.trove.map.TDoubleFloatMap
    public void clear() {
        synchronized (this.mutex) {
            this.m.clear();
        }
    }

    @Override // gnu.trove.map.TDoubleFloatMap
    public TDoubleSet keySet() {
        TDoubleSet tDoubleSet;
        synchronized (this.mutex) {
            if (this.keySet == null) {
                this.keySet = new TSynchronizedDoubleSet(this.m.keySet(), this.mutex);
            }
            tDoubleSet = this.keySet;
        }
        return tDoubleSet;
    }

    @Override // gnu.trove.map.TDoubleFloatMap
    public double[] keys() {
        double[] keys;
        synchronized (this.mutex) {
            keys = this.m.keys();
        }
        return keys;
    }

    @Override // gnu.trove.map.TDoubleFloatMap
    public double[] keys(double[] array) {
        double[] keys;
        synchronized (this.mutex) {
            keys = this.m.keys(array);
        }
        return keys;
    }

    @Override // gnu.trove.map.TDoubleFloatMap
    public TFloatCollection valueCollection() {
        TFloatCollection tFloatCollection;
        synchronized (this.mutex) {
            if (this.values == null) {
                this.values = new TSynchronizedFloatCollection(this.m.valueCollection(), this.mutex);
            }
            tFloatCollection = this.values;
        }
        return tFloatCollection;
    }

    @Override // gnu.trove.map.TDoubleFloatMap
    public float[] values() {
        float[] values;
        synchronized (this.mutex) {
            values = this.m.values();
        }
        return values;
    }

    @Override // gnu.trove.map.TDoubleFloatMap
    public float[] values(float[] array) {
        float[] values;
        synchronized (this.mutex) {
            values = this.m.values(array);
        }
        return values;
    }

    @Override // gnu.trove.map.TDoubleFloatMap
    public TDoubleFloatIterator iterator() {
        return this.m.iterator();
    }

    @Override // gnu.trove.map.TDoubleFloatMap
    public double getNoEntryKey() {
        return this.m.getNoEntryKey();
    }

    @Override // gnu.trove.map.TDoubleFloatMap
    public float getNoEntryValue() {
        return this.m.getNoEntryValue();
    }

    @Override // gnu.trove.map.TDoubleFloatMap
    public float putIfAbsent(double key, float value) {
        float putIfAbsent;
        synchronized (this.mutex) {
            putIfAbsent = this.m.putIfAbsent(key, value);
        }
        return putIfAbsent;
    }

    @Override // gnu.trove.map.TDoubleFloatMap
    public boolean forEachKey(TDoubleProcedure procedure) {
        boolean forEachKey;
        synchronized (this.mutex) {
            forEachKey = this.m.forEachKey(procedure);
        }
        return forEachKey;
    }

    @Override // gnu.trove.map.TDoubleFloatMap
    public boolean forEachValue(TFloatProcedure procedure) {
        boolean forEachValue;
        synchronized (this.mutex) {
            forEachValue = this.m.forEachValue(procedure);
        }
        return forEachValue;
    }

    @Override // gnu.trove.map.TDoubleFloatMap
    public boolean forEachEntry(TDoubleFloatProcedure procedure) {
        boolean forEachEntry;
        synchronized (this.mutex) {
            forEachEntry = this.m.forEachEntry(procedure);
        }
        return forEachEntry;
    }

    @Override // gnu.trove.map.TDoubleFloatMap
    public void transformValues(TFloatFunction function) {
        synchronized (this.mutex) {
            this.m.transformValues(function);
        }
    }

    @Override // gnu.trove.map.TDoubleFloatMap
    public boolean retainEntries(TDoubleFloatProcedure procedure) {
        boolean retainEntries;
        synchronized (this.mutex) {
            retainEntries = this.m.retainEntries(procedure);
        }
        return retainEntries;
    }

    @Override // gnu.trove.map.TDoubleFloatMap
    public boolean increment(double key) {
        boolean increment;
        synchronized (this.mutex) {
            increment = this.m.increment(key);
        }
        return increment;
    }

    @Override // gnu.trove.map.TDoubleFloatMap
    public boolean adjustValue(double key, float amount) {
        boolean adjustValue;
        synchronized (this.mutex) {
            adjustValue = this.m.adjustValue(key, amount);
        }
        return adjustValue;
    }

    @Override // gnu.trove.map.TDoubleFloatMap
    public float adjustOrPutValue(double key, float adjust_amount, float put_amount) {
        float adjustOrPutValue;
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
