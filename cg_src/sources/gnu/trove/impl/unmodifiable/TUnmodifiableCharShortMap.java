package gnu.trove.impl.unmodifiable;

import gnu.trove.TCollections;
import gnu.trove.TShortCollection;
import gnu.trove.function.TShortFunction;
import gnu.trove.iterator.TCharShortIterator;
import gnu.trove.map.TCharShortMap;
import gnu.trove.procedure.TCharProcedure;
import gnu.trove.procedure.TCharShortProcedure;
import gnu.trove.procedure.TShortProcedure;
import gnu.trove.set.TCharSet;
import java.io.Serializable;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/impl/unmodifiable/TUnmodifiableCharShortMap.class */
public class TUnmodifiableCharShortMap implements TCharShortMap, Serializable {
    private static final long serialVersionUID = -1034234728574286014L;
    private final TCharShortMap m;
    private transient TCharSet keySet = null;
    private transient TShortCollection values = null;

    public TUnmodifiableCharShortMap(TCharShortMap m) {
        if (m == null) {
            throw new NullPointerException();
        }
        this.m = m;
    }

    @Override // gnu.trove.map.TCharShortMap
    public int size() {
        return this.m.size();
    }

    @Override // gnu.trove.map.TCharShortMap
    public boolean isEmpty() {
        return this.m.isEmpty();
    }

    @Override // gnu.trove.map.TCharShortMap
    public boolean containsKey(char key) {
        return this.m.containsKey(key);
    }

    @Override // gnu.trove.map.TCharShortMap
    public boolean containsValue(short val) {
        return this.m.containsValue(val);
    }

    @Override // gnu.trove.map.TCharShortMap
    public short get(char key) {
        return this.m.get(key);
    }

    @Override // gnu.trove.map.TCharShortMap
    public short put(char key, short value) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TCharShortMap
    public short remove(char key) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TCharShortMap
    public void putAll(TCharShortMap m) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TCharShortMap
    public void putAll(Map<? extends Character, ? extends Short> map) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TCharShortMap
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TCharShortMap
    public TCharSet keySet() {
        if (this.keySet == null) {
            this.keySet = TCollections.unmodifiableSet(this.m.keySet());
        }
        return this.keySet;
    }

    @Override // gnu.trove.map.TCharShortMap
    public char[] keys() {
        return this.m.keys();
    }

    @Override // gnu.trove.map.TCharShortMap
    public char[] keys(char[] array) {
        return this.m.keys(array);
    }

    @Override // gnu.trove.map.TCharShortMap
    public TShortCollection valueCollection() {
        if (this.values == null) {
            this.values = TCollections.unmodifiableCollection(this.m.valueCollection());
        }
        return this.values;
    }

    @Override // gnu.trove.map.TCharShortMap
    public short[] values() {
        return this.m.values();
    }

    @Override // gnu.trove.map.TCharShortMap
    public short[] values(short[] array) {
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

    @Override // gnu.trove.map.TCharShortMap
    public char getNoEntryKey() {
        return this.m.getNoEntryKey();
    }

    @Override // gnu.trove.map.TCharShortMap
    public short getNoEntryValue() {
        return this.m.getNoEntryValue();
    }

    @Override // gnu.trove.map.TCharShortMap
    public boolean forEachKey(TCharProcedure procedure) {
        return this.m.forEachKey(procedure);
    }

    @Override // gnu.trove.map.TCharShortMap
    public boolean forEachValue(TShortProcedure procedure) {
        return this.m.forEachValue(procedure);
    }

    @Override // gnu.trove.map.TCharShortMap
    public boolean forEachEntry(TCharShortProcedure procedure) {
        return this.m.forEachEntry(procedure);
    }

    @Override // gnu.trove.map.TCharShortMap
    public TCharShortIterator iterator() {
        return new TCharShortIterator() { // from class: gnu.trove.impl.unmodifiable.TUnmodifiableCharShortMap.1
            TCharShortIterator iter;

            {
                this.iter = TUnmodifiableCharShortMap.this.m.iterator();
            }

            @Override // gnu.trove.iterator.TCharShortIterator
            public char key() {
                return this.iter.key();
            }

            @Override // gnu.trove.iterator.TCharShortIterator
            public short value() {
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

            @Override // gnu.trove.iterator.TCharShortIterator
            public short setValue(short val) {
                throw new UnsupportedOperationException();
            }

            @Override // gnu.trove.iterator.TIterator, java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override // gnu.trove.map.TCharShortMap
    public short putIfAbsent(char key, short value) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TCharShortMap
    public void transformValues(TShortFunction function) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TCharShortMap
    public boolean retainEntries(TCharShortProcedure procedure) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TCharShortMap
    public boolean increment(char key) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TCharShortMap
    public boolean adjustValue(char key, short amount) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TCharShortMap
    public short adjustOrPutValue(char key, short adjust_amount, short put_amount) {
        throw new UnsupportedOperationException();
    }
}
