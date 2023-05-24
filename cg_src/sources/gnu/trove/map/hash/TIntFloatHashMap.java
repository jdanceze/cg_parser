package gnu.trove.map.hash;

import gnu.trove.TFloatCollection;
import gnu.trove.TIntCollection;
import gnu.trove.function.TFloatFunction;
import gnu.trove.impl.HashFunctions;
import gnu.trove.impl.hash.THashPrimitiveIterator;
import gnu.trove.impl.hash.TIntFloatHash;
import gnu.trove.impl.hash.TPrimitiveHash;
import gnu.trove.iterator.TFloatIterator;
import gnu.trove.iterator.TIntFloatIterator;
import gnu.trove.iterator.TIntIterator;
import gnu.trove.map.TIntFloatMap;
import gnu.trove.procedure.TFloatProcedure;
import gnu.trove.procedure.TIntFloatProcedure;
import gnu.trove.procedure.TIntProcedure;
import gnu.trove.set.TIntSet;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Arrays;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TIntFloatHashMap.class */
public class TIntFloatHashMap extends TIntFloatHash implements TIntFloatMap, Externalizable {
    static final long serialVersionUID = 1;
    protected transient float[] _values;

    public TIntFloatHashMap() {
    }

    public TIntFloatHashMap(int initialCapacity) {
        super(initialCapacity);
    }

    public TIntFloatHashMap(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    public TIntFloatHashMap(int initialCapacity, float loadFactor, int noEntryKey, float noEntryValue) {
        super(initialCapacity, loadFactor, noEntryKey, noEntryValue);
    }

    public TIntFloatHashMap(int[] keys, float[] values) {
        super(Math.max(keys.length, values.length));
        int size = Math.min(keys.length, values.length);
        for (int i = 0; i < size; i++) {
            put(keys[i], values[i]);
        }
    }

    public TIntFloatHashMap(TIntFloatMap map) {
        super(map.size());
        if (map instanceof TIntFloatHashMap) {
            TIntFloatHashMap hashmap = (TIntFloatHashMap) map;
            this._loadFactor = hashmap._loadFactor;
            this.no_entry_key = hashmap.no_entry_key;
            this.no_entry_value = hashmap.no_entry_value;
            if (this.no_entry_key != 0) {
                Arrays.fill(this._set, this.no_entry_key);
            }
            if (this.no_entry_value != 0.0f) {
                Arrays.fill(this._values, this.no_entry_value);
            }
            setUp((int) Math.ceil(10.0f / this._loadFactor));
        }
        putAll(map);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // gnu.trove.impl.hash.TIntFloatHash, gnu.trove.impl.hash.TPrimitiveHash, gnu.trove.impl.hash.THash
    public int setUp(int initialCapacity) {
        int capacity = super.setUp(initialCapacity);
        this._values = new float[capacity];
        return capacity;
    }

    @Override // gnu.trove.impl.hash.THash
    protected void rehash(int newCapacity) {
        int oldCapacity = this._set.length;
        int[] oldKeys = this._set;
        float[] oldVals = this._values;
        byte[] oldStates = this._states;
        this._set = new int[newCapacity];
        this._values = new float[newCapacity];
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

    @Override // gnu.trove.map.TIntFloatMap
    public float put(int key, float value) {
        int index = insertKey(key);
        return doPut(key, value, index);
    }

    @Override // gnu.trove.map.TIntFloatMap
    public float putIfAbsent(int key, float value) {
        int index = insertKey(key);
        if (index < 0) {
            return this._values[(-index) - 1];
        }
        return doPut(key, value, index);
    }

    private float doPut(int key, float value, int index) {
        float previous = this.no_entry_value;
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

    @Override // gnu.trove.map.TIntFloatMap
    public void putAll(Map<? extends Integer, ? extends Float> map) {
        ensureCapacity(map.size());
        for (Map.Entry<? extends Integer, ? extends Float> entry : map.entrySet()) {
            put(entry.getKey().intValue(), entry.getValue().floatValue());
        }
    }

    @Override // gnu.trove.map.TIntFloatMap
    public void putAll(TIntFloatMap map) {
        ensureCapacity(map.size());
        TIntFloatIterator iter = map.iterator();
        while (iter.hasNext()) {
            iter.advance();
            put(iter.key(), iter.value());
        }
    }

    @Override // gnu.trove.map.TIntFloatMap
    public float get(int key) {
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

    @Override // gnu.trove.map.TIntFloatMap
    public float remove(int key) {
        float prev = this.no_entry_value;
        int index = index(key);
        if (index >= 0) {
            prev = this._values[index];
            removeAt(index);
        }
        return prev;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // gnu.trove.impl.hash.TIntFloatHash, gnu.trove.impl.hash.TPrimitiveHash, gnu.trove.impl.hash.THash
    public void removeAt(int index) {
        this._values[index] = this.no_entry_value;
        super.removeAt(index);
    }

    @Override // gnu.trove.map.TIntFloatMap
    public TIntSet keySet() {
        return new TKeyView();
    }

    @Override // gnu.trove.map.TIntFloatMap
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

    @Override // gnu.trove.map.TIntFloatMap
    public int[] keys(int[] array) {
        int size = size();
        if (array.length < size) {
            array = new int[size];
        }
        int[] keys = this._set;
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

    @Override // gnu.trove.map.TIntFloatMap
    public TFloatCollection valueCollection() {
        return new TValueView();
    }

    @Override // gnu.trove.map.TIntFloatMap
    public float[] values() {
        float[] vals = new float[size()];
        float[] v = this._values;
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

    @Override // gnu.trove.map.TIntFloatMap
    public float[] values(float[] array) {
        int size = size();
        if (array.length < size) {
            array = new float[size];
        }
        float[] v = this._values;
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

    @Override // gnu.trove.map.TIntFloatMap
    public boolean containsValue(float val) {
        byte[] states = this._states;
        float[] vals = this._values;
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

    @Override // gnu.trove.map.TIntFloatMap
    public boolean containsKey(int key) {
        return contains(key);
    }

    @Override // gnu.trove.map.TIntFloatMap
    public TIntFloatIterator iterator() {
        return new TIntFloatHashIterator(this);
    }

    @Override // gnu.trove.map.TIntFloatMap
    public boolean forEachKey(TIntProcedure procedure) {
        return forEach(procedure);
    }

    @Override // gnu.trove.map.TIntFloatMap
    public boolean forEachValue(TFloatProcedure procedure) {
        byte[] states = this._states;
        float[] values = this._values;
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

    @Override // gnu.trove.map.TIntFloatMap
    public boolean forEachEntry(TIntFloatProcedure procedure) {
        byte[] states = this._states;
        int[] keys = this._set;
        float[] values = this._values;
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

    @Override // gnu.trove.map.TIntFloatMap
    public void transformValues(TFloatFunction function) {
        byte[] states = this._states;
        float[] values = this._values;
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

    @Override // gnu.trove.map.TIntFloatMap
    public boolean retainEntries(TIntFloatProcedure procedure) {
        boolean modified = false;
        byte[] states = this._states;
        int[] keys = this._set;
        float[] values = this._values;
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

    @Override // gnu.trove.map.TIntFloatMap
    public boolean increment(int key) {
        return adjustValue(key, 1.0f);
    }

    @Override // gnu.trove.map.TIntFloatMap
    public boolean adjustValue(int key, float amount) {
        int index = index(key);
        if (index < 0) {
            return false;
        }
        float[] fArr = this._values;
        fArr[index] = fArr[index] + amount;
        return true;
    }

    @Override // gnu.trove.map.TIntFloatMap
    public float adjustOrPutValue(int key, float adjust_amount, float put_amount) {
        float newValue;
        boolean isNewMapping;
        int index = insertKey(key);
        if (index < 0) {
            index = (-index) - 1;
            float[] fArr = this._values;
            float f = fArr[index] + adjust_amount;
            fArr[index] = f;
            newValue = f;
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

    /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TIntFloatHashMap$TKeyView.class */
    protected class TKeyView implements TIntSet {
        protected TKeyView() {
        }

        @Override // gnu.trove.set.TIntSet, gnu.trove.TIntCollection
        public TIntIterator iterator() {
            return new TIntFloatKeyHashIterator(TIntFloatHashMap.this);
        }

        @Override // gnu.trove.set.TIntSet, gnu.trove.TIntCollection
        public int getNoEntryValue() {
            return TIntFloatHashMap.this.no_entry_key;
        }

        @Override // gnu.trove.set.TIntSet, gnu.trove.TIntCollection
        public int size() {
            return TIntFloatHashMap.this._size;
        }

        @Override // gnu.trove.set.TIntSet, gnu.trove.TIntCollection
        public boolean isEmpty() {
            return 0 == TIntFloatHashMap.this._size;
        }

        @Override // gnu.trove.set.TIntSet, gnu.trove.TIntCollection
        public boolean contains(int entry) {
            return TIntFloatHashMap.this.contains(entry);
        }

        @Override // gnu.trove.set.TIntSet, gnu.trove.TIntCollection
        public int[] toArray() {
            return TIntFloatHashMap.this.keys();
        }

        @Override // gnu.trove.set.TIntSet, gnu.trove.TIntCollection
        public int[] toArray(int[] dest) {
            return TIntFloatHashMap.this.keys(dest);
        }

        @Override // gnu.trove.set.TIntSet, gnu.trove.TIntCollection
        public boolean add(int entry) {
            throw new UnsupportedOperationException();
        }

        @Override // gnu.trove.set.TIntSet, gnu.trove.TIntCollection
        public boolean remove(int entry) {
            return TIntFloatHashMap.this.no_entry_value != TIntFloatHashMap.this.remove(entry);
        }

        @Override // gnu.trove.set.TIntSet, gnu.trove.TIntCollection
        public boolean containsAll(Collection<?> collection) {
            for (Object element : collection) {
                if (element instanceof Integer) {
                    int ele = ((Integer) element).intValue();
                    if (!TIntFloatHashMap.this.containsKey(ele)) {
                        return false;
                    }
                } else {
                    return false;
                }
            }
            return true;
        }

        @Override // gnu.trove.set.TIntSet, gnu.trove.TIntCollection
        public boolean containsAll(TIntCollection collection) {
            TIntIterator iter = collection.iterator();
            while (iter.hasNext()) {
                if (!TIntFloatHashMap.this.containsKey(iter.next())) {
                    return false;
                }
            }
            return true;
        }

        @Override // gnu.trove.set.TIntSet, gnu.trove.TIntCollection
        public boolean containsAll(int[] array) {
            for (int element : array) {
                if (!TIntFloatHashMap.this.contains(element)) {
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
            int[] set = TIntFloatHashMap.this._set;
            byte[] states = TIntFloatHashMap.this._states;
            int i = set.length;
            while (true) {
                int i2 = i;
                i--;
                if (i2 > 0) {
                    if (states[i] == 1 && Arrays.binarySearch(array, set[i]) < 0) {
                        TIntFloatHashMap.this.removeAt(i);
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
            TIntFloatHashMap.this.clear();
        }

        @Override // gnu.trove.set.TIntSet, gnu.trove.TIntCollection
        public boolean forEach(TIntProcedure procedure) {
            return TIntFloatHashMap.this.forEachKey(procedure);
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
            int i = TIntFloatHashMap.this._states.length;
            while (true) {
                int i2 = i;
                i--;
                if (i2 > 0) {
                    if (TIntFloatHashMap.this._states[i] == 1 && !that.contains(TIntFloatHashMap.this._set[i])) {
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
            int i = TIntFloatHashMap.this._states.length;
            while (true) {
                int i2 = i;
                i--;
                if (i2 > 0) {
                    if (TIntFloatHashMap.this._states[i] == 1) {
                        hashcode += HashFunctions.hash(TIntFloatHashMap.this._set[i]);
                    }
                } else {
                    return hashcode;
                }
            }
        }

        public String toString() {
            final StringBuilder buf = new StringBuilder("{");
            TIntFloatHashMap.this.forEachKey(new TIntProcedure() { // from class: gnu.trove.map.hash.TIntFloatHashMap.TKeyView.1
                private boolean first = true;

                @Override // gnu.trove.procedure.TIntProcedure
                public boolean execute(int key) {
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

    /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TIntFloatHashMap$TValueView.class */
    protected class TValueView implements TFloatCollection {
        protected TValueView() {
        }

        @Override // gnu.trove.TFloatCollection
        public TFloatIterator iterator() {
            return new TIntFloatValueHashIterator(TIntFloatHashMap.this);
        }

        @Override // gnu.trove.TFloatCollection
        public float getNoEntryValue() {
            return TIntFloatHashMap.this.no_entry_value;
        }

        @Override // gnu.trove.TFloatCollection
        public int size() {
            return TIntFloatHashMap.this._size;
        }

        @Override // gnu.trove.TFloatCollection
        public boolean isEmpty() {
            return 0 == TIntFloatHashMap.this._size;
        }

        @Override // gnu.trove.TFloatCollection
        public boolean contains(float entry) {
            return TIntFloatHashMap.this.containsValue(entry);
        }

        @Override // gnu.trove.TFloatCollection
        public float[] toArray() {
            return TIntFloatHashMap.this.values();
        }

        @Override // gnu.trove.TFloatCollection
        public float[] toArray(float[] dest) {
            return TIntFloatHashMap.this.values(dest);
        }

        @Override // gnu.trove.TFloatCollection
        public boolean add(float entry) {
            throw new UnsupportedOperationException();
        }

        @Override // gnu.trove.TFloatCollection
        public boolean remove(float entry) {
            float[] values = TIntFloatHashMap.this._values;
            int[] set = TIntFloatHashMap.this._set;
            int i = values.length;
            while (true) {
                int i2 = i;
                i--;
                if (i2 > 0) {
                    if (set[i] != 0 && set[i] != 2 && entry == values[i]) {
                        TIntFloatHashMap.this.removeAt(i);
                        return true;
                    }
                } else {
                    return false;
                }
            }
        }

        @Override // gnu.trove.TFloatCollection
        public boolean containsAll(Collection<?> collection) {
            for (Object element : collection) {
                if (element instanceof Float) {
                    float ele = ((Float) element).floatValue();
                    if (!TIntFloatHashMap.this.containsValue(ele)) {
                        return false;
                    }
                } else {
                    return false;
                }
            }
            return true;
        }

        @Override // gnu.trove.TFloatCollection
        public boolean containsAll(TFloatCollection collection) {
            TFloatIterator iter = collection.iterator();
            while (iter.hasNext()) {
                if (!TIntFloatHashMap.this.containsValue(iter.next())) {
                    return false;
                }
            }
            return true;
        }

        @Override // gnu.trove.TFloatCollection
        public boolean containsAll(float[] array) {
            for (float element : array) {
                if (!TIntFloatHashMap.this.containsValue(element)) {
                    return false;
                }
            }
            return true;
        }

        @Override // gnu.trove.TFloatCollection
        public boolean addAll(Collection<? extends Float> collection) {
            throw new UnsupportedOperationException();
        }

        @Override // gnu.trove.TFloatCollection
        public boolean addAll(TFloatCollection collection) {
            throw new UnsupportedOperationException();
        }

        @Override // gnu.trove.TFloatCollection
        public boolean addAll(float[] array) {
            throw new UnsupportedOperationException();
        }

        @Override // gnu.trove.TFloatCollection
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

        @Override // gnu.trove.TFloatCollection
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

        @Override // gnu.trove.TFloatCollection
        public boolean retainAll(float[] array) {
            boolean changed = false;
            Arrays.sort(array);
            float[] values = TIntFloatHashMap.this._values;
            byte[] states = TIntFloatHashMap.this._states;
            int i = values.length;
            while (true) {
                int i2 = i;
                i--;
                if (i2 > 0) {
                    if (states[i] == 1 && Arrays.binarySearch(array, values[i]) < 0) {
                        TIntFloatHashMap.this.removeAt(i);
                        changed = true;
                    }
                } else {
                    return changed;
                }
            }
        }

        @Override // gnu.trove.TFloatCollection
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

        @Override // gnu.trove.TFloatCollection
        public boolean removeAll(TFloatCollection collection) {
            if (this == collection) {
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

        @Override // gnu.trove.TFloatCollection
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

        @Override // gnu.trove.TFloatCollection
        public void clear() {
            TIntFloatHashMap.this.clear();
        }

        @Override // gnu.trove.TFloatCollection
        public boolean forEach(TFloatProcedure procedure) {
            return TIntFloatHashMap.this.forEachValue(procedure);
        }

        public String toString() {
            final StringBuilder buf = new StringBuilder("{");
            TIntFloatHashMap.this.forEachValue(new TFloatProcedure() { // from class: gnu.trove.map.hash.TIntFloatHashMap.TValueView.1
                private boolean first = true;

                @Override // gnu.trove.procedure.TFloatProcedure
                public boolean execute(float value) {
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
    /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TIntFloatHashMap$TIntFloatKeyHashIterator.class */
    public class TIntFloatKeyHashIterator extends THashPrimitiveIterator implements TIntIterator {
        TIntFloatKeyHashIterator(TPrimitiveHash hash) {
            super(hash);
        }

        @Override // gnu.trove.iterator.TIntIterator
        public int next() {
            moveToNextIndex();
            return TIntFloatHashMap.this._set[this._index];
        }

        @Override // gnu.trove.impl.hash.THashPrimitiveIterator, gnu.trove.iterator.TPrimitiveIterator, gnu.trove.iterator.TIterator, java.util.Iterator
        public void remove() {
            if (this._expectedSize != this._hash.size()) {
                throw new ConcurrentModificationException();
            }
            try {
                this._hash.tempDisableAutoCompaction();
                TIntFloatHashMap.this.removeAt(this._index);
                this._hash.reenableAutoCompaction(false);
                this._expectedSize--;
            } catch (Throwable th) {
                this._hash.reenableAutoCompaction(false);
                throw th;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TIntFloatHashMap$TIntFloatValueHashIterator.class */
    public class TIntFloatValueHashIterator extends THashPrimitiveIterator implements TFloatIterator {
        TIntFloatValueHashIterator(TPrimitiveHash hash) {
            super(hash);
        }

        @Override // gnu.trove.iterator.TFloatIterator
        public float next() {
            moveToNextIndex();
            return TIntFloatHashMap.this._values[this._index];
        }

        @Override // gnu.trove.impl.hash.THashPrimitiveIterator, gnu.trove.iterator.TPrimitiveIterator, gnu.trove.iterator.TIterator, java.util.Iterator
        public void remove() {
            if (this._expectedSize != this._hash.size()) {
                throw new ConcurrentModificationException();
            }
            try {
                this._hash.tempDisableAutoCompaction();
                TIntFloatHashMap.this.removeAt(this._index);
                this._hash.reenableAutoCompaction(false);
                this._expectedSize--;
            } catch (Throwable th) {
                this._hash.reenableAutoCompaction(false);
                throw th;
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TIntFloatHashMap$TIntFloatHashIterator.class */
    class TIntFloatHashIterator extends THashPrimitiveIterator implements TIntFloatIterator {
        TIntFloatHashIterator(TIntFloatHashMap map) {
            super(map);
        }

        @Override // gnu.trove.iterator.TAdvancingIterator
        public void advance() {
            moveToNextIndex();
        }

        @Override // gnu.trove.iterator.TIntFloatIterator
        public int key() {
            return TIntFloatHashMap.this._set[this._index];
        }

        @Override // gnu.trove.iterator.TIntFloatIterator
        public float value() {
            return TIntFloatHashMap.this._values[this._index];
        }

        @Override // gnu.trove.iterator.TIntFloatIterator
        public float setValue(float val) {
            float old = value();
            TIntFloatHashMap.this._values[this._index] = val;
            return old;
        }

        @Override // gnu.trove.impl.hash.THashPrimitiveIterator, gnu.trove.iterator.TPrimitiveIterator, gnu.trove.iterator.TIterator, java.util.Iterator
        public void remove() {
            if (this._expectedSize != this._hash.size()) {
                throw new ConcurrentModificationException();
            }
            try {
                this._hash.tempDisableAutoCompaction();
                TIntFloatHashMap.this.removeAt(this._index);
                this._hash.reenableAutoCompaction(false);
                this._expectedSize--;
            } catch (Throwable th) {
                this._hash.reenableAutoCompaction(false);
                throw th;
            }
        }
    }

    public boolean equals(Object other) {
        if (!(other instanceof TIntFloatMap)) {
            return false;
        }
        TIntFloatMap that = (TIntFloatMap) other;
        if (that.size() != size()) {
            return false;
        }
        float[] values = this._values;
        byte[] states = this._states;
        float this_no_entry_value = getNoEntryValue();
        float that_no_entry_value = that.getNoEntryValue();
        int i = values.length;
        while (true) {
            int i2 = i;
            i--;
            if (i2 > 0) {
                if (states[i] == 1) {
                    int key = this._set[i];
                    float that_value = that.get(key);
                    float this_value = values[i];
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
        forEachEntry(new TIntFloatProcedure() { // from class: gnu.trove.map.hash.TIntFloatHashMap.1
            private boolean first = true;

            @Override // gnu.trove.procedure.TIntFloatProcedure
            public boolean execute(int key, float value) {
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

    @Override // gnu.trove.impl.hash.TIntFloatHash, gnu.trove.impl.hash.THash, java.io.Externalizable
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
                    out.writeInt(this._set[i]);
                    out.writeFloat(this._values[i]);
                }
            } else {
                return;
            }
        }
    }

    @Override // gnu.trove.impl.hash.TIntFloatHash, gnu.trove.impl.hash.THash, java.io.Externalizable
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        in.readByte();
        super.readExternal(in);
        int size = in.readInt();
        setUp(size);
        while (true) {
            int i = size;
            size--;
            if (i > 0) {
                int key = in.readInt();
                float val = in.readFloat();
                put(key, val);
            } else {
                return;
            }
        }
    }
}
