package gnu.trove.map.hash;

import gnu.trove.TCharCollection;
import gnu.trove.function.TObjectFunction;
import gnu.trove.impl.Constants;
import gnu.trove.impl.HashFunctions;
import gnu.trove.impl.hash.TCharHash;
import gnu.trove.impl.hash.THashPrimitiveIterator;
import gnu.trove.iterator.TCharIterator;
import gnu.trove.iterator.TCharObjectIterator;
import gnu.trove.map.TCharObjectMap;
import gnu.trove.procedure.TCharObjectProcedure;
import gnu.trove.procedure.TCharProcedure;
import gnu.trove.procedure.TObjectProcedure;
import gnu.trove.set.TCharSet;
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
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TCharObjectHashMap.class */
public class TCharObjectHashMap<V> extends TCharHash implements TCharObjectMap<V>, Externalizable {
    static final long serialVersionUID = 1;
    private final TCharObjectProcedure<V> PUT_ALL_PROC;
    protected transient V[] _values;
    protected char no_entry_key;

    public TCharObjectHashMap() {
        this.PUT_ALL_PROC = new TCharObjectProcedure<V>() { // from class: gnu.trove.map.hash.TCharObjectHashMap.1
            @Override // gnu.trove.procedure.TCharObjectProcedure
            public boolean execute(char key, V value) {
                TCharObjectHashMap.this.put(key, value);
                return true;
            }
        };
    }

    public TCharObjectHashMap(int initialCapacity) {
        super(initialCapacity);
        this.PUT_ALL_PROC = new TCharObjectProcedure<V>() { // from class: gnu.trove.map.hash.TCharObjectHashMap.1
            @Override // gnu.trove.procedure.TCharObjectProcedure
            public boolean execute(char key, V value) {
                TCharObjectHashMap.this.put(key, value);
                return true;
            }
        };
        this.no_entry_key = Constants.DEFAULT_CHAR_NO_ENTRY_VALUE;
    }

    public TCharObjectHashMap(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
        this.PUT_ALL_PROC = new TCharObjectProcedure<V>() { // from class: gnu.trove.map.hash.TCharObjectHashMap.1
            @Override // gnu.trove.procedure.TCharObjectProcedure
            public boolean execute(char key, V value) {
                TCharObjectHashMap.this.put(key, value);
                return true;
            }
        };
        this.no_entry_key = Constants.DEFAULT_CHAR_NO_ENTRY_VALUE;
    }

    public TCharObjectHashMap(int initialCapacity, float loadFactor, char noEntryKey) {
        super(initialCapacity, loadFactor);
        this.PUT_ALL_PROC = new TCharObjectProcedure<V>() { // from class: gnu.trove.map.hash.TCharObjectHashMap.1
            @Override // gnu.trove.procedure.TCharObjectProcedure
            public boolean execute(char key, V value) {
                TCharObjectHashMap.this.put(key, value);
                return true;
            }
        };
        this.no_entry_key = noEntryKey;
    }

    public TCharObjectHashMap(TCharObjectMap<? extends V> map) {
        this(map.size(), 0.5f, map.getNoEntryKey());
        putAll(map);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // gnu.trove.impl.hash.TCharHash, gnu.trove.impl.hash.TPrimitiveHash, gnu.trove.impl.hash.THash
    public int setUp(int initialCapacity) {
        int capacity = super.setUp(initialCapacity);
        this._values = (V[]) new Object[capacity];
        return capacity;
    }

    @Override // gnu.trove.impl.hash.THash
    protected void rehash(int newCapacity) {
        int oldCapacity = this._set.length;
        char[] oldKeys = this._set;
        V[] oldVals = this._values;
        byte[] oldStates = this._states;
        this._set = new char[newCapacity];
        this._values = (V[]) new Object[newCapacity];
        this._states = new byte[newCapacity];
        int i = oldCapacity;
        while (true) {
            int i2 = i;
            i--;
            if (i2 > 0) {
                if (oldStates[i] == 1) {
                    char o = oldKeys[i];
                    int index = insertKey(o);
                    this._values[index] = oldVals[i];
                }
            } else {
                return;
            }
        }
    }

    @Override // gnu.trove.map.TCharObjectMap
    public char getNoEntryKey() {
        return this.no_entry_key;
    }

    @Override // gnu.trove.map.TCharObjectMap
    public boolean containsKey(char key) {
        return contains(key);
    }

    @Override // gnu.trove.map.TCharObjectMap
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

    @Override // gnu.trove.map.TCharObjectMap
    public V get(char key) {
        int index = index(key);
        if (index < 0) {
            return null;
        }
        return this._values[index];
    }

    @Override // gnu.trove.map.TCharObjectMap
    public V put(char key, V value) {
        int index = insertKey(key);
        return doPut(value, index);
    }

    @Override // gnu.trove.map.TCharObjectMap
    public V putIfAbsent(char key, V value) {
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

    @Override // gnu.trove.map.TCharObjectMap
    public V remove(char key) {
        V prev = null;
        int index = index(key);
        if (index >= 0) {
            prev = this._values[index];
            removeAt(index);
        }
        return prev;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // gnu.trove.impl.hash.TCharHash, gnu.trove.impl.hash.TPrimitiveHash, gnu.trove.impl.hash.THash
    public void removeAt(int index) {
        this._values[index] = null;
        super.removeAt(index);
    }

    @Override // gnu.trove.map.TCharObjectMap
    public void putAll(Map<? extends Character, ? extends V> map) {
        Set<? extends Map.Entry<? extends Character, ? extends V>> set = map.entrySet();
        for (Map.Entry<? extends Character, ? extends V> entry : set) {
            put(entry.getKey().charValue(), entry.getValue());
        }
    }

    @Override // gnu.trove.map.TCharObjectMap
    public void putAll(TCharObjectMap<? extends V> map) {
        map.forEachEntry(this.PUT_ALL_PROC);
    }

    @Override // gnu.trove.impl.hash.THash, gnu.trove.map.TObjectByteMap
    public void clear() {
        super.clear();
        Arrays.fill(this._set, 0, this._set.length, this.no_entry_key);
        Arrays.fill(this._states, 0, this._states.length, (byte) 0);
        Arrays.fill(this._values, 0, this._values.length, (Object) null);
    }

    @Override // gnu.trove.map.TCharObjectMap
    public TCharSet keySet() {
        return new KeyView();
    }

    @Override // gnu.trove.map.TCharObjectMap
    public char[] keys() {
        char[] keys = new char[size()];
        char[] k = this._set;
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

    @Override // gnu.trove.map.TCharObjectMap
    public char[] keys(char[] dest) {
        if (dest.length < this._size) {
            dest = new char[this._size];
        }
        char[] k = this._set;
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

    @Override // gnu.trove.map.TCharObjectMap
    public Collection<V> valueCollection() {
        return new ValueView();
    }

    @Override // gnu.trove.map.TCharObjectMap
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
    @Override // gnu.trove.map.TCharObjectMap
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

    @Override // gnu.trove.map.TCharObjectMap
    public TCharObjectIterator<V> iterator() {
        return new TCharObjectHashIterator(this);
    }

    @Override // gnu.trove.map.TCharObjectMap
    public boolean forEachKey(TCharProcedure procedure) {
        return forEach(procedure);
    }

    @Override // gnu.trove.map.TCharObjectMap
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

    @Override // gnu.trove.map.TCharObjectMap
    public boolean forEachEntry(TCharObjectProcedure<? super V> procedure) {
        byte[] states = this._states;
        char[] keys = this._set;
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

    @Override // gnu.trove.map.TCharObjectMap
    public boolean retainEntries(TCharObjectProcedure<? super V> procedure) {
        boolean modified = false;
        byte[] states = this._states;
        char[] keys = this._set;
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

    @Override // gnu.trove.map.TCharObjectMap
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

    @Override // gnu.trove.map.TCharObjectMap
    public boolean equals(Object other) {
        if (!(other instanceof TCharObjectMap)) {
            return false;
        }
        TCharObjectMap that = (TCharObjectMap) other;
        if (that.size() != size()) {
            return false;
        }
        try {
            TCharObjectIterator iter = iterator();
            while (iter.hasNext()) {
                iter.advance();
                char key = iter.key();
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

    @Override // gnu.trove.map.TCharObjectMap
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
                    hashcode += HashFunctions.hash((int) this._set[i]) ^ (values[i] == null ? 0 : values[i].hashCode());
                }
            } else {
                return hashcode;
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TCharObjectHashMap$KeyView.class */
    class KeyView implements TCharSet {
        KeyView() {
        }

        @Override // gnu.trove.set.TCharSet, gnu.trove.TCharCollection
        public char getNoEntryValue() {
            return TCharObjectHashMap.this.no_entry_key;
        }

        @Override // gnu.trove.set.TCharSet, gnu.trove.TCharCollection
        public int size() {
            return TCharObjectHashMap.this._size;
        }

        @Override // gnu.trove.set.TCharSet, gnu.trove.TCharCollection
        public boolean isEmpty() {
            return TCharObjectHashMap.this._size == 0;
        }

        @Override // gnu.trove.set.TCharSet, gnu.trove.TCharCollection
        public boolean contains(char entry) {
            return TCharObjectHashMap.this.containsKey(entry);
        }

        @Override // gnu.trove.set.TCharSet, gnu.trove.TCharCollection
        public TCharIterator iterator() {
            return new TCharHashIterator(TCharObjectHashMap.this);
        }

        @Override // gnu.trove.set.TCharSet, gnu.trove.TCharCollection
        public char[] toArray() {
            return TCharObjectHashMap.this.keys();
        }

        @Override // gnu.trove.set.TCharSet, gnu.trove.TCharCollection
        public char[] toArray(char[] dest) {
            return TCharObjectHashMap.this.keys(dest);
        }

        @Override // gnu.trove.set.TCharSet, gnu.trove.TCharCollection
        public boolean add(char entry) {
            throw new UnsupportedOperationException();
        }

        @Override // gnu.trove.set.TCharSet, gnu.trove.TCharCollection
        public boolean remove(char entry) {
            return null != TCharObjectHashMap.this.remove(entry);
        }

        @Override // gnu.trove.set.TCharSet, gnu.trove.TCharCollection
        public boolean containsAll(Collection<?> collection) {
            for (Object element : collection) {
                if (!TCharObjectHashMap.this.containsKey(((Character) element).charValue())) {
                    return false;
                }
            }
            return true;
        }

        @Override // gnu.trove.set.TCharSet, gnu.trove.TCharCollection
        public boolean containsAll(TCharCollection collection) {
            if (collection == this) {
                return true;
            }
            TCharIterator iter = collection.iterator();
            while (iter.hasNext()) {
                if (!TCharObjectHashMap.this.containsKey(iter.next())) {
                    return false;
                }
            }
            return true;
        }

        @Override // gnu.trove.set.TCharSet, gnu.trove.TCharCollection
        public boolean containsAll(char[] array) {
            for (char element : array) {
                if (!TCharObjectHashMap.this.containsKey(element)) {
                    return false;
                }
            }
            return true;
        }

        @Override // gnu.trove.set.TCharSet, gnu.trove.TCharCollection
        public boolean addAll(Collection<? extends Character> collection) {
            throw new UnsupportedOperationException();
        }

        @Override // gnu.trove.set.TCharSet, gnu.trove.TCharCollection
        public boolean addAll(TCharCollection collection) {
            throw new UnsupportedOperationException();
        }

        @Override // gnu.trove.set.TCharSet, gnu.trove.TCharCollection
        public boolean addAll(char[] array) {
            throw new UnsupportedOperationException();
        }

        @Override // gnu.trove.set.TCharSet, gnu.trove.TCharCollection
        public boolean retainAll(Collection<?> collection) {
            boolean modified = false;
            TCharIterator iter = iterator();
            while (iter.hasNext()) {
                if (!collection.contains(Character.valueOf(iter.next()))) {
                    iter.remove();
                    modified = true;
                }
            }
            return modified;
        }

        @Override // gnu.trove.set.TCharSet, gnu.trove.TCharCollection
        public boolean retainAll(TCharCollection collection) {
            if (this == collection) {
                return false;
            }
            boolean modified = false;
            TCharIterator iter = iterator();
            while (iter.hasNext()) {
                if (!collection.contains(iter.next())) {
                    iter.remove();
                    modified = true;
                }
            }
            return modified;
        }

        @Override // gnu.trove.set.TCharSet, gnu.trove.TCharCollection
        public boolean retainAll(char[] array) {
            boolean changed = false;
            Arrays.sort(array);
            char[] set = TCharObjectHashMap.this._set;
            byte[] states = TCharObjectHashMap.this._states;
            int i = set.length;
            while (true) {
                int i2 = i;
                i--;
                if (i2 > 0) {
                    if (states[i] == 1 && Arrays.binarySearch(array, set[i]) < 0) {
                        TCharObjectHashMap.this.removeAt(i);
                        changed = true;
                    }
                } else {
                    return changed;
                }
            }
        }

        @Override // gnu.trove.set.TCharSet, gnu.trove.TCharCollection
        public boolean removeAll(Collection<?> collection) {
            boolean changed = false;
            for (Object element : collection) {
                if (element instanceof Character) {
                    char c = ((Character) element).charValue();
                    if (remove(c)) {
                        changed = true;
                    }
                }
            }
            return changed;
        }

        @Override // gnu.trove.set.TCharSet, gnu.trove.TCharCollection
        public boolean removeAll(TCharCollection collection) {
            if (collection == this) {
                clear();
                return true;
            }
            boolean changed = false;
            TCharIterator iter = collection.iterator();
            while (iter.hasNext()) {
                char element = iter.next();
                if (remove(element)) {
                    changed = true;
                }
            }
            return changed;
        }

        @Override // gnu.trove.set.TCharSet, gnu.trove.TCharCollection
        public boolean removeAll(char[] array) {
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

        @Override // gnu.trove.set.TCharSet, gnu.trove.TCharCollection
        public void clear() {
            TCharObjectHashMap.this.clear();
        }

        @Override // gnu.trove.set.TCharSet, gnu.trove.TCharCollection
        public boolean forEach(TCharProcedure procedure) {
            return TCharObjectHashMap.this.forEachKey(procedure);
        }

        @Override // gnu.trove.set.TCharSet, gnu.trove.TCharCollection
        public boolean equals(Object other) {
            if (!(other instanceof TCharSet)) {
                return false;
            }
            TCharSet that = (TCharSet) other;
            if (that.size() != size()) {
                return false;
            }
            int i = TCharObjectHashMap.this._states.length;
            while (true) {
                int i2 = i;
                i--;
                if (i2 > 0) {
                    if (TCharObjectHashMap.this._states[i] == 1 && !that.contains(TCharObjectHashMap.this._set[i])) {
                        return false;
                    }
                } else {
                    return true;
                }
            }
        }

        @Override // gnu.trove.set.TCharSet, gnu.trove.TCharCollection
        public int hashCode() {
            int hashcode = 0;
            int i = TCharObjectHashMap.this._states.length;
            while (true) {
                int i2 = i;
                i--;
                if (i2 > 0) {
                    if (TCharObjectHashMap.this._states[i] == 1) {
                        hashcode += HashFunctions.hash((int) TCharObjectHashMap.this._set[i]);
                    }
                } else {
                    return hashcode;
                }
            }
        }

        public String toString() {
            StringBuilder buf = new StringBuilder("{");
            boolean first = true;
            int i = TCharObjectHashMap.this._states.length;
            while (true) {
                int i2 = i;
                i--;
                if (i2 > 0) {
                    if (TCharObjectHashMap.this._states[i] == 1) {
                        if (first) {
                            first = false;
                        } else {
                            buf.append(",");
                        }
                        buf.append(TCharObjectHashMap.this._set[i]);
                    }
                } else {
                    return buf.toString();
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TCharObjectHashMap$KeyView$TCharHashIterator.class */
        public class TCharHashIterator extends THashPrimitiveIterator implements TCharIterator {
            private final TCharHash _hash;

            public TCharHashIterator(TCharHash hash) {
                super(hash);
                this._hash = hash;
            }

            @Override // gnu.trove.iterator.TCharIterator
            public char next() {
                moveToNextIndex();
                return this._hash._set[this._index];
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TCharObjectHashMap$ValueView.class */
    protected class ValueView extends TCharObjectHashMap<V>.MapBackedView<V> {
        protected ValueView() {
            super();
        }

        @Override // gnu.trove.map.hash.TCharObjectHashMap.MapBackedView, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public Iterator<V> iterator() {
            return new TCharObjectValueHashIterator(TCharObjectHashMap.this) { // from class: gnu.trove.map.hash.TCharObjectHashMap.ValueView.1
                @Override // gnu.trove.map.hash.TCharObjectHashMap.ValueView.TCharObjectValueHashIterator
                protected V objectAtIndex(int index) {
                    return TCharObjectHashMap.this._values[index];
                }
            };
        }

        @Override // gnu.trove.map.hash.TCharObjectHashMap.MapBackedView
        public boolean containsElement(V value) {
            return TCharObjectHashMap.this.containsValue(value);
        }

        @Override // gnu.trove.map.hash.TCharObjectHashMap.MapBackedView
        public boolean removeElement(V value) {
            V[] values = TCharObjectHashMap.this._values;
            byte[] states = TCharObjectHashMap.this._states;
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
            TCharObjectHashMap.this.removeAt(i);
            return true;
        }

        /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TCharObjectHashMap$ValueView$TCharObjectValueHashIterator.class */
        class TCharObjectValueHashIterator extends THashPrimitiveIterator implements Iterator<V> {
            protected final TCharObjectHashMap _map;

            public TCharObjectValueHashIterator(TCharObjectHashMap map) {
                super(map);
                this._map = map;
            }

            protected V objectAtIndex(int index) {
                byte[] states = TCharObjectHashMap.this._states;
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

    /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TCharObjectHashMap$MapBackedView.class */
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
            TCharObjectHashMap.this.clear();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean add(E obj) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return TCharObjectHashMap.this.size();
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
            return TCharObjectHashMap.this.isEmpty();
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
    /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TCharObjectHashMap$TCharObjectHashIterator.class */
    public class TCharObjectHashIterator<V> extends THashPrimitiveIterator implements TCharObjectIterator<V> {
        private final TCharObjectHashMap<V> _map;

        public TCharObjectHashIterator(TCharObjectHashMap<V> map) {
            super(map);
            this._map = map;
        }

        @Override // gnu.trove.iterator.TAdvancingIterator
        public void advance() {
            moveToNextIndex();
        }

        @Override // gnu.trove.iterator.TCharObjectIterator
        public char key() {
            return this._map._set[this._index];
        }

        @Override // gnu.trove.iterator.TCharObjectIterator
        public V value() {
            return this._map._values[this._index];
        }

        @Override // gnu.trove.iterator.TCharObjectIterator
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
        out.writeChar(this.no_entry_key);
        out.writeInt(this._size);
        int i = this._states.length;
        while (true) {
            int i2 = i;
            i--;
            if (i2 > 0) {
                if (this._states[i] == 1) {
                    out.writeChar(this._set[i]);
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
        this.no_entry_key = in.readChar();
        int size = in.readInt();
        setUp(size);
        while (true) {
            int i = size;
            size--;
            if (i > 0) {
                char key = in.readChar();
                put(key, in.readObject());
            } else {
                return;
            }
        }
    }

    public String toString() {
        final StringBuilder buf = new StringBuilder("{");
        forEachEntry(new TCharObjectProcedure<V>() { // from class: gnu.trove.map.hash.TCharObjectHashMap.2
            private boolean first = true;

            @Override // gnu.trove.procedure.TCharObjectProcedure
            public boolean execute(char key, Object value) {
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
