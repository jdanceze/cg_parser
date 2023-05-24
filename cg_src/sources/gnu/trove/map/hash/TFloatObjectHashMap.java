package gnu.trove.map.hash;

import gnu.trove.TFloatCollection;
import gnu.trove.function.TObjectFunction;
import gnu.trove.impl.Constants;
import gnu.trove.impl.HashFunctions;
import gnu.trove.impl.hash.TFloatHash;
import gnu.trove.impl.hash.THashPrimitiveIterator;
import gnu.trove.iterator.TFloatIterator;
import gnu.trove.iterator.TFloatObjectIterator;
import gnu.trove.map.TFloatObjectMap;
import gnu.trove.procedure.TFloatObjectProcedure;
import gnu.trove.procedure.TFloatProcedure;
import gnu.trove.procedure.TObjectProcedure;
import gnu.trove.set.TFloatSet;
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
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TFloatObjectHashMap.class */
public class TFloatObjectHashMap<V> extends TFloatHash implements TFloatObjectMap<V>, Externalizable {
    static final long serialVersionUID = 1;
    private final TFloatObjectProcedure<V> PUT_ALL_PROC;
    protected transient V[] _values;
    protected float no_entry_key;

    public TFloatObjectHashMap() {
        this.PUT_ALL_PROC = new TFloatObjectProcedure<V>() { // from class: gnu.trove.map.hash.TFloatObjectHashMap.1
            @Override // gnu.trove.procedure.TFloatObjectProcedure
            public boolean execute(float key, V value) {
                TFloatObjectHashMap.this.put(key, value);
                return true;
            }
        };
    }

    public TFloatObjectHashMap(int initialCapacity) {
        super(initialCapacity);
        this.PUT_ALL_PROC = new TFloatObjectProcedure<V>() { // from class: gnu.trove.map.hash.TFloatObjectHashMap.1
            @Override // gnu.trove.procedure.TFloatObjectProcedure
            public boolean execute(float key, V value) {
                TFloatObjectHashMap.this.put(key, value);
                return true;
            }
        };
        this.no_entry_key = Constants.DEFAULT_FLOAT_NO_ENTRY_VALUE;
    }

    public TFloatObjectHashMap(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
        this.PUT_ALL_PROC = new TFloatObjectProcedure<V>() { // from class: gnu.trove.map.hash.TFloatObjectHashMap.1
            @Override // gnu.trove.procedure.TFloatObjectProcedure
            public boolean execute(float key, V value) {
                TFloatObjectHashMap.this.put(key, value);
                return true;
            }
        };
        this.no_entry_key = Constants.DEFAULT_FLOAT_NO_ENTRY_VALUE;
    }

    public TFloatObjectHashMap(int initialCapacity, float loadFactor, float noEntryKey) {
        super(initialCapacity, loadFactor);
        this.PUT_ALL_PROC = new TFloatObjectProcedure<V>() { // from class: gnu.trove.map.hash.TFloatObjectHashMap.1
            @Override // gnu.trove.procedure.TFloatObjectProcedure
            public boolean execute(float key, V value) {
                TFloatObjectHashMap.this.put(key, value);
                return true;
            }
        };
        this.no_entry_key = noEntryKey;
    }

    public TFloatObjectHashMap(TFloatObjectMap<? extends V> map) {
        this(map.size(), 0.5f, map.getNoEntryKey());
        putAll(map);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // gnu.trove.impl.hash.TFloatHash, gnu.trove.impl.hash.TPrimitiveHash, gnu.trove.impl.hash.THash
    public int setUp(int initialCapacity) {
        int capacity = super.setUp(initialCapacity);
        this._values = (V[]) new Object[capacity];
        return capacity;
    }

    @Override // gnu.trove.impl.hash.THash
    protected void rehash(int newCapacity) {
        int oldCapacity = this._set.length;
        float[] oldKeys = this._set;
        V[] oldVals = this._values;
        byte[] oldStates = this._states;
        this._set = new float[newCapacity];
        this._values = (V[]) new Object[newCapacity];
        this._states = new byte[newCapacity];
        int i = oldCapacity;
        while (true) {
            int i2 = i;
            i--;
            if (i2 > 0) {
                if (oldStates[i] == 1) {
                    float o = oldKeys[i];
                    int index = insertKey(o);
                    this._values[index] = oldVals[i];
                }
            } else {
                return;
            }
        }
    }

    @Override // gnu.trove.map.TFloatObjectMap
    public float getNoEntryKey() {
        return this.no_entry_key;
    }

    @Override // gnu.trove.map.TFloatObjectMap
    public boolean containsKey(float key) {
        return contains(key);
    }

    @Override // gnu.trove.map.TFloatObjectMap
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

    @Override // gnu.trove.map.TFloatObjectMap
    public V get(float key) {
        int index = index(key);
        if (index < 0) {
            return null;
        }
        return this._values[index];
    }

    @Override // gnu.trove.map.TFloatObjectMap
    public V put(float key, V value) {
        int index = insertKey(key);
        return doPut(value, index);
    }

    @Override // gnu.trove.map.TFloatObjectMap
    public V putIfAbsent(float key, V value) {
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

    @Override // gnu.trove.map.TFloatObjectMap
    public V remove(float key) {
        V prev = null;
        int index = index(key);
        if (index >= 0) {
            prev = this._values[index];
            removeAt(index);
        }
        return prev;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // gnu.trove.impl.hash.TFloatHash, gnu.trove.impl.hash.TPrimitiveHash, gnu.trove.impl.hash.THash
    public void removeAt(int index) {
        this._values[index] = null;
        super.removeAt(index);
    }

    @Override // gnu.trove.map.TFloatObjectMap
    public void putAll(Map<? extends Float, ? extends V> map) {
        Set<? extends Map.Entry<? extends Float, ? extends V>> set = map.entrySet();
        for (Map.Entry<? extends Float, ? extends V> entry : set) {
            put(entry.getKey().floatValue(), entry.getValue());
        }
    }

    @Override // gnu.trove.map.TFloatObjectMap
    public void putAll(TFloatObjectMap<? extends V> map) {
        map.forEachEntry(this.PUT_ALL_PROC);
    }

    @Override // gnu.trove.impl.hash.THash, gnu.trove.map.TObjectByteMap
    public void clear() {
        super.clear();
        Arrays.fill(this._set, 0, this._set.length, this.no_entry_key);
        Arrays.fill(this._states, 0, this._states.length, (byte) 0);
        Arrays.fill(this._values, 0, this._values.length, (Object) null);
    }

    @Override // gnu.trove.map.TFloatObjectMap
    public TFloatSet keySet() {
        return new KeyView();
    }

    @Override // gnu.trove.map.TFloatObjectMap
    public float[] keys() {
        float[] keys = new float[size()];
        float[] k = this._set;
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

    @Override // gnu.trove.map.TFloatObjectMap
    public float[] keys(float[] dest) {
        if (dest.length < this._size) {
            dest = new float[this._size];
        }
        float[] k = this._set;
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

    @Override // gnu.trove.map.TFloatObjectMap
    public Collection<V> valueCollection() {
        return new ValueView();
    }

    @Override // gnu.trove.map.TFloatObjectMap
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
    @Override // gnu.trove.map.TFloatObjectMap
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

    @Override // gnu.trove.map.TFloatObjectMap
    public TFloatObjectIterator<V> iterator() {
        return new TFloatObjectHashIterator(this);
    }

    @Override // gnu.trove.map.TFloatObjectMap
    public boolean forEachKey(TFloatProcedure procedure) {
        return forEach(procedure);
    }

    @Override // gnu.trove.map.TFloatObjectMap
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

    @Override // gnu.trove.map.TFloatObjectMap
    public boolean forEachEntry(TFloatObjectProcedure<? super V> procedure) {
        byte[] states = this._states;
        float[] keys = this._set;
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

    @Override // gnu.trove.map.TFloatObjectMap
    public boolean retainEntries(TFloatObjectProcedure<? super V> procedure) {
        boolean modified = false;
        byte[] states = this._states;
        float[] keys = this._set;
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

    @Override // gnu.trove.map.TFloatObjectMap
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

    @Override // gnu.trove.map.TFloatObjectMap
    public boolean equals(Object other) {
        if (!(other instanceof TFloatObjectMap)) {
            return false;
        }
        TFloatObjectMap that = (TFloatObjectMap) other;
        if (that.size() != size()) {
            return false;
        }
        try {
            TFloatObjectIterator iter = iterator();
            while (iter.hasNext()) {
                iter.advance();
                float key = iter.key();
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

    @Override // gnu.trove.map.TFloatObjectMap
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

    /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TFloatObjectHashMap$KeyView.class */
    class KeyView implements TFloatSet {
        KeyView() {
        }

        @Override // gnu.trove.set.TFloatSet, gnu.trove.TFloatCollection
        public float getNoEntryValue() {
            return TFloatObjectHashMap.this.no_entry_key;
        }

        @Override // gnu.trove.set.TFloatSet, gnu.trove.TFloatCollection
        public int size() {
            return TFloatObjectHashMap.this._size;
        }

        @Override // gnu.trove.set.TFloatSet, gnu.trove.TFloatCollection
        public boolean isEmpty() {
            return TFloatObjectHashMap.this._size == 0;
        }

        @Override // gnu.trove.set.TFloatSet, gnu.trove.TFloatCollection
        public boolean contains(float entry) {
            return TFloatObjectHashMap.this.containsKey(entry);
        }

        @Override // gnu.trove.set.TFloatSet, gnu.trove.TFloatCollection
        public TFloatIterator iterator() {
            return new TFloatHashIterator(TFloatObjectHashMap.this);
        }

        @Override // gnu.trove.set.TFloatSet, gnu.trove.TFloatCollection
        public float[] toArray() {
            return TFloatObjectHashMap.this.keys();
        }

        @Override // gnu.trove.set.TFloatSet, gnu.trove.TFloatCollection
        public float[] toArray(float[] dest) {
            return TFloatObjectHashMap.this.keys(dest);
        }

        @Override // gnu.trove.set.TFloatSet, gnu.trove.TFloatCollection
        public boolean add(float entry) {
            throw new UnsupportedOperationException();
        }

        @Override // gnu.trove.set.TFloatSet, gnu.trove.TFloatCollection
        public boolean remove(float entry) {
            return null != TFloatObjectHashMap.this.remove(entry);
        }

        @Override // gnu.trove.set.TFloatSet, gnu.trove.TFloatCollection
        public boolean containsAll(Collection<?> collection) {
            for (Object element : collection) {
                if (!TFloatObjectHashMap.this.containsKey(((Float) element).floatValue())) {
                    return false;
                }
            }
            return true;
        }

        @Override // gnu.trove.set.TFloatSet, gnu.trove.TFloatCollection
        public boolean containsAll(TFloatCollection collection) {
            if (collection == this) {
                return true;
            }
            TFloatIterator iter = collection.iterator();
            while (iter.hasNext()) {
                if (!TFloatObjectHashMap.this.containsKey(iter.next())) {
                    return false;
                }
            }
            return true;
        }

        @Override // gnu.trove.set.TFloatSet, gnu.trove.TFloatCollection
        public boolean containsAll(float[] array) {
            for (float element : array) {
                if (!TFloatObjectHashMap.this.containsKey(element)) {
                    return false;
                }
            }
            return true;
        }

        @Override // gnu.trove.set.TFloatSet, gnu.trove.TFloatCollection
        public boolean addAll(Collection<? extends Float> collection) {
            throw new UnsupportedOperationException();
        }

        @Override // gnu.trove.set.TFloatSet, gnu.trove.TFloatCollection
        public boolean addAll(TFloatCollection collection) {
            throw new UnsupportedOperationException();
        }

        @Override // gnu.trove.set.TFloatSet, gnu.trove.TFloatCollection
        public boolean addAll(float[] array) {
            throw new UnsupportedOperationException();
        }

        @Override // gnu.trove.set.TFloatSet, gnu.trove.TFloatCollection
        public boolean retainAll(Collection<?> collection) {
            boolean modified = false;
            TFloatIterator iter = iterator();
            while (iter.hasNext()) {
                if (!collection.contains(Float.valueOf(iter.next()))) {
                    iter.remove();
                    modified = true;
                }
            }
            return modified;
        }

        @Override // gnu.trove.set.TFloatSet, gnu.trove.TFloatCollection
        public boolean retainAll(TFloatCollection collection) {
            if (this == collection) {
                return false;
            }
            boolean modified = false;
            TFloatIterator iter = iterator();
            while (iter.hasNext()) {
                if (!collection.contains(iter.next())) {
                    iter.remove();
                    modified = true;
                }
            }
            return modified;
        }

        @Override // gnu.trove.set.TFloatSet, gnu.trove.TFloatCollection
        public boolean retainAll(float[] array) {
            boolean changed = false;
            Arrays.sort(array);
            float[] set = TFloatObjectHashMap.this._set;
            byte[] states = TFloatObjectHashMap.this._states;
            int i = set.length;
            while (true) {
                int i2 = i;
                i--;
                if (i2 > 0) {
                    if (states[i] == 1 && Arrays.binarySearch(array, set[i]) < 0) {
                        TFloatObjectHashMap.this.removeAt(i);
                        changed = true;
                    }
                } else {
                    return changed;
                }
            }
        }

        @Override // gnu.trove.set.TFloatSet, gnu.trove.TFloatCollection
        public boolean removeAll(Collection<?> collection) {
            boolean changed = false;
            for (Object element : collection) {
                if (element instanceof Float) {
                    float c = ((Float) element).floatValue();
                    if (remove(c)) {
                        changed = true;
                    }
                }
            }
            return changed;
        }

        @Override // gnu.trove.set.TFloatSet, gnu.trove.TFloatCollection
        public boolean removeAll(TFloatCollection collection) {
            if (collection == this) {
                clear();
                return true;
            }
            boolean changed = false;
            TFloatIterator iter = collection.iterator();
            while (iter.hasNext()) {
                float element = iter.next();
                if (remove(element)) {
                    changed = true;
                }
            }
            return changed;
        }

        @Override // gnu.trove.set.TFloatSet, gnu.trove.TFloatCollection
        public boolean removeAll(float[] array) {
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

        @Override // gnu.trove.set.TFloatSet, gnu.trove.TFloatCollection
        public void clear() {
            TFloatObjectHashMap.this.clear();
        }

        @Override // gnu.trove.set.TFloatSet, gnu.trove.TFloatCollection
        public boolean forEach(TFloatProcedure procedure) {
            return TFloatObjectHashMap.this.forEachKey(procedure);
        }

        @Override // gnu.trove.set.TFloatSet, gnu.trove.TFloatCollection
        public boolean equals(Object other) {
            if (!(other instanceof TFloatSet)) {
                return false;
            }
            TFloatSet that = (TFloatSet) other;
            if (that.size() != size()) {
                return false;
            }
            int i = TFloatObjectHashMap.this._states.length;
            while (true) {
                int i2 = i;
                i--;
                if (i2 > 0) {
                    if (TFloatObjectHashMap.this._states[i] == 1 && !that.contains(TFloatObjectHashMap.this._set[i])) {
                        return false;
                    }
                } else {
                    return true;
                }
            }
        }

        @Override // gnu.trove.set.TFloatSet, gnu.trove.TFloatCollection
        public int hashCode() {
            int hashcode = 0;
            int i = TFloatObjectHashMap.this._states.length;
            while (true) {
                int i2 = i;
                i--;
                if (i2 > 0) {
                    if (TFloatObjectHashMap.this._states[i] == 1) {
                        hashcode += HashFunctions.hash(TFloatObjectHashMap.this._set[i]);
                    }
                } else {
                    return hashcode;
                }
            }
        }

        public String toString() {
            StringBuilder buf = new StringBuilder("{");
            boolean first = true;
            int i = TFloatObjectHashMap.this._states.length;
            while (true) {
                int i2 = i;
                i--;
                if (i2 > 0) {
                    if (TFloatObjectHashMap.this._states[i] == 1) {
                        if (first) {
                            first = false;
                        } else {
                            buf.append(",");
                        }
                        buf.append(TFloatObjectHashMap.this._set[i]);
                    }
                } else {
                    return buf.toString();
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TFloatObjectHashMap$KeyView$TFloatHashIterator.class */
        public class TFloatHashIterator extends THashPrimitiveIterator implements TFloatIterator {
            private final TFloatHash _hash;

            public TFloatHashIterator(TFloatHash hash) {
                super(hash);
                this._hash = hash;
            }

            @Override // gnu.trove.iterator.TFloatIterator
            public float next() {
                moveToNextIndex();
                return this._hash._set[this._index];
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TFloatObjectHashMap$ValueView.class */
    protected class ValueView extends TFloatObjectHashMap<V>.MapBackedView<V> {
        protected ValueView() {
            super();
        }

        @Override // gnu.trove.map.hash.TFloatObjectHashMap.MapBackedView, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public Iterator<V> iterator() {
            return new TFloatObjectValueHashIterator(TFloatObjectHashMap.this) { // from class: gnu.trove.map.hash.TFloatObjectHashMap.ValueView.1
                @Override // gnu.trove.map.hash.TFloatObjectHashMap.ValueView.TFloatObjectValueHashIterator
                protected V objectAtIndex(int index) {
                    return TFloatObjectHashMap.this._values[index];
                }
            };
        }

        @Override // gnu.trove.map.hash.TFloatObjectHashMap.MapBackedView
        public boolean containsElement(V value) {
            return TFloatObjectHashMap.this.containsValue(value);
        }

        @Override // gnu.trove.map.hash.TFloatObjectHashMap.MapBackedView
        public boolean removeElement(V value) {
            V[] values = TFloatObjectHashMap.this._values;
            byte[] states = TFloatObjectHashMap.this._states;
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
            TFloatObjectHashMap.this.removeAt(i);
            return true;
        }

        /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TFloatObjectHashMap$ValueView$TFloatObjectValueHashIterator.class */
        class TFloatObjectValueHashIterator extends THashPrimitiveIterator implements Iterator<V> {
            protected final TFloatObjectHashMap _map;

            public TFloatObjectValueHashIterator(TFloatObjectHashMap map) {
                super(map);
                this._map = map;
            }

            protected V objectAtIndex(int index) {
                byte[] states = TFloatObjectHashMap.this._states;
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

    /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TFloatObjectHashMap$MapBackedView.class */
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
            TFloatObjectHashMap.this.clear();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean add(E obj) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return TFloatObjectHashMap.this.size();
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
            return TFloatObjectHashMap.this.isEmpty();
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
    /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TFloatObjectHashMap$TFloatObjectHashIterator.class */
    public class TFloatObjectHashIterator<V> extends THashPrimitiveIterator implements TFloatObjectIterator<V> {
        private final TFloatObjectHashMap<V> _map;

        public TFloatObjectHashIterator(TFloatObjectHashMap<V> map) {
            super(map);
            this._map = map;
        }

        @Override // gnu.trove.iterator.TAdvancingIterator
        public void advance() {
            moveToNextIndex();
        }

        @Override // gnu.trove.iterator.TFloatObjectIterator
        public float key() {
            return this._map._set[this._index];
        }

        @Override // gnu.trove.iterator.TFloatObjectIterator
        public V value() {
            return this._map._values[this._index];
        }

        @Override // gnu.trove.iterator.TFloatObjectIterator
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
        out.writeFloat(this.no_entry_key);
        out.writeInt(this._size);
        int i = this._states.length;
        while (true) {
            int i2 = i;
            i--;
            if (i2 > 0) {
                if (this._states[i] == 1) {
                    out.writeFloat(this._set[i]);
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
        this.no_entry_key = in.readFloat();
        int size = in.readInt();
        setUp(size);
        while (true) {
            int i = size;
            size--;
            if (i > 0) {
                float key = in.readFloat();
                put(key, in.readObject());
            } else {
                return;
            }
        }
    }

    public String toString() {
        final StringBuilder buf = new StringBuilder("{");
        forEachEntry(new TFloatObjectProcedure<V>() { // from class: gnu.trove.map.hash.TFloatObjectHashMap.2
            private boolean first = true;

            @Override // gnu.trove.procedure.TFloatObjectProcedure
            public boolean execute(float key, Object value) {
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
