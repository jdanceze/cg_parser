package gnu.trove.map.hash;

import gnu.trove.TIntCollection;
import gnu.trove.TShortCollection;
import gnu.trove.function.TIntFunction;
import gnu.trove.impl.HashFunctions;
import gnu.trove.impl.hash.THashPrimitiveIterator;
import gnu.trove.impl.hash.TPrimitiveHash;
import gnu.trove.impl.hash.TShortIntHash;
import gnu.trove.iterator.TIntIterator;
import gnu.trove.iterator.TShortIntIterator;
import gnu.trove.iterator.TShortIterator;
import gnu.trove.map.TShortIntMap;
import gnu.trove.procedure.TIntProcedure;
import gnu.trove.procedure.TShortIntProcedure;
import gnu.trove.procedure.TShortProcedure;
import gnu.trove.set.TShortSet;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Arrays;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TShortIntHashMap.class */
public class TShortIntHashMap extends TShortIntHash implements TShortIntMap, Externalizable {
    static final long serialVersionUID = 1;
    protected transient int[] _values;

    public TShortIntHashMap() {
    }

    public TShortIntHashMap(int initialCapacity) {
        super(initialCapacity);
    }

    public TShortIntHashMap(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    public TShortIntHashMap(int initialCapacity, float loadFactor, short noEntryKey, int noEntryValue) {
        super(initialCapacity, loadFactor, noEntryKey, noEntryValue);
    }

    public TShortIntHashMap(short[] keys, int[] values) {
        super(Math.max(keys.length, values.length));
        int size = Math.min(keys.length, values.length);
        for (int i = 0; i < size; i++) {
            put(keys[i], values[i]);
        }
    }

    public TShortIntHashMap(TShortIntMap map) {
        super(map.size());
        if (map instanceof TShortIntHashMap) {
            TShortIntHashMap hashmap = (TShortIntHashMap) map;
            this._loadFactor = hashmap._loadFactor;
            this.no_entry_key = hashmap.no_entry_key;
            this.no_entry_value = hashmap.no_entry_value;
            if (this.no_entry_key != 0) {
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
    @Override // gnu.trove.impl.hash.TShortIntHash, gnu.trove.impl.hash.TPrimitiveHash, gnu.trove.impl.hash.THash
    public int setUp(int initialCapacity) {
        int capacity = super.setUp(initialCapacity);
        this._values = new int[capacity];
        return capacity;
    }

    @Override // gnu.trove.impl.hash.THash
    protected void rehash(int newCapacity) {
        int oldCapacity = this._set.length;
        short[] oldKeys = this._set;
        int[] oldVals = this._values;
        byte[] oldStates = this._states;
        this._set = new short[newCapacity];
        this._values = new int[newCapacity];
        this._states = new byte[newCapacity];
        int i = oldCapacity;
        while (true) {
            int i2 = i;
            i--;
            if (i2 > 0) {
                if (oldStates[i] == 1) {
                    short o = oldKeys[i];
                    int index = insertKey(o);
                    this._values[index] = oldVals[i];
                }
            } else {
                return;
            }
        }
    }

    @Override // gnu.trove.map.TShortIntMap
    public int put(short key, int value) {
        int index = insertKey(key);
        return doPut(key, value, index);
    }

    @Override // gnu.trove.map.TShortIntMap
    public int putIfAbsent(short key, int value) {
        int index = insertKey(key);
        if (index < 0) {
            return this._values[(-index) - 1];
        }
        return doPut(key, value, index);
    }

    private int doPut(short key, int value, int index) {
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

    @Override // gnu.trove.map.TShortIntMap
    public void putAll(Map<? extends Short, ? extends Integer> map) {
        ensureCapacity(map.size());
        for (Map.Entry<? extends Short, ? extends Integer> entry : map.entrySet()) {
            put(entry.getKey().shortValue(), entry.getValue().intValue());
        }
    }

    @Override // gnu.trove.map.TShortIntMap
    public void putAll(TShortIntMap map) {
        ensureCapacity(map.size());
        TShortIntIterator iter = map.iterator();
        while (iter.hasNext()) {
            iter.advance();
            put(iter.key(), iter.value());
        }
    }

    @Override // gnu.trove.map.TShortIntMap
    public int get(short key) {
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

    @Override // gnu.trove.map.TShortIntMap
    public int remove(short key) {
        int prev = this.no_entry_value;
        int index = index(key);
        if (index >= 0) {
            prev = this._values[index];
            removeAt(index);
        }
        return prev;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // gnu.trove.impl.hash.TShortIntHash, gnu.trove.impl.hash.TPrimitiveHash, gnu.trove.impl.hash.THash
    public void removeAt(int index) {
        this._values[index] = this.no_entry_value;
        super.removeAt(index);
    }

    @Override // gnu.trove.map.TShortIntMap
    public TShortSet keySet() {
        return new TKeyView();
    }

    @Override // gnu.trove.map.TShortIntMap
    public short[] keys() {
        short[] keys = new short[size()];
        short[] k = this._set;
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

    @Override // gnu.trove.map.TShortIntMap
    public short[] keys(short[] array) {
        int size = size();
        if (array.length < size) {
            array = new short[size];
        }
        short[] keys = this._set;
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

    @Override // gnu.trove.map.TShortIntMap
    public TIntCollection valueCollection() {
        return new TValueView();
    }

    @Override // gnu.trove.map.TShortIntMap
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

    @Override // gnu.trove.map.TShortIntMap
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

    @Override // gnu.trove.map.TShortIntMap
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

    @Override // gnu.trove.map.TShortIntMap
    public boolean containsKey(short key) {
        return contains(key);
    }

    @Override // gnu.trove.map.TShortIntMap
    public TShortIntIterator iterator() {
        return new TShortIntHashIterator(this);
    }

    @Override // gnu.trove.map.TShortIntMap
    public boolean forEachKey(TShortProcedure procedure) {
        return forEach(procedure);
    }

    @Override // gnu.trove.map.TShortIntMap
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

    @Override // gnu.trove.map.TShortIntMap
    public boolean forEachEntry(TShortIntProcedure procedure) {
        byte[] states = this._states;
        short[] keys = this._set;
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

    @Override // gnu.trove.map.TShortIntMap
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

    @Override // gnu.trove.map.TShortIntMap
    public boolean retainEntries(TShortIntProcedure procedure) {
        boolean modified = false;
        byte[] states = this._states;
        short[] keys = this._set;
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

    @Override // gnu.trove.map.TShortIntMap
    public boolean increment(short key) {
        return adjustValue(key, 1);
    }

    @Override // gnu.trove.map.TShortIntMap
    public boolean adjustValue(short key, int amount) {
        int index = index(key);
        if (index < 0) {
            return false;
        }
        int[] iArr = this._values;
        iArr[index] = iArr[index] + amount;
        return true;
    }

    @Override // gnu.trove.map.TShortIntMap
    public int adjustOrPutValue(short key, int adjust_amount, int put_amount) {
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

    /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TShortIntHashMap$TKeyView.class */
    protected class TKeyView implements TShortSet {
        protected TKeyView() {
        }

        @Override // gnu.trove.set.TShortSet, gnu.trove.TShortCollection
        public TShortIterator iterator() {
            return new TShortIntKeyHashIterator(TShortIntHashMap.this);
        }

        @Override // gnu.trove.set.TShortSet, gnu.trove.TShortCollection
        public short getNoEntryValue() {
            return TShortIntHashMap.this.no_entry_key;
        }

        @Override // gnu.trove.set.TShortSet, gnu.trove.TShortCollection
        public int size() {
            return TShortIntHashMap.this._size;
        }

        @Override // gnu.trove.set.TShortSet, gnu.trove.TShortCollection
        public boolean isEmpty() {
            return 0 == TShortIntHashMap.this._size;
        }

        @Override // gnu.trove.set.TShortSet, gnu.trove.TShortCollection
        public boolean contains(short entry) {
            return TShortIntHashMap.this.contains(entry);
        }

        @Override // gnu.trove.set.TShortSet, gnu.trove.TShortCollection
        public short[] toArray() {
            return TShortIntHashMap.this.keys();
        }

        @Override // gnu.trove.set.TShortSet, gnu.trove.TShortCollection
        public short[] toArray(short[] dest) {
            return TShortIntHashMap.this.keys(dest);
        }

        @Override // gnu.trove.set.TShortSet, gnu.trove.TShortCollection
        public boolean add(short entry) {
            throw new UnsupportedOperationException();
        }

        @Override // gnu.trove.set.TShortSet, gnu.trove.TShortCollection
        public boolean remove(short entry) {
            return TShortIntHashMap.this.no_entry_value != TShortIntHashMap.this.remove(entry);
        }

        @Override // gnu.trove.set.TShortSet, gnu.trove.TShortCollection
        public boolean containsAll(Collection<?> collection) {
            for (Object element : collection) {
                if (element instanceof Short) {
                    short ele = ((Short) element).shortValue();
                    if (!TShortIntHashMap.this.containsKey(ele)) {
                        return false;
                    }
                } else {
                    return false;
                }
            }
            return true;
        }

        @Override // gnu.trove.set.TShortSet, gnu.trove.TShortCollection
        public boolean containsAll(TShortCollection collection) {
            TShortIterator iter = collection.iterator();
            while (iter.hasNext()) {
                if (!TShortIntHashMap.this.containsKey(iter.next())) {
                    return false;
                }
            }
            return true;
        }

        @Override // gnu.trove.set.TShortSet, gnu.trove.TShortCollection
        public boolean containsAll(short[] array) {
            for (short element : array) {
                if (!TShortIntHashMap.this.contains(element)) {
                    return false;
                }
            }
            return true;
        }

        @Override // gnu.trove.set.TShortSet, gnu.trove.TShortCollection
        public boolean addAll(Collection<? extends Short> collection) {
            throw new UnsupportedOperationException();
        }

        @Override // gnu.trove.set.TShortSet, gnu.trove.TShortCollection
        public boolean addAll(TShortCollection collection) {
            throw new UnsupportedOperationException();
        }

        @Override // gnu.trove.set.TShortSet, gnu.trove.TShortCollection
        public boolean addAll(short[] array) {
            throw new UnsupportedOperationException();
        }

        @Override // gnu.trove.set.TShortSet, gnu.trove.TShortCollection
        public boolean retainAll(Collection<?> collection) {
            boolean modified = false;
            TShortIterator iter = iterator();
            while (iter.hasNext()) {
                if (!collection.contains(Short.valueOf(iter.next()))) {
                    iter.remove();
                    modified = true;
                }
            }
            return modified;
        }

        @Override // gnu.trove.set.TShortSet, gnu.trove.TShortCollection
        public boolean retainAll(TShortCollection collection) {
            if (this == collection) {
                return false;
            }
            boolean modified = false;
            TShortIterator iter = iterator();
            while (iter.hasNext()) {
                if (!collection.contains(iter.next())) {
                    iter.remove();
                    modified = true;
                }
            }
            return modified;
        }

        @Override // gnu.trove.set.TShortSet, gnu.trove.TShortCollection
        public boolean retainAll(short[] array) {
            boolean changed = false;
            Arrays.sort(array);
            short[] set = TShortIntHashMap.this._set;
            byte[] states = TShortIntHashMap.this._states;
            int i = set.length;
            while (true) {
                int i2 = i;
                i--;
                if (i2 > 0) {
                    if (states[i] == 1 && Arrays.binarySearch(array, set[i]) < 0) {
                        TShortIntHashMap.this.removeAt(i);
                        changed = true;
                    }
                } else {
                    return changed;
                }
            }
        }

        @Override // gnu.trove.set.TShortSet, gnu.trove.TShortCollection
        public boolean removeAll(Collection<?> collection) {
            boolean changed = false;
            for (Object element : collection) {
                if (element instanceof Short) {
                    short c = ((Short) element).shortValue();
                    if (remove(c)) {
                        changed = true;
                    }
                }
            }
            return changed;
        }

        @Override // gnu.trove.set.TShortSet, gnu.trove.TShortCollection
        public boolean removeAll(TShortCollection collection) {
            if (this == collection) {
                clear();
                return true;
            }
            boolean changed = false;
            TShortIterator iter = collection.iterator();
            while (iter.hasNext()) {
                short element = iter.next();
                if (remove(element)) {
                    changed = true;
                }
            }
            return changed;
        }

        @Override // gnu.trove.set.TShortSet, gnu.trove.TShortCollection
        public boolean removeAll(short[] array) {
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

        @Override // gnu.trove.set.TShortSet, gnu.trove.TShortCollection
        public void clear() {
            TShortIntHashMap.this.clear();
        }

        @Override // gnu.trove.set.TShortSet, gnu.trove.TShortCollection
        public boolean forEach(TShortProcedure procedure) {
            return TShortIntHashMap.this.forEachKey(procedure);
        }

        @Override // gnu.trove.set.TShortSet, gnu.trove.TShortCollection
        public boolean equals(Object other) {
            if (!(other instanceof TShortSet)) {
                return false;
            }
            TShortSet that = (TShortSet) other;
            if (that.size() != size()) {
                return false;
            }
            int i = TShortIntHashMap.this._states.length;
            while (true) {
                int i2 = i;
                i--;
                if (i2 > 0) {
                    if (TShortIntHashMap.this._states[i] == 1 && !that.contains(TShortIntHashMap.this._set[i])) {
                        return false;
                    }
                } else {
                    return true;
                }
            }
        }

        @Override // gnu.trove.set.TShortSet, gnu.trove.TShortCollection
        public int hashCode() {
            int hashcode = 0;
            int i = TShortIntHashMap.this._states.length;
            while (true) {
                int i2 = i;
                i--;
                if (i2 > 0) {
                    if (TShortIntHashMap.this._states[i] == 1) {
                        hashcode += HashFunctions.hash((int) TShortIntHashMap.this._set[i]);
                    }
                } else {
                    return hashcode;
                }
            }
        }

        public String toString() {
            final StringBuilder buf = new StringBuilder("{");
            TShortIntHashMap.this.forEachKey(new TShortProcedure() { // from class: gnu.trove.map.hash.TShortIntHashMap.TKeyView.1
                private boolean first = true;

                @Override // gnu.trove.procedure.TShortProcedure
                public boolean execute(short key) {
                    if (this.first) {
                        this.first = false;
                    } else {
                        buf.append(", ");
                    }
                    buf.append((int) key);
                    return true;
                }
            });
            buf.append("}");
            return buf.toString();
        }
    }

    /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TShortIntHashMap$TValueView.class */
    protected class TValueView implements TIntCollection {
        protected TValueView() {
        }

        @Override // gnu.trove.TIntCollection
        public TIntIterator iterator() {
            return new TShortIntValueHashIterator(TShortIntHashMap.this);
        }

        @Override // gnu.trove.TIntCollection
        public int getNoEntryValue() {
            return TShortIntHashMap.this.no_entry_value;
        }

        @Override // gnu.trove.TIntCollection
        public int size() {
            return TShortIntHashMap.this._size;
        }

        @Override // gnu.trove.TIntCollection
        public boolean isEmpty() {
            return 0 == TShortIntHashMap.this._size;
        }

        @Override // gnu.trove.TIntCollection
        public boolean contains(int entry) {
            return TShortIntHashMap.this.containsValue(entry);
        }

        @Override // gnu.trove.TIntCollection
        public int[] toArray() {
            return TShortIntHashMap.this.values();
        }

        @Override // gnu.trove.TIntCollection
        public int[] toArray(int[] dest) {
            return TShortIntHashMap.this.values(dest);
        }

        @Override // gnu.trove.TIntCollection
        public boolean add(int entry) {
            throw new UnsupportedOperationException();
        }

        @Override // gnu.trove.TIntCollection
        public boolean remove(int entry) {
            int[] values = TShortIntHashMap.this._values;
            short[] set = TShortIntHashMap.this._set;
            int i = values.length;
            while (true) {
                int i2 = i;
                i--;
                if (i2 > 0) {
                    if (set[i] != 0 && set[i] != 2 && entry == values[i]) {
                        TShortIntHashMap.this.removeAt(i);
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
                    if (!TShortIntHashMap.this.containsValue(ele)) {
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
                if (!TShortIntHashMap.this.containsValue(iter.next())) {
                    return false;
                }
            }
            return true;
        }

        @Override // gnu.trove.TIntCollection
        public boolean containsAll(int[] array) {
            for (int element : array) {
                if (!TShortIntHashMap.this.containsValue(element)) {
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
            int[] values = TShortIntHashMap.this._values;
            byte[] states = TShortIntHashMap.this._states;
            int i = values.length;
            while (true) {
                int i2 = i;
                i--;
                if (i2 > 0) {
                    if (states[i] == 1 && Arrays.binarySearch(array, values[i]) < 0) {
                        TShortIntHashMap.this.removeAt(i);
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
            TShortIntHashMap.this.clear();
        }

        @Override // gnu.trove.TIntCollection
        public boolean forEach(TIntProcedure procedure) {
            return TShortIntHashMap.this.forEachValue(procedure);
        }

        public String toString() {
            final StringBuilder buf = new StringBuilder("{");
            TShortIntHashMap.this.forEachValue(new TIntProcedure() { // from class: gnu.trove.map.hash.TShortIntHashMap.TValueView.1
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
    /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TShortIntHashMap$TShortIntKeyHashIterator.class */
    public class TShortIntKeyHashIterator extends THashPrimitiveIterator implements TShortIterator {
        TShortIntKeyHashIterator(TPrimitiveHash hash) {
            super(hash);
        }

        @Override // gnu.trove.iterator.TShortIterator
        public short next() {
            moveToNextIndex();
            return TShortIntHashMap.this._set[this._index];
        }

        @Override // gnu.trove.impl.hash.THashPrimitiveIterator, gnu.trove.iterator.TPrimitiveIterator, gnu.trove.iterator.TIterator, java.util.Iterator
        public void remove() {
            if (this._expectedSize != this._hash.size()) {
                throw new ConcurrentModificationException();
            }
            try {
                this._hash.tempDisableAutoCompaction();
                TShortIntHashMap.this.removeAt(this._index);
                this._hash.reenableAutoCompaction(false);
                this._expectedSize--;
            } catch (Throwable th) {
                this._hash.reenableAutoCompaction(false);
                throw th;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TShortIntHashMap$TShortIntValueHashIterator.class */
    public class TShortIntValueHashIterator extends THashPrimitiveIterator implements TIntIterator {
        TShortIntValueHashIterator(TPrimitiveHash hash) {
            super(hash);
        }

        @Override // gnu.trove.iterator.TIntIterator
        public int next() {
            moveToNextIndex();
            return TShortIntHashMap.this._values[this._index];
        }

        @Override // gnu.trove.impl.hash.THashPrimitiveIterator, gnu.trove.iterator.TPrimitiveIterator, gnu.trove.iterator.TIterator, java.util.Iterator
        public void remove() {
            if (this._expectedSize != this._hash.size()) {
                throw new ConcurrentModificationException();
            }
            try {
                this._hash.tempDisableAutoCompaction();
                TShortIntHashMap.this.removeAt(this._index);
                this._hash.reenableAutoCompaction(false);
                this._expectedSize--;
            } catch (Throwable th) {
                this._hash.reenableAutoCompaction(false);
                throw th;
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TShortIntHashMap$TShortIntHashIterator.class */
    class TShortIntHashIterator extends THashPrimitiveIterator implements TShortIntIterator {
        TShortIntHashIterator(TShortIntHashMap map) {
            super(map);
        }

        @Override // gnu.trove.iterator.TAdvancingIterator
        public void advance() {
            moveToNextIndex();
        }

        @Override // gnu.trove.iterator.TShortIntIterator
        public short key() {
            return TShortIntHashMap.this._set[this._index];
        }

        @Override // gnu.trove.iterator.TShortIntIterator
        public int value() {
            return TShortIntHashMap.this._values[this._index];
        }

        @Override // gnu.trove.iterator.TShortIntIterator
        public int setValue(int val) {
            int old = value();
            TShortIntHashMap.this._values[this._index] = val;
            return old;
        }

        @Override // gnu.trove.impl.hash.THashPrimitiveIterator, gnu.trove.iterator.TPrimitiveIterator, gnu.trove.iterator.TIterator, java.util.Iterator
        public void remove() {
            if (this._expectedSize != this._hash.size()) {
                throw new ConcurrentModificationException();
            }
            try {
                this._hash.tempDisableAutoCompaction();
                TShortIntHashMap.this.removeAt(this._index);
                this._hash.reenableAutoCompaction(false);
                this._expectedSize--;
            } catch (Throwable th) {
                this._hash.reenableAutoCompaction(false);
                throw th;
            }
        }
    }

    public boolean equals(Object other) {
        if (!(other instanceof TShortIntMap)) {
            return false;
        }
        TShortIntMap that = (TShortIntMap) other;
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
                    short key = this._set[i];
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
                    hashcode += HashFunctions.hash((int) this._set[i]) ^ HashFunctions.hash(this._values[i]);
                }
            } else {
                return hashcode;
            }
        }
    }

    public String toString() {
        final StringBuilder buf = new StringBuilder("{");
        forEachEntry(new TShortIntProcedure() { // from class: gnu.trove.map.hash.TShortIntHashMap.1
            private boolean first = true;

            @Override // gnu.trove.procedure.TShortIntProcedure
            public boolean execute(short key, int value) {
                if (this.first) {
                    this.first = false;
                } else {
                    buf.append(", ");
                }
                buf.append((int) key);
                buf.append("=");
                buf.append(value);
                return true;
            }
        });
        buf.append("}");
        return buf.toString();
    }

    @Override // gnu.trove.impl.hash.TShortIntHash, gnu.trove.impl.hash.THash, java.io.Externalizable
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
                    out.writeShort(this._set[i]);
                    out.writeInt(this._values[i]);
                }
            } else {
                return;
            }
        }
    }

    @Override // gnu.trove.impl.hash.TShortIntHash, gnu.trove.impl.hash.THash, java.io.Externalizable
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        in.readByte();
        super.readExternal(in);
        int size = in.readInt();
        setUp(size);
        while (true) {
            int i = size;
            size--;
            if (i > 0) {
                short key = in.readShort();
                int val = in.readInt();
                put(key, val);
            } else {
                return;
            }
        }
    }
}
