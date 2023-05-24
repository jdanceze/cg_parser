package gnu.trove.map.hash;

import com.sun.xml.bind.v2.runtime.reflect.opt.Const;
import gnu.trove.TDoubleCollection;
import gnu.trove.TIntCollection;
import gnu.trove.function.TIntFunction;
import gnu.trove.impl.HashFunctions;
import gnu.trove.impl.hash.TDoubleIntHash;
import gnu.trove.impl.hash.THashPrimitiveIterator;
import gnu.trove.impl.hash.TPrimitiveHash;
import gnu.trove.iterator.TDoubleIntIterator;
import gnu.trove.iterator.TDoubleIterator;
import gnu.trove.iterator.TIntIterator;
import gnu.trove.map.TDoubleIntMap;
import gnu.trove.procedure.TDoubleIntProcedure;
import gnu.trove.procedure.TDoubleProcedure;
import gnu.trove.procedure.TIntProcedure;
import gnu.trove.set.TDoubleSet;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Arrays;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TDoubleIntHashMap.class */
public class TDoubleIntHashMap extends TDoubleIntHash implements TDoubleIntMap, Externalizable {
    static final long serialVersionUID = 1;
    protected transient int[] _values;

    public TDoubleIntHashMap() {
    }

    public TDoubleIntHashMap(int initialCapacity) {
        super(initialCapacity);
    }

    public TDoubleIntHashMap(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    public TDoubleIntHashMap(int initialCapacity, float loadFactor, double noEntryKey, int noEntryValue) {
        super(initialCapacity, loadFactor, noEntryKey, noEntryValue);
    }

    public TDoubleIntHashMap(double[] keys, int[] values) {
        super(Math.max(keys.length, values.length));
        int size = Math.min(keys.length, values.length);
        for (int i = 0; i < size; i++) {
            put(keys[i], values[i]);
        }
    }

    public TDoubleIntHashMap(TDoubleIntMap map) {
        super(map.size());
        if (map instanceof TDoubleIntHashMap) {
            TDoubleIntHashMap hashmap = (TDoubleIntHashMap) map;
            this._loadFactor = hashmap._loadFactor;
            this.no_entry_key = hashmap.no_entry_key;
            this.no_entry_value = hashmap.no_entry_value;
            if (this.no_entry_key != Const.default_value_double) {
                Arrays.fill(this._set, this.no_entry_key);
            }
            if (this.no_entry_value != 0) {
                Arrays.fill(this._values, this.no_entry_value);
            }
            setUp((int) Math.ceil(10.0f / this._loadFactor));
        }
        putAll(map);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // gnu.trove.impl.hash.TDoubleIntHash, gnu.trove.impl.hash.TPrimitiveHash, gnu.trove.impl.hash.THash
    public int setUp(int initialCapacity) {
        int capacity = super.setUp(initialCapacity);
        this._values = new int[capacity];
        return capacity;
    }

    @Override // gnu.trove.impl.hash.THash
    protected void rehash(int newCapacity) {
        int oldCapacity = this._set.length;
        double[] oldKeys = this._set;
        int[] oldVals = this._values;
        byte[] oldStates = this._states;
        this._set = new double[newCapacity];
        this._values = new int[newCapacity];
        this._states = new byte[newCapacity];
        int i = oldCapacity;
        while (true) {
            int i2 = i;
            i--;
            if (i2 > 0) {
                if (oldStates[i] == 1) {
                    double o = oldKeys[i];
                    int index = insertKey(o);
                    this._values[index] = oldVals[i];
                }
            } else {
                return;
            }
        }
    }

    @Override // gnu.trove.map.TDoubleIntMap
    public int put(double key, int value) {
        int index = insertKey(key);
        return doPut(key, value, index);
    }

    @Override // gnu.trove.map.TDoubleIntMap
    public int putIfAbsent(double key, int value) {
        int index = insertKey(key);
        if (index < 0) {
            return this._values[(-index) - 1];
        }
        return doPut(key, value, index);
    }

    private int doPut(double key, int value, int index) {
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

    @Override // gnu.trove.map.TDoubleIntMap
    public void putAll(Map<? extends Double, ? extends Integer> map) {
        ensureCapacity(map.size());
        for (Map.Entry<? extends Double, ? extends Integer> entry : map.entrySet()) {
            put(entry.getKey().doubleValue(), entry.getValue().intValue());
        }
    }

    @Override // gnu.trove.map.TDoubleIntMap
    public void putAll(TDoubleIntMap map) {
        ensureCapacity(map.size());
        TDoubleIntIterator iter = map.iterator();
        while (iter.hasNext()) {
            iter.advance();
            put(iter.key(), iter.value());
        }
    }

    @Override // gnu.trove.map.TDoubleIntMap
    public int get(double key) {
        int index = index(key);
        return index < 0 ? this.no_entry_value : this._values[index];
    }

    @Override // gnu.trove.impl.hash.THash, gnu.trove.map.TObjectByteMap
    public void clear() {
        super.clear();
        Arrays.fill(this._set, 0, this._set.length, this.no_entry_key);
        Arrays.fill(this._values, 0, this._values.length, this.no_entry_value);
        Arrays.fill(this._states, 0, this._states.length, (byte) 0);
    }

    @Override // gnu.trove.impl.hash.THash, gnu.trove.map.TByteByteMap
    public boolean isEmpty() {
        return 0 == this._size;
    }

    @Override // gnu.trove.map.TDoubleIntMap
    public int remove(double key) {
        int prev = this.no_entry_value;
        int index = index(key);
        if (index >= 0) {
            prev = this._values[index];
            removeAt(index);
        }
        return prev;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // gnu.trove.impl.hash.TDoubleIntHash, gnu.trove.impl.hash.TPrimitiveHash, gnu.trove.impl.hash.THash
    public void removeAt(int index) {
        this._values[index] = this.no_entry_value;
        super.removeAt(index);
    }

    @Override // gnu.trove.map.TDoubleIntMap
    public TDoubleSet keySet() {
        return new TKeyView();
    }

    @Override // gnu.trove.map.TDoubleIntMap
    public double[] keys() {
        double[] keys = new double[size()];
        double[] k = this._set;
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

    @Override // gnu.trove.map.TDoubleIntMap
    public double[] keys(double[] array) {
        int size = size();
        if (array.length < size) {
            array = new double[size];
        }
        double[] keys = this._set;
        byte[] states = this._states;
        int i = keys.length;
        int j = 0;
        while (true) {
            int i2 = i;
            i--;
            if (i2 > 0) {
                if (states[i] == 1) {
                    int i3 = j;
                    j++;
                    array[i3] = keys[i];
                }
            } else {
                return array;
            }
        }
    }

    @Override // gnu.trove.map.TDoubleIntMap
    public TIntCollection valueCollection() {
        return new TValueView();
    }

    @Override // gnu.trove.map.TDoubleIntMap
    public int[] values() {
        int[] vals = new int[size()];
        int[] v = this._values;
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

    @Override // gnu.trove.map.TDoubleIntMap
    public int[] values(int[] array) {
        int size = size();
        if (array.length < size) {
            array = new int[size];
        }
        int[] v = this._values;
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
                    array[i3] = v[i];
                }
            } else {
                return array;
            }
        }
    }

    @Override // gnu.trove.map.TDoubleIntMap
    public boolean containsValue(int val) {
        byte[] states = this._states;
        int[] vals = this._values;
        int i = vals.length;
        while (true) {
            int i2 = i;
            i--;
            if (i2 > 0) {
                if (states[i] == 1 && val == vals[i]) {
                    return true;
                }
            } else {
                return false;
            }
        }
    }

    @Override // gnu.trove.map.TDoubleIntMap
    public boolean containsKey(double key) {
        return contains(key);
    }

    @Override // gnu.trove.map.TDoubleIntMap
    public TDoubleIntIterator iterator() {
        return new TDoubleIntHashIterator(this);
    }

    @Override // gnu.trove.map.TDoubleIntMap
    public boolean forEachKey(TDoubleProcedure procedure) {
        return forEach(procedure);
    }

    @Override // gnu.trove.map.TDoubleIntMap
    public boolean forEachValue(TIntProcedure procedure) {
        byte[] states = this._states;
        int[] values = this._values;
        int i = values.length;
        while (true) {
            int i2 = i;
            i--;
            if (i2 > 0) {
                if (states[i] == 1 && !procedure.execute(values[i])) {
                    return false;
                }
            } else {
                return true;
            }
        }
    }

    @Override // gnu.trove.map.TDoubleIntMap
    public boolean forEachEntry(TDoubleIntProcedure procedure) {
        byte[] states = this._states;
        double[] keys = this._set;
        int[] values = this._values;
        int i = keys.length;
        while (true) {
            int i2 = i;
            i--;
            if (i2 > 0) {
                if (states[i] == 1 && !procedure.execute(keys[i], values[i])) {
                    return false;
                }
            } else {
                return true;
            }
        }
    }

    @Override // gnu.trove.map.TDoubleIntMap
    public void transformValues(TIntFunction function) {
        byte[] states = this._states;
        int[] values = this._values;
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

    @Override // gnu.trove.map.TDoubleIntMap
    public boolean retainEntries(TDoubleIntProcedure procedure) {
        boolean modified = false;
        byte[] states = this._states;
        double[] keys = this._set;
        int[] values = this._values;
        tempDisableAutoCompaction();
        try {
            int i = keys.length;
            while (true) {
                int i2 = i;
                i--;
                if (i2 > 0) {
                    if (states[i] == 1 && !procedure.execute(keys[i], values[i])) {
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

    @Override // gnu.trove.map.TDoubleIntMap
    public boolean increment(double key) {
        return adjustValue(key, 1);
    }

    @Override // gnu.trove.map.TDoubleIntMap
    public boolean adjustValue(double key, int amount) {
        int index = index(key);
        if (index < 0) {
            return false;
        }
        int[] iArr = this._values;
        iArr[index] = iArr[index] + amount;
        return true;
    }

    @Override // gnu.trove.map.TDoubleIntMap
    public int adjustOrPutValue(double key, int adjust_amount, int put_amount) {
        int newValue;
        boolean isNewMapping;
        int index = insertKey(key);
        if (index < 0) {
            index = (-index) - 1;
            int[] iArr = this._values;
            int i = iArr[index] + adjust_amount;
            iArr[index] = i;
            newValue = i;
            isNewMapping = false;
        } else {
            this._values[index] = put_amount;
            newValue = put_amount;
            isNewMapping = true;
        }
        byte b = this._states[index];
        if (isNewMapping) {
            postInsertHook(this.consumeFreeSlot);
        }
        return newValue;
    }

    /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TDoubleIntHashMap$TKeyView.class */
    protected class TKeyView implements TDoubleSet {
        protected TKeyView() {
        }

        @Override // gnu.trove.set.TDoubleSet, gnu.trove.TDoubleCollection
        public TDoubleIterator iterator() {
            return new TDoubleIntKeyHashIterator(TDoubleIntHashMap.this);
        }

        @Override // gnu.trove.set.TDoubleSet, gnu.trove.TDoubleCollection
        public double getNoEntryValue() {
            return TDoubleIntHashMap.this.no_entry_key;
        }

        @Override // gnu.trove.set.TDoubleSet, gnu.trove.TDoubleCollection
        public int size() {
            return TDoubleIntHashMap.this._size;
        }

        @Override // gnu.trove.set.TDoubleSet, gnu.trove.TDoubleCollection
        public boolean isEmpty() {
            return 0 == TDoubleIntHashMap.this._size;
        }

        @Override // gnu.trove.set.TDoubleSet, gnu.trove.TDoubleCollection
        public boolean contains(double entry) {
            return TDoubleIntHashMap.this.contains(entry);
        }

        @Override // gnu.trove.set.TDoubleSet, gnu.trove.TDoubleCollection
        public double[] toArray() {
            return TDoubleIntHashMap.this.keys();
        }

        @Override // gnu.trove.set.TDoubleSet, gnu.trove.TDoubleCollection
        public double[] toArray(double[] dest) {
            return TDoubleIntHashMap.this.keys(dest);
        }

        @Override // gnu.trove.set.TDoubleSet, gnu.trove.TDoubleCollection
        public boolean add(double entry) {
            throw new UnsupportedOperationException();
        }

        @Override // gnu.trove.set.TDoubleSet, gnu.trove.TDoubleCollection
        public boolean remove(double entry) {
            return TDoubleIntHashMap.this.no_entry_value != TDoubleIntHashMap.this.remove(entry);
        }

        @Override // gnu.trove.set.TDoubleSet, gnu.trove.TDoubleCollection
        public boolean containsAll(Collection<?> collection) {
            for (Object element : collection) {
                if (element instanceof Double) {
                    double ele = ((Double) element).doubleValue();
                    if (!TDoubleIntHashMap.this.containsKey(ele)) {
                        return false;
                    }
                } else {
                    return false;
                }
            }
            return true;
        }

        @Override // gnu.trove.set.TDoubleSet, gnu.trove.TDoubleCollection
        public boolean containsAll(TDoubleCollection collection) {
            TDoubleIterator iter = collection.iterator();
            while (iter.hasNext()) {
                if (!TDoubleIntHashMap.this.containsKey(iter.next())) {
                    return false;
                }
            }
            return true;
        }

        @Override // gnu.trove.set.TDoubleSet, gnu.trove.TDoubleCollection
        public boolean containsAll(double[] array) {
            for (double element : array) {
                if (!TDoubleIntHashMap.this.contains(element)) {
                    return false;
                }
            }
            return true;
        }

        @Override // gnu.trove.set.TDoubleSet, gnu.trove.TDoubleCollection
        public boolean addAll(Collection<? extends Double> collection) {
            throw new UnsupportedOperationException();
        }

        @Override // gnu.trove.set.TDoubleSet, gnu.trove.TDoubleCollection
        public boolean addAll(TDoubleCollection collection) {
            throw new UnsupportedOperationException();
        }

        @Override // gnu.trove.set.TDoubleSet, gnu.trove.TDoubleCollection
        public boolean addAll(double[] array) {
            throw new UnsupportedOperationException();
        }

        @Override // gnu.trove.set.TDoubleSet, gnu.trove.TDoubleCollection
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

        @Override // gnu.trove.set.TDoubleSet, gnu.trove.TDoubleCollection
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

        @Override // gnu.trove.set.TDoubleSet, gnu.trove.TDoubleCollection
        public boolean retainAll(double[] array) {
            boolean changed = false;
            Arrays.sort(array);
            double[] set = TDoubleIntHashMap.this._set;
            byte[] states = TDoubleIntHashMap.this._states;
            int i = set.length;
            while (true) {
                int i2 = i;
                i--;
                if (i2 > 0) {
                    if (states[i] == 1 && Arrays.binarySearch(array, set[i]) < 0) {
                        TDoubleIntHashMap.this.removeAt(i);
                        changed = true;
                    }
                } else {
                    return changed;
                }
            }
        }

        @Override // gnu.trove.set.TDoubleSet, gnu.trove.TDoubleCollection
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

        @Override // gnu.trove.set.TDoubleSet, gnu.trove.TDoubleCollection
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

        @Override // gnu.trove.set.TDoubleSet, gnu.trove.TDoubleCollection
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

        @Override // gnu.trove.set.TDoubleSet, gnu.trove.TDoubleCollection
        public void clear() {
            TDoubleIntHashMap.this.clear();
        }

        @Override // gnu.trove.set.TDoubleSet, gnu.trove.TDoubleCollection
        public boolean forEach(TDoubleProcedure procedure) {
            return TDoubleIntHashMap.this.forEachKey(procedure);
        }

        @Override // gnu.trove.set.TDoubleSet, gnu.trove.TDoubleCollection
        public boolean equals(Object other) {
            if (!(other instanceof TDoubleSet)) {
                return false;
            }
            TDoubleSet that = (TDoubleSet) other;
            if (that.size() != size()) {
                return false;
            }
            int i = TDoubleIntHashMap.this._states.length;
            while (true) {
                int i2 = i;
                i--;
                if (i2 > 0) {
                    if (TDoubleIntHashMap.this._states[i] == 1 && !that.contains(TDoubleIntHashMap.this._set[i])) {
                        return false;
                    }
                } else {
                    return true;
                }
            }
        }

        @Override // gnu.trove.set.TDoubleSet, gnu.trove.TDoubleCollection
        public int hashCode() {
            int hashcode = 0;
            int i = TDoubleIntHashMap.this._states.length;
            while (true) {
                int i2 = i;
                i--;
                if (i2 > 0) {
                    if (TDoubleIntHashMap.this._states[i] == 1) {
                        hashcode += HashFunctions.hash(TDoubleIntHashMap.this._set[i]);
                    }
                } else {
                    return hashcode;
                }
            }
        }

        public String toString() {
            final StringBuilder buf = new StringBuilder("{");
            TDoubleIntHashMap.this.forEachKey(new TDoubleProcedure() { // from class: gnu.trove.map.hash.TDoubleIntHashMap.TKeyView.1
                private boolean first = true;

                @Override // gnu.trove.procedure.TDoubleProcedure
                public boolean execute(double key) {
                    if (this.first) {
                        this.first = false;
                    } else {
                        buf.append(", ");
                    }
                    buf.append(key);
                    return true;
                }
            });
            buf.append("}");
            return buf.toString();
        }
    }

    /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TDoubleIntHashMap$TValueView.class */
    protected class TValueView implements TIntCollection {
        protected TValueView() {
        }

        @Override // gnu.trove.TIntCollection
        public TIntIterator iterator() {
            return new TDoubleIntValueHashIterator(TDoubleIntHashMap.this);
        }

        @Override // gnu.trove.TIntCollection
        public int getNoEntryValue() {
            return TDoubleIntHashMap.this.no_entry_value;
        }

        @Override // gnu.trove.TIntCollection
        public int size() {
            return TDoubleIntHashMap.this._size;
        }

        @Override // gnu.trove.TIntCollection
        public boolean isEmpty() {
            return 0 == TDoubleIntHashMap.this._size;
        }

        @Override // gnu.trove.TIntCollection
        public boolean contains(int entry) {
            return TDoubleIntHashMap.this.containsValue(entry);
        }

        @Override // gnu.trove.TIntCollection
        public int[] toArray() {
            return TDoubleIntHashMap.this.values();
        }

        @Override // gnu.trove.TIntCollection
        public int[] toArray(int[] dest) {
            return TDoubleIntHashMap.this.values(dest);
        }

        @Override // gnu.trove.TIntCollection
        public boolean add(int entry) {
            throw new UnsupportedOperationException();
        }

        @Override // gnu.trove.TIntCollection
        public boolean remove(int entry) {
            int[] values = TDoubleIntHashMap.this._values;
            double[] set = TDoubleIntHashMap.this._set;
            int i = values.length;
            while (true) {
                int i2 = i;
                i--;
                if (i2 > 0) {
                    if (set[i] != Const.default_value_double && set[i] != 2.0d && entry == values[i]) {
                        TDoubleIntHashMap.this.removeAt(i);
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
                    if (!TDoubleIntHashMap.this.containsValue(ele)) {
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
                if (!TDoubleIntHashMap.this.containsValue(iter.next())) {
                    return false;
                }
            }
            return true;
        }

        @Override // gnu.trove.TIntCollection
        public boolean containsAll(int[] array) {
            for (int element : array) {
                if (!TDoubleIntHashMap.this.containsValue(element)) {
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
            int[] values = TDoubleIntHashMap.this._values;
            byte[] states = TDoubleIntHashMap.this._states;
            int i = values.length;
            while (true) {
                int i2 = i;
                i--;
                if (i2 > 0) {
                    if (states[i] == 1 && Arrays.binarySearch(array, values[i]) < 0) {
                        TDoubleIntHashMap.this.removeAt(i);
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
            TDoubleIntHashMap.this.clear();
        }

        @Override // gnu.trove.TIntCollection
        public boolean forEach(TIntProcedure procedure) {
            return TDoubleIntHashMap.this.forEachValue(procedure);
        }

        public String toString() {
            final StringBuilder buf = new StringBuilder("{");
            TDoubleIntHashMap.this.forEachValue(new TIntProcedure() { // from class: gnu.trove.map.hash.TDoubleIntHashMap.TValueView.1
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
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TDoubleIntHashMap$TDoubleIntKeyHashIterator.class */
    public class TDoubleIntKeyHashIterator extends THashPrimitiveIterator implements TDoubleIterator {
        TDoubleIntKeyHashIterator(TPrimitiveHash hash) {
            super(hash);
        }

        @Override // gnu.trove.iterator.TDoubleIterator
        public double next() {
            moveToNextIndex();
            return TDoubleIntHashMap.this._set[this._index];
        }

        @Override // gnu.trove.impl.hash.THashPrimitiveIterator, gnu.trove.iterator.TPrimitiveIterator, gnu.trove.iterator.TIterator, java.util.Iterator
        public void remove() {
            if (this._expectedSize != this._hash.size()) {
                throw new ConcurrentModificationException();
            }
            try {
                this._hash.tempDisableAutoCompaction();
                TDoubleIntHashMap.this.removeAt(this._index);
                this._hash.reenableAutoCompaction(false);
                this._expectedSize--;
            } catch (Throwable th) {
                this._hash.reenableAutoCompaction(false);
                throw th;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TDoubleIntHashMap$TDoubleIntValueHashIterator.class */
    public class TDoubleIntValueHashIterator extends THashPrimitiveIterator implements TIntIterator {
        TDoubleIntValueHashIterator(TPrimitiveHash hash) {
            super(hash);
        }

        @Override // gnu.trove.iterator.TIntIterator
        public int next() {
            moveToNextIndex();
            return TDoubleIntHashMap.this._values[this._index];
        }

        @Override // gnu.trove.impl.hash.THashPrimitiveIterator, gnu.trove.iterator.TPrimitiveIterator, gnu.trove.iterator.TIterator, java.util.Iterator
        public void remove() {
            if (this._expectedSize != this._hash.size()) {
                throw new ConcurrentModificationException();
            }
            try {
                this._hash.tempDisableAutoCompaction();
                TDoubleIntHashMap.this.removeAt(this._index);
                this._hash.reenableAutoCompaction(false);
                this._expectedSize--;
            } catch (Throwable th) {
                this._hash.reenableAutoCompaction(false);
                throw th;
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TDoubleIntHashMap$TDoubleIntHashIterator.class */
    class TDoubleIntHashIterator extends THashPrimitiveIterator implements TDoubleIntIterator {
        TDoubleIntHashIterator(TDoubleIntHashMap map) {
            super(map);
        }

        @Override // gnu.trove.iterator.TAdvancingIterator
        public void advance() {
            moveToNextIndex();
        }

        @Override // gnu.trove.iterator.TDoubleIntIterator
        public double key() {
            return TDoubleIntHashMap.this._set[this._index];
        }

        @Override // gnu.trove.iterator.TDoubleIntIterator
        public int value() {
            return TDoubleIntHashMap.this._values[this._index];
        }

        @Override // gnu.trove.iterator.TDoubleIntIterator
        public int setValue(int val) {
            int old = value();
            TDoubleIntHashMap.this._values[this._index] = val;
            return old;
        }

        @Override // gnu.trove.impl.hash.THashPrimitiveIterator, gnu.trove.iterator.TPrimitiveIterator, gnu.trove.iterator.TIterator, java.util.Iterator
        public void remove() {
            if (this._expectedSize != this._hash.size()) {
                throw new ConcurrentModificationException();
            }
            try {
                this._hash.tempDisableAutoCompaction();
                TDoubleIntHashMap.this.removeAt(this._index);
                this._hash.reenableAutoCompaction(false);
                this._expectedSize--;
            } catch (Throwable th) {
                this._hash.reenableAutoCompaction(false);
                throw th;
            }
        }
    }

    public boolean equals(Object other) {
        if (!(other instanceof TDoubleIntMap)) {
            return false;
        }
        TDoubleIntMap that = (TDoubleIntMap) other;
        if (that.size() != size()) {
            return false;
        }
        int[] values = this._values;
        byte[] states = this._states;
        int this_no_entry_value = getNoEntryValue();
        int that_no_entry_value = that.getNoEntryValue();
        int i = values.length;
        while (true) {
            int i2 = i;
            i--;
            if (i2 > 0) {
                if (states[i] == 1) {
                    double key = this._set[i];
                    int that_value = that.get(key);
                    int this_value = values[i];
                    if (this_value != that_value && this_value != this_no_entry_value && that_value != that_no_entry_value) {
                        return false;
                    }
                }
            } else {
                return true;
            }
        }
    }

    public int hashCode() {
        int hashcode = 0;
        byte[] states = this._states;
        int i = this._values.length;
        while (true) {
            int i2 = i;
            i--;
            if (i2 > 0) {
                if (states[i] == 1) {
                    hashcode += HashFunctions.hash(this._set[i]) ^ HashFunctions.hash(this._values[i]);
                }
            } else {
                return hashcode;
            }
        }
    }

    public String toString() {
        final StringBuilder buf = new StringBuilder("{");
        forEachEntry(new TDoubleIntProcedure() { // from class: gnu.trove.map.hash.TDoubleIntHashMap.1
            private boolean first = true;

            @Override // gnu.trove.procedure.TDoubleIntProcedure
            public boolean execute(double key, int value) {
                if (this.first) {
                    this.first = false;
                } else {
                    buf.append(", ");
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

    @Override // gnu.trove.impl.hash.TDoubleIntHash, gnu.trove.impl.hash.THash, java.io.Externalizable
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeByte(0);
        super.writeExternal(out);
        out.writeInt(this._size);
        int i = this._states.length;
        while (true) {
            int i2 = i;
            i--;
            if (i2 > 0) {
                if (this._states[i] == 1) {
                    out.writeDouble(this._set[i]);
                    out.writeInt(this._values[i]);
                }
            } else {
                return;
            }
        }
    }

    @Override // gnu.trove.impl.hash.TDoubleIntHash, gnu.trove.impl.hash.THash, java.io.Externalizable
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        in.readByte();
        super.readExternal(in);
        int size = in.readInt();
        setUp(size);
        while (true) {
            int i = size;
            size--;
            if (i > 0) {
                double key = in.readDouble();
                int val = in.readInt();
                put(key, val);
            } else {
                return;
            }
        }
    }
}
