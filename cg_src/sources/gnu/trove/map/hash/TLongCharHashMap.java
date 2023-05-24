package gnu.trove.map.hash;

import gnu.trove.TCharCollection;
import gnu.trove.TLongCollection;
import gnu.trove.function.TCharFunction;
import gnu.trove.impl.HashFunctions;
import gnu.trove.impl.hash.THashPrimitiveIterator;
import gnu.trove.impl.hash.TLongCharHash;
import gnu.trove.impl.hash.TPrimitiveHash;
import gnu.trove.iterator.TCharIterator;
import gnu.trove.iterator.TLongCharIterator;
import gnu.trove.iterator.TLongIterator;
import gnu.trove.map.TLongCharMap;
import gnu.trove.procedure.TCharProcedure;
import gnu.trove.procedure.TLongCharProcedure;
import gnu.trove.procedure.TLongProcedure;
import gnu.trove.set.TLongSet;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Arrays;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TLongCharHashMap.class */
public class TLongCharHashMap extends TLongCharHash implements TLongCharMap, Externalizable {
    static final long serialVersionUID = 1;
    protected transient char[] _values;

    public TLongCharHashMap() {
    }

    public TLongCharHashMap(int initialCapacity) {
        super(initialCapacity);
    }

    public TLongCharHashMap(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    public TLongCharHashMap(int initialCapacity, float loadFactor, long noEntryKey, char noEntryValue) {
        super(initialCapacity, loadFactor, noEntryKey, noEntryValue);
    }

    public TLongCharHashMap(long[] keys, char[] values) {
        super(Math.max(keys.length, values.length));
        int size = Math.min(keys.length, values.length);
        for (int i = 0; i < size; i++) {
            put(keys[i], values[i]);
        }
    }

    public TLongCharHashMap(TLongCharMap map) {
        super(map.size());
        if (map instanceof TLongCharHashMap) {
            TLongCharHashMap hashmap = (TLongCharHashMap) map;
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
    @Override // gnu.trove.impl.hash.TLongCharHash, gnu.trove.impl.hash.TPrimitiveHash, gnu.trove.impl.hash.THash
    public int setUp(int initialCapacity) {
        int capacity = super.setUp(initialCapacity);
        this._values = new char[capacity];
        return capacity;
    }

    @Override // gnu.trove.impl.hash.THash
    protected void rehash(int newCapacity) {
        int oldCapacity = this._set.length;
        long[] oldKeys = this._set;
        char[] oldVals = this._values;
        byte[] oldStates = this._states;
        this._set = new long[newCapacity];
        this._values = new char[newCapacity];
        this._states = new byte[newCapacity];
        int i = oldCapacity;
        while (true) {
            int i2 = i;
            i--;
            if (i2 > 0) {
                if (oldStates[i] == 1) {
                    long o = oldKeys[i];
                    int index = insertKey(o);
                    this._values[index] = oldVals[i];
                }
            } else {
                return;
            }
        }
    }

    @Override // gnu.trove.map.TLongCharMap
    public char put(long key, char value) {
        int index = insertKey(key);
        return doPut(key, value, index);
    }

    @Override // gnu.trove.map.TLongCharMap
    public char putIfAbsent(long key, char value) {
        int index = insertKey(key);
        if (index < 0) {
            return this._values[(-index) - 1];
        }
        return doPut(key, value, index);
    }

    private char doPut(long key, char value, int index) {
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

    @Override // gnu.trove.map.TLongCharMap
    public void putAll(Map<? extends Long, ? extends Character> map) {
        ensureCapacity(map.size());
        for (Map.Entry<? extends Long, ? extends Character> entry : map.entrySet()) {
            put(entry.getKey().longValue(), entry.getValue().charValue());
        }
    }

    @Override // gnu.trove.map.TLongCharMap
    public void putAll(TLongCharMap map) {
        ensureCapacity(map.size());
        TLongCharIterator iter = map.iterator();
        while (iter.hasNext()) {
            iter.advance();
            put(iter.key(), iter.value());
        }
    }

    @Override // gnu.trove.map.TLongCharMap
    public char get(long key) {
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

    @Override // gnu.trove.map.TLongCharMap
    public char remove(long key) {
        char prev = this.no_entry_value;
        int index = index(key);
        if (index >= 0) {
            prev = this._values[index];
            removeAt(index);
        }
        return prev;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // gnu.trove.impl.hash.TLongCharHash, gnu.trove.impl.hash.TPrimitiveHash, gnu.trove.impl.hash.THash
    public void removeAt(int index) {
        this._values[index] = this.no_entry_value;
        super.removeAt(index);
    }

    @Override // gnu.trove.map.TLongCharMap
    public TLongSet keySet() {
        return new TKeyView();
    }

    @Override // gnu.trove.map.TLongCharMap
    public long[] keys() {
        long[] keys = new long[size()];
        long[] k = this._set;
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

    @Override // gnu.trove.map.TLongCharMap
    public long[] keys(long[] array) {
        int size = size();
        if (array.length < size) {
            array = new long[size];
        }
        long[] keys = this._set;
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

    @Override // gnu.trove.map.TLongCharMap
    public TCharCollection valueCollection() {
        return new TValueView();
    }

    @Override // gnu.trove.map.TLongCharMap
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

    @Override // gnu.trove.map.TLongCharMap
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

    @Override // gnu.trove.map.TLongCharMap
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

    @Override // gnu.trove.map.TLongCharMap
    public boolean containsKey(long key) {
        return contains(key);
    }

    @Override // gnu.trove.map.TLongCharMap
    public TLongCharIterator iterator() {
        return new TLongCharHashIterator(this);
    }

    @Override // gnu.trove.map.TLongCharMap
    public boolean forEachKey(TLongProcedure procedure) {
        return forEach(procedure);
    }

    @Override // gnu.trove.map.TLongCharMap
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

    @Override // gnu.trove.map.TLongCharMap
    public boolean forEachEntry(TLongCharProcedure procedure) {
        byte[] states = this._states;
        long[] keys = this._set;
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

    @Override // gnu.trove.map.TLongCharMap
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

    @Override // gnu.trove.map.TLongCharMap
    public boolean retainEntries(TLongCharProcedure procedure) {
        boolean modified = false;
        byte[] states = this._states;
        long[] keys = this._set;
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

    @Override // gnu.trove.map.TLongCharMap
    public boolean increment(long key) {
        return adjustValue(key, (char) 1);
    }

    @Override // gnu.trove.map.TLongCharMap
    public boolean adjustValue(long key, char amount) {
        int index = index(key);
        if (index < 0) {
            return false;
        }
        char[] cArr = this._values;
        cArr[index] = (char) (cArr[index] + amount);
        return true;
    }

    @Override // gnu.trove.map.TLongCharMap
    public char adjustOrPutValue(long key, char adjust_amount, char put_amount) {
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

    /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TLongCharHashMap$TKeyView.class */
    protected class TKeyView implements TLongSet {
        protected TKeyView() {
        }

        @Override // gnu.trove.set.TLongSet, gnu.trove.TLongCollection
        public TLongIterator iterator() {
            return new TLongCharKeyHashIterator(TLongCharHashMap.this);
        }

        @Override // gnu.trove.set.TLongSet, gnu.trove.TLongCollection
        public long getNoEntryValue() {
            return TLongCharHashMap.this.no_entry_key;
        }

        @Override // gnu.trove.set.TLongSet, gnu.trove.TLongCollection
        public int size() {
            return TLongCharHashMap.this._size;
        }

        @Override // gnu.trove.set.TLongSet, gnu.trove.TLongCollection
        public boolean isEmpty() {
            return 0 == TLongCharHashMap.this._size;
        }

        @Override // gnu.trove.set.TLongSet, gnu.trove.TLongCollection
        public boolean contains(long entry) {
            return TLongCharHashMap.this.contains(entry);
        }

        @Override // gnu.trove.set.TLongSet, gnu.trove.TLongCollection
        public long[] toArray() {
            return TLongCharHashMap.this.keys();
        }

        @Override // gnu.trove.set.TLongSet, gnu.trove.TLongCollection
        public long[] toArray(long[] dest) {
            return TLongCharHashMap.this.keys(dest);
        }

        @Override // gnu.trove.set.TLongSet, gnu.trove.TLongCollection
        public boolean add(long entry) {
            throw new UnsupportedOperationException();
        }

        @Override // gnu.trove.set.TLongSet, gnu.trove.TLongCollection
        public boolean remove(long entry) {
            return TLongCharHashMap.this.no_entry_value != TLongCharHashMap.this.remove(entry);
        }

        @Override // gnu.trove.set.TLongSet, gnu.trove.TLongCollection
        public boolean containsAll(Collection<?> collection) {
            for (Object element : collection) {
                if (element instanceof Long) {
                    long ele = ((Long) element).longValue();
                    if (!TLongCharHashMap.this.containsKey(ele)) {
                        return false;
                    }
                } else {
                    return false;
                }
            }
            return true;
        }

        @Override // gnu.trove.set.TLongSet, gnu.trove.TLongCollection
        public boolean containsAll(TLongCollection collection) {
            TLongIterator iter = collection.iterator();
            while (iter.hasNext()) {
                if (!TLongCharHashMap.this.containsKey(iter.next())) {
                    return false;
                }
            }
            return true;
        }

        @Override // gnu.trove.set.TLongSet, gnu.trove.TLongCollection
        public boolean containsAll(long[] array) {
            for (long element : array) {
                if (!TLongCharHashMap.this.contains(element)) {
                    return false;
                }
            }
            return true;
        }

        @Override // gnu.trove.set.TLongSet, gnu.trove.TLongCollection
        public boolean addAll(Collection<? extends Long> collection) {
            throw new UnsupportedOperationException();
        }

        @Override // gnu.trove.set.TLongSet, gnu.trove.TLongCollection
        public boolean addAll(TLongCollection collection) {
            throw new UnsupportedOperationException();
        }

        @Override // gnu.trove.set.TLongSet, gnu.trove.TLongCollection
        public boolean addAll(long[] array) {
            throw new UnsupportedOperationException();
        }

        @Override // gnu.trove.set.TLongSet, gnu.trove.TLongCollection
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

        @Override // gnu.trove.set.TLongSet, gnu.trove.TLongCollection
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

        @Override // gnu.trove.set.TLongSet, gnu.trove.TLongCollection
        public boolean retainAll(long[] array) {
            boolean changed = false;
            Arrays.sort(array);
            long[] set = TLongCharHashMap.this._set;
            byte[] states = TLongCharHashMap.this._states;
            int i = set.length;
            while (true) {
                int i2 = i;
                i--;
                if (i2 > 0) {
                    if (states[i] == 1 && Arrays.binarySearch(array, set[i]) < 0) {
                        TLongCharHashMap.this.removeAt(i);
                        changed = true;
                    }
                } else {
                    return changed;
                }
            }
        }

        @Override // gnu.trove.set.TLongSet, gnu.trove.TLongCollection
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

        @Override // gnu.trove.set.TLongSet, gnu.trove.TLongCollection
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

        @Override // gnu.trove.set.TLongSet, gnu.trove.TLongCollection
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

        @Override // gnu.trove.set.TLongSet, gnu.trove.TLongCollection
        public void clear() {
            TLongCharHashMap.this.clear();
        }

        @Override // gnu.trove.set.TLongSet, gnu.trove.TLongCollection
        public boolean forEach(TLongProcedure procedure) {
            return TLongCharHashMap.this.forEachKey(procedure);
        }

        @Override // gnu.trove.set.TLongSet, gnu.trove.TLongCollection
        public boolean equals(Object other) {
            if (!(other instanceof TLongSet)) {
                return false;
            }
            TLongSet that = (TLongSet) other;
            if (that.size() != size()) {
                return false;
            }
            int i = TLongCharHashMap.this._states.length;
            while (true) {
                int i2 = i;
                i--;
                if (i2 > 0) {
                    if (TLongCharHashMap.this._states[i] == 1 && !that.contains(TLongCharHashMap.this._set[i])) {
                        return false;
                    }
                } else {
                    return true;
                }
            }
        }

        @Override // gnu.trove.set.TLongSet, gnu.trove.TLongCollection
        public int hashCode() {
            int hashcode = 0;
            int i = TLongCharHashMap.this._states.length;
            while (true) {
                int i2 = i;
                i--;
                if (i2 > 0) {
                    if (TLongCharHashMap.this._states[i] == 1) {
                        hashcode += HashFunctions.hash(TLongCharHashMap.this._set[i]);
                    }
                } else {
                    return hashcode;
                }
            }
        }

        public String toString() {
            final StringBuilder buf = new StringBuilder("{");
            TLongCharHashMap.this.forEachKey(new TLongProcedure() { // from class: gnu.trove.map.hash.TLongCharHashMap.TKeyView.1
                private boolean first = true;

                @Override // gnu.trove.procedure.TLongProcedure
                public boolean execute(long key) {
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

    /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TLongCharHashMap$TValueView.class */
    protected class TValueView implements TCharCollection {
        protected TValueView() {
        }

        @Override // gnu.trove.TCharCollection
        public TCharIterator iterator() {
            return new TLongCharValueHashIterator(TLongCharHashMap.this);
        }

        @Override // gnu.trove.TCharCollection
        public char getNoEntryValue() {
            return TLongCharHashMap.this.no_entry_value;
        }

        @Override // gnu.trove.TCharCollection
        public int size() {
            return TLongCharHashMap.this._size;
        }

        @Override // gnu.trove.TCharCollection
        public boolean isEmpty() {
            return 0 == TLongCharHashMap.this._size;
        }

        @Override // gnu.trove.TCharCollection
        public boolean contains(char entry) {
            return TLongCharHashMap.this.containsValue(entry);
        }

        @Override // gnu.trove.TCharCollection
        public char[] toArray() {
            return TLongCharHashMap.this.values();
        }

        @Override // gnu.trove.TCharCollection
        public char[] toArray(char[] dest) {
            return TLongCharHashMap.this.values(dest);
        }

        @Override // gnu.trove.TCharCollection
        public boolean add(char entry) {
            throw new UnsupportedOperationException();
        }

        @Override // gnu.trove.TCharCollection
        public boolean remove(char entry) {
            char[] values = TLongCharHashMap.this._values;
            long[] set = TLongCharHashMap.this._set;
            int i = values.length;
            while (true) {
                int i2 = i;
                i--;
                if (i2 > 0) {
                    if (set[i] != 0 && set[i] != 2 && entry == values[i]) {
                        TLongCharHashMap.this.removeAt(i);
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
                    if (!TLongCharHashMap.this.containsValue(ele)) {
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
                if (!TLongCharHashMap.this.containsValue(iter.next())) {
                    return false;
                }
            }
            return true;
        }

        @Override // gnu.trove.TCharCollection
        public boolean containsAll(char[] array) {
            for (char element : array) {
                if (!TLongCharHashMap.this.containsValue(element)) {
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
            char[] values = TLongCharHashMap.this._values;
            byte[] states = TLongCharHashMap.this._states;
            int i = values.length;
            while (true) {
                int i2 = i;
                i--;
                if (i2 > 0) {
                    if (states[i] == 1 && Arrays.binarySearch(array, values[i]) < 0) {
                        TLongCharHashMap.this.removeAt(i);
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
            TLongCharHashMap.this.clear();
        }

        @Override // gnu.trove.TCharCollection
        public boolean forEach(TCharProcedure procedure) {
            return TLongCharHashMap.this.forEachValue(procedure);
        }

        public String toString() {
            final StringBuilder buf = new StringBuilder("{");
            TLongCharHashMap.this.forEachValue(new TCharProcedure() { // from class: gnu.trove.map.hash.TLongCharHashMap.TValueView.1
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
    /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TLongCharHashMap$TLongCharKeyHashIterator.class */
    public class TLongCharKeyHashIterator extends THashPrimitiveIterator implements TLongIterator {
        TLongCharKeyHashIterator(TPrimitiveHash hash) {
            super(hash);
        }

        @Override // gnu.trove.iterator.TLongIterator
        public long next() {
            moveToNextIndex();
            return TLongCharHashMap.this._set[this._index];
        }

        @Override // gnu.trove.impl.hash.THashPrimitiveIterator, gnu.trove.iterator.TPrimitiveIterator, gnu.trove.iterator.TIterator, java.util.Iterator
        public void remove() {
            if (this._expectedSize != this._hash.size()) {
                throw new ConcurrentModificationException();
            }
            try {
                this._hash.tempDisableAutoCompaction();
                TLongCharHashMap.this.removeAt(this._index);
                this._hash.reenableAutoCompaction(false);
                this._expectedSize--;
            } catch (Throwable th) {
                this._hash.reenableAutoCompaction(false);
                throw th;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TLongCharHashMap$TLongCharValueHashIterator.class */
    public class TLongCharValueHashIterator extends THashPrimitiveIterator implements TCharIterator {
        TLongCharValueHashIterator(TPrimitiveHash hash) {
            super(hash);
        }

        @Override // gnu.trove.iterator.TCharIterator
        public char next() {
            moveToNextIndex();
            return TLongCharHashMap.this._values[this._index];
        }

        @Override // gnu.trove.impl.hash.THashPrimitiveIterator, gnu.trove.iterator.TPrimitiveIterator, gnu.trove.iterator.TIterator, java.util.Iterator
        public void remove() {
            if (this._expectedSize != this._hash.size()) {
                throw new ConcurrentModificationException();
            }
            try {
                this._hash.tempDisableAutoCompaction();
                TLongCharHashMap.this.removeAt(this._index);
                this._hash.reenableAutoCompaction(false);
                this._expectedSize--;
            } catch (Throwable th) {
                this._hash.reenableAutoCompaction(false);
                throw th;
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TLongCharHashMap$TLongCharHashIterator.class */
    class TLongCharHashIterator extends THashPrimitiveIterator implements TLongCharIterator {
        TLongCharHashIterator(TLongCharHashMap map) {
            super(map);
        }

        @Override // gnu.trove.iterator.TAdvancingIterator
        public void advance() {
            moveToNextIndex();
        }

        @Override // gnu.trove.iterator.TLongCharIterator
        public long key() {
            return TLongCharHashMap.this._set[this._index];
        }

        @Override // gnu.trove.iterator.TLongCharIterator
        public char value() {
            return TLongCharHashMap.this._values[this._index];
        }

        @Override // gnu.trove.iterator.TLongCharIterator
        public char setValue(char val) {
            char old = value();
            TLongCharHashMap.this._values[this._index] = val;
            return old;
        }

        @Override // gnu.trove.impl.hash.THashPrimitiveIterator, gnu.trove.iterator.TPrimitiveIterator, gnu.trove.iterator.TIterator, java.util.Iterator
        public void remove() {
            if (this._expectedSize != this._hash.size()) {
                throw new ConcurrentModificationException();
            }
            try {
                this._hash.tempDisableAutoCompaction();
                TLongCharHashMap.this.removeAt(this._index);
                this._hash.reenableAutoCompaction(false);
                this._expectedSize--;
            } catch (Throwable th) {
                this._hash.reenableAutoCompaction(false);
                throw th;
            }
        }
    }

    public boolean equals(Object other) {
        if (!(other instanceof TLongCharMap)) {
            return false;
        }
        TLongCharMap that = (TLongCharMap) other;
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
                    long key = this._set[i];
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
        forEachEntry(new TLongCharProcedure() { // from class: gnu.trove.map.hash.TLongCharHashMap.1
            private boolean first = true;

            @Override // gnu.trove.procedure.TLongCharProcedure
            public boolean execute(long key, char value) {
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

    @Override // gnu.trove.impl.hash.TLongCharHash, gnu.trove.impl.hash.THash, java.io.Externalizable
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
                    out.writeLong(this._set[i]);
                    out.writeChar(this._values[i]);
                }
            } else {
                return;
            }
        }
    }

    @Override // gnu.trove.impl.hash.TLongCharHash, gnu.trove.impl.hash.THash, java.io.Externalizable
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        in.readByte();
        super.readExternal(in);
        int size = in.readInt();
        setUp(size);
        while (true) {
            int i = size;
            size--;
            if (i > 0) {
                long key = in.readLong();
                char val = in.readChar();
                put(key, val);
            } else {
                return;
            }
        }
    }
}
