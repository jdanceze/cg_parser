package gnu.trove.map.hash;

import gnu.trove.TFloatCollection;
import gnu.trove.TLongCollection;
import gnu.trove.function.TLongFunction;
import gnu.trove.impl.HashFunctions;
import gnu.trove.impl.hash.TFloatLongHash;
import gnu.trove.impl.hash.THashPrimitiveIterator;
import gnu.trove.impl.hash.TPrimitiveHash;
import gnu.trove.iterator.TFloatIterator;
import gnu.trove.iterator.TFloatLongIterator;
import gnu.trove.iterator.TLongIterator;
import gnu.trove.map.TFloatLongMap;
import gnu.trove.procedure.TFloatLongProcedure;
import gnu.trove.procedure.TFloatProcedure;
import gnu.trove.procedure.TLongProcedure;
import gnu.trove.set.TFloatSet;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Arrays;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TFloatLongHashMap.class */
public class TFloatLongHashMap extends TFloatLongHash implements TFloatLongMap, Externalizable {
    static final long serialVersionUID = 1;
    protected transient long[] _values;

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: JavaClassParseException: Unknown opcode: 0x5e in method: gnu.trove.map.hash.TFloatLongHashMap.adjustOrPutValue(float, long, long):long, file: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TFloatLongHashMap.class
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
    @Override // gnu.trove.map.TFloatLongMap
    public long adjustOrPutValue(float r1, long r2, long r4) {
        /*
        // Can't load method instructions: Load method exception: JavaClassParseException: Unknown opcode: 0x5e in method: gnu.trove.map.hash.TFloatLongHashMap.adjustOrPutValue(float, long, long):long, file: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TFloatLongHashMap.class
        */
        throw new UnsupportedOperationException("Method not decompiled: gnu.trove.map.hash.TFloatLongHashMap.adjustOrPutValue(float, long, long):long");
    }

    public TFloatLongHashMap() {
    }

    public TFloatLongHashMap(int initialCapacity) {
        super(initialCapacity);
    }

    public TFloatLongHashMap(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    public TFloatLongHashMap(int initialCapacity, float loadFactor, float noEntryKey, long noEntryValue) {
        super(initialCapacity, loadFactor, noEntryKey, noEntryValue);
    }

    public TFloatLongHashMap(float[] keys, long[] values) {
        super(Math.max(keys.length, values.length));
        int size = Math.min(keys.length, values.length);
        for (int i = 0; i < size; i++) {
            put(keys[i], values[i]);
        }
    }

    public TFloatLongHashMap(TFloatLongMap map) {
        super(map.size());
        if (map instanceof TFloatLongHashMap) {
            TFloatLongHashMap hashmap = (TFloatLongHashMap) map;
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
    @Override // gnu.trove.impl.hash.TFloatLongHash, gnu.trove.impl.hash.TPrimitiveHash, gnu.trove.impl.hash.THash
    public int setUp(int initialCapacity) {
        int capacity = super.setUp(initialCapacity);
        this._values = new long[capacity];
        return capacity;
    }

    @Override // gnu.trove.impl.hash.THash
    protected void rehash(int newCapacity) {
        int oldCapacity = this._set.length;
        float[] oldKeys = this._set;
        long[] oldVals = this._values;
        byte[] oldStates = this._states;
        this._set = new float[newCapacity];
        this._values = new long[newCapacity];
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

    @Override // gnu.trove.map.TFloatLongMap
    public long put(float key, long value) {
        int index = insertKey(key);
        return doPut(key, value, index);
    }

    @Override // gnu.trove.map.TFloatLongMap
    public long putIfAbsent(float key, long value) {
        int index = insertKey(key);
        if (index < 0) {
            return this._values[(-index) - 1];
        }
        return doPut(key, value, index);
    }

    private long doPut(float key, long value, int index) {
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

    @Override // gnu.trove.map.TFloatLongMap
    public void putAll(Map<? extends Float, ? extends Long> map) {
        ensureCapacity(map.size());
        for (Map.Entry<? extends Float, ? extends Long> entry : map.entrySet()) {
            put(entry.getKey().floatValue(), entry.getValue().longValue());
        }
    }

    @Override // gnu.trove.map.TFloatLongMap
    public void putAll(TFloatLongMap map) {
        ensureCapacity(map.size());
        TFloatLongIterator iter = map.iterator();
        while (iter.hasNext()) {
            iter.advance();
            put(iter.key(), iter.value());
        }
    }

    @Override // gnu.trove.map.TFloatLongMap
    public long get(float key) {
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

    @Override // gnu.trove.map.TFloatLongMap
    public long remove(float key) {
        long prev = this.no_entry_value;
        int index = index(key);
        if (index >= 0) {
            prev = this._values[index];
            removeAt(index);
        }
        return prev;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // gnu.trove.impl.hash.TFloatLongHash, gnu.trove.impl.hash.TPrimitiveHash, gnu.trove.impl.hash.THash
    public void removeAt(int index) {
        this._values[index] = this.no_entry_value;
        super.removeAt(index);
    }

    @Override // gnu.trove.map.TFloatLongMap
    public TFloatSet keySet() {
        return new TKeyView();
    }

    @Override // gnu.trove.map.TFloatLongMap
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

    @Override // gnu.trove.map.TFloatLongMap
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

    @Override // gnu.trove.map.TFloatLongMap
    public TLongCollection valueCollection() {
        return new TValueView();
    }

    @Override // gnu.trove.map.TFloatLongMap
    public long[] values() {
        long[] vals = new long[size()];
        long[] v = this._values;
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

    @Override // gnu.trove.map.TFloatLongMap
    public long[] values(long[] array) {
        int size = size();
        if (array.length < size) {
            array = new long[size];
        }
        long[] v = this._values;
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

    @Override // gnu.trove.map.TFloatLongMap
    public boolean containsValue(long val) {
        byte[] states = this._states;
        long[] vals = this._values;
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

    @Override // gnu.trove.map.TFloatLongMap
    public boolean containsKey(float key) {
        return contains(key);
    }

    @Override // gnu.trove.map.TFloatLongMap
    public TFloatLongIterator iterator() {
        return new TFloatLongHashIterator(this);
    }

    @Override // gnu.trove.map.TFloatLongMap
    public boolean forEachKey(TFloatProcedure procedure) {
        return forEach(procedure);
    }

    @Override // gnu.trove.map.TFloatLongMap
    public boolean forEachValue(TLongProcedure procedure) {
        byte[] states = this._states;
        long[] values = this._values;
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

    @Override // gnu.trove.map.TFloatLongMap
    public boolean forEachEntry(TFloatLongProcedure procedure) {
        byte[] states = this._states;
        float[] keys = this._set;
        long[] values = this._values;
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

    @Override // gnu.trove.map.TFloatLongMap
    public void transformValues(TLongFunction function) {
        byte[] states = this._states;
        long[] values = this._values;
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

    @Override // gnu.trove.map.TFloatLongMap
    public boolean retainEntries(TFloatLongProcedure procedure) {
        boolean modified = false;
        byte[] states = this._states;
        float[] keys = this._set;
        long[] values = this._values;
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

    @Override // gnu.trove.map.TFloatLongMap
    public boolean increment(float key) {
        return adjustValue(key, 1L);
    }

    @Override // gnu.trove.map.TFloatLongMap
    public boolean adjustValue(float key, long amount) {
        int index = index(key);
        if (index < 0) {
            return false;
        }
        long[] jArr = this._values;
        jArr[index] = jArr[index] + amount;
        return true;
    }

    /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TFloatLongHashMap$TKeyView.class */
    protected class TKeyView implements TFloatSet {
        protected TKeyView() {
        }

        @Override // gnu.trove.set.TFloatSet, gnu.trove.TFloatCollection
        public TFloatIterator iterator() {
            return new TFloatLongKeyHashIterator(TFloatLongHashMap.this);
        }

        @Override // gnu.trove.set.TFloatSet, gnu.trove.TFloatCollection
        public float getNoEntryValue() {
            return TFloatLongHashMap.this.no_entry_key;
        }

        @Override // gnu.trove.set.TFloatSet, gnu.trove.TFloatCollection
        public int size() {
            return TFloatLongHashMap.this._size;
        }

        @Override // gnu.trove.set.TFloatSet, gnu.trove.TFloatCollection
        public boolean isEmpty() {
            return 0 == TFloatLongHashMap.this._size;
        }

        @Override // gnu.trove.set.TFloatSet, gnu.trove.TFloatCollection
        public boolean contains(float entry) {
            return TFloatLongHashMap.this.contains(entry);
        }

        @Override // gnu.trove.set.TFloatSet, gnu.trove.TFloatCollection
        public float[] toArray() {
            return TFloatLongHashMap.this.keys();
        }

        @Override // gnu.trove.set.TFloatSet, gnu.trove.TFloatCollection
        public float[] toArray(float[] dest) {
            return TFloatLongHashMap.this.keys(dest);
        }

        @Override // gnu.trove.set.TFloatSet, gnu.trove.TFloatCollection
        public boolean add(float entry) {
            throw new UnsupportedOperationException();
        }

        @Override // gnu.trove.set.TFloatSet, gnu.trove.TFloatCollection
        public boolean remove(float entry) {
            return TFloatLongHashMap.this.no_entry_value != TFloatLongHashMap.this.remove(entry);
        }

        @Override // gnu.trove.set.TFloatSet, gnu.trove.TFloatCollection
        public boolean containsAll(Collection<?> collection) {
            for (Object element : collection) {
                if (element instanceof Float) {
                    float ele = ((Float) element).floatValue();
                    if (!TFloatLongHashMap.this.containsKey(ele)) {
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
                if (!TFloatLongHashMap.this.containsKey(iter.next())) {
                    return false;
                }
            }
            return true;
        }

        @Override // gnu.trove.set.TFloatSet, gnu.trove.TFloatCollection
        public boolean containsAll(float[] array) {
            for (float element : array) {
                if (!TFloatLongHashMap.this.contains(element)) {
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
            float[] set = TFloatLongHashMap.this._set;
            byte[] states = TFloatLongHashMap.this._states;
            int i = set.length;
            while (true) {
                int i2 = i;
                i--;
                if (i2 > 0) {
                    if (states[i] == 1 && Arrays.binarySearch(array, set[i]) < 0) {
                        TFloatLongHashMap.this.removeAt(i);
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
            TFloatLongHashMap.this.clear();
        }

        @Override // gnu.trove.set.TFloatSet, gnu.trove.TFloatCollection
        public boolean forEach(TFloatProcedure procedure) {
            return TFloatLongHashMap.this.forEachKey(procedure);
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
            int i = TFloatLongHashMap.this._states.length;
            while (true) {
                int i2 = i;
                i--;
                if (i2 > 0) {
                    if (TFloatLongHashMap.this._states[i] == 1 && !that.contains(TFloatLongHashMap.this._set[i])) {
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
            int i = TFloatLongHashMap.this._states.length;
            while (true) {
                int i2 = i;
                i--;
                if (i2 > 0) {
                    if (TFloatLongHashMap.this._states[i] == 1) {
                        hashcode += HashFunctions.hash(TFloatLongHashMap.this._set[i]);
                    }
                } else {
                    return hashcode;
                }
            }
        }

        public String toString() {
            final StringBuilder buf = new StringBuilder("{");
            TFloatLongHashMap.this.forEachKey(new TFloatProcedure() { // from class: gnu.trove.map.hash.TFloatLongHashMap.TKeyView.1
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

    /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TFloatLongHashMap$TValueView.class */
    protected class TValueView implements TLongCollection {
        protected TValueView() {
        }

        @Override // gnu.trove.TLongCollection
        public TLongIterator iterator() {
            return new TFloatLongValueHashIterator(TFloatLongHashMap.this);
        }

        @Override // gnu.trove.TLongCollection
        public long getNoEntryValue() {
            return TFloatLongHashMap.this.no_entry_value;
        }

        @Override // gnu.trove.TLongCollection
        public int size() {
            return TFloatLongHashMap.this._size;
        }

        @Override // gnu.trove.TLongCollection
        public boolean isEmpty() {
            return 0 == TFloatLongHashMap.this._size;
        }

        @Override // gnu.trove.TLongCollection
        public boolean contains(long entry) {
            return TFloatLongHashMap.this.containsValue(entry);
        }

        @Override // gnu.trove.TLongCollection
        public long[] toArray() {
            return TFloatLongHashMap.this.values();
        }

        @Override // gnu.trove.TLongCollection
        public long[] toArray(long[] dest) {
            return TFloatLongHashMap.this.values(dest);
        }

        @Override // gnu.trove.TLongCollection
        public boolean add(long entry) {
            throw new UnsupportedOperationException();
        }

        @Override // gnu.trove.TLongCollection
        public boolean remove(long entry) {
            long[] values = TFloatLongHashMap.this._values;
            float[] set = TFloatLongHashMap.this._set;
            int i = values.length;
            while (true) {
                int i2 = i;
                i--;
                if (i2 > 0) {
                    if (set[i] != 0.0f && set[i] != 2.0f && entry == values[i]) {
                        TFloatLongHashMap.this.removeAt(i);
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
                    if (!TFloatLongHashMap.this.containsValue(ele)) {
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
                if (!TFloatLongHashMap.this.containsValue(iter.next())) {
                    return false;
                }
            }
            return true;
        }

        @Override // gnu.trove.TLongCollection
        public boolean containsAll(long[] array) {
            for (long element : array) {
                if (!TFloatLongHashMap.this.containsValue(element)) {
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
            long[] values = TFloatLongHashMap.this._values;
            byte[] states = TFloatLongHashMap.this._states;
            int i = values.length;
            while (true) {
                int i2 = i;
                i--;
                if (i2 > 0) {
                    if (states[i] == 1 && Arrays.binarySearch(array, values[i]) < 0) {
                        TFloatLongHashMap.this.removeAt(i);
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
            TFloatLongHashMap.this.clear();
        }

        @Override // gnu.trove.TLongCollection
        public boolean forEach(TLongProcedure procedure) {
            return TFloatLongHashMap.this.forEachValue(procedure);
        }

        public String toString() {
            final StringBuilder buf = new StringBuilder("{");
            TFloatLongHashMap.this.forEachValue(new TLongProcedure() { // from class: gnu.trove.map.hash.TFloatLongHashMap.TValueView.1
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
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TFloatLongHashMap$TFloatLongKeyHashIterator.class */
    public class TFloatLongKeyHashIterator extends THashPrimitiveIterator implements TFloatIterator {
        TFloatLongKeyHashIterator(TPrimitiveHash hash) {
            super(hash);
        }

        @Override // gnu.trove.iterator.TFloatIterator
        public float next() {
            moveToNextIndex();
            return TFloatLongHashMap.this._set[this._index];
        }

        @Override // gnu.trove.impl.hash.THashPrimitiveIterator, gnu.trove.iterator.TPrimitiveIterator, gnu.trove.iterator.TIterator, java.util.Iterator
        public void remove() {
            if (this._expectedSize != this._hash.size()) {
                throw new ConcurrentModificationException();
            }
            try {
                this._hash.tempDisableAutoCompaction();
                TFloatLongHashMap.this.removeAt(this._index);
                this._hash.reenableAutoCompaction(false);
                this._expectedSize--;
            } catch (Throwable th) {
                this._hash.reenableAutoCompaction(false);
                throw th;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TFloatLongHashMap$TFloatLongValueHashIterator.class */
    public class TFloatLongValueHashIterator extends THashPrimitiveIterator implements TLongIterator {
        TFloatLongValueHashIterator(TPrimitiveHash hash) {
            super(hash);
        }

        @Override // gnu.trove.iterator.TLongIterator
        public long next() {
            moveToNextIndex();
            return TFloatLongHashMap.this._values[this._index];
        }

        @Override // gnu.trove.impl.hash.THashPrimitiveIterator, gnu.trove.iterator.TPrimitiveIterator, gnu.trove.iterator.TIterator, java.util.Iterator
        public void remove() {
            if (this._expectedSize != this._hash.size()) {
                throw new ConcurrentModificationException();
            }
            try {
                this._hash.tempDisableAutoCompaction();
                TFloatLongHashMap.this.removeAt(this._index);
                this._hash.reenableAutoCompaction(false);
                this._expectedSize--;
            } catch (Throwable th) {
                this._hash.reenableAutoCompaction(false);
                throw th;
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TFloatLongHashMap$TFloatLongHashIterator.class */
    class TFloatLongHashIterator extends THashPrimitiveIterator implements TFloatLongIterator {
        TFloatLongHashIterator(TFloatLongHashMap map) {
            super(map);
        }

        @Override // gnu.trove.iterator.TAdvancingIterator
        public void advance() {
            moveToNextIndex();
        }

        @Override // gnu.trove.iterator.TFloatLongIterator
        public float key() {
            return TFloatLongHashMap.this._set[this._index];
        }

        @Override // gnu.trove.iterator.TFloatLongIterator
        public long value() {
            return TFloatLongHashMap.this._values[this._index];
        }

        @Override // gnu.trove.iterator.TFloatLongIterator
        public long setValue(long val) {
            long old = value();
            TFloatLongHashMap.this._values[this._index] = val;
            return old;
        }

        @Override // gnu.trove.impl.hash.THashPrimitiveIterator, gnu.trove.iterator.TPrimitiveIterator, gnu.trove.iterator.TIterator, java.util.Iterator
        public void remove() {
            if (this._expectedSize != this._hash.size()) {
                throw new ConcurrentModificationException();
            }
            try {
                this._hash.tempDisableAutoCompaction();
                TFloatLongHashMap.this.removeAt(this._index);
                this._hash.reenableAutoCompaction(false);
                this._expectedSize--;
            } catch (Throwable th) {
                this._hash.reenableAutoCompaction(false);
                throw th;
            }
        }
    }

    public boolean equals(Object other) {
        if (!(other instanceof TFloatLongMap)) {
            return false;
        }
        TFloatLongMap that = (TFloatLongMap) other;
        if (that.size() != size()) {
            return false;
        }
        long[] values = this._values;
        byte[] states = this._states;
        long this_no_entry_value = getNoEntryValue();
        long that_no_entry_value = that.getNoEntryValue();
        int i = values.length;
        while (true) {
            int i2 = i;
            i--;
            if (i2 > 0) {
                if (states[i] == 1) {
                    float key = this._set[i];
                    long that_value = that.get(key);
                    long this_value = values[i];
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
        forEachEntry(new TFloatLongProcedure() { // from class: gnu.trove.map.hash.TFloatLongHashMap.1
            private boolean first = true;

            @Override // gnu.trove.procedure.TFloatLongProcedure
            public boolean execute(float key, long value) {
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

    @Override // gnu.trove.impl.hash.TFloatLongHash, gnu.trove.impl.hash.THash, java.io.Externalizable
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
                    out.writeLong(this._values[i]);
                }
            } else {
                return;
            }
        }
    }

    @Override // gnu.trove.impl.hash.TFloatLongHash, gnu.trove.impl.hash.THash, java.io.Externalizable
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
                long val = in.readLong();
                put(key, val);
            } else {
                return;
            }
        }
    }
}
