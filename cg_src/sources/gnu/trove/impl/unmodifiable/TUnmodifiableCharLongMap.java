package gnu.trove.impl.unmodifiable;

import gnu.trove.TCollections;
import gnu.trove.TLongCollection;
import gnu.trove.function.TLongFunction;
import gnu.trove.iterator.TCharLongIterator;
import gnu.trove.map.TCharLongMap;
import gnu.trove.procedure.TCharLongProcedure;
import gnu.trove.procedure.TCharProcedure;
import gnu.trove.procedure.TLongProcedure;
import gnu.trove.set.TCharSet;
import java.io.Serializable;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/impl/unmodifiable/TUnmodifiableCharLongMap.class */
public class TUnmodifiableCharLongMap implements TCharLongMap, Serializable {
    private static final long serialVersionUID = -1034234728574286014L;
    private final TCharLongMap m;
    private transient TCharSet keySet = null;
    private transient TLongCollection values = null;

    public TUnmodifiableCharLongMap(TCharLongMap m) {
        if (m == null) {
            throw new NullPointerException();
        }
        this.m = m;
    }

    @Override // gnu.trove.map.TCharLongMap
    public int size() {
        return this.m.size();
    }

    @Override // gnu.trove.map.TCharLongMap
    public boolean isEmpty() {
        return this.m.isEmpty();
    }

    @Override // gnu.trove.map.TCharLongMap
    public boolean containsKey(char key) {
        return this.m.containsKey(key);
    }

    @Override // gnu.trove.map.TCharLongMap
    public boolean containsValue(long val) {
        return this.m.containsValue(val);
    }

    @Override // gnu.trove.map.TCharLongMap
    public long get(char key) {
        return this.m.get(key);
    }

    @Override // gnu.trove.map.TCharLongMap
    public long put(char key, long value) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TCharLongMap
    public long remove(char key) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TCharLongMap
    public void putAll(TCharLongMap m) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TCharLongMap
    public void putAll(Map<? extends Character, ? extends Long> map) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TCharLongMap
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TCharLongMap
    public TCharSet keySet() {
        if (this.keySet == null) {
            this.keySet = TCollections.unmodifiableSet(this.m.keySet());
        }
        return this.keySet;
    }

    @Override // gnu.trove.map.TCharLongMap
    public char[] keys() {
        return this.m.keys();
    }

    @Override // gnu.trove.map.TCharLongMap
    public char[] keys(char[] array) {
        return this.m.keys(array);
    }

    @Override // gnu.trove.map.TCharLongMap
    public TLongCollection valueCollection() {
        if (this.values == null) {
            this.values = TCollections.unmodifiableCollection(this.m.valueCollection());
        }
        return this.values;
    }

    @Override // gnu.trove.map.TCharLongMap
    public long[] values() {
        return this.m.values();
    }

    @Override // gnu.trove.map.TCharLongMap
    public long[] values(long[] array) {
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

    @Override // gnu.trove.map.TCharLongMap
    public char getNoEntryKey() {
        return this.m.getNoEntryKey();
    }

    @Override // gnu.trove.map.TCharLongMap
    public long getNoEntryValue() {
        return this.m.getNoEntryValue();
    }

    @Override // gnu.trove.map.TCharLongMap
    public boolean forEachKey(TCharProcedure procedure) {
        return this.m.forEachKey(procedure);
    }

    @Override // gnu.trove.map.TCharLongMap
    public boolean forEachValue(TLongProcedure procedure) {
        return this.m.forEachValue(procedure);
    }

    @Override // gnu.trove.map.TCharLongMap
    public boolean forEachEntry(TCharLongProcedure procedure) {
        return this.m.forEachEntry(procedure);
    }

    @Override // gnu.trove.map.TCharLongMap
    public TCharLongIterator iterator() {
        return new TCharLongIterator() { // from class: gnu.trove.impl.unmodifiable.TUnmodifiableCharLongMap.1
            TCharLongIterator iter;

            {
                this.iter = TUnmodifiableCharLongMap.this.m.iterator();
            }

            @Override // gnu.trove.iterator.TCharLongIterator
            public char key() {
                return this.iter.key();
            }

            @Override // gnu.trove.iterator.TCharLongIterator
            public long value() {
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

            @Override // gnu.trove.iterator.TCharLongIterator
            public long setValue(long val) {
                throw new UnsupportedOperationException();
            }

            @Override // gnu.trove.iterator.TIterator, java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override // gnu.trove.map.TCharLongMap
    public long putIfAbsent(char key, long value) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TCharLongMap
    public void transformValues(TLongFunction function) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TCharLongMap
    public boolean retainEntries(TCharLongProcedure procedure) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TCharLongMap
    public boolean increment(char key) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TCharLongMap
    public boolean adjustValue(char key, long amount) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.map.TCharLongMap
    public long adjustOrPutValue(char key, long adjust_amount, long put_amount) {
        throw new UnsupportedOperationException();
    }
}
