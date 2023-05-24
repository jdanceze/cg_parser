package gnu.trove.map.hash;

import com.sun.xml.bind.v2.runtime.reflect.opt.Const;
import gnu.trove.TDoubleCollection;
import gnu.trove.TFloatCollection;
import gnu.trove.function.TFloatFunction;
import gnu.trove.impl.HashFunctions;
import gnu.trove.impl.hash.TDoubleFloatHash;
import gnu.trove.impl.hash.THashPrimitiveIterator;
import gnu.trove.impl.hash.TPrimitiveHash;
import gnu.trove.iterator.TDoubleFloatIterator;
import gnu.trove.iterator.TDoubleIterator;
import gnu.trove.iterator.TFloatIterator;
import gnu.trove.map.TDoubleFloatMap;
import gnu.trove.procedure.TDoubleFloatProcedure;
import gnu.trove.procedure.TDoubleProcedure;
import gnu.trove.procedure.TFloatProcedure;
import gnu.trove.set.TDoubleSet;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Arrays;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TDoubleFloatHashMap.class */
public class TDoubleFloatHashMap extends TDoubleFloatHash implements TDoubleFloatMap, Externalizable {
    static final long serialVersionUID = 1;
    protected transient float[] _values;

    public TDoubleFloatHashMap() {
    }

    public TDoubleFloatHashMap(int initialCapacity) {
        super(initialCapacity);
    }

    public TDoubleFloatHashMap(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    public TDoubleFloatHashMap(int initialCapacity, float loadFactor, double noEntryKey, float noEntryValue) {
        super(initialCapacity, loadFactor, noEntryKey, noEntryValue);
    }

    public TDoubleFloatHashMap(double[] keys, float[] values) {
        super(Math.max(keys.length, values.length));
        int size = Math.min(keys.length, values.length);
        for (int i = 0; i < size; i++) {
            put(keys[i], values[i]);
        }
    }

    public TDoubleFloatHashMap(TDoubleFloatMap map) {
        super(map.size());
        if (map instanceof TDoubleFloatHashMap) {
            TDoubleFloatHashMap hashmap = (TDoubleFloatHashMap) map;
            this._loadFactor = hashmap._loadFactor;
            this.no_entry_key = hashmap.no_entry_key;
            this.no_entry_value = hashmap.no_entry_value;
            if (this.no_entry_key != Const.default_value_double) {
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
    @Override // gnu.trove.impl.hash.TDoubleFloatHash, gnu.trove.impl.hash.TPrimitiveHash, gnu.trove.impl.hash.THash
    public int setUp(int initialCapacity) {
        int capacity = super.setUp(initialCapacity);
        this._values = new float[capacity];
        return capacity;
    }

    @Override // gnu.trove.impl.hash.THash
    protected void rehash(int newCapacity) {
        int oldCapacity = this._set.length;
        double[] oldKeys = this._set;
        float[] oldVals = this._values;
        byte[] oldStates = this._states;
        this._set = new double[newCapacity];
        this._values = new float[newCapacity];
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

    @Override // gnu.trove.map.TDoubleFloatMap
    public float put(double key, float value) {
        int index = insertKey(key);
        return doPut(key, value, index);
    }

    @Override // gnu.trove.map.TDoubleFloatMap
    public float putIfAbsent(double key, float value) {
        int index = insertKey(key);
        if (index < 0) {
            return this._values[(-index) - 1];
        }
        return doPut(key, value, index);
    }

    private float doPut(double key, float value, int index) {
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

    @Override // gnu.trove.map.TDoubleFloatMap
    public void putAll(Map<? extends Double, ? extends Float> map) {
        ensureCapacity(map.size());
        for (Map.Entry<? extends Double, ? extends Float> entry : map.entrySet()) {
            put(entry.getKey().doubleValue(), entry.getValue().floatValue());
        }
    }

    @Override // gnu.trove.map.TDoubleFloatMap
    public void putAll(TDoubleFloatMap map) {
        ensureCapacity(map.size());
        TDoubleFloatIterator iter = map.iterator();
        while (iter.hasNext()) {
            iter.advance();
            put(iter.key(), iter.value());
        }
    }

    @Override // gnu.trove.map.TDoubleFloatMap
    public float get(double key) {
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

    @Override // gnu.trove.map.TDoubleFloatMap
    public float remove(double key) {
        float prev = this.no_entry_value;
        int index = index(key);
        if (index >= 0) {
            prev = this._values[index];
            removeAt(index);
        }
        return prev;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // gnu.trove.impl.hash.TDoubleFloatHash, gnu.trove.impl.hash.TPrimitiveHash, gnu.trove.impl.hash.THash
    public void removeAt(int index) {
        this._values[index] = this.no_entry_value;
        super.removeAt(index);
    }

    @Override // gnu.trove.map.TDoubleFloatMap
    public TDoubleSet keySet() {
        return new TKeyView();
    }

    @Override // gnu.trove.map.TDoubleFloatMap
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

    @Override // gnu.trove.map.TDoubleFloatMap
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

    @Override // gnu.trove.map.TDoubleFloatMap
    public TFloatCollection valueCollection() {
        return new TValueView();
    }

    @Override // gnu.trove.map.TDoubleFloatMap
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

    @Override // gnu.trove.map.TDoubleFloatMap
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

    @Override // gnu.trove.map.TDoubleFloatMap
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

    @Override // gnu.trove.map.TDoubleFloatMap
    public boolean containsKey(double key) {
        return contains(key);
    }

    @Override // gnu.trove.map.TDoubleFloatMap
    public TDoubleFloatIterator iterator() {
        return new TDoubleFloatHashIterator(this);
    }

    @Override // gnu.trove.map.TDoubleFloatMap
    public boolean forEachKey(TDoubleProcedure procedure) {
        return forEach(procedure);
    }

    @Override // gnu.trove.map.TDoubleFloatMap
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

    @Override // gnu.trove.map.TDoubleFloatMap
    public boolean forEachEntry(TDoubleFloatProcedure procedure) {
        byte[] states = this._states;
        double[] keys = this._set;
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

    @Override // gnu.trove.map.TDoubleFloatMap
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

    @Override // gnu.trove.map.TDoubleFloatMap
    public boolean retainEntries(TDoubleFloatProcedure procedure) {
        boolean modified = false;
        byte[] states = this._states;
        double[] keys = this._set;
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

    @Override // gnu.trove.map.TDoubleFloatMap
    public boolean increment(double key) {
        return adjustValue(key, 1.0f);
    }

    @Override // gnu.trove.map.TDoubleFloatMap
    public boolean adjustValue(double key, float amount) {
        int index = index(key);
        if (index < 0) {
            return false;
        }
        float[] fArr = this._values;
        fArr[index] = fArr[index] + amount;
        return true;
    }

    @Override // gnu.trove.map.TDoubleFloatMap
    public float adjustOrPutValue(double key, float adjust_amount, float put_amount) {
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

    /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TDoubleFloatHashMap$TKeyView.class */
    protected class TKeyView implements TDoubleSet {
        protected TKeyView() {
        }

        @Override // gnu.trove.set.TDoubleSet, gnu.trove.TDoubleCollection
        public TDoubleIterator iterator() {
            return new TDoubleFloatKeyHashIterator(TDoubleFloatHashMap.this);
        }

        @Override // gnu.trove.set.TDoubleSet, gnu.trove.TDoubleCollection
        public double getNoEntryValue() {
            return TDoubleFloatHashMap.this.no_entry_key;
        }

        @Override // gnu.trove.set.TDoubleSet, gnu.trove.TDoubleCollection
        public int size() {
            return TDoubleFloatHashMap.this._size;
        }

        @Override // gnu.trove.set.TDoubleSet, gnu.trove.TDoubleCollection
        public boolean isEmpty() {
            return 0 == TDoubleFloatHashMap.this._size;
        }

        @Override // gnu.trove.set.TDoubleSet, gnu.trove.TDoubleCollection
        public boolean contains(double entry) {
            return TDoubleFloatHashMap.this.contains(entry);
        }

        @Override // gnu.trove.set.TDoubleSet, gnu.trove.TDoubleCollection
        public double[] toArray() {
            return TDoubleFloatHashMap.this.keys();
        }

        @Override // gnu.trove.set.TDoubleSet, gnu.trove.TDoubleCollection
        public double[] toArray(double[] dest) {
            return TDoubleFloatHashMap.this.keys(dest);
        }

        @Override // gnu.trove.set.TDoubleSet, gnu.trove.TDoubleCollection
        public boolean add(double entry) {
            throw new UnsupportedOperationException();
        }

        @Override // gnu.trove.set.TDoubleSet, gnu.trove.TDoubleCollection
        public boolean remove(double entry) {
            return TDoubleFloatHashMap.this.no_entry_value != TDoubleFloatHashMap.this.remove(entry);
        }

        @Override // gnu.trove.set.TDoubleSet, gnu.trove.TDoubleCollection
        public boolean containsAll(Collection<?> collection) {
            for (Object element : collection) {
                if (element instanceof Double) {
                    double ele = ((Double) element).doubleValue();
                    if (!TDoubleFloatHashMap.this.containsKey(ele)) {
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
                if (!TDoubleFloatHashMap.this.containsKey(iter.next())) {
                    return false;
                }
            }
            return true;
        }

        @Override // gnu.trove.set.TDoubleSet, gnu.trove.TDoubleCollection
        public boolean containsAll(double[] array) {
            for (double element : array) {
                if (!TDoubleFloatHashMap.this.contains(element)) {
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
            double[] set = TDoubleFloatHashMap.this._set;
            byte[] states = TDoubleFloatHashMap.this._states;
            int i = set.length;
            while (true) {
                int i2 = i;
                i--;
                if (i2 > 0) {
                    if (states[i] == 1 && Arrays.binarySearch(array, set[i]) < 0) {
                        TDoubleFloatHashMap.this.removeAt(i);
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
            TDoubleFloatHashMap.this.clear();
        }

        @Override // gnu.trove.set.TDoubleSet, gnu.trove.TDoubleCollection
        public boolean forEach(TDoubleProcedure procedure) {
            return TDoubleFloatHashMap.this.forEachKey(procedure);
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
            int i = TDoubleFloatHashMap.this._states.length;
            while (true) {
                int i2 = i;
                i--;
                if (i2 > 0) {
                    if (TDoubleFloatHashMap.this._states[i] == 1 && !that.contains(TDoubleFloatHashMap.this._set[i])) {
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
            int i = TDoubleFloatHashMap.this._states.length;
            while (true) {
                int i2 = i;
                i--;
                if (i2 > 0) {
                    if (TDoubleFloatHashMap.this._states[i] == 1) {
                        hashcode += HashFunctions.hash(TDoubleFloatHashMap.this._set[i]);
                    }
                } else {
                    return hashcode;
                }
            }
        }

        public String toString() {
            final StringBuilder buf = new StringBuilder("{");
            TDoubleFloatHashMap.this.forEachKey(new TDoubleProcedure() { // from class: gnu.trove.map.hash.TDoubleFloatHashMap.TKeyView.1
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

    /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TDoubleFloatHashMap$TValueView.class */
    protected class TValueView implements TFloatCollection {
        protected TValueView() {
        }

        @Override // gnu.trove.TFloatCollection
        public TFloatIterator iterator() {
            return new TDoubleFloatValueHashIterator(TDoubleFloatHashMap.this);
        }

        @Override // gnu.trove.TFloatCollection
        public float getNoEntryValue() {
            return TDoubleFloatHashMap.this.no_entry_value;
        }

        @Override // gnu.trove.TFloatCollection
        public int size() {
            return TDoubleFloatHashMap.this._size;
        }

        @Override // gnu.trove.TFloatCollection
        public boolean isEmpty() {
            return 0 == TDoubleFloatHashMap.this._size;
        }

        @Override // gnu.trove.TFloatCollection
        public boolean contains(float entry) {
            return TDoubleFloatHashMap.this.containsValue(entry);
        }

        @Override // gnu.trove.TFloatCollection
        public float[] toArray() {
            return TDoubleFloatHashMap.this.values();
        }

        @Override // gnu.trove.TFloatCollection
        public float[] toArray(float[] dest) {
            return TDoubleFloatHashMap.this.values(dest);
        }

        @Override // gnu.trove.TFloatCollection
        public boolean add(float entry) {
            throw new UnsupportedOperationException();
        }

        @Override // gnu.trove.TFloatCollection
        public boolean remove(float entry) {
            float[] values = TDoubleFloatHashMap.this._values;
            double[] set = TDoubleFloatHashMap.this._set;
            int i = values.length;
            while (true) {
                int i2 = i;
                i--;
                if (i2 > 0) {
                    if (set[i] != Const.default_value_double && set[i] != 2.0d && entry == values[i]) {
                        TDoubleFloatHashMap.this.removeAt(i);
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
                    if (!TDoubleFloatHashMap.this.containsValue(ele)) {
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
                if (!TDoubleFloatHashMap.this.containsValue(iter.next())) {
                    return false;
                }
            }
            return true;
        }

        @Override // gnu.trove.TFloatCollection
        public boolean containsAll(float[] array) {
            for (float element : array) {
                if (!TDoubleFloatHashMap.this.containsValue(element)) {
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
            float[] values = TDoubleFloatHashMap.this._values;
            byte[] states = TDoubleFloatHashMap.this._states;
            int i = values.length;
            while (true) {
                int i2 = i;
                i--;
                if (i2 > 0) {
                    if (states[i] == 1 && Arrays.binarySearch(array, values[i]) < 0) {
                        TDoubleFloatHashMap.this.removeAt(i);
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
            TDoubleFloatHashMap.this.clear();
        }

        @Override // gnu.trove.TFloatCollection
        public boolean forEach(TFloatProcedure procedure) {
            return TDoubleFloatHashMap.this.forEachValue(procedure);
        }

        public String toString() {
            final StringBuilder buf = new StringBuilder("{");
            TDoubleFloatHashMap.this.forEachValue(new TFloatProcedure() { // from class: gnu.trove.map.hash.TDoubleFloatHashMap.TValueView.1
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
    /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TDoubleFloatHashMap$TDoubleFloatKeyHashIterator.class */
    public class TDoubleFloatKeyHashIterator extends THashPrimitiveIterator implements TDoubleIterator {
        TDoubleFloatKeyHashIterator(TPrimitiveHash hash) {
            super(hash);
        }

        @Override // gnu.trove.iterator.TDoubleIterator
        public double next() {
            moveToNextIndex();
            return TDoubleFloatHashMap.this._set[this._index];
        }

        @Override // gnu.trove.impl.hash.THashPrimitiveIterator, gnu.trove.iterator.TPrimitiveIterator, gnu.trove.iterator.TIterator, java.util.Iterator
        public void remove() {
            if (this._expectedSize != this._hash.size()) {
                throw new ConcurrentModificationException();
            }
            try {
                this._hash.tempDisableAutoCompaction();
                TDoubleFloatHashMap.this.removeAt(this._index);
                this._hash.reenableAutoCompaction(false);
                this._expectedSize--;
            } catch (Throwable th) {
                this._hash.reenableAutoCompaction(false);
                throw th;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TDoubleFloatHashMap$TDoubleFloatValueHashIterator.class */
    public class TDoubleFloatValueHashIterator extends THashPrimitiveIterator implements TFloatIterator {
        TDoubleFloatValueHashIterator(TPrimitiveHash hash) {
            super(hash);
        }

        @Override // gnu.trove.iterator.TFloatIterator
        public float next() {
            moveToNextIndex();
            return TDoubleFloatHashMap.this._values[this._index];
        }

        @Override // gnu.trove.impl.hash.THashPrimitiveIterator, gnu.trove.iterator.TPrimitiveIterator, gnu.trove.iterator.TIterator, java.util.Iterator
        public void remove() {
            if (this._expectedSize != this._hash.size()) {
                throw new ConcurrentModificationException();
            }
            try {
                this._hash.tempDisableAutoCompaction();
                TDoubleFloatHashMap.this.removeAt(this._index);
                this._hash.reenableAutoCompaction(false);
                this._expectedSize--;
            } catch (Throwable th) {
                this._hash.reenableAutoCompaction(false);
                throw th;
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TDoubleFloatHashMap$TDoubleFloatHashIterator.class */
    class TDoubleFloatHashIterator extends THashPrimitiveIterator implements TDoubleFloatIterator {
        TDoubleFloatHashIterator(TDoubleFloatHashMap map) {
            super(map);
        }

        @Override // gnu.trove.iterator.TAdvancingIterator
        public void advance() {
            moveToNextIndex();
        }

        @Override // gnu.trove.iterator.TDoubleFloatIterator
        public double key() {
            return TDoubleFloatHashMap.this._set[this._index];
        }

        @Override // gnu.trove.iterator.TDoubleFloatIterator
        public float value() {
            return TDoubleFloatHashMap.this._values[this._index];
        }

        @Override // gnu.trove.iterator.TDoubleFloatIterator
        public float setValue(float val) {
            float old = value();
            TDoubleFloatHashMap.this._values[this._index] = val;
            return old;
        }

        @Override // gnu.trove.impl.hash.THashPrimitiveIterator, gnu.trove.iterator.TPrimitiveIterator, gnu.trove.iterator.TIterator, java.util.Iterator
        public void remove() {
            if (this._expectedSize != this._hash.size()) {
                throw new ConcurrentModificationException();
            }
            try {
                this._hash.tempDisableAutoCompaction();
                TDoubleFloatHashMap.this.removeAt(this._index);
                this._hash.reenableAutoCompaction(false);
                this._expectedSize--;
            } catch (Throwable th) {
                this._hash.reenableAutoCompaction(false);
                throw th;
            }
        }
    }

    public boolean equals(Object other) {
        if (!(other instanceof TDoubleFloatMap)) {
            return false;
        }
        TDoubleFloatMap that = (TDoubleFloatMap) other;
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
                    double key = this._set[i];
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
        forEachEntry(new TDoubleFloatProcedure() { // from class: gnu.trove.map.hash.TDoubleFloatHashMap.1
            private boolean first = true;

            @Override // gnu.trove.procedure.TDoubleFloatProcedure
            public boolean execute(double key, float value) {
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

    @Override // gnu.trove.impl.hash.TDoubleFloatHash, gnu.trove.impl.hash.THash, java.io.Externalizable
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
                    out.writeFloat(this._values[i]);
                }
            } else {
                return;
            }
        }
    }

    @Override // gnu.trove.impl.hash.TDoubleFloatHash, gnu.trove.impl.hash.THash, java.io.Externalizable
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
                float val = in.readFloat();
                put(key, val);
            } else {
                return;
            }
        }
    }
}
