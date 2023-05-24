package gnu.trove.map.hash;

import gnu.trove.TCharCollection;
import gnu.trove.TFloatCollection;
import gnu.trove.function.TCharFunction;
import gnu.trove.impl.HashFunctions;
import gnu.trove.impl.hash.TFloatCharHash;
import gnu.trove.impl.hash.THashPrimitiveIterator;
import gnu.trove.impl.hash.TPrimitiveHash;
import gnu.trove.iterator.TCharIterator;
import gnu.trove.iterator.TFloatCharIterator;
import gnu.trove.iterator.TFloatIterator;
import gnu.trove.map.TFloatCharMap;
import gnu.trove.procedure.TCharProcedure;
import gnu.trove.procedure.TFloatCharProcedure;
import gnu.trove.procedure.TFloatProcedure;
import gnu.trove.set.TFloatSet;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Arrays;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TFloatCharHashMap.class */
public class TFloatCharHashMap extends TFloatCharHash implements TFloatCharMap, Externalizable {
    static final long serialVersionUID = 1;
    protected transient char[] _values;

    public TFloatCharHashMap() {
    }

    public TFloatCharHashMap(int initialCapacity) {
        super(initialCapacity);
    }

    public TFloatCharHashMap(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    public TFloatCharHashMap(int initialCapacity, float loadFactor, float noEntryKey, char noEntryValue) {
        super(initialCapacity, loadFactor, noEntryKey, noEntryValue);
    }

    public TFloatCharHashMap(float[] keys, char[] values) {
        super(Math.max(keys.length, values.length));
        int size = Math.min(keys.length, values.length);
        for (int i = 0; i < size; i++) {
            put(keys[i], values[i]);
        }
    }

    public TFloatCharHashMap(TFloatCharMap map) {
        super(map.size());
        if (map instanceof TFloatCharHashMap) {
            TFloatCharHashMap hashmap = (TFloatCharHashMap) map;
            this._loadFactor = hashmap._loadFactor;
            this.no_entry_key = hashmap.no_entry_key;
            this.no_entry_value = hashmap.no_entry_value;
            if (this.no_entry_key != 0.0f) {
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
    @Override // gnu.trove.impl.hash.TFloatCharHash, gnu.trove.impl.hash.TPrimitiveHash, gnu.trove.impl.hash.THash
    public int setUp(int initialCapacity) {
        int capacity = super.setUp(initialCapacity);
        this._values = new char[capacity];
        return capacity;
    }

    @Override // gnu.trove.impl.hash.THash
    protected void rehash(int newCapacity) {
        int oldCapacity = this._set.length;
        float[] oldKeys = this._set;
        char[] oldVals = this._values;
        byte[] oldStates = this._states;
        this._set = new float[newCapacity];
        this._values = new char[newCapacity];
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

    @Override // gnu.trove.map.TFloatCharMap
    public char put(float key, char value) {
        int index = insertKey(key);
        return doPut(key, value, index);
    }

    @Override // gnu.trove.map.TFloatCharMap
    public char putIfAbsent(float key, char value) {
        int index = insertKey(key);
        if (index < 0) {
            return this._values[(-index) - 1];
        }
        return doPut(key, value, index);
    }

    private char doPut(float key, char value, int index) {
        char previous = this.no_entry_value;
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

    @Override // gnu.trove.map.TFloatCharMap
    public void putAll(Map<? extends Float, ? extends Character> map) {
        ensureCapacity(map.size());
        for (Map.Entry<? extends Float, ? extends Character> entry : map.entrySet()) {
            put(entry.getKey().floatValue(), entry.getValue().charValue());
        }
    }

    @Override // gnu.trove.map.TFloatCharMap
    public void putAll(TFloatCharMap map) {
        ensureCapacity(map.size());
        TFloatCharIterator iter = map.iterator();
        while (iter.hasNext()) {
            iter.advance();
            put(iter.key(), iter.value());
        }
    }

    @Override // gnu.trove.map.TFloatCharMap
    public char get(float key) {
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

    @Override // gnu.trove.map.TFloatCharMap
    public char remove(float key) {
        char prev = this.no_entry_value;
        int index = index(key);
        if (index >= 0) {
            prev = this._values[index];
            removeAt(index);
        }
        return prev;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // gnu.trove.impl.hash.TFloatCharHash, gnu.trove.impl.hash.TPrimitiveHash, gnu.trove.impl.hash.THash
    public void removeAt(int index) {
        this._values[index] = this.no_entry_value;
        super.removeAt(index);
    }

    @Override // gnu.trove.map.TFloatCharMap
    public TFloatSet keySet() {
        return new TKeyView();
    }

    @Override // gnu.trove.map.TFloatCharMap
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

    @Override // gnu.trove.map.TFloatCharMap
    public float[] keys(float[] array) {
        int size = size();
        if (array.length < size) {
            array = new float[size];
        }
        float[] keys = this._set;
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

    @Override // gnu.trove.map.TFloatCharMap
    public TCharCollection valueCollection() {
        return new TValueView();
    }

    @Override // gnu.trove.map.TFloatCharMap
    public char[] values() {
        char[] vals = new char[size()];
        char[] v = this._values;
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

    @Override // gnu.trove.map.TFloatCharMap
    public char[] values(char[] array) {
        int size = size();
        if (array.length < size) {
            array = new char[size];
        }
        char[] v = this._values;
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

    @Override // gnu.trove.map.TFloatCharMap
    public boolean containsValue(char val) {
        byte[] states = this._states;
        char[] vals = this._values;
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

    @Override // gnu.trove.map.TFloatCharMap
    public boolean containsKey(float key) {
        return contains(key);
    }

    @Override // gnu.trove.map.TFloatCharMap
    public TFloatCharIterator iterator() {
        return new TFloatCharHashIterator(this);
    }

    @Override // gnu.trove.map.TFloatCharMap
    public boolean forEachKey(TFloatProcedure procedure) {
        return forEach(procedure);
    }

    @Override // gnu.trove.map.TFloatCharMap
    public boolean forEachValue(TCharProcedure procedure) {
        byte[] states = this._states;
        char[] values = this._values;
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

    @Override // gnu.trove.map.TFloatCharMap
    public boolean forEachEntry(TFloatCharProcedure procedure) {
        byte[] states = this._states;
        float[] keys = this._set;
        char[] values = this._values;
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

    @Override // gnu.trove.map.TFloatCharMap
    public void transformValues(TCharFunction function) {
        byte[] states = this._states;
        char[] values = this._values;
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

    @Override // gnu.trove.map.TFloatCharMap
    public boolean retainEntries(TFloatCharProcedure procedure) {
        boolean modified = false;
        byte[] states = this._states;
        float[] keys = this._set;
        char[] values = this._values;
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

    @Override // gnu.trove.map.TFloatCharMap
    public boolean increment(float key) {
        return adjustValue(key, (char) 1);
    }

    @Override // gnu.trove.map.TFloatCharMap
    public boolean adjustValue(float key, char amount) {
        int index = index(key);
        if (index < 0) {
            return false;
        }
        char[] cArr = this._values;
        cArr[index] = (char) (cArr[index] + amount);
        return true;
    }

    @Override // gnu.trove.map.TFloatCharMap
    public char adjustOrPutValue(float key, char adjust_amount, char put_amount) {
        char newValue;
        boolean isNewMapping;
        int index = insertKey(key);
        if (index < 0) {
            index = (-index) - 1;
            char[] cArr = this._values;
            char c = (char) (cArr[index] + adjust_amount);
            cArr[index] = c;
            newValue = c;
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

    /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TFloatCharHashMap$TKeyView.class */
    protected class TKeyView implements TFloatSet {
        protected TKeyView() {
        }

        @Override // gnu.trove.set.TFloatSet, gnu.trove.TFloatCollection
        public TFloatIterator iterator() {
            return new TFloatCharKeyHashIterator(TFloatCharHashMap.this);
        }

        @Override // gnu.trove.set.TFloatSet, gnu.trove.TFloatCollection
        public float getNoEntryValue() {
            return TFloatCharHashMap.this.no_entry_key;
        }

        @Override // gnu.trove.set.TFloatSet, gnu.trove.TFloatCollection
        public int size() {
            return TFloatCharHashMap.this._size;
        }

        @Override // gnu.trove.set.TFloatSet, gnu.trove.TFloatCollection
        public boolean isEmpty() {
            return 0 == TFloatCharHashMap.this._size;
        }

        @Override // gnu.trove.set.TFloatSet, gnu.trove.TFloatCollection
        public boolean contains(float entry) {
            return TFloatCharHashMap.this.contains(entry);
        }

        @Override // gnu.trove.set.TFloatSet, gnu.trove.TFloatCollection
        public float[] toArray() {
            return TFloatCharHashMap.this.keys();
        }

        @Override // gnu.trove.set.TFloatSet, gnu.trove.TFloatCollection
        public float[] toArray(float[] dest) {
            return TFloatCharHashMap.this.keys(dest);
        }

        @Override // gnu.trove.set.TFloatSet, gnu.trove.TFloatCollection
        public boolean add(float entry) {
            throw new UnsupportedOperationException();
        }

        @Override // gnu.trove.set.TFloatSet, gnu.trove.TFloatCollection
        public boolean remove(float entry) {
            return TFloatCharHashMap.this.no_entry_value != TFloatCharHashMap.this.remove(entry);
        }

        @Override // gnu.trove.set.TFloatSet, gnu.trove.TFloatCollection
        public boolean containsAll(Collection<?> collection) {
            for (Object element : collection) {
                if (element instanceof Float) {
                    float ele = ((Float) element).floatValue();
                    if (!TFloatCharHashMap.this.containsKey(ele)) {
                        return false;
                    }
                } else {
                    return false;
                }
            }
            return true;
        }

        @Override // gnu.trove.set.TFloatSet, gnu.trove.TFloatCollection
        public boolean containsAll(TFloatCollection collection) {
            TFloatIterator iter = collection.iterator();
            while (iter.hasNext()) {
                if (!TFloatCharHashMap.this.containsKey(iter.next())) {
                    return false;
                }
            }
            return true;
        }

        @Override // gnu.trove.set.TFloatSet, gnu.trove.TFloatCollection
        public boolean containsAll(float[] array) {
            for (float element : array) {
                if (!TFloatCharHashMap.this.contains(element)) {
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
            float[] set = TFloatCharHashMap.this._set;
            byte[] states = TFloatCharHashMap.this._states;
            int i = set.length;
            while (true) {
                int i2 = i;
                i--;
                if (i2 > 0) {
                    if (states[i] == 1 && Arrays.binarySearch(array, set[i]) < 0) {
                        TFloatCharHashMap.this.removeAt(i);
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
            TFloatCharHashMap.this.clear();
        }

        @Override // gnu.trove.set.TFloatSet, gnu.trove.TFloatCollection
        public boolean forEach(TFloatProcedure procedure) {
            return TFloatCharHashMap.this.forEachKey(procedure);
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
            int i = TFloatCharHashMap.this._states.length;
            while (true) {
                int i2 = i;
                i--;
                if (i2 > 0) {
                    if (TFloatCharHashMap.this._states[i] == 1 && !that.contains(TFloatCharHashMap.this._set[i])) {
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
            int i = TFloatCharHashMap.this._states.length;
            while (true) {
                int i2 = i;
                i--;
                if (i2 > 0) {
                    if (TFloatCharHashMap.this._states[i] == 1) {
                        hashcode += HashFunctions.hash(TFloatCharHashMap.this._set[i]);
                    }
                } else {
                    return hashcode;
                }
            }
        }

        public String toString() {
            final StringBuilder buf = new StringBuilder("{");
            TFloatCharHashMap.this.forEachKey(new TFloatProcedure() { // from class: gnu.trove.map.hash.TFloatCharHashMap.TKeyView.1
                private boolean first = true;

                @Override // gnu.trove.procedure.TFloatProcedure
                public boolean execute(float key) {
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

    /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TFloatCharHashMap$TValueView.class */
    protected class TValueView implements TCharCollection {
        protected TValueView() {
        }

        @Override // gnu.trove.TCharCollection
        public TCharIterator iterator() {
            return new TFloatCharValueHashIterator(TFloatCharHashMap.this);
        }

        @Override // gnu.trove.TCharCollection
        public char getNoEntryValue() {
            return TFloatCharHashMap.this.no_entry_value;
        }

        @Override // gnu.trove.TCharCollection
        public int size() {
            return TFloatCharHashMap.this._size;
        }

        @Override // gnu.trove.TCharCollection
        public boolean isEmpty() {
            return 0 == TFloatCharHashMap.this._size;
        }

        @Override // gnu.trove.TCharCollection
        public boolean contains(char entry) {
            return TFloatCharHashMap.this.containsValue(entry);
        }

        @Override // gnu.trove.TCharCollection
        public char[] toArray() {
            return TFloatCharHashMap.this.values();
        }

        @Override // gnu.trove.TCharCollection
        public char[] toArray(char[] dest) {
            return TFloatCharHashMap.this.values(dest);
        }

        @Override // gnu.trove.TCharCollection
        public boolean add(char entry) {
            throw new UnsupportedOperationException();
        }

        @Override // gnu.trove.TCharCollection
        public boolean remove(char entry) {
            char[] values = TFloatCharHashMap.this._values;
            float[] set = TFloatCharHashMap.this._set;
            int i = values.length;
            while (true) {
                int i2 = i;
                i--;
                if (i2 > 0) {
                    if (set[i] != 0.0f && set[i] != 2.0f && entry == values[i]) {
                        TFloatCharHashMap.this.removeAt(i);
                        return true;
                    }
                } else {
                    return false;
                }
            }
        }

        @Override // gnu.trove.TCharCollection
        public boolean containsAll(Collection<?> collection) {
            for (Object element : collection) {
                if (element instanceof Character) {
                    char ele = ((Character) element).charValue();
                    if (!TFloatCharHashMap.this.containsValue(ele)) {
                        return false;
                    }
                } else {
                    return false;
                }
            }
            return true;
        }

        @Override // gnu.trove.TCharCollection
        public boolean containsAll(TCharCollection collection) {
            TCharIterator iter = collection.iterator();
            while (iter.hasNext()) {
                if (!TFloatCharHashMap.this.containsValue(iter.next())) {
                    return false;
                }
            }
            return true;
        }

        @Override // gnu.trove.TCharCollection
        public boolean containsAll(char[] array) {
            for (char element : array) {
                if (!TFloatCharHashMap.this.containsValue(element)) {
                    return false;
                }
            }
            return true;
        }

        @Override // gnu.trove.TCharCollection
        public boolean addAll(Collection<? extends Character> collection) {
            throw new UnsupportedOperationException();
        }

        @Override // gnu.trove.TCharCollection
        public boolean addAll(TCharCollection collection) {
            throw new UnsupportedOperationException();
        }

        @Override // gnu.trove.TCharCollection
        public boolean addAll(char[] array) {
            throw new UnsupportedOperationException();
        }

        @Override // gnu.trove.TCharCollection
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

        @Override // gnu.trove.TCharCollection
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

        @Override // gnu.trove.TCharCollection
        public boolean retainAll(char[] array) {
            boolean changed = false;
            Arrays.sort(array);
            char[] values = TFloatCharHashMap.this._values;
            byte[] states = TFloatCharHashMap.this._states;
            int i = values.length;
            while (true) {
                int i2 = i;
                i--;
                if (i2 > 0) {
                    if (states[i] == 1 && Arrays.binarySearch(array, values[i]) < 0) {
                        TFloatCharHashMap.this.removeAt(i);
                        changed = true;
                    }
                } else {
                    return changed;
                }
            }
        }

        @Override // gnu.trove.TCharCollection
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

        @Override // gnu.trove.TCharCollection
        public boolean removeAll(TCharCollection collection) {
            if (this == collection) {
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

        @Override // gnu.trove.TCharCollection
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

        @Override // gnu.trove.TCharCollection
        public void clear() {
            TFloatCharHashMap.this.clear();
        }

        @Override // gnu.trove.TCharCollection
        public boolean forEach(TCharProcedure procedure) {
            return TFloatCharHashMap.this.forEachValue(procedure);
        }

        public String toString() {
            final StringBuilder buf = new StringBuilder("{");
            TFloatCharHashMap.this.forEachValue(new TCharProcedure() { // from class: gnu.trove.map.hash.TFloatCharHashMap.TValueView.1
                private boolean first = true;

                @Override // gnu.trove.procedure.TCharProcedure
                public boolean execute(char value) {
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
    /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TFloatCharHashMap$TFloatCharKeyHashIterator.class */
    public class TFloatCharKeyHashIterator extends THashPrimitiveIterator implements TFloatIterator {
        TFloatCharKeyHashIterator(TPrimitiveHash hash) {
            super(hash);
        }

        @Override // gnu.trove.iterator.TFloatIterator
        public float next() {
            moveToNextIndex();
            return TFloatCharHashMap.this._set[this._index];
        }

        @Override // gnu.trove.impl.hash.THashPrimitiveIterator, gnu.trove.iterator.TPrimitiveIterator, gnu.trove.iterator.TIterator, java.util.Iterator
        public void remove() {
            if (this._expectedSize != this._hash.size()) {
                throw new ConcurrentModificationException();
            }
            try {
                this._hash.tempDisableAutoCompaction();
                TFloatCharHashMap.this.removeAt(this._index);
                this._hash.reenableAutoCompaction(false);
                this._expectedSize--;
            } catch (Throwable th) {
                this._hash.reenableAutoCompaction(false);
                throw th;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TFloatCharHashMap$TFloatCharValueHashIterator.class */
    public class TFloatCharValueHashIterator extends THashPrimitiveIterator implements TCharIterator {
        TFloatCharValueHashIterator(TPrimitiveHash hash) {
            super(hash);
        }

        @Override // gnu.trove.iterator.TCharIterator
        public char next() {
            moveToNextIndex();
            return TFloatCharHashMap.this._values[this._index];
        }

        @Override // gnu.trove.impl.hash.THashPrimitiveIterator, gnu.trove.iterator.TPrimitiveIterator, gnu.trove.iterator.TIterator, java.util.Iterator
        public void remove() {
            if (this._expectedSize != this._hash.size()) {
                throw new ConcurrentModificationException();
            }
            try {
                this._hash.tempDisableAutoCompaction();
                TFloatCharHashMap.this.removeAt(this._index);
                this._hash.reenableAutoCompaction(false);
                this._expectedSize--;
            } catch (Throwable th) {
                this._hash.reenableAutoCompaction(false);
                throw th;
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TFloatCharHashMap$TFloatCharHashIterator.class */
    class TFloatCharHashIterator extends THashPrimitiveIterator implements TFloatCharIterator {
        TFloatCharHashIterator(TFloatCharHashMap map) {
            super(map);
        }

        @Override // gnu.trove.iterator.TAdvancingIterator
        public void advance() {
            moveToNextIndex();
        }

        @Override // gnu.trove.iterator.TFloatCharIterator
        public float key() {
            return TFloatCharHashMap.this._set[this._index];
        }

        @Override // gnu.trove.iterator.TFloatCharIterator
        public char value() {
            return TFloatCharHashMap.this._values[this._index];
        }

        @Override // gnu.trove.iterator.TFloatCharIterator
        public char setValue(char val) {
            char old = value();
            TFloatCharHashMap.this._values[this._index] = val;
            return old;
        }

        @Override // gnu.trove.impl.hash.THashPrimitiveIterator, gnu.trove.iterator.TPrimitiveIterator, gnu.trove.iterator.TIterator, java.util.Iterator
        public void remove() {
            if (this._expectedSize != this._hash.size()) {
                throw new ConcurrentModificationException();
            }
            try {
                this._hash.tempDisableAutoCompaction();
                TFloatCharHashMap.this.removeAt(this._index);
                this._hash.reenableAutoCompaction(false);
                this._expectedSize--;
            } catch (Throwable th) {
                this._hash.reenableAutoCompaction(false);
                throw th;
            }
        }
    }

    public boolean equals(Object other) {
        if (!(other instanceof TFloatCharMap)) {
            return false;
        }
        TFloatCharMap that = (TFloatCharMap) other;
        if (that.size() != size()) {
            return false;
        }
        char[] values = this._values;
        byte[] states = this._states;
        char this_no_entry_value = getNoEntryValue();
        char that_no_entry_value = that.getNoEntryValue();
        int i = values.length;
        while (true) {
            int i2 = i;
            i--;
            if (i2 > 0) {
                if (states[i] == 1) {
                    float key = this._set[i];
                    char that_value = that.get(key);
                    char this_value = values[i];
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
                    hashcode += HashFunctions.hash(this._set[i]) ^ HashFunctions.hash((int) this._values[i]);
                }
            } else {
                return hashcode;
            }
        }
    }

    public String toString() {
        final StringBuilder buf = new StringBuilder("{");
        forEachEntry(new TFloatCharProcedure() { // from class: gnu.trove.map.hash.TFloatCharHashMap.1
            private boolean first = true;

            @Override // gnu.trove.procedure.TFloatCharProcedure
            public boolean execute(float key, char value) {
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

    @Override // gnu.trove.impl.hash.TFloatCharHash, gnu.trove.impl.hash.THash, java.io.Externalizable
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
                    out.writeFloat(this._set[i]);
                    out.writeChar(this._values[i]);
                }
            } else {
                return;
            }
        }
    }

    @Override // gnu.trove.impl.hash.TFloatCharHash, gnu.trove.impl.hash.THash, java.io.Externalizable
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        in.readByte();
        super.readExternal(in);
        int size = in.readInt();
        setUp(size);
        while (true) {
            int i = size;
            size--;
            if (i > 0) {
                float key = in.readFloat();
                char val = in.readChar();
                put(key, val);
            } else {
                return;
            }
        }
    }
}
