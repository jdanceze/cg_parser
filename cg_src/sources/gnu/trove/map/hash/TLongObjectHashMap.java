package gnu.trove.map.hash;

import gnu.trove.TLongCollection;
import gnu.trove.function.TObjectFunction;
import gnu.trove.impl.Constants;
import gnu.trove.impl.HashFunctions;
import gnu.trove.impl.hash.THashPrimitiveIterator;
import gnu.trove.impl.hash.TLongHash;
import gnu.trove.iterator.TLongIterator;
import gnu.trove.iterator.TLongObjectIterator;
import gnu.trove.map.TLongObjectMap;
import gnu.trove.procedure.TLongObjectProcedure;
import gnu.trove.procedure.TLongProcedure;
import gnu.trove.procedure.TObjectProcedure;
import gnu.trove.set.TLongSet;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.lang.reflect.Array;
import java.util.AbstractSet;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TLongObjectHashMap.class */
public class TLongObjectHashMap<V> extends TLongHash implements TLongObjectMap<V>, Externalizable {
    static final long serialVersionUID = 1;
    private final TLongObjectProcedure<V> PUT_ALL_PROC;
    protected transient V[] _values;
    protected long no_entry_key;

    public TLongObjectHashMap() {
        this.PUT_ALL_PROC = new TLongObjectProcedure<V>() { // from class: gnu.trove.map.hash.TLongObjectHashMap.1
            @Override // gnu.trove.procedure.TLongObjectProcedure
            public boolean execute(long key, V value) {
                TLongObjectHashMap.this.put(key, value);
                return true;
            }
        };
    }

    public TLongObjectHashMap(int initialCapacity) {
        super(initialCapacity);
        this.PUT_ALL_PROC = new TLongObjectProcedure<V>() { // from class: gnu.trove.map.hash.TLongObjectHashMap.1
            @Override // gnu.trove.procedure.TLongObjectProcedure
            public boolean execute(long key, V value) {
                TLongObjectHashMap.this.put(key, value);
                return true;
            }
        };
        this.no_entry_key = Constants.DEFAULT_LONG_NO_ENTRY_VALUE;
    }

    public TLongObjectHashMap(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
        this.PUT_ALL_PROC = new TLongObjectProcedure<V>() { // from class: gnu.trove.map.hash.TLongObjectHashMap.1
            @Override // gnu.trove.procedure.TLongObjectProcedure
            public boolean execute(long key, V value) {
                TLongObjectHashMap.this.put(key, value);
                return true;
            }
        };
        this.no_entry_key = Constants.DEFAULT_LONG_NO_ENTRY_VALUE;
    }

    public TLongObjectHashMap(int initialCapacity, float loadFactor, long noEntryKey) {
        super(initialCapacity, loadFactor);
        this.PUT_ALL_PROC = new TLongObjectProcedure<V>() { // from class: gnu.trove.map.hash.TLongObjectHashMap.1
            @Override // gnu.trove.procedure.TLongObjectProcedure
            public boolean execute(long key, V value) {
                TLongObjectHashMap.this.put(key, value);
                return true;
            }
        };
        this.no_entry_key = noEntryKey;
    }

    public TLongObjectHashMap(TLongObjectMap<? extends V> map) {
        this(map.size(), 0.5f, map.getNoEntryKey());
        putAll(map);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // gnu.trove.impl.hash.TLongHash, gnu.trove.impl.hash.TPrimitiveHash, gnu.trove.impl.hash.THash
    public int setUp(int initialCapacity) {
        int capacity = super.setUp(initialCapacity);
        this._values = (V[]) new Object[capacity];
        return capacity;
    }

    @Override // gnu.trove.impl.hash.THash
    protected void rehash(int newCapacity) {
        int oldCapacity = this._set.length;
        long[] oldKeys = this._set;
        V[] oldVals = this._values;
        byte[] oldStates = this._states;
        this._set = new long[newCapacity];
        this._values = (V[]) new Object[newCapacity];
        this._states = new byte[newCapacity];
        int i = oldCapacity;
        while (true) {
            int i2 = i;
            i--;
            if (i2 > 0) {
                if (oldStates[i] == 1) {
                    long o = oldKeys[i];
                    int index = insertKey(o);
                    this._values[index] = oldVals[i];
                }
            } else {
                return;
            }
        }
    }

    @Override // gnu.trove.map.TLongObjectMap
    public long getNoEntryKey() {
        return this.no_entry_key;
    }

    @Override // gnu.trove.map.TLongObjectMap
    public boolean containsKey(long key) {
        return contains(key);
    }

    @Override // gnu.trove.map.TLongObjectMap
    public boolean containsValue(Object val) {
        byte[] states = this._states;
        V[] vals = this._values;
        if (null == val) {
            int i = vals.length;
            while (true) {
                int i2 = i;
                i--;
                if (i2 > 0) {
                    if (states[i] == 1 && null == vals[i]) {
                        return true;
                    }
                } else {
                    return false;
                }
            }
        } else {
            int i3 = vals.length;
            while (true) {
                int i4 = i3;
                i3--;
                if (i4 > 0) {
                    if (states[i3] == 1 && (val == vals[i3] || val.equals(vals[i3]))) {
                        return true;
                    }
                } else {
                    return false;
                }
            }
        }
    }

    @Override // gnu.trove.map.TLongObjectMap
    public V get(long key) {
        int index = index(key);
        if (index < 0) {
            return null;
        }
        return this._values[index];
    }

    @Override // gnu.trove.map.TLongObjectMap
    public V put(long key, V value) {
        int index = insertKey(key);
        return doPut(value, index);
    }

    @Override // gnu.trove.map.TLongObjectMap
    public V putIfAbsent(long key, V value) {
        int index = insertKey(key);
        if (index < 0) {
            return this._values[(-index) - 1];
        }
        return doPut(value, index);
    }

    private V doPut(V value, int index) {
        V previous = null;
        boolean isNewMapping = true;
        if (index < 0) {
            index = (-index) - 1;
            previous = this._values[index];
            isNewMapping = false;
        }
        this._values[index] = value;
        if (isNewMapping) {
            postInsertHook(this.consumeFreeSlot);
        }
        return previous;
    }

    @Override // gnu.trove.map.TLongObjectMap
    public V remove(long key) {
        V prev = null;
        int index = index(key);
        if (index >= 0) {
            prev = this._values[index];
            removeAt(index);
        }
        return prev;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // gnu.trove.impl.hash.TLongHash, gnu.trove.impl.hash.TPrimitiveHash, gnu.trove.impl.hash.THash
    public void removeAt(int index) {
        this._values[index] = null;
        super.removeAt(index);
    }

    @Override // gnu.trove.map.TLongObjectMap
    public void putAll(Map<? extends Long, ? extends V> map) {
        Set<? extends Map.Entry<? extends Long, ? extends V>> set = map.entrySet();
        for (Map.Entry<? extends Long, ? extends V> entry : set) {
            put(entry.getKey().longValue(), entry.getValue());
        }
    }

    @Override // gnu.trove.map.TLongObjectMap
    public void putAll(TLongObjectMap<? extends V> map) {
        map.forEachEntry(this.PUT_ALL_PROC);
    }

    @Override // gnu.trove.impl.hash.THash, gnu.trove.map.TObjectByteMap
    public void clear() {
        super.clear();
        Arrays.fill(this._set, 0, this._set.length, this.no_entry_key);
        Arrays.fill(this._states, 0, this._states.length, (byte) 0);
        Arrays.fill(this._values, 0, this._values.length, (Object) null);
    }

    @Override // gnu.trove.map.TLongObjectMap
    public TLongSet keySet() {
        return new KeyView();
    }

    @Override // gnu.trove.map.TLongObjectMap
    public long[] keys() {
        long[] keys = new long[size()];
        long[] k = this._set;
        byte[] states = this._states;
        int i = k.length;
        int j = 0;
        while (true) {
            int i2 = i;
            i--;
            if (i2 > 0) {
                if (states[i] == 1) {
                    int i3 = j;
                    j++;
                    keys[i3] = k[i];
                }
            } else {
                return keys;
            }
        }
    }

    @Override // gnu.trove.map.TLongObjectMap
    public long[] keys(long[] dest) {
        if (dest.length < this._size) {
            dest = new long[this._size];
        }
        long[] k = this._set;
        byte[] states = this._states;
        int i = k.length;
        int j = 0;
        while (true) {
            int i2 = i;
            i--;
            if (i2 > 0) {
                if (states[i] == 1) {
                    int i3 = j;
                    j++;
                    dest[i3] = k[i];
                }
            } else {
                return dest;
            }
        }
    }

    @Override // gnu.trove.map.TLongObjectMap
    public Collection<V> valueCollection() {
        return new ValueView();
    }

    @Override // gnu.trove.map.TLongObjectMap
    public Object[] values() {
        Object[] vals = new Object[size()];
        V[] v = this._values;
        byte[] states = this._states;
        int i = v.length;
        int j = 0;
        while (true) {
            int i2 = i;
            i--;
            if (i2 > 0) {
                if (states[i] == 1) {
                    int i3 = j;
                    j++;
                    vals[i3] = v[i];
                }
            } else {
                return vals;
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v19, types: [java.lang.Object[]] */
    @Override // gnu.trove.map.TLongObjectMap
    public V[] values(V[] dest) {
        if (dest.length < this._size) {
            dest = (Object[]) Array.newInstance(dest.getClass().getComponentType(), this._size);
        }
        V[] v = this._values;
        byte[] states = this._states;
        int i = v.length;
        int j = 0;
        while (true) {
            int i2 = i;
            i--;
            if (i2 > 0) {
                if (states[i] == 1) {
                    int i3 = j;
                    j++;
                    dest[i3] = v[i];
                }
            } else {
                return dest;
            }
        }
    }

    @Override // gnu.trove.map.TLongObjectMap
    public TLongObjectIterator<V> iterator() {
        return new TLongObjectHashIterator(this);
    }

    @Override // gnu.trove.map.TLongObjectMap
    public boolean forEachKey(TLongProcedure procedure) {
        return forEach(procedure);
    }

    @Override // gnu.trove.map.TLongObjectMap
    public boolean forEachValue(TObjectProcedure<? super V> procedure) {
        byte[] states = this._states;
        V[] values = this._values;
        int i = values.length;
        while (true) {
            int i2 = i;
            i--;
            if (i2 > 0) {
                if (states[i] == 1 && !procedure.execute((Object) values[i])) {
                    return false;
                }
            } else {
                return true;
            }
        }
    }

    @Override // gnu.trove.map.TLongObjectMap
    public boolean forEachEntry(TLongObjectProcedure<? super V> procedure) {
        byte[] states = this._states;
        long[] keys = this._set;
        V[] values = this._values;
        int i = keys.length;
        while (true) {
            int i2 = i;
            i--;
            if (i2 > 0) {
                if (states[i] == 1 && !procedure.execute(keys[i], (Object) values[i])) {
                    return false;
                }
            } else {
                return true;
            }
        }
    }

    @Override // gnu.trove.map.TLongObjectMap
    public boolean retainEntries(TLongObjectProcedure<? super V> procedure) {
        boolean modified = false;
        byte[] states = this._states;
        long[] keys = this._set;
        V[] values = this._values;
        tempDisableAutoCompaction();
        try {
            int i = keys.length;
            while (true) {
                int i2 = i;
                i--;
                if (i2 > 0) {
                    if (states[i] == 1 && !procedure.execute(keys[i], (Object) values[i])) {
                        removeAt(i);
                        modified = true;
                    }
                } else {
                    return modified;
                }
            }
        } finally {
            reenableAutoCompaction(true);
        }
    }

    @Override // gnu.trove.map.TLongObjectMap
    public void transformValues(TObjectFunction<V, V> function) {
        byte[] states = this._states;
        V[] values = this._values;
        int i = values.length;
        while (true) {
            int i2 = i;
            i--;
            if (i2 > 0) {
                if (states[i] == 1) {
                    values[i] = function.execute(values[i]);
                }
            } else {
                return;
            }
        }
    }

    @Override // gnu.trove.map.TLongObjectMap
    public boolean equals(Object other) {
        if (!(other instanceof TLongObjectMap)) {
            return false;
        }
        TLongObjectMap that = (TLongObjectMap) other;
        if (that.size() != size()) {
            return false;
        }
        try {
            TLongObjectIterator iter = iterator();
            while (iter.hasNext()) {
                iter.advance();
                long key = iter.key();
                Object value = iter.value();
                if (value == null) {
                    if (that.get(key) != null || !that.containsKey(key)) {
                        return false;
                    }
                } else if (!value.equals(that.get(key))) {
                    return false;
                }
            }
            return true;
        } catch (ClassCastException e) {
            return true;
        }
    }

    @Override // gnu.trove.map.TLongObjectMap
    public int hashCode() {
        int hashcode = 0;
        V[] values = this._values;
        byte[] states = this._states;
        int i = values.length;
        while (true) {
            int i2 = i;
            i--;
            if (i2 > 0) {
                if (states[i] == 1) {
                    hashcode += HashFunctions.hash(this._set[i]) ^ (values[i] == null ? 0 : values[i].hashCode());
                }
            } else {
                return hashcode;
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TLongObjectHashMap$KeyView.class */
    class KeyView implements TLongSet {
        KeyView() {
        }

        @Override // gnu.trove.set.TLongSet, gnu.trove.TLongCollection
        public long getNoEntryValue() {
            return TLongObjectHashMap.this.no_entry_key;
        }

        @Override // gnu.trove.set.TLongSet, gnu.trove.TLongCollection
        public int size() {
            return TLongObjectHashMap.this._size;
        }

        @Override // gnu.trove.set.TLongSet, gnu.trove.TLongCollection
        public boolean isEmpty() {
            return TLongObjectHashMap.this._size == 0;
        }

        @Override // gnu.trove.set.TLongSet, gnu.trove.TLongCollection
        public boolean contains(long entry) {
            return TLongObjectHashMap.this.containsKey(entry);
        }

        @Override // gnu.trove.set.TLongSet, gnu.trove.TLongCollection
        public TLongIterator iterator() {
            return new TLongHashIterator(TLongObjectHashMap.this);
        }

        @Override // gnu.trove.set.TLongSet, gnu.trove.TLongCollection
        public long[] toArray() {
            return TLongObjectHashMap.this.keys();
        }

        @Override // gnu.trove.set.TLongSet, gnu.trove.TLongCollection
        public long[] toArray(long[] dest) {
            return TLongObjectHashMap.this.keys(dest);
        }

        @Override // gnu.trove.set.TLongSet, gnu.trove.TLongCollection
        public boolean add(long entry) {
            throw new UnsupportedOperationException();
        }

        @Override // gnu.trove.set.TLongSet, gnu.trove.TLongCollection
        public boolean remove(long entry) {
            return null != TLongObjectHashMap.this.remove(entry);
        }

        @Override // gnu.trove.set.TLongSet, gnu.trove.TLongCollection
        public boolean containsAll(Collection<?> collection) {
            for (Object element : collection) {
                if (!TLongObjectHashMap.this.containsKey(((Long) element).longValue())) {
                    return false;
                }
            }
            return true;
        }

        @Override // gnu.trove.set.TLongSet, gnu.trove.TLongCollection
        public boolean containsAll(TLongCollection collection) {
            if (collection == this) {
                return true;
            }
            TLongIterator iter = collection.iterator();
            while (iter.hasNext()) {
                if (!TLongObjectHashMap.this.containsKey(iter.next())) {
                    return false;
                }
            }
            return true;
        }

        @Override // gnu.trove.set.TLongSet, gnu.trove.TLongCollection
        public boolean containsAll(long[] array) {
            for (long element : array) {
                if (!TLongObjectHashMap.this.containsKey(element)) {
                    return false;
                }
            }
            return true;
        }

        @Override // gnu.trove.set.TLongSet, gnu.trove.TLongCollection
        public boolean addAll(Collection<? extends Long> collection) {
            throw new UnsupportedOperationException();
        }

        @Override // gnu.trove.set.TLongSet, gnu.trove.TLongCollection
        public boolean addAll(TLongCollection collection) {
            throw new UnsupportedOperationException();
        }

        @Override // gnu.trove.set.TLongSet, gnu.trove.TLongCollection
        public boolean addAll(long[] array) {
            throw new UnsupportedOperationException();
        }

        @Override // gnu.trove.set.TLongSet, gnu.trove.TLongCollection
        public boolean retainAll(Collection<?> collection) {
            boolean modified = false;
            TLongIterator iter = iterator();
            while (iter.hasNext()) {
                if (!collection.contains(Long.valueOf(iter.next()))) {
                    iter.remove();
                    modified = true;
                }
            }
            return modified;
        }

        @Override // gnu.trove.set.TLongSet, gnu.trove.TLongCollection
        public boolean retainAll(TLongCollection collection) {
            if (this == collection) {
                return false;
            }
            boolean modified = false;
            TLongIterator iter = iterator();
            while (iter.hasNext()) {
                if (!collection.contains(iter.next())) {
                    iter.remove();
                    modified = true;
                }
            }
            return modified;
        }

        @Override // gnu.trove.set.TLongSet, gnu.trove.TLongCollection
        public boolean retainAll(long[] array) {
            boolean changed = false;
            Arrays.sort(array);
            long[] set = TLongObjectHashMap.this._set;
            byte[] states = TLongObjectHashMap.this._states;
            int i = set.length;
            while (true) {
                int i2 = i;
                i--;
                if (i2 > 0) {
                    if (states[i] == 1 && Arrays.binarySearch(array, set[i]) < 0) {
                        TLongObjectHashMap.this.removeAt(i);
                        changed = true;
                    }
                } else {
                    return changed;
                }
            }
        }

        @Override // gnu.trove.set.TLongSet, gnu.trove.TLongCollection
        public boolean removeAll(Collection<?> collection) {
            boolean changed = false;
            for (Object element : collection) {
                if (element instanceof Long) {
                    long c = ((Long) element).longValue();
                    if (remove(c)) {
                        changed = true;
                    }
                }
            }
            return changed;
        }

        @Override // gnu.trove.set.TLongSet, gnu.trove.TLongCollection
        public boolean removeAll(TLongCollection collection) {
            if (collection == this) {
                clear();
                return true;
            }
            boolean changed = false;
            TLongIterator iter = collection.iterator();
            while (iter.hasNext()) {
                long element = iter.next();
                if (remove(element)) {
                    changed = true;
                }
            }
            return changed;
        }

        @Override // gnu.trove.set.TLongSet, gnu.trove.TLongCollection
        public boolean removeAll(long[] array) {
            boolean changed = false;
            int i = array.length;
            while (true) {
                int i2 = i;
                i--;
                if (i2 > 0) {
                    if (remove(array[i])) {
                        changed = true;
                    }
                } else {
                    return changed;
                }
            }
        }

        @Override // gnu.trove.set.TLongSet, gnu.trove.TLongCollection
        public void clear() {
            TLongObjectHashMap.this.clear();
        }

        @Override // gnu.trove.set.TLongSet, gnu.trove.TLongCollection
        public boolean forEach(TLongProcedure procedure) {
            return TLongObjectHashMap.this.forEachKey(procedure);
        }

        @Override // gnu.trove.set.TLongSet, gnu.trove.TLongCollection
        public boolean equals(Object other) {
            if (!(other instanceof TLongSet)) {
                return false;
            }
            TLongSet that = (TLongSet) other;
            if (that.size() != size()) {
                return false;
            }
            int i = TLongObjectHashMap.this._states.length;
            while (true) {
                int i2 = i;
                i--;
                if (i2 > 0) {
                    if (TLongObjectHashMap.this._states[i] == 1 && !that.contains(TLongObjectHashMap.this._set[i])) {
                        return false;
                    }
                } else {
                    return true;
                }
            }
        }

        @Override // gnu.trove.set.TLongSet, gnu.trove.TLongCollection
        public int hashCode() {
            int hashcode = 0;
            int i = TLongObjectHashMap.this._states.length;
            while (true) {
                int i2 = i;
                i--;
                if (i2 > 0) {
                    if (TLongObjectHashMap.this._states[i] == 1) {
                        hashcode += HashFunctions.hash(TLongObjectHashMap.this._set[i]);
                    }
                } else {
                    return hashcode;
                }
            }
        }

        public String toString() {
            StringBuilder buf = new StringBuilder("{");
            boolean first = true;
            int i = TLongObjectHashMap.this._states.length;
            while (true) {
                int i2 = i;
                i--;
                if (i2 > 0) {
                    if (TLongObjectHashMap.this._states[i] == 1) {
                        if (first) {
                            first = false;
                        } else {
                            buf.append(",");
                        }
                        buf.append(TLongObjectHashMap.this._set[i]);
                    }
                } else {
                    return buf.toString();
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TLongObjectHashMap$KeyView$TLongHashIterator.class */
        public class TLongHashIterator extends THashPrimitiveIterator implements TLongIterator {
            private final TLongHash _hash;

            public TLongHashIterator(TLongHash hash) {
                super(hash);
                this._hash = hash;
            }

            @Override // gnu.trove.iterator.TLongIterator
            public long next() {
                moveToNextIndex();
                return this._hash._set[this._index];
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TLongObjectHashMap$ValueView.class */
    protected class ValueView extends TLongObjectHashMap<V>.MapBackedView<V> {
        protected ValueView() {
            super();
        }

        @Override // gnu.trove.map.hash.TLongObjectHashMap.MapBackedView, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public Iterator<V> iterator() {
            return new TLongObjectValueHashIterator(TLongObjectHashMap.this) { // from class: gnu.trove.map.hash.TLongObjectHashMap.ValueView.1
                @Override // gnu.trove.map.hash.TLongObjectHashMap.ValueView.TLongObjectValueHashIterator
                protected V objectAtIndex(int index) {
                    return TLongObjectHashMap.this._values[index];
                }
            };
        }

        @Override // gnu.trove.map.hash.TLongObjectHashMap.MapBackedView
        public boolean containsElement(V value) {
            return TLongObjectHashMap.this.containsValue(value);
        }

        @Override // gnu.trove.map.hash.TLongObjectHashMap.MapBackedView
        public boolean removeElement(V value) {
            V[] values = TLongObjectHashMap.this._values;
            byte[] states = TLongObjectHashMap.this._states;
            int i = values.length;
            while (true) {
                int i2 = i;
                i--;
                if (i2 > 0) {
                    if (states[i] != 1 || (value != values[i] && (null == values[i] || !values[i].equals(value)))) {
                    }
                } else {
                    return false;
                }
            }
            TLongObjectHashMap.this.removeAt(i);
            return true;
        }

        /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TLongObjectHashMap$ValueView$TLongObjectValueHashIterator.class */
        class TLongObjectValueHashIterator extends THashPrimitiveIterator implements Iterator<V> {
            protected final TLongObjectHashMap _map;

            public TLongObjectValueHashIterator(TLongObjectHashMap map) {
                super(map);
                this._map = map;
            }

            protected V objectAtIndex(int index) {
                byte[] states = TLongObjectHashMap.this._states;
                V v = this._map._values[index];
                if (states[index] != 1) {
                    return null;
                }
                return v;
            }

            @Override // java.util.Iterator
            public V next() {
                moveToNextIndex();
                return this._map._values[this._index];
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TLongObjectHashMap$MapBackedView.class */
    private abstract class MapBackedView<E> extends AbstractSet<E> implements Set<E>, Iterable<E> {
        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public abstract Iterator<E> iterator();

        public abstract boolean removeElement(E e);

        public abstract boolean containsElement(E e);

        private MapBackedView() {
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(Object key) {
            return containsElement(key);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(Object o) {
            return removeElement(o);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public void clear() {
            TLongObjectHashMap.this.clear();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean add(E obj) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return TLongObjectHashMap.this.size();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public Object[] toArray() {
            Object[] result = new Object[size()];
            Iterator<E> e = iterator();
            int i = 0;
            while (e.hasNext()) {
                result[i] = e.next();
                i++;
            }
            return result;
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r0v19, types: [java.lang.Object[]] */
        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public <T> T[] toArray(T[] a) {
            int size = size();
            if (a.length < size) {
                a = (Object[]) Array.newInstance(a.getClass().getComponentType(), size);
            }
            Iterator<E> it = iterator();
            Object[] result = a;
            for (int i = 0; i < size; i++) {
                result[i] = it.next();
            }
            if (a.length > size) {
                a[size] = null;
            }
            return a;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean isEmpty() {
            return TLongObjectHashMap.this.isEmpty();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean addAll(Collection<? extends E> collection) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean retainAll(Collection<?> collection) {
            boolean changed = false;
            Iterator<E> i = iterator();
            while (i.hasNext()) {
                if (!collection.contains(i.next())) {
                    i.remove();
                    changed = true;
                }
            }
            return changed;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TLongObjectHashMap$TLongObjectHashIterator.class */
    public class TLongObjectHashIterator<V> extends THashPrimitiveIterator implements TLongObjectIterator<V> {
        private final TLongObjectHashMap<V> _map;

        public TLongObjectHashIterator(TLongObjectHashMap<V> map) {
            super(map);
            this._map = map;
        }

        @Override // gnu.trove.iterator.TAdvancingIterator
        public void advance() {
            moveToNextIndex();
        }

        @Override // gnu.trove.iterator.TLongObjectIterator
        public long key() {
            return this._map._set[this._index];
        }

        @Override // gnu.trove.iterator.TLongObjectIterator
        public V value() {
            return this._map._values[this._index];
        }

        @Override // gnu.trove.iterator.TLongObjectIterator
        public V setValue(V val) {
            V old = value();
            this._map._values[this._index] = val;
            return old;
        }
    }

    @Override // gnu.trove.impl.hash.THash, java.io.Externalizable
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeByte(0);
        super.writeExternal(out);
        out.writeLong(this.no_entry_key);
        out.writeInt(this._size);
        int i = this._states.length;
        while (true) {
            int i2 = i;
            i--;
            if (i2 > 0) {
                if (this._states[i] == 1) {
                    out.writeLong(this._set[i]);
                    out.writeObject(this._values[i]);
                }
            } else {
                return;
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // gnu.trove.impl.hash.THash, java.io.Externalizable
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        in.readByte();
        super.readExternal(in);
        this.no_entry_key = in.readLong();
        int size = in.readInt();
        setUp(size);
        while (true) {
            int i = size;
            size--;
            if (i > 0) {
                long key = in.readLong();
                put(key, in.readObject());
            } else {
                return;
            }
        }
    }

    public String toString() {
        final StringBuilder buf = new StringBuilder("{");
        forEachEntry(new TLongObjectProcedure<V>() { // from class: gnu.trove.map.hash.TLongObjectHashMap.2
            private boolean first = true;

            @Override // gnu.trove.procedure.TLongObjectProcedure
            public boolean execute(long key, Object value) {
                if (this.first) {
                    this.first = false;
                } else {
                    buf.append(",");
                }
                buf.append(key);
                buf.append("=");
                buf.append(value);
                return true;
            }
        });
        buf.append("}");
        return buf.toString();
    }
}
