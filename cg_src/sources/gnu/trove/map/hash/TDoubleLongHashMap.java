package gnu.trove.map.hash;

import com.sun.xml.bind.v2.runtime.reflect.opt.Const;
import gnu.trove.TDoubleCollection;
import gnu.trove.TLongCollection;
import gnu.trove.function.TLongFunction;
import gnu.trove.impl.HashFunctions;
import gnu.trove.impl.hash.TDoubleLongHash;
import gnu.trove.impl.hash.THashPrimitiveIterator;
import gnu.trove.impl.hash.TPrimitiveHash;
import gnu.trove.iterator.TDoubleIterator;
import gnu.trove.iterator.TDoubleLongIterator;
import gnu.trove.iterator.TLongIterator;
import gnu.trove.map.TDoubleLongMap;
import gnu.trove.procedure.TDoubleLongProcedure;
import gnu.trove.procedure.TDoubleProcedure;
import gnu.trove.procedure.TLongProcedure;
import gnu.trove.set.TDoubleSet;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Arrays;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TDoubleLongHashMap.class */
public class TDoubleLongHashMap extends TDoubleLongHash implements TDoubleLongMap, Externalizable {
    static final long serialVersionUID = 1;
    protected transient long[] _values;

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: JavaClassParseException: Unknown opcode: 0x5e in method: gnu.trove.map.hash.TDoubleLongHashMap.adjustOrPutValue(double, long, long):long, file: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TDoubleLongHashMap.class
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
    @Override // gnu.trove.map.TDoubleLongMap
    public long adjustOrPutValue(double r1, long r3, long r5) {
        /*
        // Can't load method instructions: Load method exception: JavaClassParseException: Unknown opcode: 0x5e in method: gnu.trove.map.hash.TDoubleLongHashMap.adjustOrPutValue(double, long, long):long, file: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TDoubleLongHashMap.class
        */
        throw new UnsupportedOperationException("Method not decompiled: gnu.trove.map.hash.TDoubleLongHashMap.adjustOrPutValue(double, long, long):long");
    }

    public TDoubleLongHashMap() {
    }

    public TDoubleLongHashMap(int initialCapacity) {
        super(initialCapacity);
    }

    public TDoubleLongHashMap(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    public TDoubleLongHashMap(int initialCapacity, float loadFactor, double noEntryKey, long noEntryValue) {
        super(initialCapacity, loadFactor, noEntryKey, noEntryValue);
    }

    public TDoubleLongHashMap(double[] keys, long[] values) {
        super(Math.max(keys.length, values.length));
        int size = Math.min(keys.length, values.length);
        for (int i = 0; i < size; i++) {
            put(keys[i], values[i]);
        }
    }

    public TDoubleLongHashMap(TDoubleLongMap map) {
        super(map.size());
        if (map instanceof TDoubleLongHashMap) {
            TDoubleLongHashMap hashmap = (TDoubleLongHashMap) map;
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
    @Override // gnu.trove.impl.hash.TDoubleLongHash, gnu.trove.impl.hash.TPrimitiveHash, gnu.trove.impl.hash.THash
    public int setUp(int initialCapacity) {
        int capacity = super.setUp(initialCapacity);
        this._values = new long[capacity];
        return capacity;
    }

    @Override // gnu.trove.impl.hash.THash
    protected void rehash(int newCapacity) {
        int oldCapacity = this._set.length;
        double[] oldKeys = this._set;
        long[] oldVals = this._values;
        byte[] oldStates = this._states;
        this._set = new double[newCapacity];
        this._values = new long[newCapacity];
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

    @Override // gnu.trove.map.TDoubleLongMap
    public long put(double key, long value) {
        int index = insertKey(key);
        return doPut(key, value, index);
    }

    @Override // gnu.trove.map.TDoubleLongMap
    public long putIfAbsent(double key, long value) {
        int index = insertKey(key);
        if (index < 0) {
            return this._values[(-index) - 1];
        }
        return doPut(key, value, index);
    }

    private long doPut(double key, long value, int index) {
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

    @Override // gnu.trove.map.TDoubleLongMap
    public void putAll(Map<? extends Double, ? extends Long> map) {
        ensureCapacity(map.size());
        for (Map.Entry<? extends Double, ? extends Long> entry : map.entrySet()) {
            put(entry.getKey().doubleValue(), entry.getValue().longValue());
        }
    }

    @Override // gnu.trove.map.TDoubleLongMap
    public void putAll(TDoubleLongMap map) {
        ensureCapacity(map.size());
        TDoubleLongIterator iter = map.iterator();
        while (iter.hasNext()) {
            iter.advance();
            put(iter.key(), iter.value());
        }
    }

    @Override // gnu.trove.map.TDoubleLongMap
    public long get(double key) {
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

    @Override // gnu.trove.map.TDoubleLongMap
    public long remove(double key) {
        long prev = this.no_entry_value;
        int index = index(key);
        if (index >= 0) {
            prev = this._values[index];
            removeAt(index);
        }
        return prev;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // gnu.trove.impl.hash.TDoubleLongHash, gnu.trove.impl.hash.TPrimitiveHash, gnu.trove.impl.hash.THash
    public void removeAt(int index) {
        this._values[index] = this.no_entry_value;
        super.removeAt(index);
    }

    @Override // gnu.trove.map.TDoubleLongMap
    public TDoubleSet keySet() {
        return new TKeyView();
    }

    @Override // gnu.trove.map.TDoubleLongMap
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

    @Override // gnu.trove.map.TDoubleLongMap
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

    @Override // gnu.trove.map.TDoubleLongMap
    public TLongCollection valueCollection() {
        return new TValueView();
    }

    @Override // gnu.trove.map.TDoubleLongMap
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

    @Override // gnu.trove.map.TDoubleLongMap
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

    @Override // gnu.trove.map.TDoubleLongMap
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

    @Override // gnu.trove.map.TDoubleLongMap
    public boolean containsKey(double key) {
        return contains(key);
    }

    @Override // gnu.trove.map.TDoubleLongMap
    public TDoubleLongIterator iterator() {
        return new TDoubleLongHashIterator(this);
    }

    @Override // gnu.trove.map.TDoubleLongMap
    public boolean forEachKey(TDoubleProcedure procedure) {
        return forEach(procedure);
    }

    @Override // gnu.trove.map.TDoubleLongMap
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

    @Override // gnu.trove.map.TDoubleLongMap
    public boolean forEachEntry(TDoubleLongProcedure procedure) {
        byte[] states = this._states;
        double[] keys = this._set;
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

    @Override // gnu.trove.map.TDoubleLongMap
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

    @Override // gnu.trove.map.TDoubleLongMap
    public boolean retainEntries(TDoubleLongProcedure procedure) {
        boolean modified = false;
        byte[] states = this._states;
        double[] keys = this._set;
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

    @Override // gnu.trove.map.TDoubleLongMap
    public boolean increment(double key) {
        return adjustValue(key, 1L);
    }

    @Override // gnu.trove.map.TDoubleLongMap
    public boolean adjustValue(double key, long amount) {
        int index = index(key);
        if (index < 0) {
            return false;
        }
        long[] jArr = this._values;
        jArr[index] = jArr[index] + amount;
        return true;
    }

    /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TDoubleLongHashMap$TKeyView.class */
    protected class TKeyView implements TDoubleSet {
        protected TKeyView() {
        }

        @Override // gnu.trove.set.TDoubleSet, gnu.trove.TDoubleCollection
        public TDoubleIterator iterator() {
            return new TDoubleLongKeyHashIterator(TDoubleLongHashMap.this);
        }

        @Override // gnu.trove.set.TDoubleSet, gnu.trove.TDoubleCollection
        public double getNoEntryValue() {
            return TDoubleLongHashMap.this.no_entry_key;
        }

        @Override // gnu.trove.set.TDoubleSet, gnu.trove.TDoubleCollection
        public int size() {
            return TDoubleLongHashMap.this._size;
        }

        @Override // gnu.trove.set.TDoubleSet, gnu.trove.TDoubleCollection
        public boolean isEmpty() {
            return 0 == TDoubleLongHashMap.this._size;
        }

        @Override // gnu.trove.set.TDoubleSet, gnu.trove.TDoubleCollection
        public boolean contains(double entry) {
            return TDoubleLongHashMap.this.contains(entry);
        }

        @Override // gnu.trove.set.TDoubleSet, gnu.trove.TDoubleCollection
        public double[] toArray() {
            return TDoubleLongHashMap.this.keys();
        }

        @Override // gnu.trove.set.TDoubleSet, gnu.trove.TDoubleCollection
        public double[] toArray(double[] dest) {
            return TDoubleLongHashMap.this.keys(dest);
        }

        @Override // gnu.trove.set.TDoubleSet, gnu.trove.TDoubleCollection
        public boolean add(double entry) {
            throw new UnsupportedOperationException();
        }

        @Override // gnu.trove.set.TDoubleSet, gnu.trove.TDoubleCollection
        public boolean remove(double entry) {
            return TDoubleLongHashMap.this.no_entry_value != TDoubleLongHashMap.this.remove(entry);
        }

        @Override // gnu.trove.set.TDoubleSet, gnu.trove.TDoubleCollection
        public boolean containsAll(Collection<?> collection) {
            for (Object element : collection) {
                if (element instanceof Double) {
                    double ele = ((Double) element).doubleValue();
                    if (!TDoubleLongHashMap.this.containsKey(ele)) {
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
                if (!TDoubleLongHashMap.this.containsKey(iter.next())) {
                    return false;
                }
            }
            return true;
        }

        @Override // gnu.trove.set.TDoubleSet, gnu.trove.TDoubleCollection
        public boolean containsAll(double[] array) {
            for (double element : array) {
                if (!TDoubleLongHashMap.this.contains(element)) {
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
            double[] set = TDoubleLongHashMap.this._set;
            byte[] states = TDoubleLongHashMap.this._states;
            int i = set.length;
            while (true) {
                int i2 = i;
                i--;
                if (i2 > 0) {
                    if (states[i] == 1 && Arrays.binarySearch(array, set[i]) < 0) {
                        TDoubleLongHashMap.this.removeAt(i);
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
            TDoubleLongHashMap.this.clear();
        }

        @Override // gnu.trove.set.TDoubleSet, gnu.trove.TDoubleCollection
        public boolean forEach(TDoubleProcedure procedure) {
            return TDoubleLongHashMap.this.forEachKey(procedure);
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
            int i = TDoubleLongHashMap.this._states.length;
            while (true) {
                int i2 = i;
                i--;
                if (i2 > 0) {
                    if (TDoubleLongHashMap.this._states[i] == 1 && !that.contains(TDoubleLongHashMap.this._set[i])) {
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
            int i = TDoubleLongHashMap.this._states.length;
            while (true) {
                int i2 = i;
                i--;
                if (i2 > 0) {
                    if (TDoubleLongHashMap.this._states[i] == 1) {
                        hashcode += HashFunctions.hash(TDoubleLongHashMap.this._set[i]);
                    }
                } else {
                    return hashcode;
                }
            }
        }

        public String toString() {
            final StringBuilder buf = new StringBuilder("{");
            TDoubleLongHashMap.this.forEachKey(new TDoubleProcedure() { // from class: gnu.trove.map.hash.TDoubleLongHashMap.TKeyView.1
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

    /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TDoubleLongHashMap$TValueView.class */
    protected class TValueView implements TLongCollection {
        protected TValueView() {
        }

        @Override // gnu.trove.TLongCollection
        public TLongIterator iterator() {
            return new TDoubleLongValueHashIterator(TDoubleLongHashMap.this);
        }

        @Override // gnu.trove.TLongCollection
        public long getNoEntryValue() {
            return TDoubleLongHashMap.this.no_entry_value;
        }

        @Override // gnu.trove.TLongCollection
        public int size() {
            return TDoubleLongHashMap.this._size;
        }

        @Override // gnu.trove.TLongCollection
        public boolean isEmpty() {
            return 0 == TDoubleLongHashMap.this._size;
        }

        @Override // gnu.trove.TLongCollection
        public boolean contains(long entry) {
            return TDoubleLongHashMap.this.containsValue(entry);
        }

        @Override // gnu.trove.TLongCollection
        public long[] toArray() {
            return TDoubleLongHashMap.this.values();
        }

        @Override // gnu.trove.TLongCollection
        public long[] toArray(long[] dest) {
            return TDoubleLongHashMap.this.values(dest);
        }

        @Override // gnu.trove.TLongCollection
        public boolean add(long entry) {
            throw new UnsupportedOperationException();
        }

        @Override // gnu.trove.TLongCollection
        public boolean remove(long entry) {
            long[] values = TDoubleLongHashMap.this._values;
            double[] set = TDoubleLongHashMap.this._set;
            int i = values.length;
            while (true) {
                int i2 = i;
                i--;
                if (i2 > 0) {
                    if (set[i] != Const.default_value_double && set[i] != 2.0d && entry == values[i]) {
                        TDoubleLongHashMap.this.removeAt(i);
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
                    if (!TDoubleLongHashMap.this.containsValue(ele)) {
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
                if (!TDoubleLongHashMap.this.containsValue(iter.next())) {
                    return false;
                }
            }
            return true;
        }

        @Override // gnu.trove.TLongCollection
        public boolean containsAll(long[] array) {
            for (long element : array) {
                if (!TDoubleLongHashMap.this.containsValue(element)) {
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
            long[] values = TDoubleLongHashMap.this._values;
            byte[] states = TDoubleLongHashMap.this._states;
            int i = values.length;
            while (true) {
                int i2 = i;
                i--;
                if (i2 > 0) {
                    if (states[i] == 1 && Arrays.binarySearch(array, values[i]) < 0) {
                        TDoubleLongHashMap.this.removeAt(i);
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
            TDoubleLongHashMap.this.clear();
        }

        @Override // gnu.trove.TLongCollection
        public boolean forEach(TLongProcedure procedure) {
            return TDoubleLongHashMap.this.forEachValue(procedure);
        }

        public String toString() {
            final StringBuilder buf = new StringBuilder("{");
            TDoubleLongHashMap.this.forEachValue(new TLongProcedure() { // from class: gnu.trove.map.hash.TDoubleLongHashMap.TValueView.1
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
    /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TDoubleLongHashMap$TDoubleLongKeyHashIterator.class */
    public class TDoubleLongKeyHashIterator extends THashPrimitiveIterator implements TDoubleIterator {
        TDoubleLongKeyHashIterator(TPrimitiveHash hash) {
            super(hash);
        }

        @Override // gnu.trove.iterator.TDoubleIterator
        public double next() {
            moveToNextIndex();
            return TDoubleLongHashMap.this._set[this._index];
        }

        @Override // gnu.trove.impl.hash.THashPrimitiveIterator, gnu.trove.iterator.TPrimitiveIterator, gnu.trove.iterator.TIterator, java.util.Iterator
        public void remove() {
            if (this._expectedSize != this._hash.size()) {
                throw new ConcurrentModificationException();
            }
            try {
                this._hash.tempDisableAutoCompaction();
                TDoubleLongHashMap.this.removeAt(this._index);
                this._hash.reenableAutoCompaction(false);
                this._expectedSize--;
            } catch (Throwable th) {
                this._hash.reenableAutoCompaction(false);
                throw th;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TDoubleLongHashMap$TDoubleLongValueHashIterator.class */
    public class TDoubleLongValueHashIterator extends THashPrimitiveIterator implements TLongIterator {
        TDoubleLongValueHashIterator(TPrimitiveHash hash) {
            super(hash);
        }

        @Override // gnu.trove.iterator.TLongIterator
        public long next() {
            moveToNextIndex();
            return TDoubleLongHashMap.this._values[this._index];
        }

        @Override // gnu.trove.impl.hash.THashPrimitiveIterator, gnu.trove.iterator.TPrimitiveIterator, gnu.trove.iterator.TIterator, java.util.Iterator
        public void remove() {
            if (this._expectedSize != this._hash.size()) {
                throw new ConcurrentModificationException();
            }
            try {
                this._hash.tempDisableAutoCompaction();
                TDoubleLongHashMap.this.removeAt(this._index);
                this._hash.reenableAutoCompaction(false);
                this._expectedSize--;
            } catch (Throwable th) {
                this._hash.reenableAutoCompaction(false);
                throw th;
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TDoubleLongHashMap$TDoubleLongHashIterator.class */
    class TDoubleLongHashIterator extends THashPrimitiveIterator implements TDoubleLongIterator {
        TDoubleLongHashIterator(TDoubleLongHashMap map) {
            super(map);
        }

        @Override // gnu.trove.iterator.TAdvancingIterator
        public void advance() {
            moveToNextIndex();
        }

        @Override // gnu.trove.iterator.TDoubleLongIterator
        public double key() {
            return TDoubleLongHashMap.this._set[this._index];
        }

        @Override // gnu.trove.iterator.TDoubleLongIterator
        public long value() {
            return TDoubleLongHashMap.this._values[this._index];
        }

        @Override // gnu.trove.iterator.TDoubleLongIterator
        public long setValue(long val) {
            long old = value();
            TDoubleLongHashMap.this._values[this._index] = val;
            return old;
        }

        @Override // gnu.trove.impl.hash.THashPrimitiveIterator, gnu.trove.iterator.TPrimitiveIterator, gnu.trove.iterator.TIterator, java.util.Iterator
        public void remove() {
            if (this._expectedSize != this._hash.size()) {
                throw new ConcurrentModificationException();
            }
            try {
                this._hash.tempDisableAutoCompaction();
                TDoubleLongHashMap.this.removeAt(this._index);
                this._hash.reenableAutoCompaction(false);
                this._expectedSize--;
            } catch (Throwable th) {
                this._hash.reenableAutoCompaction(false);
                throw th;
            }
        }
    }

    public boolean equals(Object other) {
        if (!(other instanceof TDoubleLongMap)) {
            return false;
        }
        TDoubleLongMap that = (TDoubleLongMap) other;
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
                    double key = this._set[i];
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
        forEachEntry(new TDoubleLongProcedure() { // from class: gnu.trove.map.hash.TDoubleLongHashMap.1
            private boolean first = true;

            @Override // gnu.trove.procedure.TDoubleLongProcedure
            public boolean execute(double key, long value) {
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

    @Override // gnu.trove.impl.hash.TDoubleLongHash, gnu.trove.impl.hash.THash, java.io.Externalizable
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
                    out.writeLong(this._values[i]);
                }
            } else {
                return;
            }
        }
    }

    @Override // gnu.trove.impl.hash.TDoubleLongHash, gnu.trove.impl.hash.THash, java.io.Externalizable
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
                long val = in.readLong();
                put(key, val);
            } else {
                return;
            }
        }
    }
}
