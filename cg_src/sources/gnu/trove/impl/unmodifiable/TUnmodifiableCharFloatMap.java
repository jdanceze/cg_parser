package gnu.trove.impl.unmodifiable;

import gnu.trove.TCollections;
import gnu.trove.TFloatCollection;
import gnu.trove.function.TFloatFunction;
import gnu.trove.iterator.TCharFloatIterator;
import gnu.trove.map.TCharFloatMap;
import gnu.trove.procedure.TCharFloatProcedure;
import gnu.trove.procedure.TCharProcedure;
import gnu.trove.procedure.TFloatProcedure;
import gnu.trove.set.TCharSet;
import java.io.Serializable;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/impl/unmodifiable/TUnmodifiableCharFloatMap.class */
public class TUnmodifiableCharFloatMap implements TCharFloatMap, Serializable {
    private static final long serialVersionUID = -1034234728574286014L;
    private final TCharFloatMap m;
    private transient TCharSet keySet = null;
    private transient TFloatCollection values = null;

    public TUnmodifiableCharFloatMap(TCharFloatMap m) {
        if (m == null) {
            throw new NullPointerException();
        }
        this.m = m;
    }

    @Override // gnu.trove.map.TCharFloatMap
    public int size() {
        return this.m.size();
    }

    @Override // gnu.trove.map.TCharFloatMap
    public boolean isEmpty() {
        return this.m.isEmpty();
    }

    @Override // gnu.trove.map.TCharFloatMap
    public boolean containsKey(char key) {
        return this.m.containsKey(key);
    }

    @Override // gnu.trove.map.TCharFloatMap
    public boolean containsValue(float val) {
        return this.m.containsValue(val);
    }

    @Override // gnu.trove.map.TCharFloatMap
    public float get(char key) {
        return this.m.get(key);
    }

    @Override // gnu.trove.map.TCharFloatMap
    public float put(char key, float value) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TCharFloatMap
    public float remove(char key) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TCharFloatMap
    public void putAll(TCharFloatMap m) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TCharFloatMap
    public void putAll(Map<? extends Character, ? extends Float> map) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TCharFloatMap
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TCharFloatMap
    public TCharSet keySet() {
        if (this.keySet == null) {
            this.keySet = TCollections.unmodifiableSet(this.m.keySet());
        }
        return this.keySet;
    }

    @Override // gnu.trove.map.TCharFloatMap
    public char[] keys() {
        return this.m.keys();
    }

    @Override // gnu.trove.map.TCharFloatMap
    public char[] keys(char[] array) {
        return this.m.keys(array);
    }

    @Override // gnu.trove.map.TCharFloatMap
    public TFloatCollection valueCollection() {
        if (this.values == null) {
            this.values = TCollections.unmodifiableCollection(this.m.valueCollection());
        }
        return this.values;
    }

    @Override // gnu.trove.map.TCharFloatMap
    public float[] values() {
        return this.m.values();
    }

    @Override // gnu.trove.map.TCharFloatMap
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

    @Override // gnu.trove.map.TCharFloatMap
    public char getNoEntryKey() {
        return this.m.getNoEntryKey();
    }

    @Override // gnu.trove.map.TCharFloatMap
    public float getNoEntryValue() {
        return this.m.getNoEntryValue();
    }

    @Override // gnu.trove.map.TCharFloatMap
    public boolean forEachKey(TCharProcedure procedure) {
        return this.m.forEachKey(procedure);
    }

    @Override // gnu.trove.map.TCharFloatMap
    public boolean forEachValue(TFloatProcedure procedure) {
        return this.m.forEachValue(procedure);
    }

    @Override // gnu.trove.map.TCharFloatMap
    public boolean forEachEntry(TCharFloatProcedure procedure) {
        return this.m.forEachEntry(procedure);
    }

    @Override // gnu.trove.map.TCharFloatMap
    public TCharFloatIterator iterator() {
        return new TCharFloatIterator() { // from class: gnu.trove.impl.unmodifiable.TUnmodifiableCharFloatMap.1
            TCharFloatIterator iter;

            {
                this.iter = TUnmodifiableCharFloatMap.this.m.iterator();
            }

            @Override // gnu.trove.iterator.TCharFloatIterator
            public char key() {
                return this.iter.key();
            }

            @Override // gnu.trove.iterator.TCharFloatIterator
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

            @Override // gnu.trove.iterator.TCharFloatIterator
            public float setValue(float val) {
                throw new UnsupportedOperationException();
            }

            @Override // gnu.trove.iterator.TIterator, java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override // gnu.trove.map.TCharFloatMap
    public float putIfAbsent(char key, float value) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TCharFloatMap
    public void transformValues(TFloatFunction function) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TCharFloatMap
    public boolean retainEntries(TCharFloatProcedure procedure) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TCharFloatMap
    public boolean increment(char key) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TCharFloatMap
    public boolean adjustValue(char key, float amount) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TCharFloatMap
    public float adjustOrPutValue(char key, float adjust_amount, float put_amount) {
        throw new UnsupportedOperationException();
    }
}
