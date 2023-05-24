package gnu.trove.map.hash;

import gnu.trove.TIntCollection;
import gnu.trove.function.TIntFunction;
import gnu.trove.impl.Constants;
import gnu.trove.impl.HashFunctions;
import gnu.trove.impl.hash.THash;
import gnu.trove.impl.hash.TObjectHash;
import gnu.trove.iterator.TIntIterator;
import gnu.trove.iterator.TObjectIntIterator;
import gnu.trove.iterator.hash.TObjectHashIterator;
import gnu.trove.map.TObjectIntMap;
import gnu.trove.procedure.TIntProcedure;
import gnu.trove.procedure.TObjectIntProcedure;
import gnu.trove.procedure.TObjectProcedure;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.lang.reflect.Array;
import java.util.AbstractSet;
import java.util.Arrays;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TObjectIntHashMap.class */
public class TObjectIntHashMap<K> extends TObjectHash<K> implements TObjectIntMap<K>, Externalizable {
    static final long serialVersionUID = 1;
    private final TObjectIntProcedure<K> PUT_ALL_PROC;
    protected transient int[] _values;
    protected int no_entry_value;

    public TObjectIntHashMap() {
        this.PUT_ALL_PROC = new TObjectIntProcedure<K>() { // from class: gnu.trove.map.hash.TObjectIntHashMap.1
            @Override // gnu.trove.procedure.TObjectIntProcedure
            public boolean execute(K key, int value) {
                TObjectIntHashMap.this.put(key, value);
                return true;
            }
        };
        this.no_entry_value = Constants.DEFAULT_INT_NO_ENTRY_VALUE;
    }

    public TObjectIntHashMap(int initialCapacity) {
        super(initialCapacity);
        this.PUT_ALL_PROC = new TObjectIntProcedure<K>() { // from class: gnu.trove.map.hash.TObjectIntHashMap.1
            @Override // gnu.trove.procedure.TObjectIntProcedure
            public boolean execute(K key, int value) {
                TObjectIntHashMap.this.put(key, value);
                return true;
            }
        };
        this.no_entry_value = Constants.DEFAULT_INT_NO_ENTRY_VALUE;
    }

    public TObjectIntHashMap(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
        this.PUT_ALL_PROC = new TObjectIntProcedure<K>() { // from class: gnu.trove.map.hash.TObjectIntHashMap.1
            @Override // gnu.trove.procedure.TObjectIntProcedure
            public boolean execute(K key, int value) {
                TObjectIntHashMap.this.put(key, value);
                return true;
            }
        };
        this.no_entry_value = Constants.DEFAULT_INT_NO_ENTRY_VALUE;
    }

    public TObjectIntHashMap(int initialCapacity, float loadFactor, int noEntryValue) {
        super(initialCapacity, loadFactor);
        this.PUT_ALL_PROC = new TObjectIntProcedure<K>() { // from class: gnu.trove.map.hash.TObjectIntHashMap.1
            @Override // gnu.trove.procedure.TObjectIntProcedure
            public boolean execute(K key, int value) {
                TObjectIntHashMap.this.put(key, value);
                return true;
            }
        };
        this.no_entry_value = noEntryValue;
        if (this.no_entry_value != 0) {
            Arrays.fill(this._values, this.no_entry_value);
        }
    }

    public TObjectIntHashMap(TObjectIntMap<? extends K> map) {
        this(map.size(), 0.5f, map.getNoEntryValue());
        if (map instanceof TObjectIntHashMap) {
            TObjectIntHashMap hashmap = (TObjectIntHashMap) map;
            this._loadFactor = hashmap._loadFactor;
            this.no_entry_value = hashmap.no_entry_value;
            if (this.no_entry_value != 0) {
                Arrays.fill(this._values, this.no_entry_value);
            }
            setUp((int) Math.ceil(10.0f / this._loadFactor));
        }
        putAll(map);
    }

    @Override // gnu.trove.impl.hash.TObjectHash, gnu.trove.impl.hash.THash
    public int setUp(int initialCapacity) {
        int capacity = super.setUp(initialCapacity);
        this._values = new int[capacity];
        return capacity;
    }

    @Override // gnu.trove.impl.hash.THash
    protected void rehash(int newCapacity) {
        int oldCapacity = this._set.length;
        Object[] objArr = this._set;
        int[] oldVals = this._values;
        this._set = new Object[newCapacity];
        Arrays.fill(this._set, FREE);
        this._values = new int[newCapacity];
        Arrays.fill(this._values, this.no_entry_value);
        int i = oldCapacity;
        while (true) {
            int i2 = i;
            i--;
            if (i2 > 0) {
                if (objArr[i] != FREE && objArr[i] != REMOVED) {
                    Object obj = objArr[i];
                    int index = insertKey(obj);
                    if (index < 0) {
                        throwObjectContractViolation(this._set[(-index) - 1], obj);
                    }
                    this._set[index] = obj;
                    this._values[index] = oldVals[i];
                }
            } else {
                return;
            }
        }
    }

    @Override // gnu.trove.map.TObjectIntMap
    public int getNoEntryValue() {
        return this.no_entry_value;
    }

    @Override // gnu.trove.map.TObjectIntMap
    public boolean containsKey(Object key) {
        return contains(key);
    }

    @Override // gnu.trove.map.TObjectIntMap
    public boolean containsValue(int val) {
        Object[] keys = this._set;
        int[] vals = this._values;
        int i = vals.length;
        while (true) {
            int i2 = i;
            i--;
            if (i2 > 0) {
                if (keys[i] != FREE && keys[i] != REMOVED && val == vals[i]) {
                    return true;
                }
            } else {
                return false;
            }
        }
    }

    @Override // gnu.trove.map.TObjectIntMap
    public int get(Object key) {
        int index = index(key);
        return index < 0 ? this.no_entry_value : this._values[index];
    }

    @Override // gnu.trove.map.TObjectIntMap
    public int put(K key, int value) {
        int index = insertKey(key);
        return doPut(value, index);
    }

    @Override // gnu.trove.map.TObjectIntMap
    public int putIfAbsent(K key, int value) {
        int index = insertKey(key);
        if (index < 0) {
            return this._values[(-index) - 1];
        }
        return doPut(value, index);
    }

    private int doPut(int value, int index) {
        int previous = this.no_entry_value;
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

    @Override // gnu.trove.map.TObjectIntMap
    public int remove(Object key) {
        int prev = this.no_entry_value;
        int index = index(key);
        if (index >= 0) {
            prev = this._values[index];
            removeAt(index);
        }
        return prev;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // gnu.trove.impl.hash.TObjectHash, gnu.trove.impl.hash.THash
    public void removeAt(int index) {
        this._values[index] = this.no_entry_value;
        super.removeAt(index);
    }

    @Override // gnu.trove.map.TObjectIntMap
    public void putAll(Map<? extends K, ? extends Integer> map) {
        Set<? extends Map.Entry<? extends K, ? extends Integer>> set = map.entrySet();
        for (Map.Entry<? extends K, ? extends Integer> entry : set) {
            put(entry.getKey(), entry.getValue().intValue());
        }
    }

    @Override // gnu.trove.map.TObjectIntMap
    public void putAll(TObjectIntMap<? extends K> map) {
        map.forEachEntry(this.PUT_ALL_PROC);
    }

    @Override // gnu.trove.impl.hash.THash, gnu.trove.map.TObjectByteMap
    public void clear() {
        super.clear();
        Arrays.fill(this._set, 0, this._set.length, FREE);
        Arrays.fill(this._values, 0, this._values.length, this.no_entry_value);
    }

    @Override // gnu.trove.map.TObjectIntMap
    public Set<K> keySet() {
        return new KeyView();
    }

    @Override // gnu.trove.map.TObjectIntMap
    public Object[] keys() {
        Object[] objArr = new Object[size()];
        Object[] k = this._set;
        int i = k.length;
        int j = 0;
        while (true) {
            int i2 = i;
            i--;
            if (i2 > 0) {
                if (k[i] != FREE && k[i] != REMOVED) {
                    int i3 = j;
                    j++;
                    objArr[i3] = k[i];
                }
            } else {
                return objArr;
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v21, types: [java.lang.Object[]] */
    @Override // gnu.trove.map.TObjectIntMap
    public K[] keys(K[] a) {
        int size = size();
        if (a.length < size) {
            a = (Object[]) Array.newInstance(a.getClass().getComponentType(), size);
        }
        Object[] k = this._set;
        int i = k.length;
        int j = 0;
        while (true) {
            int i2 = i;
            i--;
            if (i2 > 0) {
                if (k[i] != FREE && k[i] != REMOVED) {
                    int i3 = j;
                    j++;
                    a[i3] = k[i];
                }
            } else {
                return a;
            }
        }
    }

    @Override // gnu.trove.map.TObjectIntMap
    public TIntCollection valueCollection() {
        return new TIntValueCollection();
    }

    @Override // gnu.trove.map.TObjectIntMap
    public int[] values() {
        int[] vals = new int[size()];
        int[] v = this._values;
        Object[] keys = this._set;
        int i = v.length;
        int j = 0;
        while (true) {
            int i2 = i;
            i--;
            if (i2 > 0) {
                if (keys[i] != FREE && keys[i] != REMOVED) {
                    int i3 = j;
                    j++;
                    vals[i3] = v[i];
                }
            } else {
                return vals;
            }
        }
    }

    @Override // gnu.trove.map.TObjectIntMap
    public int[] values(int[] array) {
        int size = size();
        if (array.length < size) {
            array = new int[size];
        }
        int[] v = this._values;
        Object[] keys = this._set;
        int i = v.length;
        int j = 0;
        while (true) {
            int i2 = i;
            i--;
            if (i2 <= 0) {
                break;
            } else if (keys[i] != FREE && keys[i] != REMOVED) {
                int i3 = j;
                j++;
                array[i3] = v[i];
            }
        }
        if (array.length > size) {
            array[size] = this.no_entry_value;
        }
        return array;
    }

    @Override // gnu.trove.map.TObjectIntMap
    public TObjectIntIterator<K> iterator() {
        return new TObjectIntHashIterator(this);
    }

    @Override // gnu.trove.map.TObjectIntMap
    public boolean increment(K key) {
        return adjustValue(key, 1);
    }

    @Override // gnu.trove.map.TObjectIntMap
    public boolean adjustValue(K key, int amount) {
        int index = index(key);
        if (index < 0) {
            return false;
        }
        int[] iArr = this._values;
        iArr[index] = iArr[index] + amount;
        return true;
    }

    @Override // gnu.trove.map.TObjectIntMap
    public int adjustOrPutValue(K key, int adjust_amount, int put_amount) {
        int newValue;
        boolean isNewMapping;
        int index = insertKey(key);
        if (index < 0) {
            int index2 = (-index) - 1;
            int[] iArr = this._values;
            int i = iArr[index2] + adjust_amount;
            iArr[index2] = i;
            newValue = i;
            isNewMapping = false;
        } else {
            this._values[index] = put_amount;
            newValue = put_amount;
            isNewMapping = true;
        }
        if (isNewMapping) {
            postInsertHook(this.consumeFreeSlot);
        }
        return newValue;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // gnu.trove.map.TObjectIntMap
    public boolean forEachKey(TObjectProcedure<? super K> procedure) {
        return forEach(procedure);
    }

    @Override // gnu.trove.map.TObjectIntMap
    public boolean forEachValue(TIntProcedure procedure) {
        Object[] keys = this._set;
        int[] values = this._values;
        int i = values.length;
        while (true) {
            int i2 = i;
            i--;
            if (i2 > 0) {
                if (keys[i] != FREE && keys[i] != REMOVED && !procedure.execute(values[i])) {
                    return false;
                }
            } else {
                return true;
            }
        }
    }

    @Override // gnu.trove.map.TObjectIntMap
    public boolean forEachEntry(TObjectIntProcedure<? super K> procedure) {
        Object[] keys = this._set;
        int[] values = this._values;
        int i = keys.length;
        while (true) {
            int i2 = i;
            i--;
            if (i2 > 0) {
                if (keys[i] != FREE && keys[i] != REMOVED && !procedure.execute(keys[i], values[i])) {
                    return false;
                }
            } else {
                return true;
            }
        }
    }

    @Override // gnu.trove.map.TObjectIntMap
    public boolean retainEntries(TObjectIntProcedure<? super K> procedure) {
        boolean modified = false;
        Object[] objArr = this._set;
        int[] values = this._values;
        tempDisableAutoCompaction();
        try {
            int i = objArr.length;
            while (true) {
                int i2 = i;
                i--;
                if (i2 > 0) {
                    if (objArr[i] != FREE && objArr[i] != REMOVED && !procedure.execute(objArr[i], values[i])) {
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

    @Override // gnu.trove.map.TObjectIntMap
    public void transformValues(TIntFunction function) {
        Object[] keys = this._set;
        int[] values = this._values;
        int i = values.length;
        while (true) {
            int i2 = i;
            i--;
            if (i2 > 0) {
                if (keys[i] != null && keys[i] != REMOVED) {
                    values[i] = function.execute(values[i]);
                }
            } else {
                return;
            }
        }
    }

    @Override // gnu.trove.map.TObjectIntMap
    public boolean equals(Object other) {
        if (!(other instanceof TObjectIntMap)) {
            return false;
        }
        TObjectIntMap that = (TObjectIntMap) other;
        if (that.size() != size()) {
            return false;
        }
        try {
            TObjectIntIterator iter = iterator();
            while (iter.hasNext()) {
                iter.advance();
                Object key = iter.key();
                int value = iter.value();
                if (value == this.no_entry_value) {
                    if (that.get(key) != that.getNoEntryValue() || !that.containsKey(key)) {
                        return false;
                    }
                } else if (value != that.get(key)) {
                    return false;
                }
            }
            return true;
        } catch (ClassCastException e) {
            return true;
        }
    }

    @Override // gnu.trove.map.TObjectIntMap
    public int hashCode() {
        int hashcode = 0;
        Object[] keys = this._set;
        int[] values = this._values;
        int i = values.length;
        while (true) {
            int i2 = i;
            i--;
            if (i2 > 0) {
                if (keys[i] != FREE && keys[i] != REMOVED) {
                    hashcode += HashFunctions.hash(values[i]) ^ (keys[i] == null ? 0 : keys[i].hashCode());
                }
            } else {
                return hashcode;
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TObjectIntHashMap$KeyView.class */
    protected class KeyView extends TObjectIntHashMap<K>.MapBackedView<K> {
        protected KeyView() {
            super();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public Iterator<K> iterator() {
            return new TObjectHashIterator(TObjectIntHashMap.this);
        }

        @Override // gnu.trove.map.hash.TObjectIntHashMap.MapBackedView
        public boolean removeElement(K key) {
            return TObjectIntHashMap.this.no_entry_value != TObjectIntHashMap.this.remove(key);
        }

        @Override // gnu.trove.map.hash.TObjectIntHashMap.MapBackedView
        public boolean containsElement(K key) {
            return TObjectIntHashMap.this.contains(key);
        }
    }

    /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TObjectIntHashMap$MapBackedView.class */
    private abstract class MapBackedView<E> extends AbstractSet<E> implements Set<E>, Iterable<E> {
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
            TObjectIntHashMap.this.clear();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean add(E obj) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return TObjectIntHashMap.this.size();
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
            return TObjectIntHashMap.this.isEmpty();
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
    /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TObjectIntHashMap$TIntValueCollection.class */
    public class TIntValueCollection implements TIntCollection {
        TIntValueCollection() {
        }

        @Override // gnu.trove.TIntCollection
        public TIntIterator iterator() {
            return new TObjectIntValueHashIterator();
        }

        @Override // gnu.trove.TIntCollection
        public int getNoEntryValue() {
            return TObjectIntHashMap.this.no_entry_value;
        }

        @Override // gnu.trove.TIntCollection
        public int size() {
            return TObjectIntHashMap.this._size;
        }

        @Override // gnu.trove.TIntCollection
        public boolean isEmpty() {
            return 0 == TObjectIntHashMap.this._size;
        }

        @Override // gnu.trove.TIntCollection
        public boolean contains(int entry) {
            return TObjectIntHashMap.this.containsValue(entry);
        }

        @Override // gnu.trove.TIntCollection
        public int[] toArray() {
            return TObjectIntHashMap.this.values();
        }

        @Override // gnu.trove.TIntCollection
        public int[] toArray(int[] dest) {
            return TObjectIntHashMap.this.values(dest);
        }

        @Override // gnu.trove.TIntCollection
        public boolean add(int entry) {
            throw new UnsupportedOperationException();
        }

        @Override // gnu.trove.TIntCollection
        public boolean remove(int entry) {
            int[] values = TObjectIntHashMap.this._values;
            Object[] set = TObjectIntHashMap.this._set;
            int i = values.length;
            while (true) {
                int i2 = i;
                i--;
                if (i2 > 0) {
                    if (set[i] != TObjectHash.FREE && set[i] != TObjectHash.REMOVED && entry == values[i]) {
                        TObjectIntHashMap.this.removeAt(i);
                        return true;
                    }
                } else {
                    return false;
                }
            }
        }

        @Override // gnu.trove.TIntCollection
        public boolean containsAll(Collection<?> collection) {
            for (Object element : collection) {
                if (element instanceof Integer) {
                    int ele = ((Integer) element).intValue();
                    if (!TObjectIntHashMap.this.containsValue(ele)) {
                        return false;
                    }
                } else {
                    return false;
                }
            }
            return true;
        }

        @Override // gnu.trove.TIntCollection
        public boolean containsAll(TIntCollection collection) {
            TIntIterator iter = collection.iterator();
            while (iter.hasNext()) {
                if (!TObjectIntHashMap.this.containsValue(iter.next())) {
                    return false;
                }
            }
            return true;
        }

        @Override // gnu.trove.TIntCollection
        public boolean containsAll(int[] array) {
            for (int element : array) {
                if (!TObjectIntHashMap.this.containsValue(element)) {
                    return false;
                }
            }
            return true;
        }

        @Override // gnu.trove.TIntCollection
        public boolean addAll(Collection<? extends Integer> collection) {
            throw new UnsupportedOperationException();
        }

        @Override // gnu.trove.TIntCollection
        public boolean addAll(TIntCollection collection) {
            throw new UnsupportedOperationException();
        }

        @Override // gnu.trove.TIntCollection
        public boolean addAll(int[] array) {
            throw new UnsupportedOperationException();
        }

        @Override // gnu.trove.TIntCollection
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

        @Override // gnu.trove.TIntCollection
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

        @Override // gnu.trove.TIntCollection
        public boolean retainAll(int[] array) {
            boolean changed = false;
            Arrays.sort(array);
            int[] values = TObjectIntHashMap.this._values;
            Object[] set = TObjectIntHashMap.this._set;
            int i = set.length;
            while (true) {
                int i2 = i;
                i--;
                if (i2 > 0) {
                    if (set[i] != TObjectHash.FREE && set[i] != TObjectHash.REMOVED && Arrays.binarySearch(array, values[i]) < 0) {
                        TObjectIntHashMap.this.removeAt(i);
                        changed = true;
                    }
                } else {
                    return changed;
                }
            }
        }

        @Override // gnu.trove.TIntCollection
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

        @Override // gnu.trove.TIntCollection
        public boolean removeAll(TIntCollection collection) {
            if (this == collection) {
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

        @Override // gnu.trove.TIntCollection
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

        @Override // gnu.trove.TIntCollection
        public void clear() {
            TObjectIntHashMap.this.clear();
        }

        @Override // gnu.trove.TIntCollection
        public boolean forEach(TIntProcedure procedure) {
            return TObjectIntHashMap.this.forEachValue(procedure);
        }

        public String toString() {
            final StringBuilder buf = new StringBuilder("{");
            TObjectIntHashMap.this.forEachValue(new TIntProcedure() { // from class: gnu.trove.map.hash.TObjectIntHashMap.TIntValueCollection.1
                private boolean first = true;

                @Override // gnu.trove.procedure.TIntProcedure
                public boolean execute(int value) {
                    if (this.first) {
                        this.first = false;
                    } else {
                        buf.append(", ");
                    }
                    buf.append(value);
                    return true;
                }
            });
            buf.append("}");
            return buf.toString();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TObjectIntHashMap$TIntValueCollection$TObjectIntValueHashIterator.class */
        public class TObjectIntValueHashIterator implements TIntIterator {
            protected THash _hash;
            protected int _expectedSize;
            protected int _index;

            TObjectIntValueHashIterator() {
                this._hash = TObjectIntHashMap.this;
                this._expectedSize = this._hash.size();
                this._index = this._hash.capacity();
            }

            @Override // gnu.trove.iterator.TIterator, java.util.Iterator
            public boolean hasNext() {
                return nextIndex() >= 0;
            }

            @Override // gnu.trove.iterator.TIntIterator
            public int next() {
                moveToNextIndex();
                return TObjectIntHashMap.this._values[this._index];
            }

            @Override // gnu.trove.iterator.TIterator, java.util.Iterator
            public void remove() {
                if (this._expectedSize != this._hash.size()) {
                    throw new ConcurrentModificationException();
                }
                try {
                    this._hash.tempDisableAutoCompaction();
                    TObjectIntHashMap.this.removeAt(this._index);
                    this._hash.reenableAutoCompaction(false);
                    this._expectedSize--;
                } catch (Throwable th) {
                    this._hash.reenableAutoCompaction(false);
                    throw th;
                }
            }

            protected final void moveToNextIndex() {
                int nextIndex = nextIndex();
                this._index = nextIndex;
                if (nextIndex < 0) {
                    throw new NoSuchElementException();
                }
            }

            protected final int nextIndex() {
                if (this._expectedSize != this._hash.size()) {
                    throw new ConcurrentModificationException();
                }
                Object[] set = TObjectIntHashMap.this._set;
                int i = this._index;
                while (true) {
                    int i2 = i;
                    i--;
                    if (i2 <= 0 || (set[i] != TObjectHash.FREE && set[i] != TObjectHash.REMOVED)) {
                        break;
                    }
                }
                return i;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TObjectIntHashMap$TObjectIntHashIterator.class */
    public class TObjectIntHashIterator<K> extends TObjectHashIterator<K> implements TObjectIntIterator<K> {
        private final TObjectIntHashMap<K> _map;

        public TObjectIntHashIterator(TObjectIntHashMap<K> map) {
            super(map);
            this._map = map;
        }

        @Override // gnu.trove.iterator.TAdvancingIterator
        public void advance() {
            moveToNextIndex();
        }

        @Override // gnu.trove.iterator.TObjectIntIterator
        public K key() {
            return (K) this._map._set[this._index];
        }

        @Override // gnu.trove.iterator.TObjectIntIterator
        public int value() {
            return this._map._values[this._index];
        }

        @Override // gnu.trove.iterator.TObjectIntIterator
        public int setValue(int val) {
            int old = value();
            this._map._values[this._index] = val;
            return old;
        }
    }

    @Override // gnu.trove.impl.hash.TObjectHash, gnu.trove.impl.hash.THash, java.io.Externalizable
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeByte(0);
        super.writeExternal(out);
        out.writeInt(this.no_entry_value);
        out.writeInt(this._size);
        int i = this._set.length;
        while (true) {
            int i2 = i;
            i--;
            if (i2 > 0) {
                if (this._set[i] != REMOVED && this._set[i] != FREE) {
                    out.writeObject(this._set[i]);
                    out.writeInt(this._values[i]);
                }
            } else {
                return;
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // gnu.trove.impl.hash.TObjectHash, gnu.trove.impl.hash.THash, java.io.Externalizable
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        in.readByte();
        super.readExternal(in);
        this.no_entry_value = in.readInt();
        int size = in.readInt();
        setUp(size);
        while (true) {
            int i = size;
            size--;
            if (i > 0) {
                Object readObject = in.readObject();
                int val = in.readInt();
                put(readObject, val);
            } else {
                return;
            }
        }
    }

    public String toString() {
        final StringBuilder buf = new StringBuilder("{");
        forEachEntry(new TObjectIntProcedure<K>() { // from class: gnu.trove.map.hash.TObjectIntHashMap.2
            private boolean first = true;

            @Override // gnu.trove.procedure.TObjectIntProcedure
            public boolean execute(K key, int value) {
                if (this.first) {
                    this.first = false;
                } else {
                    buf.append(",");
                }
                buf.append(key).append("=").append(value);
                return true;
            }
        });
        buf.append("}");
        return buf.toString();
    }
}
