package gnu.trove.impl.unmodifiable;

import gnu.trove.TCollections;
import gnu.trove.TIntCollection;
import gnu.trove.function.TIntFunction;
import gnu.trove.iterator.TDoubleIntIterator;
import gnu.trove.map.TDoubleIntMap;
import gnu.trove.procedure.TDoubleIntProcedure;
import gnu.trove.procedure.TDoubleProcedure;
import gnu.trove.procedure.TIntProcedure;
import gnu.trove.set.TDoubleSet;
import java.io.Serializable;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/impl/unmodifiable/TUnmodifiableDoubleIntMap.class */
public class TUnmodifiableDoubleIntMap implements TDoubleIntMap, Serializable {
    private static final long serialVersionUID = -1034234728574286014L;
    private final TDoubleIntMap m;
    private transient TDoubleSet keySet = null;
    private transient TIntCollection values = null;

    public TUnmodifiableDoubleIntMap(TDoubleIntMap m) {
        if (m == null) {
            throw new NullPointerException();
        }
        this.m = m;
    }

    @Override // gnu.trove.map.TDoubleIntMap
    public int size() {
        return this.m.size();
    }

    @Override // gnu.trove.map.TDoubleIntMap
    public boolean isEmpty() {
        return this.m.isEmpty();
    }

    @Override // gnu.trove.map.TDoubleIntMap
    public boolean containsKey(double key) {
        return this.m.containsKey(key);
    }

    @Override // gnu.trove.map.TDoubleIntMap
    public boolean containsValue(int val) {
        return this.m.containsValue(val);
    }

    @Override // gnu.trove.map.TDoubleIntMap
    public int get(double key) {
        return this.m.get(key);
    }

    @Override // gnu.trove.map.TDoubleIntMap
    public int put(double key, int value) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TDoubleIntMap
    public int remove(double key) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TDoubleIntMap
    public void putAll(TDoubleIntMap m) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TDoubleIntMap
    public void putAll(Map<? extends Double, ? extends Integer> map) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TDoubleIntMap
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TDoubleIntMap
    public TDoubleSet keySet() {
        if (this.keySet == null) {
            this.keySet = TCollections.unmodifiableSet(this.m.keySet());
        }
        return this.keySet;
    }

    @Override // gnu.trove.map.TDoubleIntMap
    public double[] keys() {
        return this.m.keys();
    }

    @Override // gnu.trove.map.TDoubleIntMap
    public double[] keys(double[] array) {
        return this.m.keys(array);
    }

    @Override // gnu.trove.map.TDoubleIntMap
    public TIntCollection valueCollection() {
        if (this.values == null) {
            this.values = TCollections.unmodifiableCollection(this.m.valueCollection());
        }
        return this.values;
    }

    @Override // gnu.trove.map.TDoubleIntMap
    public int[] values() {
        return this.m.values();
    }

    @Override // gnu.trove.map.TDoubleIntMap
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

    @Override // gnu.trove.map.TDoubleIntMap
    public double getNoEntryKey() {
        return this.m.getNoEntryKey();
    }

    @Override // gnu.trove.map.TDoubleIntMap
    public int getNoEntryValue() {
        return this.m.getNoEntryValue();
    }

    @Override // gnu.trove.map.TDoubleIntMap
    public boolean forEachKey(TDoubleProcedure procedure) {
        return this.m.forEachKey(procedure);
    }

    @Override // gnu.trove.map.TDoubleIntMap
    public boolean forEachValue(TIntProcedure procedure) {
        return this.m.forEachValue(procedure);
    }

    @Override // gnu.trove.map.TDoubleIntMap
    public boolean forEachEntry(TDoubleIntProcedure procedure) {
        return this.m.forEachEntry(procedure);
    }

    @Override // gnu.trove.map.TDoubleIntMap
    public TDoubleIntIterator iterator() {
        return new TDoubleIntIterator() { // from class: gnu.trove.impl.unmodifiable.TUnmodifiableDoubleIntMap.1
            TDoubleIntIterator iter;

            {
                this.iter = TUnmodifiableDoubleIntMap.this.m.iterator();
            }

            @Override // gnu.trove.iterator.TDoubleIntIterator
            public double key() {
                return this.iter.key();
            }

            @Override // gnu.trove.iterator.TDoubleIntIterator
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

            @Override // gnu.trove.iterator.TDoubleIntIterator
            public int setValue(int val) {
                throw new UnsupportedOperationException();
            }

            @Override // gnu.trove.iterator.TIterator, java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override // gnu.trove.map.TDoubleIntMap
    public int putIfAbsent(double key, int value) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TDoubleIntMap
    public void transformValues(TIntFunction function) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TDoubleIntMap
    public boolean retainEntries(TDoubleIntProcedure procedure) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TDoubleIntMap
    public boolean increment(double key) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TDoubleIntMap
    public boolean adjustValue(double key, int amount) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TDoubleIntMap
    public int adjustOrPutValue(double key, int adjust_amount, int put_amount) {
        throw new UnsupportedOperationException();
    }
}
