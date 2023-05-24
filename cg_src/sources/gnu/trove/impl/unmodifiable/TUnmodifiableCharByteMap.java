package gnu.trove.impl.unmodifiable;

import gnu.trove.TByteCollection;
import gnu.trove.TCollections;
import gnu.trove.function.TByteFunction;
import gnu.trove.iterator.TCharByteIterator;
import gnu.trove.map.TCharByteMap;
import gnu.trove.procedure.TByteProcedure;
import gnu.trove.procedure.TCharByteProcedure;
import gnu.trove.procedure.TCharProcedure;
import gnu.trove.set.TCharSet;
import java.io.Serializable;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/impl/unmodifiable/TUnmodifiableCharByteMap.class */
public class TUnmodifiableCharByteMap implements TCharByteMap, Serializable {
    private static final long serialVersionUID = -1034234728574286014L;
    private final TCharByteMap m;
    private transient TCharSet keySet = null;
    private transient TByteCollection values = null;

    public TUnmodifiableCharByteMap(TCharByteMap m) {
        if (m == null) {
            throw new NullPointerException();
        }
        this.m = m;
    }

    @Override // gnu.trove.map.TCharByteMap
    public int size() {
        return this.m.size();
    }

    @Override // gnu.trove.map.TCharByteMap
    public boolean isEmpty() {
        return this.m.isEmpty();
    }

    @Override // gnu.trove.map.TCharByteMap
    public boolean containsKey(char key) {
        return this.m.containsKey(key);
    }

    @Override // gnu.trove.map.TCharByteMap
    public boolean containsValue(byte val) {
        return this.m.containsValue(val);
    }

    @Override // gnu.trove.map.TCharByteMap
    public byte get(char key) {
        return this.m.get(key);
    }

    @Override // gnu.trove.map.TCharByteMap
    public byte put(char key, byte value) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TCharByteMap
    public byte remove(char key) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TCharByteMap
    public void putAll(TCharByteMap m) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TCharByteMap
    public void putAll(Map<? extends Character, ? extends Byte> map) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TCharByteMap
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TCharByteMap
    public TCharSet keySet() {
        if (this.keySet == null) {
            this.keySet = TCollections.unmodifiableSet(this.m.keySet());
        }
        return this.keySet;
    }

    @Override // gnu.trove.map.TCharByteMap
    public char[] keys() {
        return this.m.keys();
    }

    @Override // gnu.trove.map.TCharByteMap
    public char[] keys(char[] array) {
        return this.m.keys(array);
    }

    @Override // gnu.trove.map.TCharByteMap
    public TByteCollection valueCollection() {
        if (this.values == null) {
            this.values = TCollections.unmodifiableCollection(this.m.valueCollection());
        }
        return this.values;
    }

    @Override // gnu.trove.map.TCharByteMap
    public byte[] values() {
        return this.m.values();
    }

    @Override // gnu.trove.map.TCharByteMap
    public byte[] values(byte[] array) {
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

    @Override // gnu.trove.map.TCharByteMap
    public char getNoEntryKey() {
        return this.m.getNoEntryKey();
    }

    @Override // gnu.trove.map.TCharByteMap
    public byte getNoEntryValue() {
        return this.m.getNoEntryValue();
    }

    @Override // gnu.trove.map.TCharByteMap
    public boolean forEachKey(TCharProcedure procedure) {
        return this.m.forEachKey(procedure);
    }

    @Override // gnu.trove.map.TCharByteMap
    public boolean forEachValue(TByteProcedure procedure) {
        return this.m.forEachValue(procedure);
    }

    @Override // gnu.trove.map.TCharByteMap
    public boolean forEachEntry(TCharByteProcedure procedure) {
        return this.m.forEachEntry(procedure);
    }

    @Override // gnu.trove.map.TCharByteMap
    public TCharByteIterator iterator() {
        return new TCharByteIterator() { // from class: gnu.trove.impl.unmodifiable.TUnmodifiableCharByteMap.1
            TCharByteIterator iter;

            {
                this.iter = TUnmodifiableCharByteMap.this.m.iterator();
            }

            @Override // gnu.trove.iterator.TCharByteIterator
            public char key() {
                return this.iter.key();
            }

            @Override // gnu.trove.iterator.TCharByteIterator
            public byte value() {
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

            @Override // gnu.trove.iterator.TCharByteIterator
            public byte setValue(byte val) {
                throw new UnsupportedOperationException();
            }

            @Override // gnu.trove.iterator.TIterator, java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override // gnu.trove.map.TCharByteMap
    public byte putIfAbsent(char key, byte value) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TCharByteMap
    public void transformValues(TByteFunction function) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TCharByteMap
    public boolean retainEntries(TCharByteProcedure procedure) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TCharByteMap
    public boolean increment(char key) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TCharByteMap
    public boolean adjustValue(char key, byte amount) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TCharByteMap
    public byte adjustOrPutValue(char key, byte adjust_amount, byte put_amount) {
        throw new UnsupportedOperationException();
    }
}
