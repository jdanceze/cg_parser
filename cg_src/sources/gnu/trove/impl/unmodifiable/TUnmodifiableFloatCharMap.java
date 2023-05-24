package gnu.trove.impl.unmodifiable;

import gnu.trove.TCharCollection;
import gnu.trove.TCollections;
import gnu.trove.function.TCharFunction;
import gnu.trove.iterator.TFloatCharIterator;
import gnu.trove.map.TFloatCharMap;
import gnu.trove.procedure.TCharProcedure;
import gnu.trove.procedure.TFloatCharProcedure;
import gnu.trove.procedure.TFloatProcedure;
import gnu.trove.set.TFloatSet;
import java.io.Serializable;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/impl/unmodifiable/TUnmodifiableFloatCharMap.class */
public class TUnmodifiableFloatCharMap implements TFloatCharMap, Serializable {
    private static final long serialVersionUID = -1034234728574286014L;
    private final TFloatCharMap m;
    private transient TFloatSet keySet = null;
    private transient TCharCollection values = null;

    public TUnmodifiableFloatCharMap(TFloatCharMap m) {
        if (m == null) {
            throw new NullPointerException();
        }
        this.m = m;
    }

    @Override // gnu.trove.map.TFloatCharMap
    public int size() {
        return this.m.size();
    }

    @Override // gnu.trove.map.TFloatCharMap
    public boolean isEmpty() {
        return this.m.isEmpty();
    }

    @Override // gnu.trove.map.TFloatCharMap
    public boolean containsKey(float key) {
        return this.m.containsKey(key);
    }

    @Override // gnu.trove.map.TFloatCharMap
    public boolean containsValue(char val) {
        return this.m.containsValue(val);
    }

    @Override // gnu.trove.map.TFloatCharMap
    public char get(float key) {
        return this.m.get(key);
    }

    @Override // gnu.trove.map.TFloatCharMap
    public char put(float key, char value) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TFloatCharMap
    public char remove(float key) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TFloatCharMap
    public void putAll(TFloatCharMap m) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TFloatCharMap
    public void putAll(Map<? extends Float, ? extends Character> map) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TFloatCharMap
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TFloatCharMap
    public TFloatSet keySet() {
        if (this.keySet == null) {
            this.keySet = TCollections.unmodifiableSet(this.m.keySet());
        }
        return this.keySet;
    }

    @Override // gnu.trove.map.TFloatCharMap
    public float[] keys() {
        return this.m.keys();
    }

    @Override // gnu.trove.map.TFloatCharMap
    public float[] keys(float[] array) {
        return this.m.keys(array);
    }

    @Override // gnu.trove.map.TFloatCharMap
    public TCharCollection valueCollection() {
        if (this.values == null) {
            this.values = TCollections.unmodifiableCollection(this.m.valueCollection());
        }
        return this.values;
    }

    @Override // gnu.trove.map.TFloatCharMap
    public char[] values() {
        return this.m.values();
    }

    @Override // gnu.trove.map.TFloatCharMap
    public char[] values(char[] array) {
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

    @Override // gnu.trove.map.TFloatCharMap
    public float getNoEntryKey() {
        return this.m.getNoEntryKey();
    }

    @Override // gnu.trove.map.TFloatCharMap
    public char getNoEntryValue() {
        return this.m.getNoEntryValue();
    }

    @Override // gnu.trove.map.TFloatCharMap
    public boolean forEachKey(TFloatProcedure procedure) {
        return this.m.forEachKey(procedure);
    }

    @Override // gnu.trove.map.TFloatCharMap
    public boolean forEachValue(TCharProcedure procedure) {
        return this.m.forEachValue(procedure);
    }

    @Override // gnu.trove.map.TFloatCharMap
    public boolean forEachEntry(TFloatCharProcedure procedure) {
        return this.m.forEachEntry(procedure);
    }

    @Override // gnu.trove.map.TFloatCharMap
    public TFloatCharIterator iterator() {
        return new TFloatCharIterator() { // from class: gnu.trove.impl.unmodifiable.TUnmodifiableFloatCharMap.1
            TFloatCharIterator iter;

            {
                this.iter = TUnmodifiableFloatCharMap.this.m.iterator();
            }

            @Override // gnu.trove.iterator.TFloatCharIterator
            public float key() {
                return this.iter.key();
            }

            @Override // gnu.trove.iterator.TFloatCharIterator
            public char value() {
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

            @Override // gnu.trove.iterator.TFloatCharIterator
            public char setValue(char val) {
                throw new UnsupportedOperationException();
            }

            @Override // gnu.trove.iterator.TIterator, java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override // gnu.trove.map.TFloatCharMap
    public char putIfAbsent(float key, char value) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TFloatCharMap
    public void transformValues(TCharFunction function) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TFloatCharMap
    public boolean retainEntries(TFloatCharProcedure procedure) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TFloatCharMap
    public boolean increment(float key) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TFloatCharMap
    public boolean adjustValue(float key, char amount) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TFloatCharMap
    public char adjustOrPutValue(float key, char adjust_amount, char put_amount) {
        throw new UnsupportedOperationException();
    }
}
