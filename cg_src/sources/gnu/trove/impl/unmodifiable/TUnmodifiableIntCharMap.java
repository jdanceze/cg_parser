package gnu.trove.impl.unmodifiable;

import gnu.trove.TCharCollection;
import gnu.trove.TCollections;
import gnu.trove.function.TCharFunction;
import gnu.trove.iterator.TIntCharIterator;
import gnu.trove.map.TIntCharMap;
import gnu.trove.procedure.TCharProcedure;
import gnu.trove.procedure.TIntCharProcedure;
import gnu.trove.procedure.TIntProcedure;
import gnu.trove.set.TIntSet;
import java.io.Serializable;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/impl/unmodifiable/TUnmodifiableIntCharMap.class */
public class TUnmodifiableIntCharMap implements TIntCharMap, Serializable {
    private static final long serialVersionUID = -1034234728574286014L;
    private final TIntCharMap m;
    private transient TIntSet keySet = null;
    private transient TCharCollection values = null;

    public TUnmodifiableIntCharMap(TIntCharMap m) {
        if (m == null) {
            throw new NullPointerException();
        }
        this.m = m;
    }

    @Override // gnu.trove.map.TIntCharMap
    public int size() {
        return this.m.size();
    }

    @Override // gnu.trove.map.TIntCharMap
    public boolean isEmpty() {
        return this.m.isEmpty();
    }

    @Override // gnu.trove.map.TIntCharMap
    public boolean containsKey(int key) {
        return this.m.containsKey(key);
    }

    @Override // gnu.trove.map.TIntCharMap
    public boolean containsValue(char val) {
        return this.m.containsValue(val);
    }

    @Override // gnu.trove.map.TIntCharMap
    public char get(int key) {
        return this.m.get(key);
    }

    @Override // gnu.trove.map.TIntCharMap
    public char put(int key, char value) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TIntCharMap
    public char remove(int key) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TIntCharMap
    public void putAll(TIntCharMap m) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TIntCharMap
    public void putAll(Map<? extends Integer, ? extends Character> map) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TIntCharMap
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TIntCharMap
    public TIntSet keySet() {
        if (this.keySet == null) {
            this.keySet = TCollections.unmodifiableSet(this.m.keySet());
        }
        return this.keySet;
    }

    @Override // gnu.trove.map.TIntCharMap
    public int[] keys() {
        return this.m.keys();
    }

    @Override // gnu.trove.map.TIntCharMap
    public int[] keys(int[] array) {
        return this.m.keys(array);
    }

    @Override // gnu.trove.map.TIntCharMap
    public TCharCollection valueCollection() {
        if (this.values == null) {
            this.values = TCollections.unmodifiableCollection(this.m.valueCollection());
        }
        return this.values;
    }

    @Override // gnu.trove.map.TIntCharMap
    public char[] values() {
        return this.m.values();
    }

    @Override // gnu.trove.map.TIntCharMap
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

    @Override // gnu.trove.map.TIntCharMap
    public int getNoEntryKey() {
        return this.m.getNoEntryKey();
    }

    @Override // gnu.trove.map.TIntCharMap
    public char getNoEntryValue() {
        return this.m.getNoEntryValue();
    }

    @Override // gnu.trove.map.TIntCharMap
    public boolean forEachKey(TIntProcedure procedure) {
        return this.m.forEachKey(procedure);
    }

    @Override // gnu.trove.map.TIntCharMap
    public boolean forEachValue(TCharProcedure procedure) {
        return this.m.forEachValue(procedure);
    }

    @Override // gnu.trove.map.TIntCharMap
    public boolean forEachEntry(TIntCharProcedure procedure) {
        return this.m.forEachEntry(procedure);
    }

    @Override // gnu.trove.map.TIntCharMap
    public TIntCharIterator iterator() {
        return new TIntCharIterator() { // from class: gnu.trove.impl.unmodifiable.TUnmodifiableIntCharMap.1
            TIntCharIterator iter;

            {
                this.iter = TUnmodifiableIntCharMap.this.m.iterator();
            }

            @Override // gnu.trove.iterator.TIntCharIterator
            public int key() {
                return this.iter.key();
            }

            @Override // gnu.trove.iterator.TIntCharIterator
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

            @Override // gnu.trove.iterator.TIntCharIterator
            public char setValue(char val) {
                throw new UnsupportedOperationException();
            }

            @Override // gnu.trove.iterator.TIterator, java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override // gnu.trove.map.TIntCharMap
    public char putIfAbsent(int key, char value) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TIntCharMap
    public void transformValues(TCharFunction function) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TIntCharMap
    public boolean retainEntries(TIntCharProcedure procedure) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TIntCharMap
    public boolean increment(int key) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TIntCharMap
    public boolean adjustValue(int key, char amount) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TIntCharMap
    public char adjustOrPutValue(int key, char adjust_amount, char put_amount) {
        throw new UnsupportedOperationException();
    }
}
