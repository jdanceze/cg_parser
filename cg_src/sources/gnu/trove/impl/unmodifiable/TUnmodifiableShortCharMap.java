package gnu.trove.impl.unmodifiable;

import gnu.trove.TCharCollection;
import gnu.trove.TCollections;
import gnu.trove.function.TCharFunction;
import gnu.trove.iterator.TShortCharIterator;
import gnu.trove.map.TShortCharMap;
import gnu.trove.procedure.TCharProcedure;
import gnu.trove.procedure.TShortCharProcedure;
import gnu.trove.procedure.TShortProcedure;
import gnu.trove.set.TShortSet;
import java.io.Serializable;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/impl/unmodifiable/TUnmodifiableShortCharMap.class */
public class TUnmodifiableShortCharMap implements TShortCharMap, Serializable {
    private static final long serialVersionUID = -1034234728574286014L;
    private final TShortCharMap m;
    private transient TShortSet keySet = null;
    private transient TCharCollection values = null;

    public TUnmodifiableShortCharMap(TShortCharMap m) {
        if (m == null) {
            throw new NullPointerException();
        }
        this.m = m;
    }

    @Override // gnu.trove.map.TShortCharMap
    public int size() {
        return this.m.size();
    }

    @Override // gnu.trove.map.TShortCharMap
    public boolean isEmpty() {
        return this.m.isEmpty();
    }

    @Override // gnu.trove.map.TShortCharMap
    public boolean containsKey(short key) {
        return this.m.containsKey(key);
    }

    @Override // gnu.trove.map.TShortCharMap
    public boolean containsValue(char val) {
        return this.m.containsValue(val);
    }

    @Override // gnu.trove.map.TShortCharMap
    public char get(short key) {
        return this.m.get(key);
    }

    @Override // gnu.trove.map.TShortCharMap
    public char put(short key, char value) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TShortCharMap
    public char remove(short key) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TShortCharMap
    public void putAll(TShortCharMap m) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TShortCharMap
    public void putAll(Map<? extends Short, ? extends Character> map) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TShortCharMap
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TShortCharMap
    public TShortSet keySet() {
        if (this.keySet == null) {
            this.keySet = TCollections.unmodifiableSet(this.m.keySet());
        }
        return this.keySet;
    }

    @Override // gnu.trove.map.TShortCharMap
    public short[] keys() {
        return this.m.keys();
    }

    @Override // gnu.trove.map.TShortCharMap
    public short[] keys(short[] array) {
        return this.m.keys(array);
    }

    @Override // gnu.trove.map.TShortCharMap
    public TCharCollection valueCollection() {
        if (this.values == null) {
            this.values = TCollections.unmodifiableCollection(this.m.valueCollection());
        }
        return this.values;
    }

    @Override // gnu.trove.map.TShortCharMap
    public char[] values() {
        return this.m.values();
    }

    @Override // gnu.trove.map.TShortCharMap
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

    @Override // gnu.trove.map.TShortCharMap
    public short getNoEntryKey() {
        return this.m.getNoEntryKey();
    }

    @Override // gnu.trove.map.TShortCharMap
    public char getNoEntryValue() {
        return this.m.getNoEntryValue();
    }

    @Override // gnu.trove.map.TShortCharMap
    public boolean forEachKey(TShortProcedure procedure) {
        return this.m.forEachKey(procedure);
    }

    @Override // gnu.trove.map.TShortCharMap
    public boolean forEachValue(TCharProcedure procedure) {
        return this.m.forEachValue(procedure);
    }

    @Override // gnu.trove.map.TShortCharMap
    public boolean forEachEntry(TShortCharProcedure procedure) {
        return this.m.forEachEntry(procedure);
    }

    @Override // gnu.trove.map.TShortCharMap
    public TShortCharIterator iterator() {
        return new TShortCharIterator() { // from class: gnu.trove.impl.unmodifiable.TUnmodifiableShortCharMap.1
            TShortCharIterator iter;

            {
                this.iter = TUnmodifiableShortCharMap.this.m.iterator();
            }

            @Override // gnu.trove.iterator.TShortCharIterator
            public short key() {
                return this.iter.key();
            }

            @Override // gnu.trove.iterator.TShortCharIterator
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

            @Override // gnu.trove.iterator.TShortCharIterator
            public char setValue(char val) {
                throw new UnsupportedOperationException();
            }

            @Override // gnu.trove.iterator.TIterator, java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override // gnu.trove.map.TShortCharMap
    public char putIfAbsent(short key, char value) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TShortCharMap
    public void transformValues(TCharFunction function) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TShortCharMap
    public boolean retainEntries(TShortCharProcedure procedure) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TShortCharMap
    public boolean increment(short key) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TShortCharMap
    public boolean adjustValue(short key, char amount) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TShortCharMap
    public char adjustOrPutValue(short key, char adjust_amount, char put_amount) {
        throw new UnsupportedOperationException();
    }
}
