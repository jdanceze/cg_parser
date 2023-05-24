package gnu.trove.impl.unmodifiable;

import gnu.trove.TCharCollection;
import gnu.trove.TCollections;
import gnu.trove.function.TCharFunction;
import gnu.trove.iterator.TByteCharIterator;
import gnu.trove.map.TByteCharMap;
import gnu.trove.procedure.TByteCharProcedure;
import gnu.trove.procedure.TByteProcedure;
import gnu.trove.procedure.TCharProcedure;
import gnu.trove.set.TByteSet;
import java.io.Serializable;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/impl/unmodifiable/TUnmodifiableByteCharMap.class */
public class TUnmodifiableByteCharMap implements TByteCharMap, Serializable {
    private static final long serialVersionUID = -1034234728574286014L;
    private final TByteCharMap m;
    private transient TByteSet keySet = null;
    private transient TCharCollection values = null;

    public TUnmodifiableByteCharMap(TByteCharMap m) {
        if (m == null) {
            throw new NullPointerException();
        }
        this.m = m;
    }

    @Override // gnu.trove.map.TByteCharMap
    public int size() {
        return this.m.size();
    }

    @Override // gnu.trove.map.TByteCharMap
    public boolean isEmpty() {
        return this.m.isEmpty();
    }

    @Override // gnu.trove.map.TByteCharMap
    public boolean containsKey(byte key) {
        return this.m.containsKey(key);
    }

    @Override // gnu.trove.map.TByteCharMap
    public boolean containsValue(char val) {
        return this.m.containsValue(val);
    }

    @Override // gnu.trove.map.TByteCharMap
    public char get(byte key) {
        return this.m.get(key);
    }

    @Override // gnu.trove.map.TByteCharMap
    public char put(byte key, char value) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TByteCharMap
    public char remove(byte key) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TByteCharMap
    public void putAll(TByteCharMap m) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TByteCharMap
    public void putAll(Map<? extends Byte, ? extends Character> map) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TByteCharMap
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TByteCharMap
    public TByteSet keySet() {
        if (this.keySet == null) {
            this.keySet = TCollections.unmodifiableSet(this.m.keySet());
        }
        return this.keySet;
    }

    @Override // gnu.trove.map.TByteCharMap
    public byte[] keys() {
        return this.m.keys();
    }

    @Override // gnu.trove.map.TByteCharMap
    public byte[] keys(byte[] array) {
        return this.m.keys(array);
    }

    @Override // gnu.trove.map.TByteCharMap
    public TCharCollection valueCollection() {
        if (this.values == null) {
            this.values = TCollections.unmodifiableCollection(this.m.valueCollection());
        }
        return this.values;
    }

    @Override // gnu.trove.map.TByteCharMap
    public char[] values() {
        return this.m.values();
    }

    @Override // gnu.trove.map.TByteCharMap
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

    @Override // gnu.trove.map.TByteCharMap
    public byte getNoEntryKey() {
        return this.m.getNoEntryKey();
    }

    @Override // gnu.trove.map.TByteCharMap
    public char getNoEntryValue() {
        return this.m.getNoEntryValue();
    }

    @Override // gnu.trove.map.TByteCharMap
    public boolean forEachKey(TByteProcedure procedure) {
        return this.m.forEachKey(procedure);
    }

    @Override // gnu.trove.map.TByteCharMap
    public boolean forEachValue(TCharProcedure procedure) {
        return this.m.forEachValue(procedure);
    }

    @Override // gnu.trove.map.TByteCharMap
    public boolean forEachEntry(TByteCharProcedure procedure) {
        return this.m.forEachEntry(procedure);
    }

    @Override // gnu.trove.map.TByteCharMap
    public TByteCharIterator iterator() {
        return new TByteCharIterator() { // from class: gnu.trove.impl.unmodifiable.TUnmodifiableByteCharMap.1
            TByteCharIterator iter;

            {
                this.iter = TUnmodifiableByteCharMap.this.m.iterator();
            }

            @Override // gnu.trove.iterator.TByteCharIterator
            public byte key() {
                return this.iter.key();
            }

            @Override // gnu.trove.iterator.TByteCharIterator
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

            @Override // gnu.trove.iterator.TByteCharIterator
            public char setValue(char val) {
                throw new UnsupportedOperationException();
            }

            @Override // gnu.trove.iterator.TIterator, java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override // gnu.trove.map.TByteCharMap
    public char putIfAbsent(byte key, char value) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TByteCharMap
    public void transformValues(TCharFunction function) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TByteCharMap
    public boolean retainEntries(TByteCharProcedure procedure) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TByteCharMap
    public boolean increment(byte key) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TByteCharMap
    public boolean adjustValue(byte key, char amount) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TByteCharMap
    public char adjustOrPutValue(byte key, char adjust_amount, char put_amount) {
        throw new UnsupportedOperationException();
    }
}
