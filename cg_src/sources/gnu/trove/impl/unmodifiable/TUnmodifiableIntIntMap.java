package gnu.trove.impl.unmodifiable;

import gnu.trove.TCollections;
import gnu.trove.TIntCollection;
import gnu.trove.function.TIntFunction;
import gnu.trove.iterator.TIntIntIterator;
import gnu.trove.map.TIntIntMap;
import gnu.trove.procedure.TIntIntProcedure;
import gnu.trove.procedure.TIntProcedure;
import gnu.trove.set.TIntSet;
import java.io.Serializable;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/impl/unmodifiable/TUnmodifiableIntIntMap.class */
public class TUnmodifiableIntIntMap implements TIntIntMap, Serializable {
    private static final long serialVersionUID = -1034234728574286014L;
    private final TIntIntMap m;
    private transient TIntSet keySet = null;
    private transient TIntCollection values = null;

    public TUnmodifiableIntIntMap(TIntIntMap m) {
        if (m == null) {
            throw new NullPointerException();
        }
        this.m = m;
    }

    @Override // gnu.trove.map.TIntIntMap
    public int size() {
        return this.m.size();
    }

    @Override // gnu.trove.map.TIntIntMap
    public boolean isEmpty() {
        return this.m.isEmpty();
    }

    @Override // gnu.trove.map.TIntIntMap
    public boolean containsKey(int key) {
        return this.m.containsKey(key);
    }

    @Override // gnu.trove.map.TIntIntMap
    public boolean containsValue(int val) {
        return this.m.containsValue(val);
    }

    @Override // gnu.trove.map.TIntIntMap
    public int get(int key) {
        return this.m.get(key);
    }

    @Override // gnu.trove.map.TIntIntMap
    public int put(int key, int value) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TIntIntMap
    public int remove(int key) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TIntIntMap
    public void putAll(TIntIntMap m) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TIntIntMap
    public void putAll(Map<? extends Integer, ? extends Integer> map) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TIntIntMap
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TIntIntMap
    public TIntSet keySet() {
        if (this.keySet == null) {
            this.keySet = TCollections.unmodifiableSet(this.m.keySet());
        }
        return this.keySet;
    }

    @Override // gnu.trove.map.TIntIntMap
    public int[] keys() {
        return this.m.keys();
    }

    @Override // gnu.trove.map.TIntIntMap
    public int[] keys(int[] array) {
        return this.m.keys(array);
    }

    @Override // gnu.trove.map.TIntIntMap
    public TIntCollection valueCollection() {
        if (this.values == null) {
            this.values = TCollections.unmodifiableCollection(this.m.valueCollection());
        }
        return this.values;
    }

    @Override // gnu.trove.map.TIntIntMap
    public int[] values() {
        return this.m.values();
    }

    @Override // gnu.trove.map.TIntIntMap
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

    @Override // gnu.trove.map.TIntIntMap
    public int getNoEntryKey() {
        return this.m.getNoEntryKey();
    }

    @Override // gnu.trove.map.TIntIntMap
    public int getNoEntryValue() {
        return this.m.getNoEntryValue();
    }

    @Override // gnu.trove.map.TIntIntMap
    public boolean forEachKey(TIntProcedure procedure) {
        return this.m.forEachKey(procedure);
    }

    @Override // gnu.trove.map.TIntIntMap
    public boolean forEachValue(TIntProcedure procedure) {
        return this.m.forEachValue(procedure);
    }

    @Override // gnu.trove.map.TIntIntMap
    public boolean forEachEntry(TIntIntProcedure procedure) {
        return this.m.forEachEntry(procedure);
    }

    @Override // gnu.trove.map.TIntIntMap
    public TIntIntIterator iterator() {
        return new TIntIntIterator() { // from class: gnu.trove.impl.unmodifiable.TUnmodifiableIntIntMap.1
            TIntIntIterator iter;

            {
                this.iter = TUnmodifiableIntIntMap.this.m.iterator();
            }

            @Override // gnu.trove.iterator.TIntIntIterator
            public int key() {
                return this.iter.key();
            }

            @Override // gnu.trove.iterator.TIntIntIterator
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

            @Override // gnu.trove.iterator.TIntIntIterator
            public int setValue(int val) {
                throw new UnsupportedOperationException();
            }

            @Override // gnu.trove.iterator.TIterator, java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override // gnu.trove.map.TIntIntMap
    public int putIfAbsent(int key, int value) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TIntIntMap
    public void transformValues(TIntFunction function) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TIntIntMap
    public boolean retainEntries(TIntIntProcedure procedure) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TIntIntMap
    public boolean increment(int key) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TIntIntMap
    public boolean adjustValue(int key, int amount) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TIntIntMap
    public int adjustOrPutValue(int key, int adjust_amount, int put_amount) {
        throw new UnsupportedOperationException();
    }
}
