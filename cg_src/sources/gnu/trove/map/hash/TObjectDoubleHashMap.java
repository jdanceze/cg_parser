package gnu.trove.map.hash;

import com.sun.xml.bind.v2.runtime.reflect.opt.Const;
import gnu.trove.TDoubleCollection;
import gnu.trove.function.TDoubleFunction;
import gnu.trove.impl.Constants;
import gnu.trove.impl.HashFunctions;
import gnu.trove.impl.hash.THash;
import gnu.trove.impl.hash.TObjectHash;
import gnu.trove.iterator.TDoubleIterator;
import gnu.trove.iterator.TObjectDoubleIterator;
import gnu.trove.iterator.hash.TObjectHashIterator;
import gnu.trove.map.TObjectDoubleMap;
import gnu.trove.procedure.TDoubleProcedure;
import gnu.trove.procedure.TObjectDoubleProcedure;
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
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TObjectDoubleHashMap.class */
public class TObjectDoubleHashMap<K> extends TObjectHash<K> implements TObjectDoubleMap<K>, Externalizable {
    static final long serialVersionUID = 1;
    private final TObjectDoubleProcedure<K> PUT_ALL_PROC;
    protected transient double[] _values;
    protected double no_entry_value;

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: JavaClassParseException: Unknown opcode: 0x5e in method: gnu.trove.map.hash.TObjectDoubleHashMap.adjustOrPutValue(K, double, double):double, file: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TObjectDoubleHashMap.class
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
    @Override // gnu.trove.map.TObjectDoubleMap
    public double adjustOrPutValue(K r1, double r2, double r4) {
        /*
        // Can't load method instructions: Load method exception: JavaClassParseException: Unknown opcode: 0x5e in method: gnu.trove.map.hash.TObjectDoubleHashMap.adjustOrPutValue(K, double, double):double, file: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TObjectDoubleHashMap.class
        */
        throw new UnsupportedOperationException("Method not decompiled: gnu.trove.map.hash.TObjectDoubleHashMap.adjustOrPutValue(java.lang.Object, double, double):double");
    }

    public TObjectDoubleHashMap() {
        this.PUT_ALL_PROC = new TObjectDoubleProcedure<K>() { // from class: gnu.trove.map.hash.TObjectDoubleHashMap.1
            @Override // gnu.trove.procedure.TObjectDoubleProcedure
            public boolean execute(K key, double value) {
                TObjectDoubleHashMap.this.put(key, value);
                return true;
            }
        };
        this.no_entry_value = Constants.DEFAULT_DOUBLE_NO_ENTRY_VALUE;
    }

    public TObjectDoubleHashMap(int initialCapacity) {
        super(initialCapacity);
        this.PUT_ALL_PROC = new TObjectDoubleProcedure<K>() { // from class: gnu.trove.map.hash.TObjectDoubleHashMap.1
            @Override // gnu.trove.procedure.TObjectDoubleProcedure
            public boolean execute(K key, double value) {
                TObjectDoubleHashMap.this.put(key, value);
                return true;
            }
        };
        this.no_entry_value = Constants.DEFAULT_DOUBLE_NO_ENTRY_VALUE;
    }

    public TObjectDoubleHashMap(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
        this.PUT_ALL_PROC = new TObjectDoubleProcedure<K>() { // from class: gnu.trove.map.hash.TObjectDoubleHashMap.1
            @Override // gnu.trove.procedure.TObjectDoubleProcedure
            public boolean execute(K key, double value) {
                TObjectDoubleHashMap.this.put(key, value);
                return true;
            }
        };
        this.no_entry_value = Constants.DEFAULT_DOUBLE_NO_ENTRY_VALUE;
    }

    public TObjectDoubleHashMap(int initialCapacity, float loadFactor, double noEntryValue) {
        super(initialCapacity, loadFactor);
        this.PUT_ALL_PROC = new TObjectDoubleProcedure<K>() { // from class: gnu.trove.map.hash.TObjectDoubleHashMap.1
            @Override // gnu.trove.procedure.TObjectDoubleProcedure
            public boolean execute(K key, double value) {
                TObjectDoubleHashMap.this.put(key, value);
                return true;
            }
        };
        this.no_entry_value = noEntryValue;
        if (this.no_entry_value != Const.default_value_double) {
            Arrays.fill(this._values, this.no_entry_value);
        }
    }

    public TObjectDoubleHashMap(TObjectDoubleMap<? extends K> map) {
        this(map.size(), 0.5f, map.getNoEntryValue());
        if (map instanceof TObjectDoubleHashMap) {
            TObjectDoubleHashMap hashmap = (TObjectDoubleHashMap) map;
            this._loadFactor = hashmap._loadFactor;
            this.no_entry_value = hashmap.no_entry_value;
            if (this.no_entry_value != Const.default_value_double) {
                Arrays.fill(this._values, this.no_entry_value);
            }
            setUp((int) Math.ceil(10.0f / this._loadFactor));
        }
        putAll(map);
    }

    @Override // gnu.trove.impl.hash.TObjectHash, gnu.trove.impl.hash.THash
    public int setUp(int initialCapacity) {
        int capacity = super.setUp(initialCapacity);
        this._values = new double[capacity];
        return capacity;
    }

    @Override // gnu.trove.impl.hash.THash
    protected void rehash(int newCapacity) {
        int oldCapacity = this._set.length;
        Object[] objArr = this._set;
        double[] oldVals = this._values;
        this._set = new Object[newCapacity];
        Arrays.fill(this._set, FREE);
        this._values = new double[newCapacity];
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

    @Override // gnu.trove.map.TObjectDoubleMap
    public double getNoEntryValue() {
        return this.no_entry_value;
    }

    @Override // gnu.trove.map.TObjectDoubleMap
    public boolean containsKey(Object key) {
        return contains(key);
    }

    @Override // gnu.trove.map.TObjectDoubleMap
    public boolean containsValue(double val) {
        Object[] keys = this._set;
        double[] vals = this._values;
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

    @Override // gnu.trove.map.TObjectDoubleMap
    public double get(Object key) {
        int index = index(key);
        return index < 0 ? this.no_entry_value : this._values[index];
    }

    @Override // gnu.trove.map.TObjectDoubleMap
    public double put(K key, double value) {
        int index = insertKey(key);
        return doPut(value, index);
    }

    @Override // gnu.trove.map.TObjectDoubleMap
    public double putIfAbsent(K key, double value) {
        int index = insertKey(key);
        if (index < 0) {
            return this._values[(-index) - 1];
        }
        return doPut(value, index);
    }

    private double doPut(double value, int index) {
        double previous = this.no_entry_value;
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

    @Override // gnu.trove.map.TObjectDoubleMap
    public double remove(Object key) {
        double prev = this.no_entry_value;
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

    @Override // gnu.trove.map.TObjectDoubleMap
    public void putAll(Map<? extends K, ? extends Double> map) {
        Set<? extends Map.Entry<? extends K, ? extends Double>> set = map.entrySet();
        for (Map.Entry<? extends K, ? extends Double> entry : set) {
            put(entry.getKey(), entry.getValue().doubleValue());
        }
    }

    @Override // gnu.trove.map.TObjectDoubleMap
    public void putAll(TObjectDoubleMap<? extends K> map) {
        map.forEachEntry(this.PUT_ALL_PROC);
    }

    @Override // gnu.trove.impl.hash.THash, gnu.trove.map.TObjectByteMap
    public void clear() {
        super.clear();
        Arrays.fill(this._set, 0, this._set.length, FREE);
        Arrays.fill(this._values, 0, this._values.length, this.no_entry_value);
    }

    @Override // gnu.trove.map.TObjectDoubleMap
    public Set<K> keySet() {
        return new KeyView();
    }

    @Override // gnu.trove.map.TObjectDoubleMap
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
    @Override // gnu.trove.map.TObjectDoubleMap
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

    @Override // gnu.trove.map.TObjectDoubleMap
    public TDoubleCollection valueCollection() {
        return new TDoubleValueCollection();
    }

    @Override // gnu.trove.map.TObjectDoubleMap
    public double[] values() {
        double[] vals = new double[size()];
        double[] v = this._values;
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

    @Override // gnu.trove.map.TObjectDoubleMap
    public double[] values(double[] array) {
        int size = size();
        if (array.length < size) {
            array = new double[size];
        }
        double[] v = this._values;
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

    @Override // gnu.trove.map.TObjectDoubleMap
    public TObjectDoubleIterator<K> iterator() {
        return new TObjectDoubleHashIterator(this);
    }

    @Override // gnu.trove.map.TObjectDoubleMap
    public boolean increment(K key) {
        return adjustValue(key, 1.0d);
    }

    @Override // gnu.trove.map.TObjectDoubleMap
    public boolean adjustValue(K key, double amount) {
        int index = index(key);
        if (index < 0) {
            return false;
        }
        double[] dArr = this._values;
        dArr[index] = dArr[index] + amount;
        return true;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // gnu.trove.map.TObjectDoubleMap
    public boolean forEachKey(TObjectProcedure<? super K> procedure) {
        return forEach(procedure);
    }

    @Override // gnu.trove.map.TObjectDoubleMap
    public boolean forEachValue(TDoubleProcedure procedure) {
        Object[] keys = this._set;
        double[] values = this._values;
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

    @Override // gnu.trove.map.TObjectDoubleMap
    public boolean forEachEntry(TObjectDoubleProcedure<? super K> procedure) {
        Object[] keys = this._set;
        double[] values = this._values;
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

    @Override // gnu.trove.map.TObjectDoubleMap
    public boolean retainEntries(TObjectDoubleProcedure<? super K> procedure) {
        boolean modified = false;
        Object[] objArr = this._set;
        double[] values = this._values;
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

    @Override // gnu.trove.map.TObjectDoubleMap
    public void transformValues(TDoubleFunction function) {
        Object[] keys = this._set;
        double[] values = this._values;
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

    @Override // gnu.trove.map.TObjectDoubleMap
    public boolean equals(Object other) {
        if (!(other instanceof TObjectDoubleMap)) {
            return false;
        }
        TObjectDoubleMap that = (TObjectDoubleMap) other;
        if (that.size() != size()) {
            return false;
        }
        try {
            TObjectDoubleIterator iter = iterator();
            while (iter.hasNext()) {
                iter.advance();
                Object key = iter.key();
                double value = iter.value();
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

    @Override // gnu.trove.map.TObjectDoubleMap
    public int hashCode() {
        int hashcode = 0;
        Object[] keys = this._set;
        double[] values = this._values;
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

    /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TObjectDoubleHashMap$KeyView.class */
    protected class KeyView extends TObjectDoubleHashMap<K>.MapBackedView<K> {
        protected KeyView() {
            super();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public Iterator<K> iterator() {
            return new TObjectHashIterator(TObjectDoubleHashMap.this);
        }

        @Override // gnu.trove.map.hash.TObjectDoubleHashMap.MapBackedView
        public boolean removeElement(K key) {
            return TObjectDoubleHashMap.this.no_entry_value != TObjectDoubleHashMap.this.remove(key);
        }

        @Override // gnu.trove.map.hash.TObjectDoubleHashMap.MapBackedView
        public boolean containsElement(K key) {
            return TObjectDoubleHashMap.this.contains(key);
        }
    }

    /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TObjectDoubleHashMap$MapBackedView.class */
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
            TObjectDoubleHashMap.this.clear();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean add(E obj) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return TObjectDoubleHashMap.this.size();
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
            return TObjectDoubleHashMap.this.isEmpty();
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
    /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TObjectDoubleHashMap$TDoubleValueCollection.class */
    public class TDoubleValueCollection implements TDoubleCollection {
        TDoubleValueCollection() {
        }

        @Override // gnu.trove.TDoubleCollection
        public TDoubleIterator iterator() {
            return new TObjectDoubleValueHashIterator();
        }

        @Override // gnu.trove.TDoubleCollection
        public double getNoEntryValue() {
            return TObjectDoubleHashMap.this.no_entry_value;
        }

        @Override // gnu.trove.TDoubleCollection
        public int size() {
            return TObjectDoubleHashMap.this._size;
        }

        @Override // gnu.trove.TDoubleCollection
        public boolean isEmpty() {
            return 0 == TObjectDoubleHashMap.this._size;
        }

        @Override // gnu.trove.TDoubleCollection
        public boolean contains(double entry) {
            return TObjectDoubleHashMap.this.containsValue(entry);
        }

        @Override // gnu.trove.TDoubleCollection
        public double[] toArray() {
            return TObjectDoubleHashMap.this.values();
        }

        @Override // gnu.trove.TDoubleCollection
        public double[] toArray(double[] dest) {
            return TObjectDoubleHashMap.this.values(dest);
        }

        @Override // gnu.trove.TDoubleCollection
        public boolean add(double entry) {
            throw new UnsupportedOperationException();
        }

        @Override // gnu.trove.TDoubleCollection
        public boolean remove(double entry) {
            double[] values = TObjectDoubleHashMap.this._values;
            Object[] set = TObjectDoubleHashMap.this._set;
            int i = values.length;
            while (true) {
                int i2 = i;
                i--;
                if (i2 > 0) {
                    if (set[i] != TObjectHash.FREE && set[i] != TObjectHash.REMOVED && entry == values[i]) {
                        TObjectDoubleHashMap.this.removeAt(i);
                        return true;
                    }
                } else {
                    return false;
                }
            }
        }

        @Override // gnu.trove.TDoubleCollection
        public boolean containsAll(Collection<?> collection) {
            for (Object element : collection) {
                if (element instanceof Double) {
                    double ele = ((Double) element).doubleValue();
                    if (!TObjectDoubleHashMap.this.containsValue(ele)) {
                        return false;
                    }
                } else {
                    return false;
                }
            }
            return true;
        }

        @Override // gnu.trove.TDoubleCollection
        public boolean containsAll(TDoubleCollection collection) {
            TDoubleIterator iter = collection.iterator();
            while (iter.hasNext()) {
                if (!TObjectDoubleHashMap.this.containsValue(iter.next())) {
                    return false;
                }
            }
            return true;
        }

        @Override // gnu.trove.TDoubleCollection
        public boolean containsAll(double[] array) {
            for (double element : array) {
                if (!TObjectDoubleHashMap.this.containsValue(element)) {
                    return false;
                }
            }
            return true;
        }

        @Override // gnu.trove.TDoubleCollection
        public boolean addAll(Collection<? extends Double> collection) {
            throw new UnsupportedOperationException();
        }

        @Override // gnu.trove.TDoubleCollection
        public boolean addAll(TDoubleCollection collection) {
            throw new UnsupportedOperationException();
        }

        @Override // gnu.trove.TDoubleCollection
        public boolean addAll(double[] array) {
            throw new UnsupportedOperationException();
        }

        @Override // gnu.trove.TDoubleCollection
        public boolean retainAll(Collection<?> collection) {
            boolean modified = false;
            TDoubleIterator iter = iterator();
            while (iter.hasNext()) {
                if (!collection.contains(Double.valueOf(iter.next()))) {
                    iter.remove();
                    modified = true;
                }
            }
            return modified;
        }

        @Override // gnu.trove.TDoubleCollection
        public boolean retainAll(TDoubleCollection collection) {
            if (this == collection) {
                return false;
            }
            boolean modified = false;
            TDoubleIterator iter = iterator();
            while (iter.hasNext()) {
                if (!collection.contains(iter.next())) {
                    iter.remove();
                    modified = true;
                }
            }
            return modified;
        }

        @Override // gnu.trove.TDoubleCollection
        public boolean retainAll(double[] array) {
            boolean changed = false;
            Arrays.sort(array);
            double[] values = TObjectDoubleHashMap.this._values;
            Object[] set = TObjectDoubleHashMap.this._set;
            int i = set.length;
            while (true) {
                int i2 = i;
                i--;
                if (i2 > 0) {
                    if (set[i] != TObjectHash.FREE && set[i] != TObjectHash.REMOVED && Arrays.binarySearch(array, values[i]) < 0) {
                        TObjectDoubleHashMap.this.removeAt(i);
                        changed = true;
                    }
                } else {
                    return changed;
                }
            }
        }

        @Override // gnu.trove.TDoubleCollection
        public boolean removeAll(Collection<?> collection) {
            boolean changed = false;
            for (Object element : collection) {
                if (element instanceof Double) {
                    double c = ((Double) element).doubleValue();
                    if (remove(c)) {
                        changed = true;
                    }
                }
            }
            return changed;
        }

        @Override // gnu.trove.TDoubleCollection
        public boolean removeAll(TDoubleCollection collection) {
            if (this == collection) {
                clear();
                return true;
            }
            boolean changed = false;
            TDoubleIterator iter = collection.iterator();
            while (iter.hasNext()) {
                double element = iter.next();
                if (remove(element)) {
                    changed = true;
                }
            }
            return changed;
        }

        @Override // gnu.trove.TDoubleCollection
        public boolean removeAll(double[] array) {
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

        @Override // gnu.trove.TDoubleCollection
        public void clear() {
            TObjectDoubleHashMap.this.clear();
        }

        @Override // gnu.trove.TDoubleCollection
        public boolean forEach(TDoubleProcedure procedure) {
            return TObjectDoubleHashMap.this.forEachValue(procedure);
        }

        public String toString() {
            final StringBuilder buf = new StringBuilder("{");
            TObjectDoubleHashMap.this.forEachValue(new TDoubleProcedure() { // from class: gnu.trove.map.hash.TObjectDoubleHashMap.TDoubleValueCollection.1
                private boolean first = true;

                @Override // gnu.trove.procedure.TDoubleProcedure
                public boolean execute(double value) {
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
        /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TObjectDoubleHashMap$TDoubleValueCollection$TObjectDoubleValueHashIterator.class */
        public class TObjectDoubleValueHashIterator implements TDoubleIterator {
            protected THash _hash;
            protected int _expectedSize;
            protected int _index;

            TObjectDoubleValueHashIterator() {
                this._hash = TObjectDoubleHashMap.this;
                this._expectedSize = this._hash.size();
                this._index = this._hash.capacity();
            }

            @Override // gnu.trove.iterator.TIterator, java.util.Iterator
            public boolean hasNext() {
                return nextIndex() >= 0;
            }

            @Override // gnu.trove.iterator.TDoubleIterator
            public double next() {
                moveToNextIndex();
                return TObjectDoubleHashMap.this._values[this._index];
            }

            @Override // gnu.trove.iterator.TIterator, java.util.Iterator
            public void remove() {
                if (this._expectedSize != this._hash.size()) {
                    throw new ConcurrentModificationException();
                }
                try {
                    this._hash.tempDisableAutoCompaction();
                    TObjectDoubleHashMap.this.removeAt(this._index);
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
                Object[] set = TObjectDoubleHashMap.this._set;
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
    /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TObjectDoubleHashMap$TObjectDoubleHashIterator.class */
    public class TObjectDoubleHashIterator<K> extends TObjectHashIterator<K> implements TObjectDoubleIterator<K> {
        private final TObjectDoubleHashMap<K> _map;

        public TObjectDoubleHashIterator(TObjectDoubleHashMap<K> map) {
            super(map);
            this._map = map;
        }

        @Override // gnu.trove.iterator.TAdvancingIterator
        public void advance() {
            moveToNextIndex();
        }

        @Override // gnu.trove.iterator.TObjectDoubleIterator
        public K key() {
            return (K) this._map._set[this._index];
        }

        @Override // gnu.trove.iterator.TObjectDoubleIterator
        public double value() {
            return this._map._values[this._index];
        }

        @Override // gnu.trove.iterator.TObjectDoubleIterator
        public double setValue(double val) {
            double old = value();
            this._map._values[this._index] = val;
            return old;
        }
    }

    @Override // gnu.trove.impl.hash.TObjectHash, gnu.trove.impl.hash.THash, java.io.Externalizable
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeByte(0);
        super.writeExternal(out);
        out.writeDouble(this.no_entry_value);
        out.writeInt(this._size);
        int i = this._set.length;
        while (true) {
            int i2 = i;
            i--;
            if (i2 > 0) {
                if (this._set[i] != REMOVED && this._set[i] != FREE) {
                    out.writeObject(this._set[i]);
                    out.writeDouble(this._values[i]);
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
        this.no_entry_value = in.readDouble();
        int size = in.readInt();
        setUp(size);
        while (true) {
            int i = size;
            size--;
            if (i > 0) {
                Object readObject = in.readObject();
                double val = in.readDouble();
                put(readObject, val);
            } else {
                return;
            }
        }
    }

    public String toString() {
        final StringBuilder buf = new StringBuilder("{");
        forEachEntry(new TObjectDoubleProcedure<K>() { // from class: gnu.trove.map.hash.TObjectDoubleHashMap.2
            private boolean first = true;

            @Override // gnu.trove.procedure.TObjectDoubleProcedure
            public boolean execute(K key, double value) {
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
