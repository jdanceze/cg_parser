package gnu.trove.map.hash;

import gnu.trove.TByteCollection;
import gnu.trove.function.TByteFunction;
import gnu.trove.impl.Constants;
import gnu.trove.impl.HashFunctions;
import gnu.trove.impl.hash.THash;
import gnu.trove.impl.hash.TObjectHash;
import gnu.trove.iterator.TByteIterator;
import gnu.trove.iterator.TObjectByteIterator;
import gnu.trove.iterator.hash.TObjectHashIterator;
import gnu.trove.map.TObjectByteMap;
import gnu.trove.procedure.TByteProcedure;
import gnu.trove.procedure.TObjectByteProcedure;
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
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TObjectByteHashMap.class */
public class TObjectByteHashMap<K> extends TObjectHash<K> implements TObjectByteMap<K>, Externalizable {
    static final long serialVersionUID = 1;
    private final TObjectByteProcedure<K> PUT_ALL_PROC;
    protected transient byte[] _values;
    protected byte no_entry_value;

    public TObjectByteHashMap() {
        this.PUT_ALL_PROC = new TObjectByteProcedure<K>() { // from class: gnu.trove.map.hash.TObjectByteHashMap.1
            @Override // gnu.trove.procedure.TObjectByteProcedure
            public boolean execute(K key, byte value) {
                TObjectByteHashMap.this.put(key, value);
                return true;
            }
        };
        this.no_entry_value = Constants.DEFAULT_BYTE_NO_ENTRY_VALUE;
    }

    public TObjectByteHashMap(int initialCapacity) {
        super(initialCapacity);
        this.PUT_ALL_PROC = new TObjectByteProcedure<K>() { // from class: gnu.trove.map.hash.TObjectByteHashMap.1
            @Override // gnu.trove.procedure.TObjectByteProcedure
            public boolean execute(K key, byte value) {
                TObjectByteHashMap.this.put(key, value);
                return true;
            }
        };
        this.no_entry_value = Constants.DEFAULT_BYTE_NO_ENTRY_VALUE;
    }

    public TObjectByteHashMap(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
        this.PUT_ALL_PROC = new TObjectByteProcedure<K>() { // from class: gnu.trove.map.hash.TObjectByteHashMap.1
            @Override // gnu.trove.procedure.TObjectByteProcedure
            public boolean execute(K key, byte value) {
                TObjectByteHashMap.this.put(key, value);
                return true;
            }
        };
        this.no_entry_value = Constants.DEFAULT_BYTE_NO_ENTRY_VALUE;
    }

    public TObjectByteHashMap(int initialCapacity, float loadFactor, byte noEntryValue) {
        super(initialCapacity, loadFactor);
        this.PUT_ALL_PROC = new TObjectByteProcedure<K>() { // from class: gnu.trove.map.hash.TObjectByteHashMap.1
            @Override // gnu.trove.procedure.TObjectByteProcedure
            public boolean execute(K key, byte value) {
                TObjectByteHashMap.this.put(key, value);
                return true;
            }
        };
        this.no_entry_value = noEntryValue;
        if (this.no_entry_value != 0) {
            Arrays.fill(this._values, this.no_entry_value);
        }
    }

    public TObjectByteHashMap(TObjectByteMap<? extends K> map) {
        this(map.size(), 0.5f, map.getNoEntryValue());
        if (map instanceof TObjectByteHashMap) {
            TObjectByteHashMap hashmap = (TObjectByteHashMap) map;
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
        this._values = new byte[capacity];
        return capacity;
    }

    @Override // gnu.trove.impl.hash.THash
    protected void rehash(int newCapacity) {
        int oldCapacity = this._set.length;
        Object[] objArr = this._set;
        byte[] oldVals = this._values;
        this._set = new Object[newCapacity];
        Arrays.fill(this._set, FREE);
        this._values = new byte[newCapacity];
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

    @Override // gnu.trove.map.TObjectByteMap
    public byte getNoEntryValue() {
        return this.no_entry_value;
    }

    @Override // gnu.trove.map.TObjectByteMap
    public boolean containsKey(Object key) {
        return contains(key);
    }

    @Override // gnu.trove.map.TObjectByteMap
    public boolean containsValue(byte val) {
        Object[] keys = this._set;
        byte[] vals = this._values;
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

    @Override // gnu.trove.map.TObjectByteMap
    public byte get(Object key) {
        int index = index(key);
        return index < 0 ? this.no_entry_value : this._values[index];
    }

    @Override // gnu.trove.map.TObjectByteMap
    public byte put(K key, byte value) {
        int index = insertKey(key);
        return doPut(value, index);
    }

    @Override // gnu.trove.map.TObjectByteMap
    public byte putIfAbsent(K key, byte value) {
        int index = insertKey(key);
        if (index < 0) {
            return this._values[(-index) - 1];
        }
        return doPut(value, index);
    }

    private byte doPut(byte value, int index) {
        byte previous = this.no_entry_value;
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

    @Override // gnu.trove.map.TObjectByteMap
    public byte remove(Object key) {
        byte prev = this.no_entry_value;
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

    @Override // gnu.trove.map.TObjectByteMap
    public void putAll(Map<? extends K, ? extends Byte> map) {
        Set<? extends Map.Entry<? extends K, ? extends Byte>> set = map.entrySet();
        for (Map.Entry<? extends K, ? extends Byte> entry : set) {
            put(entry.getKey(), entry.getValue().byteValue());
        }
    }

    @Override // gnu.trove.map.TObjectByteMap
    public void putAll(TObjectByteMap<? extends K> map) {
        map.forEachEntry(this.PUT_ALL_PROC);
    }

    @Override // gnu.trove.impl.hash.THash, gnu.trove.map.TObjectByteMap
    public void clear() {
        super.clear();
        Arrays.fill(this._set, 0, this._set.length, FREE);
        Arrays.fill(this._values, 0, this._values.length, this.no_entry_value);
    }

    @Override // gnu.trove.map.TObjectByteMap
    public Set<K> keySet() {
        return new KeyView();
    }

    @Override // gnu.trove.map.TObjectByteMap
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
    @Override // gnu.trove.map.TObjectByteMap
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

    @Override // gnu.trove.map.TObjectByteMap
    public TByteCollection valueCollection() {
        return new TByteValueCollection();
    }

    @Override // gnu.trove.map.TObjectByteMap
    public byte[] values() {
        byte[] vals = new byte[size()];
        byte[] v = this._values;
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

    @Override // gnu.trove.map.TObjectByteMap
    public byte[] values(byte[] array) {
        int size = size();
        if (array.length < size) {
            array = new byte[size];
        }
        byte[] v = this._values;
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

    @Override // gnu.trove.map.TObjectByteMap
    public TObjectByteIterator<K> iterator() {
        return new TObjectByteHashIterator(this);
    }

    @Override // gnu.trove.map.TObjectByteMap
    public boolean increment(K key) {
        return adjustValue(key, (byte) 1);
    }

    @Override // gnu.trove.map.TObjectByteMap
    public boolean adjustValue(K key, byte amount) {
        int index = index(key);
        if (index < 0) {
            return false;
        }
        byte[] bArr = this._values;
        bArr[index] = (byte) (bArr[index] + amount);
        return true;
    }

    @Override // gnu.trove.map.TObjectByteMap
    public byte adjustOrPutValue(K key, byte adjust_amount, byte put_amount) {
        byte newValue;
        boolean isNewMapping;
        int index = insertKey(key);
        if (index < 0) {
            int index2 = (-index) - 1;
            byte[] bArr = this._values;
            byte b = (byte) (bArr[index2] + adjust_amount);
            bArr[index2] = b;
            newValue = b;
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
    @Override // gnu.trove.map.TObjectByteMap
    public boolean forEachKey(TObjectProcedure<? super K> procedure) {
        return forEach(procedure);
    }

    @Override // gnu.trove.map.TObjectByteMap
    public boolean forEachValue(TByteProcedure procedure) {
        Object[] keys = this._set;
        byte[] values = this._values;
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

    @Override // gnu.trove.map.TObjectByteMap
    public boolean forEachEntry(TObjectByteProcedure<? super K> procedure) {
        Object[] keys = this._set;
        byte[] values = this._values;
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

    @Override // gnu.trove.map.TObjectByteMap
    public boolean retainEntries(TObjectByteProcedure<? super K> procedure) {
        boolean modified = false;
        Object[] objArr = this._set;
        byte[] values = this._values;
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

    @Override // gnu.trove.map.TObjectByteMap
    public void transformValues(TByteFunction function) {
        Object[] keys = this._set;
        byte[] values = this._values;
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

    @Override // gnu.trove.map.TObjectByteMap
    public boolean equals(Object other) {
        if (!(other instanceof TObjectByteMap)) {
            return false;
        }
        TObjectByteMap that = (TObjectByteMap) other;
        if (that.size() != size()) {
            return false;
        }
        try {
            TObjectByteIterator iter = iterator();
            while (iter.hasNext()) {
                iter.advance();
                Object key = iter.key();
                byte value = iter.value();
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

    @Override // gnu.trove.map.TObjectByteMap
    public int hashCode() {
        int hashcode = 0;
        Object[] keys = this._set;
        byte[] values = this._values;
        int i = values.length;
        while (true) {
            int i2 = i;
            i--;
            if (i2 > 0) {
                if (keys[i] != FREE && keys[i] != REMOVED) {
                    hashcode += HashFunctions.hash((int) values[i]) ^ (keys[i] == null ? 0 : keys[i].hashCode());
                }
            } else {
                return hashcode;
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TObjectByteHashMap$KeyView.class */
    protected class KeyView extends TObjectByteHashMap<K>.MapBackedView<K> {
        protected KeyView() {
            super();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public Iterator<K> iterator() {
            return new TObjectHashIterator(TObjectByteHashMap.this);
        }

        @Override // gnu.trove.map.hash.TObjectByteHashMap.MapBackedView
        public boolean removeElement(K key) {
            return TObjectByteHashMap.this.no_entry_value != TObjectByteHashMap.this.remove(key);
        }

        @Override // gnu.trove.map.hash.TObjectByteHashMap.MapBackedView
        public boolean containsElement(K key) {
            return TObjectByteHashMap.this.contains(key);
        }
    }

    /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TObjectByteHashMap$MapBackedView.class */
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
            TObjectByteHashMap.this.clear();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean add(E obj) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return TObjectByteHashMap.this.size();
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
            return TObjectByteHashMap.this.isEmpty();
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
    /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TObjectByteHashMap$TByteValueCollection.class */
    public class TByteValueCollection implements TByteCollection {
        TByteValueCollection() {
        }

        @Override // gnu.trove.TByteCollection
        public TByteIterator iterator() {
            return new TObjectByteValueHashIterator();
        }

        @Override // gnu.trove.TByteCollection
        public byte getNoEntryValue() {
            return TObjectByteHashMap.this.no_entry_value;
        }

        @Override // gnu.trove.TByteCollection
        public int size() {
            return TObjectByteHashMap.this._size;
        }

        @Override // gnu.trove.TByteCollection
        public boolean isEmpty() {
            return 0 == TObjectByteHashMap.this._size;
        }

        @Override // gnu.trove.TByteCollection
        public boolean contains(byte entry) {
            return TObjectByteHashMap.this.containsValue(entry);
        }

        @Override // gnu.trove.TByteCollection
        public byte[] toArray() {
            return TObjectByteHashMap.this.values();
        }

        @Override // gnu.trove.TByteCollection
        public byte[] toArray(byte[] dest) {
            return TObjectByteHashMap.this.values(dest);
        }

        @Override // gnu.trove.TByteCollection
        public boolean add(byte entry) {
            throw new UnsupportedOperationException();
        }

        @Override // gnu.trove.TByteCollection
        public boolean remove(byte entry) {
            byte[] values = TObjectByteHashMap.this._values;
            Object[] set = TObjectByteHashMap.this._set;
            int i = values.length;
            while (true) {
                int i2 = i;
                i--;
                if (i2 > 0) {
                    if (set[i] != TObjectHash.FREE && set[i] != TObjectHash.REMOVED && entry == values[i]) {
                        TObjectByteHashMap.this.removeAt(i);
                        return true;
                    }
                } else {
                    return false;
                }
            }
        }

        @Override // gnu.trove.TByteCollection
        public boolean containsAll(Collection<?> collection) {
            for (Object element : collection) {
                if (element instanceof Byte) {
                    byte ele = ((Byte) element).byteValue();
                    if (!TObjectByteHashMap.this.containsValue(ele)) {
                        return false;
                    }
                } else {
                    return false;
                }
            }
            return true;
        }

        @Override // gnu.trove.TByteCollection
        public boolean containsAll(TByteCollection collection) {
            TByteIterator iter = collection.iterator();
            while (iter.hasNext()) {
                if (!TObjectByteHashMap.this.containsValue(iter.next())) {
                    return false;
                }
            }
            return true;
        }

        @Override // gnu.trove.TByteCollection
        public boolean containsAll(byte[] array) {
            for (byte element : array) {
                if (!TObjectByteHashMap.this.containsValue(element)) {
                    return false;
                }
            }
            return true;
        }

        @Override // gnu.trove.TByteCollection
        public boolean addAll(Collection<? extends Byte> collection) {
            throw new UnsupportedOperationException();
        }

        @Override // gnu.trove.TByteCollection
        public boolean addAll(TByteCollection collection) {
            throw new UnsupportedOperationException();
        }

        @Override // gnu.trove.TByteCollection
        public boolean addAll(byte[] array) {
            throw new UnsupportedOperationException();
        }

        @Override // gnu.trove.TByteCollection
        public boolean retainAll(Collection<?> collection) {
            boolean modified = false;
            TByteIterator iter = iterator();
            while (iter.hasNext()) {
                if (!collection.contains(Byte.valueOf(iter.next()))) {
                    iter.remove();
                    modified = true;
                }
            }
            return modified;
        }

        @Override // gnu.trove.TByteCollection
        public boolean retainAll(TByteCollection collection) {
            if (this == collection) {
                return false;
            }
            boolean modified = false;
            TByteIterator iter = iterator();
            while (iter.hasNext()) {
                if (!collection.contains(iter.next())) {
                    iter.remove();
                    modified = true;
                }
            }
            return modified;
        }

        @Override // gnu.trove.TByteCollection
        public boolean retainAll(byte[] array) {
            boolean changed = false;
            Arrays.sort(array);
            byte[] values = TObjectByteHashMap.this._values;
            Object[] set = TObjectByteHashMap.this._set;
            int i = set.length;
            while (true) {
                int i2 = i;
                i--;
                if (i2 > 0) {
                    if (set[i] != TObjectHash.FREE && set[i] != TObjectHash.REMOVED && Arrays.binarySearch(array, values[i]) < 0) {
                        TObjectByteHashMap.this.removeAt(i);
                        changed = true;
                    }
                } else {
                    return changed;
                }
            }
        }

        @Override // gnu.trove.TByteCollection
        public boolean removeAll(Collection<?> collection) {
            boolean changed = false;
            for (Object element : collection) {
                if (element instanceof Byte) {
                    byte c = ((Byte) element).byteValue();
                    if (remove(c)) {
                        changed = true;
                    }
                }
            }
            return changed;
        }

        @Override // gnu.trove.TByteCollection
        public boolean removeAll(TByteCollection collection) {
            if (this == collection) {
                clear();
                return true;
            }
            boolean changed = false;
            TByteIterator iter = collection.iterator();
            while (iter.hasNext()) {
                byte element = iter.next();
                if (remove(element)) {
                    changed = true;
                }
            }
            return changed;
        }

        @Override // gnu.trove.TByteCollection
        public boolean removeAll(byte[] array) {
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

        @Override // gnu.trove.TByteCollection
        public void clear() {
            TObjectByteHashMap.this.clear();
        }

        @Override // gnu.trove.TByteCollection
        public boolean forEach(TByteProcedure procedure) {
            return TObjectByteHashMap.this.forEachValue(procedure);
        }

        public String toString() {
            final StringBuilder buf = new StringBuilder("{");
            TObjectByteHashMap.this.forEachValue(new TByteProcedure() { // from class: gnu.trove.map.hash.TObjectByteHashMap.TByteValueCollection.1
                private boolean first = true;

                @Override // gnu.trove.procedure.TByteProcedure
                public boolean execute(byte value) {
                    if (this.first) {
                        this.first = false;
                    } else {
                        buf.append(", ");
                    }
                    buf.append((int) value);
                    return true;
                }
            });
            buf.append("}");
            return buf.toString();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TObjectByteHashMap$TByteValueCollection$TObjectByteValueHashIterator.class */
        public class TObjectByteValueHashIterator implements TByteIterator {
            protected THash _hash;
            protected int _expectedSize;
            protected int _index;

            TObjectByteValueHashIterator() {
                this._hash = TObjectByteHashMap.this;
                this._expectedSize = this._hash.size();
                this._index = this._hash.capacity();
            }

            @Override // gnu.trove.iterator.TIterator, java.util.Iterator
            public boolean hasNext() {
                return nextIndex() >= 0;
            }

            @Override // gnu.trove.iterator.TByteIterator
            public byte next() {
                moveToNextIndex();
                return TObjectByteHashMap.this._values[this._index];
            }

            @Override // gnu.trove.iterator.TIterator, java.util.Iterator
            public void remove() {
                if (this._expectedSize != this._hash.size()) {
                    throw new ConcurrentModificationException();
                }
                try {
                    this._hash.tempDisableAutoCompaction();
                    TObjectByteHashMap.this.removeAt(this._index);
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
                Object[] set = TObjectByteHashMap.this._set;
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
    /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TObjectByteHashMap$TObjectByteHashIterator.class */
    public class TObjectByteHashIterator<K> extends TObjectHashIterator<K> implements TObjectByteIterator<K> {
        private final TObjectByteHashMap<K> _map;

        public TObjectByteHashIterator(TObjectByteHashMap<K> map) {
            super(map);
            this._map = map;
        }

        @Override // gnu.trove.iterator.TAdvancingIterator
        public void advance() {
            moveToNextIndex();
        }

        @Override // gnu.trove.iterator.TObjectByteIterator
        public K key() {
            return (K) this._map._set[this._index];
        }

        @Override // gnu.trove.iterator.TObjectByteIterator
        public byte value() {
            return this._map._values[this._index];
        }

        @Override // gnu.trove.iterator.TObjectByteIterator
        public byte setValue(byte val) {
            byte old = value();
            this._map._values[this._index] = val;
            return old;
        }
    }

    @Override // gnu.trove.impl.hash.TObjectHash, gnu.trove.impl.hash.THash, java.io.Externalizable
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeByte(0);
        super.writeExternal(out);
        out.writeByte(this.no_entry_value);
        out.writeInt(this._size);
        int i = this._set.length;
        while (true) {
            int i2 = i;
            i--;
            if (i2 > 0) {
                if (this._set[i] != REMOVED && this._set[i] != FREE) {
                    out.writeObject(this._set[i]);
                    out.writeByte(this._values[i]);
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
        this.no_entry_value = in.readByte();
        int size = in.readInt();
        setUp(size);
        while (true) {
            int i = size;
            size--;
            if (i > 0) {
                Object readObject = in.readObject();
                byte val = in.readByte();
                put(readObject, val);
            } else {
                return;
            }
        }
    }

    public String toString() {
        final StringBuilder buf = new StringBuilder("{");
        forEachEntry(new TObjectByteProcedure<K>() { // from class: gnu.trove.map.hash.TObjectByteHashMap.2
            private boolean first = true;

            @Override // gnu.trove.procedure.TObjectByteProcedure
            public boolean execute(K key, byte value) {
                if (this.first) {
                    this.first = false;
                } else {
                    buf.append(",");
                }
                buf.append(key).append("=").append((int) value);
                return true;
            }
        });
        buf.append("}");
        return buf.toString();
    }
}
