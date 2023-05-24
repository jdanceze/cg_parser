package gnu.trove.impl.unmodifiable;

import gnu.trove.TCollections;
import gnu.trove.TIntCollection;
import gnu.trove.function.TIntFunction;
import gnu.trove.iterator.TShortIntIterator;
import gnu.trove.map.TShortIntMap;
import gnu.trove.procedure.TIntProcedure;
import gnu.trove.procedure.TShortIntProcedure;
import gnu.trove.procedure.TShortProcedure;
import gnu.trove.set.TShortSet;
import java.io.Serializable;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/impl/unmodifiable/TUnmodifiableShortIntMap.class */
public class TUnmodifiableShortIntMap implements TShortIntMap, Serializable {
    private static final long serialVersionUID = -1034234728574286014L;
    private final TShortIntMap m;
    private transient TShortSet keySet = null;
    private transient TIntCollection values = null;

    public TUnmodifiableShortIntMap(TShortIntMap m) {
        if (m == null) {
            throw new NullPointerException();
        }
        this.m = m;
    }

    @Override // gnu.trove.map.TShortIntMap
    public int size() {
        return this.m.size();
    }

    @Override // gnu.trove.map.TShortIntMap
    public boolean isEmpty() {
        return this.m.isEmpty();
    }

    @Override // gnu.trove.map.TShortIntMap
    public boolean containsKey(short key) {
        return this.m.containsKey(key);
    }

    @Override // gnu.trove.map.TShortIntMap
    public boolean containsValue(int val) {
        return this.m.containsValue(val);
    }

    @Override // gnu.trove.map.TShortIntMap
    public int get(short key) {
        return this.m.get(key);
    }

    @Override // gnu.trove.map.TShortIntMap
    public int put(short key, int value) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TShortIntMap
    public int remove(short key) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TShortIntMap
    public void putAll(TShortIntMap m) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TShortIntMap
    public void putAll(Map<? extends Short, ? extends Integer> map) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TShortIntMap
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TShortIntMap
    public TShortSet keySet() {
        if (this.keySet == null) {
            this.keySet = TCollections.unmodifiableSet(this.m.keySet());
        }
        return this.keySet;
    }

    @Override // gnu.trove.map.TShortIntMap
    public short[] keys() {
        return this.m.keys();
    }

    @Override // gnu.trove.map.TShortIntMap
    public short[] keys(short[] array) {
        return this.m.keys(array);
    }

    @Override // gnu.trove.map.TShortIntMap
    public TIntCollection valueCollection() {
        if (this.values == null) {
            this.values = TCollections.unmodifiableCollection(this.m.valueCollection());
        }
        return this.values;
    }

    @Override // gnu.trove.map.TShortIntMap
    public int[] values() {
        return this.m.values();
    }

    @Override // gnu.trove.map.TShortIntMap
    public int[] values(int[] array) {
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

    @Override // gnu.trove.map.TShortIntMap
    public short getNoEntryKey() {
        return this.m.getNoEntryKey();
    }

    @Override // gnu.trove.map.TShortIntMap
    public int getNoEntryValue() {
        return this.m.getNoEntryValue();
    }

    @Override // gnu.trove.map.TShortIntMap
    public boolean forEachKey(TShortProcedure procedure) {
        return this.m.forEachKey(procedure);
    }

    @Override // gnu.trove.map.TShortIntMap
    public boolean forEachValue(TIntProcedure procedure) {
        return this.m.forEachValue(procedure);
    }

    @Override // gnu.trove.map.TShortIntMap
    public boolean forEachEntry(TShortIntProcedure procedure) {
        return this.m.forEachEntry(procedure);
    }

    @Override // gnu.trove.map.TShortIntMap
    public TShortIntIterator iterator() {
        return new TShortIntIterator() { // from class: gnu.trove.impl.unmodifiable.TUnmodifiableShortIntMap.1
            TShortIntIterator iter;

            {
                this.iter = TUnmodifiableShortIntMap.this.m.iterator();
            }

            @Override // gnu.trove.iterator.TShortIntIterator
            public short key() {
                return this.iter.key();
            }

            @Override // gnu.trove.iterator.TShortIntIterator
            public int value() {
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

            @Override // gnu.trove.iterator.TShortIntIterator
            public int setValue(int val) {
                throw new UnsupportedOperationException();
            }

            @Override // gnu.trove.iterator.TIterator, java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override // gnu.trove.map.TShortIntMap
    public int putIfAbsent(short key, int value) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TShortIntMap
    public void transformValues(TIntFunction function) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TShortIntMap
    public boolean retainEntries(TShortIntProcedure procedure) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TShortIntMap
    public boolean increment(short key) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TShortIntMap
    public boolean adjustValue(short key, int amount) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TShortIntMap
    public int adjustOrPutValue(short key, int adjust_amount, int put_amount) {
        throw new UnsupportedOperationException();
    }
}
