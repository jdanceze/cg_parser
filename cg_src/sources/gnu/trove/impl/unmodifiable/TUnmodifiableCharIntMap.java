package gnu.trove.impl.unmodifiable;

import gnu.trove.TCollections;
import gnu.trove.TIntCollection;
import gnu.trove.function.TIntFunction;
import gnu.trove.iterator.TCharIntIterator;
import gnu.trove.map.TCharIntMap;
import gnu.trove.procedure.TCharIntProcedure;
import gnu.trove.procedure.TCharProcedure;
import gnu.trove.procedure.TIntProcedure;
import gnu.trove.set.TCharSet;
import java.io.Serializable;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/impl/unmodifiable/TUnmodifiableCharIntMap.class */
public class TUnmodifiableCharIntMap implements TCharIntMap, Serializable {
    private static final long serialVersionUID = -1034234728574286014L;
    private final TCharIntMap m;
    private transient TCharSet keySet = null;
    private transient TIntCollection values = null;

    public TUnmodifiableCharIntMap(TCharIntMap m) {
        if (m == null) {
            throw new NullPointerException();
        }
        this.m = m;
    }

    @Override // gnu.trove.map.TCharIntMap
    public int size() {
        return this.m.size();
    }

    @Override // gnu.trove.map.TCharIntMap
    public boolean isEmpty() {
        return this.m.isEmpty();
    }

    @Override // gnu.trove.map.TCharIntMap
    public boolean containsKey(char key) {
        return this.m.containsKey(key);
    }

    @Override // gnu.trove.map.TCharIntMap
    public boolean containsValue(int val) {
        return this.m.containsValue(val);
    }

    @Override // gnu.trove.map.TCharIntMap
    public int get(char key) {
        return this.m.get(key);
    }

    @Override // gnu.trove.map.TCharIntMap
    public int put(char key, int value) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TCharIntMap
    public int remove(char key) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TCharIntMap
    public void putAll(TCharIntMap m) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TCharIntMap
    public void putAll(Map<? extends Character, ? extends Integer> map) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TCharIntMap
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TCharIntMap
    public TCharSet keySet() {
        if (this.keySet == null) {
            this.keySet = TCollections.unmodifiableSet(this.m.keySet());
        }
        return this.keySet;
    }

    @Override // gnu.trove.map.TCharIntMap
    public char[] keys() {
        return this.m.keys();
    }

    @Override // gnu.trove.map.TCharIntMap
    public char[] keys(char[] array) {
        return this.m.keys(array);
    }

    @Override // gnu.trove.map.TCharIntMap
    public TIntCollection valueCollection() {
        if (this.values == null) {
            this.values = TCollections.unmodifiableCollection(this.m.valueCollection());
        }
        return this.values;
    }

    @Override // gnu.trove.map.TCharIntMap
    public int[] values() {
        return this.m.values();
    }

    @Override // gnu.trove.map.TCharIntMap
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

    @Override // gnu.trove.map.TCharIntMap
    public char getNoEntryKey() {
        return this.m.getNoEntryKey();
    }

    @Override // gnu.trove.map.TCharIntMap
    public int getNoEntryValue() {
        return this.m.getNoEntryValue();
    }

    @Override // gnu.trove.map.TCharIntMap
    public boolean forEachKey(TCharProcedure procedure) {
        return this.m.forEachKey(procedure);
    }

    @Override // gnu.trove.map.TCharIntMap
    public boolean forEachValue(TIntProcedure procedure) {
        return this.m.forEachValue(procedure);
    }

    @Override // gnu.trove.map.TCharIntMap
    public boolean forEachEntry(TCharIntProcedure procedure) {
        return this.m.forEachEntry(procedure);
    }

    @Override // gnu.trove.map.TCharIntMap
    public TCharIntIterator iterator() {
        return new TCharIntIterator() { // from class: gnu.trove.impl.unmodifiable.TUnmodifiableCharIntMap.1
            TCharIntIterator iter;

            {
                this.iter = TUnmodifiableCharIntMap.this.m.iterator();
            }

            @Override // gnu.trove.iterator.TCharIntIterator
            public char key() {
                return this.iter.key();
            }

            @Override // gnu.trove.iterator.TCharIntIterator
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

            @Override // gnu.trove.iterator.TCharIntIterator
            public int setValue(int val) {
                throw new UnsupportedOperationException();
            }

            @Override // gnu.trove.iterator.TIterator, java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override // gnu.trove.map.TCharIntMap
    public int putIfAbsent(char key, int value) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TCharIntMap
    public void transformValues(TIntFunction function) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TCharIntMap
    public boolean retainEntries(TCharIntProcedure procedure) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TCharIntMap
    public boolean increment(char key) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TCharIntMap
    public boolean adjustValue(char key, int amount) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TCharIntMap
    public int adjustOrPutValue(char key, int adjust_amount, int put_amount) {
        throw new UnsupportedOperationException();
    }
}
