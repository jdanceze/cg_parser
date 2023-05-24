package gnu.trove.map.custom_hash;

import gnu.trove.TLongCollection;
import gnu.trove.function.TLongFunction;
import gnu.trove.impl.Constants;
import gnu.trove.impl.HashFunctions;
import gnu.trove.impl.hash.TCustomObjectHash;
import gnu.trove.impl.hash.THash;
import gnu.trove.impl.hash.TObjectHash;
import gnu.trove.iterator.TLongIterator;
import gnu.trove.iterator.TObjectLongIterator;
import gnu.trove.iterator.hash.TObjectHashIterator;
import gnu.trove.map.TObjectLongMap;
import gnu.trove.procedure.TLongProcedure;
import gnu.trove.procedure.TObjectLongProcedure;
import gnu.trove.procedure.TObjectProcedure;
import gnu.trove.strategy.HashingStrategy;
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
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/custom_hash/TObjectLongCustomHashMap.class */
public class TObjectLongCustomHashMap<K> extends TCustomObjectHash<K> implements TObjectLongMap<K>, Externalizable {
    static final long serialVersionUID = 1;
    private final TObjectLongProcedure<K> PUT_ALL_PROC;
    protected transient long[] _values;
    protected long no_entry_value;

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: JavaClassParseException: Unknown opcode: 0x5e in method: gnu.trove.map.custom_hash.TObjectLongCustomHashMap.adjustOrPutValue(K, long, long):long, file: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/custom_hash/TObjectLongCustomHashMap.class
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:158)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:413)
        	at jadx.core.ProcessClass.process(ProcessClass.java:67)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:115)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:387)
        	at jadx.core.dex.nodes.ClassNode.getCode(ClassNode.java:335)
        Caused by: jadx.plugins.input.java.utils.JavaClassParseException: Unknown opcode: 0x5e
        	at jadx.plugins.input.java.data.code.JavaCodeReader.visitInstructions(JavaCodeReader.java:71)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:48)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:148)
        	... 5 more
        */
    @Override // gnu.trove.map.TObjectLongMap
    public long adjustOrPutValue(K r1, long r2, long r4) {
        /*
        // Can't load method instructions: Load method exception: JavaClassParseException: Unknown opcode: 0x5e in method: gnu.trove.map.custom_hash.TObjectLongCustomHashMap.adjustOrPutValue(K, long, long):long, file: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/custom_hash/TObjectLongCustomHashMap.class
        */
        throw new UnsupportedOperationException("Method not decompiled: gnu.trove.map.custom_hash.TObjectLongCustomHashMap.adjustOrPutValue(java.lang.Object, long, long):long");
    }

    public TObjectLongCustomHashMap() {
        this.PUT_ALL_PROC = new TObjectLongProcedure<K>() { // from class: gnu.trove.map.custom_hash.TObjectLongCustomHashMap.1
            @Override // gnu.trove.procedure.TObjectLongProcedure
            public boolean execute(K key, long value) {
                TObjectLongCustomHashMap.this.put(key, value);
                return true;
            }
        };
    }

    public TObjectLongCustomHashMap(HashingStrategy<? super K> strategy) {
        super(strategy);
        this.PUT_ALL_PROC = new TObjectLongProcedure<K>() { // from class: gnu.trove.map.custom_hash.TObjectLongCustomHashMap.1
            @Override // gnu.trove.procedure.TObjectLongProcedure
            public boolean execute(K key, long value) {
                TObjectLongCustomHashMap.this.put(key, value);
                return true;
            }
        };
        this.no_entry_value = Constants.DEFAULT_LONG_NO_ENTRY_VALUE;
    }

    public TObjectLongCustomHashMap(HashingStrategy<? super K> strategy, int initialCapacity) {
        super(strategy, initialCapacity);
        this.PUT_ALL_PROC = new TObjectLongProcedure<K>() { // from class: gnu.trove.map.custom_hash.TObjectLongCustomHashMap.1
            @Override // gnu.trove.procedure.TObjectLongProcedure
            public boolean execute(K key, long value) {
                TObjectLongCustomHashMap.this.put(key, value);
                return true;
            }
        };
        this.no_entry_value = Constants.DEFAULT_LONG_NO_ENTRY_VALUE;
    }

    public TObjectLongCustomHashMap(HashingStrategy<? super K> strategy, int initialCapacity, float loadFactor) {
        super(strategy, initialCapacity, loadFactor);
        this.PUT_ALL_PROC = new TObjectLongProcedure<K>() { // from class: gnu.trove.map.custom_hash.TObjectLongCustomHashMap.1
            @Override // gnu.trove.procedure.TObjectLongProcedure
            public boolean execute(K key, long value) {
                TObjectLongCustomHashMap.this.put(key, value);
                return true;
            }
        };
        this.no_entry_value = Constants.DEFAULT_LONG_NO_ENTRY_VALUE;
    }

    public TObjectLongCustomHashMap(HashingStrategy<? super K> strategy, int initialCapacity, float loadFactor, long noEntryValue) {
        super(strategy, initialCapacity, loadFactor);
        this.PUT_ALL_PROC = new TObjectLongProcedure<K>() { // from class: gnu.trove.map.custom_hash.TObjectLongCustomHashMap.1
            @Override // gnu.trove.procedure.TObjectLongProcedure
            public boolean execute(K key, long value) {
                TObjectLongCustomHashMap.this.put(key, value);
                return true;
            }
        };
        this.no_entry_value = noEntryValue;
        if (this.no_entry_value != 0) {
            Arrays.fill(this._values, this.no_entry_value);
        }
    }

    public TObjectLongCustomHashMap(HashingStrategy<? super K> strategy, TObjectLongMap<? extends K> map) {
        this(strategy, map.size(), 0.5f, map.getNoEntryValue());
        if (map instanceof TObjectLongCustomHashMap) {
            TObjectLongCustomHashMap hashmap = (TObjectLongCustomHashMap) map;
            this._loadFactor = hashmap._loadFactor;
            this.no_entry_value = hashmap.no_entry_value;
            this.strategy = hashmap.strategy;
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
        this._values = new long[capacity];
        return capacity;
    }

    @Override // gnu.trove.impl.hash.THash
    protected void rehash(int newCapacity) {
        int oldCapacity = this._set.length;
        Object[] objArr = this._set;
        long[] oldVals = this._values;
        this._set = new Object[newCapacity];
        Arrays.fill(this._set, FREE);
        this._values = new long[newCapacity];
        Arrays.fill(this._values, this.no_entry_value);
        int i = oldCapacity;
        while (true) {
            int i2 = i;
            i--;
            if (i2 > 0) {
                Object obj = objArr[i];
                if (obj != FREE && obj != REMOVED) {
                    int index = insertKey(obj);
                    if (index < 0) {
                        throwObjectContractViolation(this._set[(-index) - 1], obj);
                    }
                    this._values[index] = oldVals[i];
                }
            } else {
                return;
            }
        }
    }

    @Override // gnu.trove.map.TObjectLongMap
    public long getNoEntryValue() {
        return this.no_entry_value;
    }

    @Override // gnu.trove.map.TObjectLongMap
    public boolean containsKey(Object key) {
        return contains(key);
    }

    @Override // gnu.trove.map.TObjectLongMap
    public boolean containsValue(long val) {
        Object[] keys = this._set;
        long[] vals = this._values;
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

    @Override // gnu.trove.map.TObjectLongMap
    public long get(Object key) {
        int index = index(key);
        return index < 0 ? this.no_entry_value : this._values[index];
    }

    @Override // gnu.trove.map.TObjectLongMap
    public long put(K key, long value) {
        int index = insertKey(key);
        return doPut(value, index);
    }

    @Override // gnu.trove.map.TObjectLongMap
    public long putIfAbsent(K key, long value) {
        int index = insertKey(key);
        if (index < 0) {
            return this._values[(-index) - 1];
        }
        return doPut(value, index);
    }

    private long doPut(long value, int index) {
        long previous = this.no_entry_value;
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

    @Override // gnu.trove.map.TObjectLongMap
    public long remove(Object key) {
        long prev = this.no_entry_value;
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

    @Override // gnu.trove.map.TObjectLongMap
    public void putAll(Map<? extends K, ? extends Long> map) {
        Set<? extends Map.Entry<? extends K, ? extends Long>> set = map.entrySet();
        for (Map.Entry<? extends K, ? extends Long> entry : set) {
            put(entry.getKey(), entry.getValue().longValue());
        }
    }

    @Override // gnu.trove.map.TObjectLongMap
    public void putAll(TObjectLongMap<? extends K> map) {
        map.forEachEntry(this.PUT_ALL_PROC);
    }

    @Override // gnu.trove.impl.hash.THash, gnu.trove.map.TObjectByteMap
    public void clear() {
        super.clear();
        Arrays.fill(this._set, 0, this._set.length, FREE);
        Arrays.fill(this._values, 0, this._values.length, this.no_entry_value);
    }

    @Override // gnu.trove.map.TObjectLongMap
    public Set<K> keySet() {
        return new KeyView();
    }

    @Override // gnu.trove.map.TObjectLongMap
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
    @Override // gnu.trove.map.TObjectLongMap
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

    @Override // gnu.trove.map.TObjectLongMap
    public TLongCollection valueCollection() {
        return new TLongValueCollection();
    }

    @Override // gnu.trove.map.TObjectLongMap
    public long[] values() {
        long[] vals = new long[size()];
        long[] v = this._values;
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

    @Override // gnu.trove.map.TObjectLongMap
    public long[] values(long[] array) {
        int size = size();
        if (array.length < size) {
            array = new long[size];
        }
        long[] v = this._values;
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

    @Override // gnu.trove.map.TObjectLongMap
    public TObjectLongIterator<K> iterator() {
        return new TObjectLongHashIterator(this);
    }

    @Override // gnu.trove.map.TObjectLongMap
    public boolean increment(K key) {
        return adjustValue(key, 1L);
    }

    @Override // gnu.trove.map.TObjectLongMap
    public boolean adjustValue(K key, long amount) {
        int index = index(key);
        if (index < 0) {
            return false;
        }
        long[] jArr = this._values;
        jArr[index] = jArr[index] + amount;
        return true;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // gnu.trove.map.TObjectLongMap
    public boolean forEachKey(TObjectProcedure<? super K> procedure) {
        return forEach(procedure);
    }

    @Override // gnu.trove.map.TObjectLongMap
    public boolean forEachValue(TLongProcedure procedure) {
        Object[] keys = this._set;
        long[] values = this._values;
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

    @Override // gnu.trove.map.TObjectLongMap
    public boolean forEachEntry(TObjectLongProcedure<? super K> procedure) {
        Object[] keys = this._set;
        long[] values = this._values;
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

    @Override // gnu.trove.map.TObjectLongMap
    public boolean retainEntries(TObjectLongProcedure<? super K> procedure) {
        boolean modified = false;
        Object[] objArr = this._set;
        long[] values = this._values;
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

    @Override // gnu.trove.map.TObjectLongMap
    public void transformValues(TLongFunction function) {
        Object[] keys = this._set;
        long[] values = this._values;
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

    @Override // gnu.trove.map.TObjectLongMap
    public boolean equals(Object other) {
        if (!(other instanceof TObjectLongMap)) {
            return false;
        }
        TObjectLongMap that = (TObjectLongMap) other;
        if (that.size() != size()) {
            return false;
        }
        try {
            TObjectLongIterator iter = iterator();
            while (iter.hasNext()) {
                iter.advance();
                Object key = iter.key();
                long value = iter.value();
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

    @Override // gnu.trove.map.TObjectLongMap
    public int hashCode() {
        int hashcode = 0;
        Object[] keys = this._set;
        long[] values = this._values;
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

    /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/custom_hash/TObjectLongCustomHashMap$KeyView.class */
    protected class KeyView extends TObjectLongCustomHashMap<K>.MapBackedView<K> {
        protected KeyView() {
            super();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public Iterator<K> iterator() {
            return new TObjectHashIterator(TObjectLongCustomHashMap.this);
        }

        @Override // gnu.trove.map.custom_hash.TObjectLongCustomHashMap.MapBackedView
        public boolean removeElement(K key) {
            return TObjectLongCustomHashMap.this.no_entry_value != TObjectLongCustomHashMap.this.remove(key);
        }

        @Override // gnu.trove.map.custom_hash.TObjectLongCustomHashMap.MapBackedView
        public boolean containsElement(K key) {
            return TObjectLongCustomHashMap.this.contains(key);
        }
    }

    /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/custom_hash/TObjectLongCustomHashMap$MapBackedView.class */
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
            TObjectLongCustomHashMap.this.clear();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean add(E obj) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return TObjectLongCustomHashMap.this.size();
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
            return TObjectLongCustomHashMap.this.isEmpty();
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
    /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/custom_hash/TObjectLongCustomHashMap$TLongValueCollection.class */
    public class TLongValueCollection implements TLongCollection {
        TLongValueCollection() {
        }

        @Override // gnu.trove.TLongCollection
        public TLongIterator iterator() {
            return new TObjectLongValueHashIterator();
        }

        @Override // gnu.trove.TLongCollection
        public long getNoEntryValue() {
            return TObjectLongCustomHashMap.this.no_entry_value;
        }

        @Override // gnu.trove.TLongCollection
        public int size() {
            return TObjectLongCustomHashMap.this._size;
        }

        @Override // gnu.trove.TLongCollection
        public boolean isEmpty() {
            return 0 == TObjectLongCustomHashMap.this._size;
        }

        @Override // gnu.trove.TLongCollection
        public boolean contains(long entry) {
            return TObjectLongCustomHashMap.this.containsValue(entry);
        }

        @Override // gnu.trove.TLongCollection
        public long[] toArray() {
            return TObjectLongCustomHashMap.this.values();
        }

        @Override // gnu.trove.TLongCollection
        public long[] toArray(long[] dest) {
            return TObjectLongCustomHashMap.this.values(dest);
        }

        @Override // gnu.trove.TLongCollection
        public boolean add(long entry) {
            throw new UnsupportedOperationException();
        }

        @Override // gnu.trove.TLongCollection
        public boolean remove(long entry) {
            long[] values = TObjectLongCustomHashMap.this._values;
            Object[] set = TObjectLongCustomHashMap.this._set;
            int i = values.length;
            while (true) {
                int i2 = i;
                i--;
                if (i2 > 0) {
                    if (set[i] != TObjectHash.FREE && set[i] != TObjectHash.REMOVED && entry == values[i]) {
                        TObjectLongCustomHashMap.this.removeAt(i);
                        return true;
                    }
                } else {
                    return false;
                }
            }
        }

        @Override // gnu.trove.TLongCollection
        public boolean containsAll(Collection<?> collection) {
            for (Object element : collection) {
                if (element instanceof Long) {
                    long ele = ((Long) element).longValue();
                    if (!TObjectLongCustomHashMap.this.containsValue(ele)) {
                        return false;
                    }
                } else {
                    return false;
                }
            }
            return true;
        }

        @Override // gnu.trove.TLongCollection
        public boolean containsAll(TLongCollection collection) {
            TLongIterator iter = collection.iterator();
            while (iter.hasNext()) {
                if (!TObjectLongCustomHashMap.this.containsValue(iter.next())) {
                    return false;
                }
            }
            return true;
        }

        @Override // gnu.trove.TLongCollection
        public boolean containsAll(long[] array) {
            for (long element : array) {
                if (!TObjectLongCustomHashMap.this.containsValue(element)) {
                    return false;
                }
            }
            return true;
        }

        @Override // gnu.trove.TLongCollection
        public boolean addAll(Collection<? extends Long> collection) {
            throw new UnsupportedOperationException();
        }

        @Override // gnu.trove.TLongCollection
        public boolean addAll(TLongCollection collection) {
            throw new UnsupportedOperationException();
        }

        @Override // gnu.trove.TLongCollection
        public boolean addAll(long[] array) {
            throw new UnsupportedOperationException();
        }

        @Override // gnu.trove.TLongCollection
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

        @Override // gnu.trove.TLongCollection
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

        @Override // gnu.trove.TLongCollection
        public boolean retainAll(long[] array) {
            boolean changed = false;
            Arrays.sort(array);
            long[] values = TObjectLongCustomHashMap.this._values;
            Object[] set = TObjectLongCustomHashMap.this._set;
            int i = set.length;
            while (true) {
                int i2 = i;
                i--;
                if (i2 > 0) {
                    if (set[i] != TObjectHash.FREE && set[i] != TObjectHash.REMOVED && Arrays.binarySearch(array, values[i]) < 0) {
                        TObjectLongCustomHashMap.this.removeAt(i);
                        changed = true;
                    }
                } else {
                    return changed;
                }
            }
        }

        @Override // gnu.trove.TLongCollection
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

        @Override // gnu.trove.TLongCollection
        public boolean removeAll(TLongCollection collection) {
            if (this == collection) {
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

        @Override // gnu.trove.TLongCollection
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

        @Override // gnu.trove.TLongCollection
        public void clear() {
            TObjectLongCustomHashMap.this.clear();
        }

        @Override // gnu.trove.TLongCollection
        public boolean forEach(TLongProcedure procedure) {
            return TObjectLongCustomHashMap.this.forEachValue(procedure);
        }

        public String toString() {
            final StringBuilder buf = new StringBuilder("{");
            TObjectLongCustomHashMap.this.forEachValue(new TLongProcedure() { // from class: gnu.trove.map.custom_hash.TObjectLongCustomHashMap.TLongValueCollection.1
                private boolean first = true;

                @Override // gnu.trove.procedure.TLongProcedure
                public boolean execute(long value) {
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
        /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/custom_hash/TObjectLongCustomHashMap$TLongValueCollection$TObjectLongValueHashIterator.class */
        public class TObjectLongValueHashIterator implements TLongIterator {
            protected THash _hash;
            protected int _expectedSize;
            protected int _index;

            TObjectLongValueHashIterator() {
                this._hash = TObjectLongCustomHashMap.this;
                this._expectedSize = this._hash.size();
                this._index = this._hash.capacity();
            }

            @Override // gnu.trove.iterator.TIterator, java.util.Iterator
            public boolean hasNext() {
                return nextIndex() >= 0;
            }

            @Override // gnu.trove.iterator.TLongIterator
            public long next() {
                moveToNextIndex();
                return TObjectLongCustomHashMap.this._values[this._index];
            }

            @Override // gnu.trove.iterator.TIterator, java.util.Iterator
            public void remove() {
                if (this._expectedSize != this._hash.size()) {
                    throw new ConcurrentModificationException();
                }
                try {
                    this._hash.tempDisableAutoCompaction();
                    TObjectLongCustomHashMap.this.removeAt(this._index);
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
                Object[] set = TObjectLongCustomHashMap.this._set;
                int i = this._index;
                while (true) {
                    int i2 = i;
                    i--;
                    if (i2 <= 0 || (set[i] != TCustomObjectHash.FREE && set[i] != TCustomObjectHash.REMOVED)) {
                        break;
                    }
                }
                return i;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/custom_hash/TObjectLongCustomHashMap$TObjectLongHashIterator.class */
    public class TObjectLongHashIterator<K> extends TObjectHashIterator<K> implements TObjectLongIterator<K> {
        private final TObjectLongCustomHashMap<K> _map;

        public TObjectLongHashIterator(TObjectLongCustomHashMap<K> map) {
            super(map);
            this._map = map;
        }

        @Override // gnu.trove.iterator.TAdvancingIterator
        public void advance() {
            moveToNextIndex();
        }

        @Override // gnu.trove.iterator.TObjectLongIterator
        public K key() {
            return (K) this._map._set[this._index];
        }

        @Override // gnu.trove.iterator.TObjectLongIterator
        public long value() {
            return this._map._values[this._index];
        }

        @Override // gnu.trove.iterator.TObjectLongIterator
        public long setValue(long val) {
            long old = value();
            this._map._values[this._index] = val;
            return old;
        }
    }

    @Override // gnu.trove.impl.hash.TCustomObjectHash, gnu.trove.impl.hash.TObjectHash, gnu.trove.impl.hash.THash, java.io.Externalizable
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeByte(0);
        super.writeExternal(out);
        out.writeObject(this.strategy);
        out.writeLong(this.no_entry_value);
        out.writeInt(this._size);
        int i = this._set.length;
        while (true) {
            int i2 = i;
            i--;
            if (i2 > 0) {
                if (this._set[i] != REMOVED && this._set[i] != FREE) {
                    out.writeObject(this._set[i]);
                    out.writeLong(this._values[i]);
                }
            } else {
                return;
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // gnu.trove.impl.hash.TCustomObjectHash, gnu.trove.impl.hash.TObjectHash, gnu.trove.impl.hash.THash, java.io.Externalizable
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        in.readByte();
        super.readExternal(in);
        this.strategy = (HashingStrategy) in.readObject();
        this.no_entry_value = in.readLong();
        int size = in.readInt();
        setUp(size);
        while (true) {
            int i = size;
            size--;
            if (i > 0) {
                Object readObject = in.readObject();
                long val = in.readLong();
                put(readObject, val);
            } else {
                return;
            }
        }
    }

    public String toString() {
        final StringBuilder buf = new StringBuilder("{");
        forEachEntry(new TObjectLongProcedure<K>() { // from class: gnu.trove.map.custom_hash.TObjectLongCustomHashMap.2
            private boolean first = true;

            @Override // gnu.trove.procedure.TObjectLongProcedure
            public boolean execute(K key, long value) {
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
