package gnu.trove.impl.unmodifiable;

import gnu.trove.TCollections;
import gnu.trove.TShortCollection;
import gnu.trove.function.TShortFunction;
import gnu.trove.iterator.TDoubleShortIterator;
import gnu.trove.map.TDoubleShortMap;
import gnu.trove.procedure.TDoubleProcedure;
import gnu.trove.procedure.TDoubleShortProcedure;
import gnu.trove.procedure.TShortProcedure;
import gnu.trove.set.TDoubleSet;
import java.io.Serializable;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/impl/unmodifiable/TUnmodifiableDoubleShortMap.class */
public class TUnmodifiableDoubleShortMap implements TDoubleShortMap, Serializable {
    private static final long serialVersionUID = -1034234728574286014L;
    private final TDoubleShortMap m;
    private transient TDoubleSet keySet = null;
    private transient TShortCollection values = null;

    public TUnmodifiableDoubleShortMap(TDoubleShortMap m) {
        if (m == null) {
            throw new NullPointerException();
        }
        this.m = m;
    }

    @Override // gnu.trove.map.TDoubleShortMap
    public int size() {
        return this.m.size();
    }

    @Override // gnu.trove.map.TDoubleShortMap
    public boolean isEmpty() {
        return this.m.isEmpty();
    }

    @Override // gnu.trove.map.TDoubleShortMap
    public boolean containsKey(double key) {
        return this.m.containsKey(key);
    }

    @Override // gnu.trove.map.TDoubleShortMap
    public boolean containsValue(short val) {
        return this.m.containsValue(val);
    }

    @Override // gnu.trove.map.TDoubleShortMap
    public short get(double key) {
        return this.m.get(key);
    }

    @Override // gnu.trove.map.TDoubleShortMap
    public short put(double key, short value) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TDoubleShortMap
    public short remove(double key) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TDoubleShortMap
    public void putAll(TDoubleShortMap m) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TDoubleShortMap
    public void putAll(Map<? extends Double, ? extends Short> map) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TDoubleShortMap
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TDoubleShortMap
    public TDoubleSet keySet() {
        if (this.keySet == null) {
            this.keySet = TCollections.unmodifiableSet(this.m.keySet());
        }
        return this.keySet;
    }

    @Override // gnu.trove.map.TDoubleShortMap
    public double[] keys() {
        return this.m.keys();
    }

    @Override // gnu.trove.map.TDoubleShortMap
    public double[] keys(double[] array) {
        return this.m.keys(array);
    }

    @Override // gnu.trove.map.TDoubleShortMap
    public TShortCollection valueCollection() {
        if (this.values == null) {
            this.values = TCollections.unmodifiableCollection(this.m.valueCollection());
        }
        return this.values;
    }

    @Override // gnu.trove.map.TDoubleShortMap
    public short[] values() {
        return this.m.values();
    }

    @Override // gnu.trove.map.TDoubleShortMap
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

    @Override // gnu.trove.map.TDoubleShortMap
    public double getNoEntryKey() {
        return this.m.getNoEntryKey();
    }

    @Override // gnu.trove.map.TDoubleShortMap
    public short getNoEntryValue() {
        return this.m.getNoEntryValue();
    }

    @Override // gnu.trove.map.TDoubleShortMap
    public boolean forEachKey(TDoubleProcedure procedure) {
        return this.m.forEachKey(procedure);
    }

    @Override // gnu.trove.map.TDoubleShortMap
    public boolean forEachValue(TShortProcedure procedure) {
        return this.m.forEachValue(procedure);
    }

    @Override // gnu.trove.map.TDoubleShortMap
    public boolean forEachEntry(TDoubleShortProcedure procedure) {
        return this.m.forEachEntry(procedure);
    }

    @Override // gnu.trove.map.TDoubleShortMap
    public TDoubleShortIterator iterator() {
        return new TDoubleShortIterator() { // from class: gnu.trove.impl.unmodifiable.TUnmodifiableDoubleShortMap.1
            TDoubleShortIterator iter;

            {
                this.iter = TUnmodifiableDoubleShortMap.this.m.iterator();
            }

            @Override // gnu.trove.iterator.TDoubleShortIterator
            public double key() {
                return this.iter.key();
            }

            @Override // gnu.trove.iterator.TDoubleShortIterator
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

            @Override // gnu.trove.iterator.TDoubleShortIterator
            public short setValue(short val) {
                throw new UnsupportedOperationException();
            }

            @Override // gnu.trove.iterator.TIterator, java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override // gnu.trove.map.TDoubleShortMap
    public short putIfAbsent(double key, short value) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TDoubleShortMap
    public void transformValues(TShortFunction function) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TDoubleShortMap
    public boolean retainEntries(TDoubleShortProcedure procedure) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TDoubleShortMap
    public boolean increment(double key) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TDoubleShortMap
    public boolean adjustValue(double key, short amount) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TDoubleShortMap
    public short adjustOrPutValue(double key, short adjust_amount, short put_amount) {
        throw new UnsupportedOperationException();
    }
}
