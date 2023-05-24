package gnu.trove.impl.unmodifiable;

import gnu.trove.TCollections;
import gnu.trove.TShortCollection;
import gnu.trove.function.TShortFunction;
import gnu.trove.iterator.TIntShortIterator;
import gnu.trove.map.TIntShortMap;
import gnu.trove.procedure.TIntProcedure;
import gnu.trove.procedure.TIntShortProcedure;
import gnu.trove.procedure.TShortProcedure;
import gnu.trove.set.TIntSet;
import java.io.Serializable;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/impl/unmodifiable/TUnmodifiableIntShortMap.class */
public class TUnmodifiableIntShortMap implements TIntShortMap, Serializable {
    private static final long serialVersionUID = -1034234728574286014L;
    private final TIntShortMap m;
    private transient TIntSet keySet = null;
    private transient TShortCollection values = null;

    public TUnmodifiableIntShortMap(TIntShortMap m) {
        if (m == null) {
            throw new NullPointerException();
        }
        this.m = m;
    }

    @Override // gnu.trove.map.TIntShortMap
    public int size() {
        return this.m.size();
    }

    @Override // gnu.trove.map.TIntShortMap
    public boolean isEmpty() {
        return this.m.isEmpty();
    }

    @Override // gnu.trove.map.TIntShortMap
    public boolean containsKey(int key) {
        return this.m.containsKey(key);
    }

    @Override // gnu.trove.map.TIntShortMap
    public boolean containsValue(short val) {
        return this.m.containsValue(val);
    }

    @Override // gnu.trove.map.TIntShortMap
    public short get(int key) {
        return this.m.get(key);
    }

    @Override // gnu.trove.map.TIntShortMap
    public short put(int key, short value) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TIntShortMap
    public short remove(int key) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TIntShortMap
    public void putAll(TIntShortMap m) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TIntShortMap
    public void putAll(Map<? extends Integer, ? extends Short> map) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TIntShortMap
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TIntShortMap
    public TIntSet keySet() {
        if (this.keySet == null) {
            this.keySet = TCollections.unmodifiableSet(this.m.keySet());
        }
        return this.keySet;
    }

    @Override // gnu.trove.map.TIntShortMap
    public int[] keys() {
        return this.m.keys();
    }

    @Override // gnu.trove.map.TIntShortMap
    public int[] keys(int[] array) {
        return this.m.keys(array);
    }

    @Override // gnu.trove.map.TIntShortMap
    public TShortCollection valueCollection() {
        if (this.values == null) {
            this.values = TCollections.unmodifiableCollection(this.m.valueCollection());
        }
        return this.values;
    }

    @Override // gnu.trove.map.TIntShortMap
    public short[] values() {
        return this.m.values();
    }

    @Override // gnu.trove.map.TIntShortMap
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

    @Override // gnu.trove.map.TIntShortMap
    public int getNoEntryKey() {
        return this.m.getNoEntryKey();
    }

    @Override // gnu.trove.map.TIntShortMap
    public short getNoEntryValue() {
        return this.m.getNoEntryValue();
    }

    @Override // gnu.trove.map.TIntShortMap
    public boolean forEachKey(TIntProcedure procedure) {
        return this.m.forEachKey(procedure);
    }

    @Override // gnu.trove.map.TIntShortMap
    public boolean forEachValue(TShortProcedure procedure) {
        return this.m.forEachValue(procedure);
    }

    @Override // gnu.trove.map.TIntShortMap
    public boolean forEachEntry(TIntShortProcedure procedure) {
        return this.m.forEachEntry(procedure);
    }

    @Override // gnu.trove.map.TIntShortMap
    public TIntShortIterator iterator() {
        return new TIntShortIterator() { // from class: gnu.trove.impl.unmodifiable.TUnmodifiableIntShortMap.1
            TIntShortIterator iter;

            {
                this.iter = TUnmodifiableIntShortMap.this.m.iterator();
            }

            @Override // gnu.trove.iterator.TIntShortIterator
            public int key() {
                return this.iter.key();
            }

            @Override // gnu.trove.iterator.TIntShortIterator
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

            @Override // gnu.trove.iterator.TIntShortIterator
            public short setValue(short val) {
                throw new UnsupportedOperationException();
            }

            @Override // gnu.trove.iterator.TIterator, java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override // gnu.trove.map.TIntShortMap
    public short putIfAbsent(int key, short value) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TIntShortMap
    public void transformValues(TShortFunction function) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TIntShortMap
    public boolean retainEntries(TIntShortProcedure procedure) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TIntShortMap
    public boolean increment(int key) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TIntShortMap
    public boolean adjustValue(int key, short amount) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TIntShortMap
    public short adjustOrPutValue(int key, short adjust_amount, short put_amount) {
        throw new UnsupportedOperationException();
    }
}
