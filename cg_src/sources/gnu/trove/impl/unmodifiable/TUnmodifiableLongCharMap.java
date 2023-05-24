package gnu.trove.impl.unmodifiable;

import gnu.trove.TCharCollection;
import gnu.trove.TCollections;
import gnu.trove.function.TCharFunction;
import gnu.trove.iterator.TLongCharIterator;
import gnu.trove.map.TLongCharMap;
import gnu.trove.procedure.TCharProcedure;
import gnu.trove.procedure.TLongCharProcedure;
import gnu.trove.procedure.TLongProcedure;
import gnu.trove.set.TLongSet;
import java.io.Serializable;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/impl/unmodifiable/TUnmodifiableLongCharMap.class */
public class TUnmodifiableLongCharMap implements TLongCharMap, Serializable {
    private static final long serialVersionUID = -1034234728574286014L;
    private final TLongCharMap m;
    private transient TLongSet keySet = null;
    private transient TCharCollection values = null;

    public TUnmodifiableLongCharMap(TLongCharMap m) {
        if (m == null) {
            throw new NullPointerException();
        }
        this.m = m;
    }

    @Override // gnu.trove.map.TLongCharMap
    public int size() {
        return this.m.size();
    }

    @Override // gnu.trove.map.TLongCharMap
    public boolean isEmpty() {
        return this.m.isEmpty();
    }

    @Override // gnu.trove.map.TLongCharMap
    public boolean containsKey(long key) {
        return this.m.containsKey(key);
    }

    @Override // gnu.trove.map.TLongCharMap
    public boolean containsValue(char val) {
        return this.m.containsValue(val);
    }

    @Override // gnu.trove.map.TLongCharMap
    public char get(long key) {
        return this.m.get(key);
    }

    @Override // gnu.trove.map.TLongCharMap
    public char put(long key, char value) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TLongCharMap
    public char remove(long key) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TLongCharMap
    public void putAll(TLongCharMap m) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TLongCharMap
    public void putAll(Map<? extends Long, ? extends Character> map) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TLongCharMap
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TLongCharMap
    public TLongSet keySet() {
        if (this.keySet == null) {
            this.keySet = TCollections.unmodifiableSet(this.m.keySet());
        }
        return this.keySet;
    }

    @Override // gnu.trove.map.TLongCharMap
    public long[] keys() {
        return this.m.keys();
    }

    @Override // gnu.trove.map.TLongCharMap
    public long[] keys(long[] array) {
        return this.m.keys(array);
    }

    @Override // gnu.trove.map.TLongCharMap
    public TCharCollection valueCollection() {
        if (this.values == null) {
            this.values = TCollections.unmodifiableCollection(this.m.valueCollection());
        }
        return this.values;
    }

    @Override // gnu.trove.map.TLongCharMap
    public char[] values() {
        return this.m.values();
    }

    @Override // gnu.trove.map.TLongCharMap
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

    @Override // gnu.trove.map.TLongCharMap
    public long getNoEntryKey() {
        return this.m.getNoEntryKey();
    }

    @Override // gnu.trove.map.TLongCharMap
    public char getNoEntryValue() {
        return this.m.getNoEntryValue();
    }

    @Override // gnu.trove.map.TLongCharMap
    public boolean forEachKey(TLongProcedure procedure) {
        return this.m.forEachKey(procedure);
    }

    @Override // gnu.trove.map.TLongCharMap
    public boolean forEachValue(TCharProcedure procedure) {
        return this.m.forEachValue(procedure);
    }

    @Override // gnu.trove.map.TLongCharMap
    public boolean forEachEntry(TLongCharProcedure procedure) {
        return this.m.forEachEntry(procedure);
    }

    @Override // gnu.trove.map.TLongCharMap
    public TLongCharIterator iterator() {
        return new TLongCharIterator() { // from class: gnu.trove.impl.unmodifiable.TUnmodifiableLongCharMap.1
            TLongCharIterator iter;

            {
                this.iter = TUnmodifiableLongCharMap.this.m.iterator();
            }

            @Override // gnu.trove.iterator.TLongCharIterator
            public long key() {
                return this.iter.key();
            }

            @Override // gnu.trove.iterator.TLongCharIterator
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

            @Override // gnu.trove.iterator.TLongCharIterator
            public char setValue(char val) {
                throw new UnsupportedOperationException();
            }

            @Override // gnu.trove.iterator.TIterator, java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override // gnu.trove.map.TLongCharMap
    public char putIfAbsent(long key, char value) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TLongCharMap
    public void transformValues(TCharFunction function) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TLongCharMap
    public boolean retainEntries(TLongCharProcedure procedure) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TLongCharMap
    public boolean increment(long key) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TLongCharMap
    public boolean adjustValue(long key, char amount) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TLongCharMap
    public char adjustOrPutValue(long key, char adjust_amount, char put_amount) {
        throw new UnsupportedOperationException();
    }
}
