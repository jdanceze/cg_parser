package gnu.trove.impl.unmodifiable;

import gnu.trove.TCharCollection;
import gnu.trove.TCollections;
import gnu.trove.function.TCharFunction;
import gnu.trove.iterator.TCharCharIterator;
import gnu.trove.map.TCharCharMap;
import gnu.trove.procedure.TCharCharProcedure;
import gnu.trove.procedure.TCharProcedure;
import gnu.trove.set.TCharSet;
import java.io.Serializable;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/impl/unmodifiable/TUnmodifiableCharCharMap.class */
public class TUnmodifiableCharCharMap implements TCharCharMap, Serializable {
    private static final long serialVersionUID = -1034234728574286014L;
    private final TCharCharMap m;
    private transient TCharSet keySet = null;
    private transient TCharCollection values = null;

    public TUnmodifiableCharCharMap(TCharCharMap m) {
        if (m == null) {
            throw new NullPointerException();
        }
        this.m = m;
    }

    @Override // gnu.trove.map.TCharCharMap
    public int size() {
        return this.m.size();
    }

    @Override // gnu.trove.map.TCharCharMap
    public boolean isEmpty() {
        return this.m.isEmpty();
    }

    @Override // gnu.trove.map.TCharCharMap
    public boolean containsKey(char key) {
        return this.m.containsKey(key);
    }

    @Override // gnu.trove.map.TCharCharMap
    public boolean containsValue(char val) {
        return this.m.containsValue(val);
    }

    @Override // gnu.trove.map.TCharCharMap
    public char get(char key) {
        return this.m.get(key);
    }

    @Override // gnu.trove.map.TCharCharMap
    public char put(char key, char value) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TCharCharMap
    public char remove(char key) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TCharCharMap
    public void putAll(TCharCharMap m) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TCharCharMap
    public void putAll(Map<? extends Character, ? extends Character> map) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TCharCharMap
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TCharCharMap
    public TCharSet keySet() {
        if (this.keySet == null) {
            this.keySet = TCollections.unmodifiableSet(this.m.keySet());
        }
        return this.keySet;
    }

    @Override // gnu.trove.map.TCharCharMap
    public char[] keys() {
        return this.m.keys();
    }

    @Override // gnu.trove.map.TCharCharMap
    public char[] keys(char[] array) {
        return this.m.keys(array);
    }

    @Override // gnu.trove.map.TCharCharMap
    public TCharCollection valueCollection() {
        if (this.values == null) {
            this.values = TCollections.unmodifiableCollection(this.m.valueCollection());
        }
        return this.values;
    }

    @Override // gnu.trove.map.TCharCharMap
    public char[] values() {
        return this.m.values();
    }

    @Override // gnu.trove.map.TCharCharMap
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

    @Override // gnu.trove.map.TCharCharMap
    public char getNoEntryKey() {
        return this.m.getNoEntryKey();
    }

    @Override // gnu.trove.map.TCharCharMap
    public char getNoEntryValue() {
        return this.m.getNoEntryValue();
    }

    @Override // gnu.trove.map.TCharCharMap
    public boolean forEachKey(TCharProcedure procedure) {
        return this.m.forEachKey(procedure);
    }

    @Override // gnu.trove.map.TCharCharMap
    public boolean forEachValue(TCharProcedure procedure) {
        return this.m.forEachValue(procedure);
    }

    @Override // gnu.trove.map.TCharCharMap
    public boolean forEachEntry(TCharCharProcedure procedure) {
        return this.m.forEachEntry(procedure);
    }

    @Override // gnu.trove.map.TCharCharMap
    public TCharCharIterator iterator() {
        return new TCharCharIterator() { // from class: gnu.trove.impl.unmodifiable.TUnmodifiableCharCharMap.1
            TCharCharIterator iter;

            {
                this.iter = TUnmodifiableCharCharMap.this.m.iterator();
            }

            @Override // gnu.trove.iterator.TCharCharIterator
            public char key() {
                return this.iter.key();
            }

            @Override // gnu.trove.iterator.TCharCharIterator
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

            @Override // gnu.trove.iterator.TCharCharIterator
            public char setValue(char val) {
                throw new UnsupportedOperationException();
            }

            @Override // gnu.trove.iterator.TIterator, java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override // gnu.trove.map.TCharCharMap
    public char putIfAbsent(char key, char value) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TCharCharMap
    public void transformValues(TCharFunction function) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TCharCharMap
    public boolean retainEntries(TCharCharProcedure procedure) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TCharCharMap
    public boolean increment(char key) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TCharCharMap
    public boolean adjustValue(char key, char amount) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TCharCharMap
    public char adjustOrPutValue(char key, char adjust_amount, char put_amount) {
        throw new UnsupportedOperationException();
    }
}
