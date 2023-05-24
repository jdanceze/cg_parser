package gnu.trove.impl.unmodifiable;

import gnu.trove.TCollections;
import gnu.trove.TFloatCollection;
import gnu.trove.function.TFloatFunction;
import gnu.trove.iterator.TByteFloatIterator;
import gnu.trove.map.TByteFloatMap;
import gnu.trove.procedure.TByteFloatProcedure;
import gnu.trove.procedure.TByteProcedure;
import gnu.trove.procedure.TFloatProcedure;
import gnu.trove.set.TByteSet;
import java.io.Serializable;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/impl/unmodifiable/TUnmodifiableByteFloatMap.class */
public class TUnmodifiableByteFloatMap implements TByteFloatMap, Serializable {
    private static final long serialVersionUID = -1034234728574286014L;
    private final TByteFloatMap m;
    private transient TByteSet keySet = null;
    private transient TFloatCollection values = null;

    public TUnmodifiableByteFloatMap(TByteFloatMap m) {
        if (m == null) {
            throw new NullPointerException();
        }
        this.m = m;
    }

    @Override // gnu.trove.map.TByteFloatMap
    public int size() {
        return this.m.size();
    }

    @Override // gnu.trove.map.TByteFloatMap
    public boolean isEmpty() {
        return this.m.isEmpty();
    }

    @Override // gnu.trove.map.TByteFloatMap
    public boolean containsKey(byte key) {
        return this.m.containsKey(key);
    }

    @Override // gnu.trove.map.TByteFloatMap
    public boolean containsValue(float val) {
        return this.m.containsValue(val);
    }

    @Override // gnu.trove.map.TByteFloatMap
    public float get(byte key) {
        return this.m.get(key);
    }

    @Override // gnu.trove.map.TByteFloatMap
    public float put(byte key, float value) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TByteFloatMap
    public float remove(byte key) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TByteFloatMap
    public void putAll(TByteFloatMap m) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TByteFloatMap
    public void putAll(Map<? extends Byte, ? extends Float> map) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TByteFloatMap
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TByteFloatMap
    public TByteSet keySet() {
        if (this.keySet == null) {
            this.keySet = TCollections.unmodifiableSet(this.m.keySet());
        }
        return this.keySet;
    }

    @Override // gnu.trove.map.TByteFloatMap
    public byte[] keys() {
        return this.m.keys();
    }

    @Override // gnu.trove.map.TByteFloatMap
    public byte[] keys(byte[] array) {
        return this.m.keys(array);
    }

    @Override // gnu.trove.map.TByteFloatMap
    public TFloatCollection valueCollection() {
        if (this.values == null) {
            this.values = TCollections.unmodifiableCollection(this.m.valueCollection());
        }
        return this.values;
    }

    @Override // gnu.trove.map.TByteFloatMap
    public float[] values() {
        return this.m.values();
    }

    @Override // gnu.trove.map.TByteFloatMap
    public float[] values(float[] array) {
        return this.m.values(array);
    }

    public boolean equals(Object o) {
        return o == this || this.m.equals(o);
    }

    public int hashCode() {
        return this.m.hashCode();
    }

    public String toString() {
        return this.m.toString();
    }

    @Override // gnu.trove.map.TByteFloatMap
    public byte getNoEntryKey() {
        return this.m.getNoEntryKey();
    }

    @Override // gnu.trove.map.TByteFloatMap
    public float getNoEntryValue() {
        return this.m.getNoEntryValue();
    }

    @Override // gnu.trove.map.TByteFloatMap
    public boolean forEachKey(TByteProcedure procedure) {
        return this.m.forEachKey(procedure);
    }

    @Override // gnu.trove.map.TByteFloatMap
    public boolean forEachValue(TFloatProcedure procedure) {
        return this.m.forEachValue(procedure);
    }

    @Override // gnu.trove.map.TByteFloatMap
    public boolean forEachEntry(TByteFloatProcedure procedure) {
        return this.m.forEachEntry(procedure);
    }

    @Override // gnu.trove.map.TByteFloatMap
    public TByteFloatIterator iterator() {
        return new TByteFloatIterator() { // from class: gnu.trove.impl.unmodifiable.TUnmodifiableByteFloatMap.1
            TByteFloatIterator iter;

            {
                this.iter = TUnmodifiableByteFloatMap.this.m.iterator();
            }

            @Override // gnu.trove.iterator.TByteFloatIterator
            public byte key() {
                return this.iter.key();
            }

            @Override // gnu.trove.iterator.TByteFloatIterator
            public float value() {
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

            @Override // gnu.trove.iterator.TByteFloatIterator
            public float setValue(float val) {
                throw new UnsupportedOperationException();
            }

            @Override // gnu.trove.iterator.TIterator, java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override // gnu.trove.map.TByteFloatMap
    public float putIfAbsent(byte key, float value) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TByteFloatMap
    public void transformValues(TFloatFunction function) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TByteFloatMap
    public boolean retainEntries(TByteFloatProcedure procedure) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TByteFloatMap
    public boolean increment(byte key) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TByteFloatMap
    public boolean adjustValue(byte key, float amount) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TByteFloatMap
    public float adjustOrPutValue(byte key, float adjust_amount, float put_amount) {
        throw new UnsupportedOperationException();
    }
}
