package gnu.trove.map.hash;

import gnu.trove.TByteCollection;
import gnu.trove.TCharCollection;
import gnu.trove.function.TByteFunction;
import gnu.trove.impl.HashFunctions;
import gnu.trove.impl.hash.TCharByteHash;
import gnu.trove.impl.hash.THashPrimitiveIterator;
import gnu.trove.impl.hash.TPrimitiveHash;
import gnu.trove.iterator.TByteIterator;
import gnu.trove.iterator.TCharByteIterator;
import gnu.trove.iterator.TCharIterator;
import gnu.trove.map.TCharByteMap;
import gnu.trove.procedure.TByteProcedure;
import gnu.trove.procedure.TCharByteProcedure;
import gnu.trove.procedure.TCharProcedure;
import gnu.trove.set.TCharSet;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Arrays;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TCharByteHashMap.class */
public class TCharByteHashMap extends TCharByteHash implements TCharByteMap, Externalizable {
    static final long serialVersionUID = 1;
    protected transient byte[] _values;

    public TCharByteHashMap() {
    }

    public TCharByteHashMap(int initialCapacity) {
        super(initialCapacity);
    }

    public TCharByteHashMap(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    public TCharByteHashMap(int initialCapacity, float loadFactor, char noEntryKey, byte noEntryValue) {
        super(initialCapacity, loadFactor, noEntryKey, noEntryValue);
    }

    public TCharByteHashMap(char[] keys, byte[] values) {
        super(Math.max(keys.length, values.length));
        int size = Math.min(keys.length, values.length);
        for (int i = 0; i < size; i++) {
            put(keys[i], values[i]);
        }
    }

    public TCharByteHashMap(TCharByteMap map) {
        super(map.size());
        if (map instanceof TCharByteHashMap) {
            TCharByteHashMap hashmap = (TCharByteHashMap) map;
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
    @Override // gnu.trove.impl.hash.TCharByteHash, gnu.trove.impl.hash.TPrimitiveHash, gnu.trove.impl.hash.THash
    public int setUp(int initialCapacity) {
        int capacity = super.setUp(initialCapacity);
        this._values = new byte[capacity];
        return capacity;
    }

    @Override // gnu.trove.impl.hash.THash
    protected void rehash(int newCapacity) {
        int oldCapacity = this._set.length;
        char[] oldKeys = this._set;
        byte[] oldVals = this._values;
        byte[] oldStates = this._states;
        this._set = new char[newCapacity];
        this._values = new byte[newCapacity];
        this._states = new byte[newCapacity];
        int i = oldCapacity;
        while (true) {
            int i2 = i;
            i--;
            if (i2 > 0) {
                if (oldStates[i] == 1) {
                    char o = oldKeys[i];
                    int index = insertKey(o);
                    this._values[index] = oldVals[i];
                }
            } else {
                return;
            }
        }
    }

    @Override // gnu.trove.map.TCharByteMap
    public byte put(char key, byte value) {
        int index = insertKey(key);
        return doPut(key, value, index);
    }

    @Override // gnu.trove.map.TCharByteMap
    public byte putIfAbsent(char key, byte value) {
        int index = insertKey(key);
        if (index < 0) {
            return this._values[(-index) - 1];
        }
        return doPut(key, value, index);
    }

    private byte doPut(char key, byte value, int index) {
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

    @Override // gnu.trove.map.TCharByteMap
    public void putAll(Map<? extends Character, ? extends Byte> map) {
        ensureCapacity(map.size());
        for (Map.Entry<? extends Character, ? extends Byte> entry : map.entrySet()) {
            put(entry.getKey().charValue(), entry.getValue().byteValue());
        }
    }

    @Override // gnu.trove.map.TCharByteMap
    public void putAll(TCharByteMap map) {
        ensureCapacity(map.size());
        TCharByteIterator iter = map.iterator();
        while (iter.hasNext()) {
            iter.advance();
            put(iter.key(), iter.value());
        }
    }

    @Override // gnu.trove.map.TCharByteMap
    public byte get(char key) {
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

    @Override // gnu.trove.map.TCharByteMap
    public byte remove(char key) {
        byte prev = this.no_entry_value;
        int index = index(key);
        if (index >= 0) {
            prev = this._values[index];
            removeAt(index);
        }
        return prev;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // gnu.trove.impl.hash.TCharByteHash, gnu.trove.impl.hash.TPrimitiveHash, gnu.trove.impl.hash.THash
    public void removeAt(int index) {
        this._values[index] = this.no_entry_value;
        super.removeAt(index);
    }

    @Override // gnu.trove.map.TCharByteMap
    public TCharSet keySet() {
        return new TKeyView();
    }

    @Override // gnu.trove.map.TCharByteMap
    public char[] keys() {
        char[] keys = new char[size()];
        char[] k = this._set;
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

    @Override // gnu.trove.map.TCharByteMap
    public char[] keys(char[] array) {
        int size = size();
        if (array.length < size) {
            array = new char[size];
        }
        char[] keys = this._set;
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

    @Override // gnu.trove.map.TCharByteMap
    public TByteCollection valueCollection() {
        return new TValueView();
    }

    @Override // gnu.trove.map.TCharByteMap
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

    @Override // gnu.trove.map.TCharByteMap
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

    @Override // gnu.trove.map.TCharByteMap
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

    @Override // gnu.trove.map.TCharByteMap
    public boolean containsKey(char key) {
        return contains(key);
    }

    @Override // gnu.trove.map.TCharByteMap
    public TCharByteIterator iterator() {
        return new TCharByteHashIterator(this);
    }

    @Override // gnu.trove.map.TCharByteMap
    public boolean forEachKey(TCharProcedure procedure) {
        return forEach(procedure);
    }

    @Override // gnu.trove.map.TCharByteMap
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

    @Override // gnu.trove.map.TCharByteMap
    public boolean forEachEntry(TCharByteProcedure procedure) {
        byte[] states = this._states;
        char[] keys = this._set;
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

    @Override // gnu.trove.map.TCharByteMap
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

    @Override // gnu.trove.map.TCharByteMap
    public boolean retainEntries(TCharByteProcedure procedure) {
        boolean modified = false;
        byte[] states = this._states;
        char[] keys = this._set;
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

    @Override // gnu.trove.map.TCharByteMap
    public boolean increment(char key) {
        return adjustValue(key, (byte) 1);
    }

    @Override // gnu.trove.map.TCharByteMap
    public boolean adjustValue(char key, byte amount) {
        int index = index(key);
        if (index < 0) {
            return false;
        }
        byte[] bArr = this._values;
        bArr[index] = (byte) (bArr[index] + amount);
        return true;
    }

    @Override // gnu.trove.map.TCharByteMap
    public byte adjustOrPutValue(char key, byte adjust_amount, byte put_amount) {
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

    /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TCharByteHashMap$TKeyView.class */
    protected class TKeyView implements TCharSet {
        protected TKeyView() {
        }

        @Override // gnu.trove.set.TCharSet, gnu.trove.TCharCollection
        public TCharIterator iterator() {
            return new TCharByteKeyHashIterator(TCharByteHashMap.this);
        }

        @Override // gnu.trove.set.TCharSet, gnu.trove.TCharCollection
        public char getNoEntryValue() {
            return TCharByteHashMap.this.no_entry_key;
        }

        @Override // gnu.trove.set.TCharSet, gnu.trove.TCharCollection
        public int size() {
            return TCharByteHashMap.this._size;
        }

        @Override // gnu.trove.set.TCharSet, gnu.trove.TCharCollection
        public boolean isEmpty() {
            return 0 == TCharByteHashMap.this._size;
        }

        @Override // gnu.trove.set.TCharSet, gnu.trove.TCharCollection
        public boolean contains(char entry) {
            return TCharByteHashMap.this.contains(entry);
        }

        @Override // gnu.trove.set.TCharSet, gnu.trove.TCharCollection
        public char[] toArray() {
            return TCharByteHashMap.this.keys();
        }

        @Override // gnu.trove.set.TCharSet, gnu.trove.TCharCollection
        public char[] toArray(char[] dest) {
            return TCharByteHashMap.this.keys(dest);
        }

        @Override // gnu.trove.set.TCharSet, gnu.trove.TCharCollection
        public boolean add(char entry) {
            throw new UnsupportedOperationException();
        }

        @Override // gnu.trove.set.TCharSet, gnu.trove.TCharCollection
        public boolean remove(char entry) {
            return TCharByteHashMap.this.no_entry_value != TCharByteHashMap.this.remove(entry);
        }

        @Override // gnu.trove.set.TCharSet, gnu.trove.TCharCollection
        public boolean containsAll(Collection<?> collection) {
            for (Object element : collection) {
                if (element instanceof Character) {
                    char ele = ((Character) element).charValue();
                    if (!TCharByteHashMap.this.containsKey(ele)) {
                        return false;
                    }
                } else {
                    return false;
                }
            }
            return true;
        }

        @Override // gnu.trove.set.TCharSet, gnu.trove.TCharCollection
        public boolean containsAll(TCharCollection collection) {
            TCharIterator iter = collection.iterator();
            while (iter.hasNext()) {
                if (!TCharByteHashMap.this.containsKey(iter.next())) {
                    return false;
                }
            }
            return true;
        }

        @Override // gnu.trove.set.TCharSet, gnu.trove.TCharCollection
        public boolean containsAll(char[] array) {
            for (char element : array) {
                if (!TCharByteHashMap.this.contains(element)) {
                    return false;
                }
            }
            return true;
        }

        @Override // gnu.trove.set.TCharSet, gnu.trove.TCharCollection
        public boolean addAll(Collection<? extends Character> collection) {
            throw new UnsupportedOperationException();
        }

        @Override // gnu.trove.set.TCharSet, gnu.trove.TCharCollection
        public boolean addAll(TCharCollection collection) {
            throw new UnsupportedOperationException();
        }

        @Override // gnu.trove.set.TCharSet, gnu.trove.TCharCollection
        public boolean addAll(char[] array) {
            throw new UnsupportedOperationException();
        }

        @Override // gnu.trove.set.TCharSet, gnu.trove.TCharCollection
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

        @Override // gnu.trove.set.TCharSet, gnu.trove.TCharCollection
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

        @Override // gnu.trove.set.TCharSet, gnu.trove.TCharCollection
        public boolean retainAll(char[] array) {
            boolean changed = false;
            Arrays.sort(array);
            char[] set = TCharByteHashMap.this._set;
            byte[] states = TCharByteHashMap.this._states;
            int i = set.length;
            while (true) {
                int i2 = i;
                i--;
                if (i2 > 0) {
                    if (states[i] == 1 && Arrays.binarySearch(array, set[i]) < 0) {
                        TCharByteHashMap.this.removeAt(i);
                        changed = true;
                    }
                } else {
                    return changed;
                }
            }
        }

        @Override // gnu.trove.set.TCharSet, gnu.trove.TCharCollection
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

        @Override // gnu.trove.set.TCharSet, gnu.trove.TCharCollection
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

        @Override // gnu.trove.set.TCharSet, gnu.trove.TCharCollection
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

        @Override // gnu.trove.set.TCharSet, gnu.trove.TCharCollection
        public void clear() {
            TCharByteHashMap.this.clear();
        }

        @Override // gnu.trove.set.TCharSet, gnu.trove.TCharCollection
        public boolean forEach(TCharProcedure procedure) {
            return TCharByteHashMap.this.forEachKey(procedure);
        }

        @Override // gnu.trove.set.TCharSet, gnu.trove.TCharCollection
        public boolean equals(Object other) {
            if (!(other instanceof TCharSet)) {
                return false;
            }
            TCharSet that = (TCharSet) other;
            if (that.size() != size()) {
                return false;
            }
            int i = TCharByteHashMap.this._states.length;
            while (true) {
                int i2 = i;
                i--;
                if (i2 > 0) {
                    if (TCharByteHashMap.this._states[i] == 1 && !that.contains(TCharByteHashMap.this._set[i])) {
                        return false;
                    }
                } else {
                    return true;
                }
            }
        }

        @Override // gnu.trove.set.TCharSet, gnu.trove.TCharCollection
        public int hashCode() {
            int hashcode = 0;
            int i = TCharByteHashMap.this._states.length;
            while (true) {
                int i2 = i;
                i--;
                if (i2 > 0) {
                    if (TCharByteHashMap.this._states[i] == 1) {
                        hashcode += HashFunctions.hash((int) TCharByteHashMap.this._set[i]);
                    }
                } else {
                    return hashcode;
                }
            }
        }

        public String toString() {
            final StringBuilder buf = new StringBuilder("{");
            TCharByteHashMap.this.forEachKey(new TCharProcedure() { // from class: gnu.trove.map.hash.TCharByteHashMap.TKeyView.1
                private boolean first = true;

                @Override // gnu.trove.procedure.TCharProcedure
                public boolean execute(char key) {
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

    /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TCharByteHashMap$TValueView.class */
    protected class TValueView implements TByteCollection {
        protected TValueView() {
        }

        @Override // gnu.trove.TByteCollection
        public TByteIterator iterator() {
            return new TCharByteValueHashIterator(TCharByteHashMap.this);
        }

        @Override // gnu.trove.TByteCollection
        public byte getNoEntryValue() {
            return TCharByteHashMap.this.no_entry_value;
        }

        @Override // gnu.trove.TByteCollection
        public int size() {
            return TCharByteHashMap.this._size;
        }

        @Override // gnu.trove.TByteCollection
        public boolean isEmpty() {
            return 0 == TCharByteHashMap.this._size;
        }

        @Override // gnu.trove.TByteCollection
        public boolean contains(byte entry) {
            return TCharByteHashMap.this.containsValue(entry);
        }

        @Override // gnu.trove.TByteCollection
        public byte[] toArray() {
            return TCharByteHashMap.this.values();
        }

        @Override // gnu.trove.TByteCollection
        public byte[] toArray(byte[] dest) {
            return TCharByteHashMap.this.values(dest);
        }

        @Override // gnu.trove.TByteCollection
        public boolean add(byte entry) {
            throw new UnsupportedOperationException();
        }

        @Override // gnu.trove.TByteCollection
        public boolean remove(byte entry) {
            byte[] values = TCharByteHashMap.this._values;
            char[] set = TCharByteHashMap.this._set;
            int i = values.length;
            while (true) {
                int i2 = i;
                i--;
                if (i2 > 0) {
                    if (set[i] != 0 && set[i] != 2 && entry == values[i]) {
                        TCharByteHashMap.this.removeAt(i);
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
                    if (!TCharByteHashMap.this.containsValue(ele)) {
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
                if (!TCharByteHashMap.this.containsValue(iter.next())) {
                    return false;
                }
            }
            return true;
        }

        @Override // gnu.trove.TByteCollection
        public boolean containsAll(byte[] array) {
            for (byte element : array) {
                if (!TCharByteHashMap.this.containsValue(element)) {
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
            byte[] values = TCharByteHashMap.this._values;
            byte[] states = TCharByteHashMap.this._states;
            int i = values.length;
            while (true) {
                int i2 = i;
                i--;
                if (i2 > 0) {
                    if (states[i] == 1 && Arrays.binarySearch(array, values[i]) < 0) {
                        TCharByteHashMap.this.removeAt(i);
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
            TCharByteHashMap.this.clear();
        }

        @Override // gnu.trove.TByteCollection
        public boolean forEach(TByteProcedure procedure) {
            return TCharByteHashMap.this.forEachValue(procedure);
        }

        public String toString() {
            final StringBuilder buf = new StringBuilder("{");
            TCharByteHashMap.this.forEachValue(new TByteProcedure() { // from class: gnu.trove.map.hash.TCharByteHashMap.TValueView.1
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
    /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TCharByteHashMap$TCharByteKeyHashIterator.class */
    public class TCharByteKeyHashIterator extends THashPrimitiveIterator implements TCharIterator {
        TCharByteKeyHashIterator(TPrimitiveHash hash) {
            super(hash);
        }

        @Override // gnu.trove.iterator.TCharIterator
        public char next() {
            moveToNextIndex();
            return TCharByteHashMap.this._set[this._index];
        }

        @Override // gnu.trove.impl.hash.THashPrimitiveIterator, gnu.trove.iterator.TPrimitiveIterator, gnu.trove.iterator.TIterator, java.util.Iterator
        public void remove() {
            if (this._expectedSize != this._hash.size()) {
                throw new ConcurrentModificationException();
            }
            try {
                this._hash.tempDisableAutoCompaction();
                TCharByteHashMap.this.removeAt(this._index);
                this._hash.reenableAutoCompaction(false);
                this._expectedSize--;
            } catch (Throwable th) {
                this._hash.reenableAutoCompaction(false);
                throw th;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TCharByteHashMap$TCharByteValueHashIterator.class */
    public class TCharByteValueHashIterator extends THashPrimitiveIterator implements TByteIterator {
        TCharByteValueHashIterator(TPrimitiveHash hash) {
            super(hash);
        }

        @Override // gnu.trove.iterator.TByteIterator
        public byte next() {
            moveToNextIndex();
            return TCharByteHashMap.this._values[this._index];
        }

        @Override // gnu.trove.impl.hash.THashPrimitiveIterator, gnu.trove.iterator.TPrimitiveIterator, gnu.trove.iterator.TIterator, java.util.Iterator
        public void remove() {
            if (this._expectedSize != this._hash.size()) {
                throw new ConcurrentModificationException();
            }
            try {
                this._hash.tempDisableAutoCompaction();
                TCharByteHashMap.this.removeAt(this._index);
                this._hash.reenableAutoCompaction(false);
                this._expectedSize--;
            } catch (Throwable th) {
                this._hash.reenableAutoCompaction(false);
                throw th;
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TCharByteHashMap$TCharByteHashIterator.class */
    class TCharByteHashIterator extends THashPrimitiveIterator implements TCharByteIterator {
        TCharByteHashIterator(TCharByteHashMap map) {
            super(map);
        }

        @Override // gnu.trove.iterator.TAdvancingIterator
        public void advance() {
            moveToNextIndex();
        }

        @Override // gnu.trove.iterator.TCharByteIterator
        public char key() {
            return TCharByteHashMap.this._set[this._index];
        }

        @Override // gnu.trove.iterator.TCharByteIterator
        public byte value() {
            return TCharByteHashMap.this._values[this._index];
        }

        @Override // gnu.trove.iterator.TCharByteIterator
        public byte setValue(byte val) {
            byte old = value();
            TCharByteHashMap.this._values[this._index] = val;
            return old;
        }

        @Override // gnu.trove.impl.hash.THashPrimitiveIterator, gnu.trove.iterator.TPrimitiveIterator, gnu.trove.iterator.TIterator, java.util.Iterator
        public void remove() {
            if (this._expectedSize != this._hash.size()) {
                throw new ConcurrentModificationException();
            }
            try {
                this._hash.tempDisableAutoCompaction();
                TCharByteHashMap.this.removeAt(this._index);
                this._hash.reenableAutoCompaction(false);
                this._expectedSize--;
            } catch (Throwable th) {
                this._hash.reenableAutoCompaction(false);
                throw th;
            }
        }
    }

    public boolean equals(Object other) {
        if (!(other instanceof TCharByteMap)) {
            return false;
        }
        TCharByteMap that = (TCharByteMap) other;
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
                    char key = this._set[i];
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
        forEachEntry(new TCharByteProcedure() { // from class: gnu.trove.map.hash.TCharByteHashMap.1
            private boolean first = true;

            @Override // gnu.trove.procedure.TCharByteProcedure
            public boolean execute(char key, byte value) {
                if (this.first) {
                    this.first = false;
                } else {
                    buf.append(", ");
                }
                buf.append(key);
                buf.append("=");
                buf.append((int) value);
                return true;
            }
        });
        buf.append("}");
        return buf.toString();
    }

    @Override // gnu.trove.impl.hash.TCharByteHash, gnu.trove.impl.hash.THash, java.io.Externalizable
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
                    out.writeChar(this._set[i]);
                    out.writeByte(this._values[i]);
                }
            } else {
                return;
            }
        }
    }

    @Override // gnu.trove.impl.hash.TCharByteHash, gnu.trove.impl.hash.THash, java.io.Externalizable
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        in.readByte();
        super.readExternal(in);
        int size = in.readInt();
        setUp(size);
        while (true) {
            int i = size;
            size--;
            if (i > 0) {
                char key = in.readChar();
                byte val = in.readByte();
                put(key, val);
            } else {
                return;
            }
        }
    }
}
