package gnu.trove.map.hash;

import gnu.trove.TIntCollection;
import gnu.trove.function.TObjectFunction;
import gnu.trove.impl.Constants;
import gnu.trove.impl.HashFunctions;
import gnu.trove.impl.hash.THashPrimitiveIterator;
import gnu.trove.impl.hash.TIntHash;
import gnu.trove.iterator.TIntIterator;
import gnu.trove.iterator.TIntObjectIterator;
import gnu.trove.map.TIntObjectMap;
import gnu.trove.procedure.TIntObjectProcedure;
import gnu.trove.procedure.TIntProcedure;
import gnu.trove.procedure.TObjectProcedure;
import gnu.trove.set.TIntSet;
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
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TIntObjectHashMap.class */
public class TIntObjectHashMap<V> extends TIntHash implements TIntObjectMap<V>, Externalizable {
    static final long serialVersionUID = 1;
    private final TIntObjectProcedure<V> PUT_ALL_PROC;
    protected transient V[] _values;
    protected int no_entry_key;

    public TIntObjectHashMap() {
        this.PUT_ALL_PROC = new TIntObjectProcedure<V>() { // from class: gnu.trove.map.hash.TIntObjectHashMap.1
            @Override // gnu.trove.procedure.TIntObjectProcedure
            public boolean execute(int key, V value) {
                TIntObjectHashMap.this.put(key, value);
                return true;
            }
        };
    }

    public TIntObjectHashMap(int initialCapacity) {
        super(initialCapacity);
        this.PUT_ALL_PROC = new TIntObjectProcedure<V>() { // from class: gnu.trove.map.hash.TIntObjectHashMap.1
            @Override // gnu.trove.procedure.TIntObjectProcedure
            public boolean execute(int key, V value) {
                TIntObjectHashMap.this.put(key, value);
                return true;
            }
        };
        this.no_entry_key = Constants.DEFAULT_INT_NO_ENTRY_VALUE;
    }

    public TIntObjectHashMap(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
        this.PUT_ALL_PROC = new TIntObjectProcedure<V>() { // from class: gnu.trove.map.hash.TIntObjectHashMap.1
            @Override // gnu.trove.procedure.TIntObjectProcedure
            public boolean execute(int key, V value) {
                TIntObjectHashMap.this.put(key, value);
                return true;
            }
        };
        this.no_entry_key = Constants.DEFAULT_INT_NO_ENTRY_VALUE;
    }

    public TIntObjectHashMap(int initialCapacity, float loadFactor, int noEntryKey) {
        super(initialCapacity, loadFactor);
        this.PUT_ALL_PROC = new TIntObjectProcedure<V>() { // from class: gnu.trove.map.hash.TIntObjectHashMap.1
            @Override // gnu.trove.procedure.TIntObjectProcedure
            public boolean execute(int key, V value) {
                TIntObjectHashMap.this.put(key, value);
                return true;
            }
        };
        this.no_entry_key = noEntryKey;
    }

    public TIntObjectHashMap(TIntObjectMap<? extends V> map) {
        this(map.size(), 0.5f, map.getNoEntryKey());
        putAll(map);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // gnu.trove.impl.hash.TIntHash, gnu.trove.impl.hash.TPrimitiveHash, gnu.trove.impl.hash.THash
    public int setUp(int initialCapacity) {
        int capacity = super.setUp(initialCapacity);
        this._values = (V[]) new Object[capacity];
        return capacity;
    }

    @Override // gnu.trove.impl.hash.THash
    protected void rehash(int newCapacity) {
        int oldCapacity = this._set.length;
        int[] oldKeys = this._set;
        V[] oldVals = this._values;
        byte[] oldStates = this._states;
        this._set = new int[newCapacity];
        this._values = (V[]) new Object[newCapacity];
        this._states = new byte[newCapacity];
        int i = oldCapacity;
        while (true) {
            int i2 = i;
            i--;
            if (i2 > 0) {
                if (oldStates[i] == 1) {
                    int o = oldKeys[i];
                    int index = insertKey(o);
                    this._values[index] = oldVals[i];
                }
            } else {
                return;
            }
        }
    }

    @Override // gnu.trove.map.TIntObjectMap
    public int getNoEntryKey() {
        return this.no_entry_key;
    }

    @Override // gnu.trove.map.TIntObjectMap
    public boolean containsKey(int key) {
        return contains(key);
    }

    @Override // gnu.trove.map.TIntObjectMap
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

    @Override // gnu.trove.map.TIntObjectMap
    public V get(int key) {
        int index = index(key);
        if (index < 0) {
            return null;
        }
        return this._values[index];
    }

    @Override // gnu.trove.map.TIntObjectMap
    public V put(int key, V value) {
        int index = insertKey(key);
        return doPut(value, index);
    }

    @Override // gnu.trove.map.TIntObjectMap
    public V putIfAbsent(int key, V value) {
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

    @Override // gnu.trove.map.TIntObjectMap
    public V remove(int key) {
        V prev = null;
        int index = index(key);
        if (index >= 0) {
            prev = this._values[index];
            removeAt(index);
        }
        return prev;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // gnu.trove.impl.hash.TIntHash, gnu.trove.impl.hash.TPrimitiveHash, gnu.trove.impl.hash.THash
    public void removeAt(int index) {
        this._values[index] = null;
        super.removeAt(index);
    }

    @Override // gnu.trove.map.TIntObjectMap
    public void putAll(Map<? extends Integer, ? extends V> map) {
        Set<? extends Map.Entry<? extends Integer, ? extends V>> set = map.entrySet();
        for (Map.Entry<? extends Integer, ? extends V> entry : set) {
            put(entry.getKey().intValue(), entry.getValue());
        }
    }

    @Override // gnu.trove.map.TIntObjectMap
    public void putAll(TIntObjectMap<? extends V> map) {
        map.forEachEntry(this.PUT_ALL_PROC);
    }

    @Override // gnu.trove.impl.hash.THash, gnu.trove.map.TObjectByteMap
    public void clear() {
        super.clear();
        Arrays.fill(this._set, 0, this._set.length, this.no_entry_key);
        Arrays.fill(this._states, 0, this._states.length, (byte) 0);
        Arrays.fill(this._values, 0, this._values.length, (Object) null);
    }

    @Override // gnu.trove.map.TIntObjectMap
    public TIntSet keySet() {
        return new KeyView();
    }

    @Override // gnu.trove.map.TIntObjectMap
    public int[] keys() {
        int[] keys = new int[size()];
        int[] k = this._set;
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

    @Override // gnu.trove.map.TIntObjectMap
    public int[] keys(int[] dest) {
        if (dest.length < this._size) {
            dest = new int[this._size];
        }
        int[] k = this._set;
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

    @Override // gnu.trove.map.TIntObjectMap
    public Collection<V> valueCollection() {
        return new ValueView();
    }

    @Override // gnu.trove.map.TIntObjectMap
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
    @Override // gnu.trove.map.TIntObjectMap
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

    @Override // gnu.trove.map.TIntObjectMap
    public TIntObjectIterator<V> iterator() {
        return new TIntObjectHashIterator(this);
    }

    @Override // gnu.trove.map.TIntObjectMap
    public boolean forEachKey(TIntProcedure procedure) {
        return forEach(procedure);
    }

    @Override // gnu.trove.map.TIntObjectMap
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

    @Override // gnu.trove.map.TIntObjectMap
    public boolean forEachEntry(TIntObjectProcedure<? super V> procedure) {
        byte[] states = this._states;
        int[] keys = this._set;
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

    @Override // gnu.trove.map.TIntObjectMap
    public boolean retainEntries(TIntObjectProcedure<? super V> procedure) {
        boolean modified = false;
        byte[] states = this._states;
        int[] keys = this._set;
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

    @Override // gnu.trove.map.TIntObjectMap
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

    @Override // gnu.trove.map.TIntObjectMap
    public boolean equals(Object other) {
        if (!(other instanceof TIntObjectMap)) {
            return false;
        }
        TIntObjectMap that = (TIntObjectMap) other;
        if (that.size() != size()) {
            return false;
        }
        try {
            TIntObjectIterator iter = iterator();
            while (iter.hasNext()) {
                iter.advance();
                int key = iter.key();
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

    @Override // gnu.trove.map.TIntObjectMap
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

    /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TIntObjectHashMap$KeyView.class */
    class KeyView implements TIntSet {
        KeyView() {
        }

        @Override // gnu.trove.set.TIntSet, gnu.trove.TIntCollection
        public int getNoEntryValue() {
            return TIntObjectHashMap.this.no_entry_key;
        }

        @Override // gnu.trove.set.TIntSet, gnu.trove.TIntCollection
        public int size() {
            return TIntObjectHashMap.this._size;
        }

        @Override // gnu.trove.set.TIntSet, gnu.trove.TIntCollection
        public boolean isEmpty() {
            return TIntObjectHashMap.this._size == 0;
        }

        @Override // gnu.trove.set.TIntSet, gnu.trove.TIntCollection
        public boolean contains(int entry) {
            return TIntObjectHashMap.this.containsKey(entry);
        }

        @Override // gnu.trove.set.TIntSet, gnu.trove.TIntCollection
        public TIntIterator iterator() {
            return new TIntHashIterator(TIntObjectHashMap.this);
        }

        @Override // gnu.trove.set.TIntSet, gnu.trove.TIntCollection
        public int[] toArray() {
            return TIntObjectHashMap.this.keys();
        }

        @Override // gnu.trove.set.TIntSet, gnu.trove.TIntCollection
        public int[] toArray(int[] dest) {
            return TIntObjectHashMap.this.keys(dest);
        }

        @Override // gnu.trove.set.TIntSet, gnu.trove.TIntCollection
        public boolean add(int entry) {
            throw new UnsupportedOperationException();
        }

        @Override // gnu.trove.set.TIntSet, gnu.trove.TIntCollection
        public boolean remove(int entry) {
            return null != TIntObjectHashMap.this.remove(entry);
        }

        @Override // gnu.trove.set.TIntSet, gnu.trove.TIntCollection
        public boolean containsAll(Collection<?> collection) {
            for (Object element : collection) {
                if (!TIntObjectHashMap.this.containsKey(((Integer) element).intValue())) {
                    return false;
                }
            }
            return true;
        }

        @Override // gnu.trove.set.TIntSet, gnu.trove.TIntCollection
        public boolean containsAll(TIntCollection collection) {
            if (collection == this) {
                return true;
            }
            TIntIterator iter = collection.iterator();
            while (iter.hasNext()) {
                if (!TIntObjectHashMap.this.containsKey(iter.next())) {
                    return false;
                }
            }
            return true;
        }

        @Override // gnu.trove.set.TIntSet, gnu.trove.TIntCollection
        public boolean containsAll(int[] array) {
            for (int element : array) {
                if (!TIntObjectHashMap.this.containsKey(element)) {
                    return false;
                }
            }
            return true;
        }

        @Override // gnu.trove.set.TIntSet, gnu.trove.TIntCollection
        public boolean addAll(Collection<? extends Integer> collection) {
            throw new UnsupportedOperationException();
        }

        @Override // gnu.trove.set.TIntSet, gnu.trove.TIntCollection
        public boolean addAll(TIntCollection collection) {
            throw new UnsupportedOperationException();
        }

        @Override // gnu.trove.set.TIntSet, gnu.trove.TIntCollection
        public boolean addAll(int[] array) {
            throw new UnsupportedOperationException();
        }

        @Override // gnu.trove.set.TIntSet, gnu.trove.TIntCollection
        public boolean retainAll(Collection<?> collection) {
            boolean modified = false;
            TIntIterator iter = iterator();
            while (iter.hasNext()) {
                if (!collection.contains(Integer.valueOf(iter.next()))) {
                    iter.remove();
                    modified = true;
                }
            }
            return modified;
        }

        @Override // gnu.trove.set.TIntSet, gnu.trove.TIntCollection
        public boolean retainAll(TIntCollection collection) {
            if (this == collection) {
                return false;
            }
            boolean modified = false;
            TIntIterator iter = iterator();
            while (iter.hasNext()) {
                if (!collection.contains(iter.next())) {
                    iter.remove();
                    modified = true;
                }
            }
            return modified;
        }

        @Override // gnu.trove.set.TIntSet, gnu.trove.TIntCollection
        public boolean retainAll(int[] array) {
            boolean changed = false;
            Arrays.sort(array);
            int[] set = TIntObjectHashMap.this._set;
            byte[] states = TIntObjectHashMap.this._states;
            int i = set.length;
            while (true) {
                int i2 = i;
                i--;
                if (i2 > 0) {
                    if (states[i] == 1 && Arrays.binarySearch(array, set[i]) < 0) {
                        TIntObjectHashMap.this.removeAt(i);
                        changed = true;
                    }
                } else {
                    return changed;
                }
            }
        }

        @Override // gnu.trove.set.TIntSet, gnu.trove.TIntCollection
        public boolean removeAll(Collection<?> collection) {
            boolean changed = false;
            for (Object element : collection) {
                if (element instanceof Integer) {
                    int c = ((Integer) element).intValue();
                    if (remove(c)) {
                        changed = true;
                    }
                }
            }
            return changed;
        }

        @Override // gnu.trove.set.TIntSet, gnu.trove.TIntCollection
        public boolean removeAll(TIntCollection collection) {
            if (collection == this) {
                clear();
                return true;
            }
            boolean changed = false;
            TIntIterator iter = collection.iterator();
            while (iter.hasNext()) {
                int element = iter.next();
                if (remove(element)) {
                    changed = true;
                }
            }
            return changed;
        }

        @Override // gnu.trove.set.TIntSet, gnu.trove.TIntCollection
        public boolean removeAll(int[] array) {
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

        @Override // gnu.trove.set.TIntSet, gnu.trove.TIntCollection
        public void clear() {
            TIntObjectHashMap.this.clear();
        }

        @Override // gnu.trove.set.TIntSet, gnu.trove.TIntCollection
        public boolean forEach(TIntProcedure procedure) {
            return TIntObjectHashMap.this.forEachKey(procedure);
        }

        @Override // gnu.trove.set.TIntSet, gnu.trove.TIntCollection
        public boolean equals(Object other) {
            if (!(other instanceof TIntSet)) {
                return false;
            }
            TIntSet that = (TIntSet) other;
            if (that.size() != size()) {
                return false;
            }
            int i = TIntObjectHashMap.this._states.length;
            while (true) {
                int i2 = i;
                i--;
                if (i2 > 0) {
                    if (TIntObjectHashMap.this._states[i] == 1 && !that.contains(TIntObjectHashMap.this._set[i])) {
                        return false;
                    }
                } else {
                    return true;
                }
            }
        }

        @Override // gnu.trove.set.TIntSet, gnu.trove.TIntCollection
        public int hashCode() {
            int hashcode = 0;
            int i = TIntObjectHashMap.this._states.length;
            while (true) {
                int i2 = i;
                i--;
                if (i2 > 0) {
                    if (TIntObjectHashMap.this._states[i] == 1) {
                        hashcode += HashFunctions.hash(TIntObjectHashMap.this._set[i]);
                    }
                } else {
                    return hashcode;
                }
            }
        }

        public String toString() {
            StringBuilder buf = new StringBuilder("{");
            boolean first = true;
            int i = TIntObjectHashMap.this._states.length;
            while (true) {
                int i2 = i;
                i--;
                if (i2 > 0) {
                    if (TIntObjectHashMap.this._states[i] == 1) {
                        if (first) {
                            first = false;
                        } else {
                            buf.append(",");
                        }
                        buf.append(TIntObjectHashMap.this._set[i]);
                    }
                } else {
                    return buf.toString();
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TIntObjectHashMap$KeyView$TIntHashIterator.class */
        public class TIntHashIterator extends THashPrimitiveIterator implements TIntIterator {
            private final TIntHash _hash;

            public TIntHashIterator(TIntHash hash) {
                super(hash);
                this._hash = hash;
            }

            @Override // gnu.trove.iterator.TIntIterator
            public int next() {
                moveToNextIndex();
                return this._hash._set[this._index];
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TIntObjectHashMap$ValueView.class */
    protected class ValueView extends TIntObjectHashMap<V>.MapBackedView<V> {
        protected ValueView() {
            super();
        }

        @Override // gnu.trove.map.hash.TIntObjectHashMap.MapBackedView, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public Iterator<V> iterator() {
            return new TIntObjectValueHashIterator(TIntObjectHashMap.this) { // from class: gnu.trove.map.hash.TIntObjectHashMap.ValueView.1
                @Override // gnu.trove.map.hash.TIntObjectHashMap.ValueView.TIntObjectValueHashIterator
                protected V objectAtIndex(int index) {
                    return TIntObjectHashMap.this._values[index];
                }
            };
        }

        @Override // gnu.trove.map.hash.TIntObjectHashMap.MapBackedView
        public boolean containsElement(V value) {
            return TIntObjectHashMap.this.containsValue(value);
        }

        @Override // gnu.trove.map.hash.TIntObjectHashMap.MapBackedView
        public boolean removeElement(V value) {
            V[] values = TIntObjectHashMap.this._values;
            byte[] states = TIntObjectHashMap.this._states;
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
            TIntObjectHashMap.this.removeAt(i);
            return true;
        }

        /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TIntObjectHashMap$ValueView$TIntObjectValueHashIterator.class */
        class TIntObjectValueHashIterator extends THashPrimitiveIterator implements Iterator<V> {
            protected final TIntObjectHashMap _map;

            public TIntObjectValueHashIterator(TIntObjectHashMap map) {
                super(map);
                this._map = map;
            }

            protected V objectAtIndex(int index) {
                byte[] states = TIntObjectHashMap.this._states;
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

    /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TIntObjectHashMap$MapBackedView.class */
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
            TIntObjectHashMap.this.clear();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean add(E obj) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return TIntObjectHashMap.this.size();
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
            return TIntObjectHashMap.this.isEmpty();
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
    /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TIntObjectHashMap$TIntObjectHashIterator.class */
    public class TIntObjectHashIterator<V> extends THashPrimitiveIterator implements TIntObjectIterator<V> {
        private final TIntObjectHashMap<V> _map;

        public TIntObjectHashIterator(TIntObjectHashMap<V> map) {
            super(map);
            this._map = map;
        }

        @Override // gnu.trove.iterator.TAdvancingIterator
        public void advance() {
            moveToNextIndex();
        }

        @Override // gnu.trove.iterator.TIntObjectIterator
        public int key() {
            return this._map._set[this._index];
        }

        @Override // gnu.trove.iterator.TIntObjectIterator
        public V value() {
            return this._map._values[this._index];
        }

        @Override // gnu.trove.iterator.TIntObjectIterator
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
        out.writeInt(this.no_entry_key);
        out.writeInt(this._size);
        int i = this._states.length;
        while (true) {
            int i2 = i;
            i--;
            if (i2 > 0) {
                if (this._states[i] == 1) {
                    out.writeInt(this._set[i]);
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
        this.no_entry_key = in.readInt();
        int size = in.readInt();
        setUp(size);
        while (true) {
            int i = size;
            size--;
            if (i > 0) {
                int key = in.readInt();
                put(key, in.readObject());
            } else {
                return;
            }
        }
    }

    public String toString() {
        final StringBuilder buf = new StringBuilder("{");
        forEachEntry(new TIntObjectProcedure<V>() { // from class: gnu.trove.map.hash.TIntObjectHashMap.2
            private boolean first = true;

            @Override // gnu.trove.procedure.TIntObjectProcedure
            public boolean execute(int key, Object value) {
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
