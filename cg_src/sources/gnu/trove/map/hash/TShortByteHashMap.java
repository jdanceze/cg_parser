package gnu.trove.map.hash;

import gnu.trove.TByteCollection;
import gnu.trove.TShortCollection;
import gnu.trove.function.TByteFunction;
import gnu.trove.impl.HashFunctions;
import gnu.trove.impl.hash.THashPrimitiveIterator;
import gnu.trove.impl.hash.TPrimitiveHash;
import gnu.trove.impl.hash.TShortByteHash;
import gnu.trove.iterator.TByteIterator;
import gnu.trove.iterator.TShortByteIterator;
import gnu.trove.iterator.TShortIterator;
import gnu.trove.map.TShortByteMap;
import gnu.trove.procedure.TByteProcedure;
import gnu.trove.procedure.TShortByteProcedure;
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
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TShortByteHashMap.class */
public class TShortByteHashMap extends TShortByteHash implements TShortByteMap, Externalizable {
    static final long serialVersionUID = 1;
    protected transient byte[] _values;

    public TShortByteHashMap() {
    }

    public TShortByteHashMap(int initialCapacity) {
        super(initialCapacity);
    }

    public TShortByteHashMap(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    public TShortByteHashMap(int initialCapacity, float loadFactor, short noEntryKey, byte noEntryValue) {
        super(initialCapacity, loadFactor, noEntryKey, noEntryValue);
    }

    public TShortByteHashMap(short[] keys, byte[] values) {
        super(Math.max(keys.length, values.length));
        int size = Math.min(keys.length, values.length);
        for (int i = 0; i < size; i++) {
            put(keys[i], values[i]);
        }
    }

    public TShortByteHashMap(TShortByteMap map) {
        super(map.size());
        if (map instanceof TShortByteHashMap) {
            TShortByteHashMap hashmap = (TShortByteHashMap) map;
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
    @Override // gnu.trove.impl.hash.TShortByteHash, gnu.trove.impl.hash.TPrimitiveHash, gnu.trove.impl.hash.THash
    public int setUp(int initialCapacity) {
        int capacity = super.setUp(initialCapacity);
        this._values = new byte[capacity];
        return capacity;
    }

    @Override // gnu.trove.impl.hash.THash
    protected void rehash(int newCapacity) {
        int oldCapacity = this._set.length;
        short[] oldKeys = this._set;
        byte[] oldVals = this._values;
        byte[] oldStates = this._states;
        this._set = new short[newCapacity];
        this._values = new byte[newCapacity];
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

    @Override // gnu.trove.map.TShortByteMap
    public byte put(short key, byte value) {
        int index = insertKey(key);
        return doPut(key, value, index);
    }

    @Override // gnu.trove.map.TShortByteMap
    public byte putIfAbsent(short key, byte value) {
        int index = insertKey(key);
        if (index < 0) {
            return this._values[(-index) - 1];
        }
        return doPut(key, value, index);
    }

    private byte doPut(short key, byte value, int index) {
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

    @Override // gnu.trove.map.TShortByteMap
    public void putAll(Map<? extends Short, ? extends Byte> map) {
        ensureCapacity(map.size());
        for (Map.Entry<? extends Short, ? extends Byte> entry : map.entrySet()) {
            put(entry.getKey().shortValue(), entry.getValue().byteValue());
        }
    }

    @Override // gnu.trove.map.TShortByteMap
    public void putAll(TShortByteMap map) {
        ensureCapacity(map.size());
        TShortByteIterator iter = map.iterator();
        while (iter.hasNext()) {
            iter.advance();
            put(iter.key(), iter.value());
        }
    }

    @Override // gnu.trove.map.TShortByteMap
    public byte get(short key) {
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

    @Override // gnu.trove.map.TShortByteMap
    public byte remove(short key) {
        byte prev = this.no_entry_value;
        int index = index(key);
        if (index >= 0) {
            prev = this._values[index];
            removeAt(index);
        }
        return prev;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // gnu.trove.impl.hash.TShortByteHash, gnu.trove.impl.hash.TPrimitiveHash, gnu.trove.impl.hash.THash
    public void removeAt(int index) {
        this._values[index] = this.no_entry_value;
        super.removeAt(index);
    }

    @Override // gnu.trove.map.TShortByteMap
    public TShortSet keySet() {
        return new TKeyView();
    }

    @Override // gnu.trove.map.TShortByteMap
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

    @Override // gnu.trove.map.TShortByteMap
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

    @Override // gnu.trove.map.TShortByteMap
    public TByteCollection valueCollection() {
        return new TValueView();
    }

    @Override // gnu.trove.map.TShortByteMap
    public byte[] values() {
        byte[] vals = new byte[size()];
        byte[] v = this._values;
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

    @Override // gnu.trove.map.TShortByteMap
    public byte[] values(byte[] array) {
        int size = size();
        if (array.length < size) {
            array = new byte[size];
        }
        byte[] v = this._values;
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

    @Override // gnu.trove.map.TShortByteMap
    public boolean containsValue(byte val) {
        byte[] states = this._states;
        byte[] vals = this._values;
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

    @Override // gnu.trove.map.TShortByteMap
    public boolean containsKey(short key) {
        return contains(key);
    }

    @Override // gnu.trove.map.TShortByteMap
    public TShortByteIterator iterator() {
        return new TShortByteHashIterator(this);
    }

    @Override // gnu.trove.map.TShortByteMap
    public boolean forEachKey(TShortProcedure procedure) {
        return forEach(procedure);
    }

    @Override // gnu.trove.map.TShortByteMap
    public boolean forEachValue(TByteProcedure procedure) {
        byte[] states = this._states;
        byte[] values = this._values;
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

    @Override // gnu.trove.map.TShortByteMap
    public boolean forEachEntry(TShortByteProcedure procedure) {
        byte[] states = this._states;
        short[] keys = this._set;
        byte[] values = this._values;
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

    @Override // gnu.trove.map.TShortByteMap
    public void transformValues(TByteFunction function) {
        byte[] states = this._states;
        byte[] values = this._values;
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

    @Override // gnu.trove.map.TShortByteMap
    public boolean retainEntries(TShortByteProcedure procedure) {
        boolean modified = false;
        byte[] states = this._states;
        short[] keys = this._set;
        byte[] values = this._values;
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

    @Override // gnu.trove.map.TShortByteMap
    public boolean increment(short key) {
        return adjustValue(key, (byte) 1);
    }

    @Override // gnu.trove.map.TShortByteMap
    public boolean adjustValue(short key, byte amount) {
        int index = index(key);
        if (index < 0) {
            return false;
        }
        byte[] bArr = this._values;
        bArr[index] = (byte) (bArr[index] + amount);
        return true;
    }

    @Override // gnu.trove.map.TShortByteMap
    public byte adjustOrPutValue(short key, byte adjust_amount, byte put_amount) {
        byte newValue;
        boolean isNewMapping;
        int index = insertKey(key);
        if (index < 0) {
            index = (-index) - 1;
            byte[] bArr = this._values;
            byte b = (byte) (bArr[index] + adjust_amount);
            bArr[index] = b;
            newValue = b;
            isNewMapping = false;
        } else {
            this._values[index] = put_amount;
            newValue = put_amount;
            isNewMapping = true;
        }
        byte b2 = this._states[index];
        if (isNewMapping) {
            postInsertHook(this.consumeFreeSlot);
        }
        return newValue;
    }

    /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TShortByteHashMap$TKeyView.class */
    protected class TKeyView implements TShortSet {
        protected TKeyView() {
        }

        @Override // gnu.trove.set.TShortSet, gnu.trove.TShortCollection
        public TShortIterator iterator() {
            return new TShortByteKeyHashIterator(TShortByteHashMap.this);
        }

        @Override // gnu.trove.set.TShortSet, gnu.trove.TShortCollection
        public short getNoEntryValue() {
            return TShortByteHashMap.this.no_entry_key;
        }

        @Override // gnu.trove.set.TShortSet, gnu.trove.TShortCollection
        public int size() {
            return TShortByteHashMap.this._size;
        }

        @Override // gnu.trove.set.TShortSet, gnu.trove.TShortCollection
        public boolean isEmpty() {
            return 0 == TShortByteHashMap.this._size;
        }

        @Override // gnu.trove.set.TShortSet, gnu.trove.TShortCollection
        public boolean contains(short entry) {
            return TShortByteHashMap.this.contains(entry);
        }

        @Override // gnu.trove.set.TShortSet, gnu.trove.TShortCollection
        public short[] toArray() {
            return TShortByteHashMap.this.keys();
        }

        @Override // gnu.trove.set.TShortSet, gnu.trove.TShortCollection
        public short[] toArray(short[] dest) {
            return TShortByteHashMap.this.keys(dest);
        }

        @Override // gnu.trove.set.TShortSet, gnu.trove.TShortCollection
        public boolean add(short entry) {
            throw new UnsupportedOperationException();
        }

        @Override // gnu.trove.set.TShortSet, gnu.trove.TShortCollection
        public boolean remove(short entry) {
            return TShortByteHashMap.this.no_entry_value != TShortByteHashMap.this.remove(entry);
        }

        @Override // gnu.trove.set.TShortSet, gnu.trove.TShortCollection
        public boolean containsAll(Collection<?> collection) {
            for (Object element : collection) {
                if (element instanceof Short) {
                    short ele = ((Short) element).shortValue();
                    if (!TShortByteHashMap.this.containsKey(ele)) {
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
                if (!TShortByteHashMap.this.containsKey(iter.next())) {
                    return false;
                }
            }
            return true;
        }

        @Override // gnu.trove.set.TShortSet, gnu.trove.TShortCollection
        public boolean containsAll(short[] array) {
            for (short element : array) {
                if (!TShortByteHashMap.this.contains(element)) {
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
            short[] set = TShortByteHashMap.this._set;
            byte[] states = TShortByteHashMap.this._states;
            int i = set.length;
            while (true) {
                int i2 = i;
                i--;
                if (i2 > 0) {
                    if (states[i] == 1 && Arrays.binarySearch(array, set[i]) < 0) {
                        TShortByteHashMap.this.removeAt(i);
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
            TShortByteHashMap.this.clear();
        }

        @Override // gnu.trove.set.TShortSet, gnu.trove.TShortCollection
        public boolean forEach(TShortProcedure procedure) {
            return TShortByteHashMap.this.forEachKey(procedure);
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
            int i = TShortByteHashMap.this._states.length;
            while (true) {
                int i2 = i;
                i--;
                if (i2 > 0) {
                    if (TShortByteHashMap.this._states[i] == 1 && !that.contains(TShortByteHashMap.this._set[i])) {
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
            int i = TShortByteHashMap.this._states.length;
            while (true) {
                int i2 = i;
                i--;
                if (i2 > 0) {
                    if (TShortByteHashMap.this._states[i] == 1) {
                        hashcode += HashFunctions.hash((int) TShortByteHashMap.this._set[i]);
                    }
                } else {
                    return hashcode;
                }
            }
        }

        public String toString() {
            final StringBuilder buf = new StringBuilder("{");
            TShortByteHashMap.this.forEachKey(new TShortProcedure() { // from class: gnu.trove.map.hash.TShortByteHashMap.TKeyView.1
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

    /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TShortByteHashMap$TValueView.class */
    protected class TValueView implements TByteCollection {
        protected TValueView() {
        }

        @Override // gnu.trove.TByteCollection
        public TByteIterator iterator() {
            return new TShortByteValueHashIterator(TShortByteHashMap.this);
        }

        @Override // gnu.trove.TByteCollection
        public byte getNoEntryValue() {
            return TShortByteHashMap.this.no_entry_value;
        }

        @Override // gnu.trove.TByteCollection
        public int size() {
            return TShortByteHashMap.this._size;
        }

        @Override // gnu.trove.TByteCollection
        public boolean isEmpty() {
            return 0 == TShortByteHashMap.this._size;
        }

        @Override // gnu.trove.TByteCollection
        public boolean contains(byte entry) {
            return TShortByteHashMap.this.containsValue(entry);
        }

        @Override // gnu.trove.TByteCollection
        public byte[] toArray() {
            return TShortByteHashMap.this.values();
        }

        @Override // gnu.trove.TByteCollection
        public byte[] toArray(byte[] dest) {
            return TShortByteHashMap.this.values(dest);
        }

        @Override // gnu.trove.TByteCollection
        public boolean add(byte entry) {
            throw new UnsupportedOperationException();
        }

        @Override // gnu.trove.TByteCollection
        public boolean remove(byte entry) {
            byte[] values = TShortByteHashMap.this._values;
            short[] set = TShortByteHashMap.this._set;
            int i = values.length;
            while (true) {
                int i2 = i;
                i--;
                if (i2 > 0) {
                    if (set[i] != 0 && set[i] != 2 && entry == values[i]) {
                        TShortByteHashMap.this.removeAt(i);
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
                    if (!TShortByteHashMap.this.containsValue(ele)) {
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
                if (!TShortByteHashMap.this.containsValue(iter.next())) {
                    return false;
                }
            }
            return true;
        }

        @Override // gnu.trove.TByteCollection
        public boolean containsAll(byte[] array) {
            for (byte element : array) {
                if (!TShortByteHashMap.this.containsValue(element)) {
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
            byte[] values = TShortByteHashMap.this._values;
            byte[] states = TShortByteHashMap.this._states;
            int i = values.length;
            while (true) {
                int i2 = i;
                i--;
                if (i2 > 0) {
                    if (states[i] == 1 && Arrays.binarySearch(array, values[i]) < 0) {
                        TShortByteHashMap.this.removeAt(i);
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
            TShortByteHashMap.this.clear();
        }

        @Override // gnu.trove.TByteCollection
        public boolean forEach(TByteProcedure procedure) {
            return TShortByteHashMap.this.forEachValue(procedure);
        }

        public String toString() {
            final StringBuilder buf = new StringBuilder("{");
            TShortByteHashMap.this.forEachValue(new TByteProcedure() { // from class: gnu.trove.map.hash.TShortByteHashMap.TValueView.1
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
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TShortByteHashMap$TShortByteKeyHashIterator.class */
    public class TShortByteKeyHashIterator extends THashPrimitiveIterator implements TShortIterator {
        TShortByteKeyHashIterator(TPrimitiveHash hash) {
            super(hash);
        }

        @Override // gnu.trove.iterator.TShortIterator
        public short next() {
            moveToNextIndex();
            return TShortByteHashMap.this._set[this._index];
        }

        @Override // gnu.trove.impl.hash.THashPrimitiveIterator, gnu.trove.iterator.TPrimitiveIterator, gnu.trove.iterator.TIterator, java.util.Iterator
        public void remove() {
            if (this._expectedSize != this._hash.size()) {
                throw new ConcurrentModificationException();
            }
            try {
                this._hash.tempDisableAutoCompaction();
                TShortByteHashMap.this.removeAt(this._index);
                this._hash.reenableAutoCompaction(false);
                this._expectedSize--;
            } catch (Throwable th) {
                this._hash.reenableAutoCompaction(false);
                throw th;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TShortByteHashMap$TShortByteValueHashIterator.class */
    public class TShortByteValueHashIterator extends THashPrimitiveIterator implements TByteIterator {
        TShortByteValueHashIterator(TPrimitiveHash hash) {
            super(hash);
        }

        @Override // gnu.trove.iterator.TByteIterator
        public byte next() {
            moveToNextIndex();
            return TShortByteHashMap.this._values[this._index];
        }

        @Override // gnu.trove.impl.hash.THashPrimitiveIterator, gnu.trove.iterator.TPrimitiveIterator, gnu.trove.iterator.TIterator, java.util.Iterator
        public void remove() {
            if (this._expectedSize != this._hash.size()) {
                throw new ConcurrentModificationException();
            }
            try {
                this._hash.tempDisableAutoCompaction();
                TShortByteHashMap.this.removeAt(this._index);
                this._hash.reenableAutoCompaction(false);
                this._expectedSize--;
            } catch (Throwable th) {
                this._hash.reenableAutoCompaction(false);
                throw th;
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TShortByteHashMap$TShortByteHashIterator.class */
    class TShortByteHashIterator extends THashPrimitiveIterator implements TShortByteIterator {
        TShortByteHashIterator(TShortByteHashMap map) {
            super(map);
        }

        @Override // gnu.trove.iterator.TAdvancingIterator
        public void advance() {
            moveToNextIndex();
        }

        @Override // gnu.trove.iterator.TShortByteIterator
        public short key() {
            return TShortByteHashMap.this._set[this._index];
        }

        @Override // gnu.trove.iterator.TShortByteIterator
        public byte value() {
            return TShortByteHashMap.this._values[this._index];
        }

        @Override // gnu.trove.iterator.TShortByteIterator
        public byte setValue(byte val) {
            byte old = value();
            TShortByteHashMap.this._values[this._index] = val;
            return old;
        }

        @Override // gnu.trove.impl.hash.THashPrimitiveIterator, gnu.trove.iterator.TPrimitiveIterator, gnu.trove.iterator.TIterator, java.util.Iterator
        public void remove() {
            if (this._expectedSize != this._hash.size()) {
                throw new ConcurrentModificationException();
            }
            try {
                this._hash.tempDisableAutoCompaction();
                TShortByteHashMap.this.removeAt(this._index);
                this._hash.reenableAutoCompaction(false);
                this._expectedSize--;
            } catch (Throwable th) {
                this._hash.reenableAutoCompaction(false);
                throw th;
            }
        }
    }

    public boolean equals(Object other) {
        if (!(other instanceof TShortByteMap)) {
            return false;
        }
        TShortByteMap that = (TShortByteMap) other;
        if (that.size() != size()) {
            return false;
        }
        byte[] values = this._values;
        byte[] states = this._states;
        byte this_no_entry_value = getNoEntryValue();
        byte that_no_entry_value = that.getNoEntryValue();
        int i = values.length;
        while (true) {
            int i2 = i;
            i--;
            if (i2 > 0) {
                if (states[i] == 1) {
                    short key = this._set[i];
                    byte that_value = that.get(key);
                    byte this_value = values[i];
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
                    hashcode += HashFunctions.hash((int) this._set[i]) ^ HashFunctions.hash((int) this._values[i]);
                }
            } else {
                return hashcode;
            }
        }
    }

    public String toString() {
        final StringBuilder buf = new StringBuilder("{");
        forEachEntry(new TShortByteProcedure() { // from class: gnu.trove.map.hash.TShortByteHashMap.1
            private boolean first = true;

            @Override // gnu.trove.procedure.TShortByteProcedure
            public boolean execute(short key, byte value) {
                if (this.first) {
                    this.first = false;
                } else {
                    buf.append(", ");
                }
                buf.append((int) key);
                buf.append("=");
                buf.append((int) value);
                return true;
            }
        });
        buf.append("}");
        return buf.toString();
    }

    @Override // gnu.trove.impl.hash.TShortByteHash, gnu.trove.impl.hash.THash, java.io.Externalizable
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
                    out.writeByte(this._values[i]);
                }
            } else {
                return;
            }
        }
    }

    @Override // gnu.trove.impl.hash.TShortByteHash, gnu.trove.impl.hash.THash, java.io.Externalizable
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
                byte val = in.readByte();
                put(key, val);
            } else {
                return;
            }
        }
    }
}
