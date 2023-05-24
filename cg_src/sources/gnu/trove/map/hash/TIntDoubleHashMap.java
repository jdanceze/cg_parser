package gnu.trove.map.hash;

import com.sun.xml.bind.v2.runtime.reflect.opt.Const;
import gnu.trove.TDoubleCollection;
import gnu.trove.TIntCollection;
import gnu.trove.function.TDoubleFunction;
import gnu.trove.impl.HashFunctions;
import gnu.trove.impl.hash.THashPrimitiveIterator;
import gnu.trove.impl.hash.TIntDoubleHash;
import gnu.trove.impl.hash.TPrimitiveHash;
import gnu.trove.iterator.TDoubleIterator;
import gnu.trove.iterator.TIntDoubleIterator;
import gnu.trove.iterator.TIntIterator;
import gnu.trove.map.TIntDoubleMap;
import gnu.trove.procedure.TDoubleProcedure;
import gnu.trove.procedure.TIntDoubleProcedure;
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
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TIntDoubleHashMap.class */
public class TIntDoubleHashMap extends TIntDoubleHash implements TIntDoubleMap, Externalizable {
    static final long serialVersionUID = 1;
    protected transient double[] _values;

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: JavaClassParseException: Unknown opcode: 0x5e in method: gnu.trove.map.hash.TIntDoubleHashMap.adjustOrPutValue(int, double, double):double, file: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TIntDoubleHashMap.class
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
    @Override // gnu.trove.map.TIntDoubleMap
    public double adjustOrPutValue(int r1, double r2, double r4) {
        /*
        // Can't load method instructions: Load method exception: JavaClassParseException: Unknown opcode: 0x5e in method: gnu.trove.map.hash.TIntDoubleHashMap.adjustOrPutValue(int, double, double):double, file: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TIntDoubleHashMap.class
        */
        throw new UnsupportedOperationException("Method not decompiled: gnu.trove.map.hash.TIntDoubleHashMap.adjustOrPutValue(int, double, double):double");
    }

    public TIntDoubleHashMap() {
    }

    public TIntDoubleHashMap(int initialCapacity) {
        super(initialCapacity);
    }

    public TIntDoubleHashMap(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    public TIntDoubleHashMap(int initialCapacity, float loadFactor, int noEntryKey, double noEntryValue) {
        super(initialCapacity, loadFactor, noEntryKey, noEntryValue);
    }

    public TIntDoubleHashMap(int[] keys, double[] values) {
        super(Math.max(keys.length, values.length));
        int size = Math.min(keys.length, values.length);
        for (int i = 0; i < size; i++) {
            put(keys[i], values[i]);
        }
    }

    public TIntDoubleHashMap(TIntDoubleMap map) {
        super(map.size());
        if (map instanceof TIntDoubleHashMap) {
            TIntDoubleHashMap hashmap = (TIntDoubleHashMap) map;
            this._loadFactor = hashmap._loadFactor;
            this.no_entry_key = hashmap.no_entry_key;
            this.no_entry_value = hashmap.no_entry_value;
            if (this.no_entry_key != 0) {
                Arrays.fill(this._set, this.no_entry_key);
            }
            if (this.no_entry_value != Const.default_value_double) {
                Arrays.fill(this._values, this.no_entry_value);
            }
            setUp((int) Math.ceil(10.0f / this._loadFactor));
        }
        putAll(map);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // gnu.trove.impl.hash.TIntDoubleHash, gnu.trove.impl.hash.TPrimitiveHash, gnu.trove.impl.hash.THash
    public int setUp(int initialCapacity) {
        int capacity = super.setUp(initialCapacity);
        this._values = new double[capacity];
        return capacity;
    }

    @Override // gnu.trove.impl.hash.THash
    protected void rehash(int newCapacity) {
        int oldCapacity = this._set.length;
        int[] oldKeys = this._set;
        double[] oldVals = this._values;
        byte[] oldStates = this._states;
        this._set = new int[newCapacity];
        this._values = new double[newCapacity];
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

    @Override // gnu.trove.map.TIntDoubleMap
    public double put(int key, double value) {
        int index = insertKey(key);
        return doPut(key, value, index);
    }

    @Override // gnu.trove.map.TIntDoubleMap
    public double putIfAbsent(int key, double value) {
        int index = insertKey(key);
        if (index < 0) {
            return this._values[(-index) - 1];
        }
        return doPut(key, value, index);
    }

    private double doPut(int key, double value, int index) {
        double previous = this.no_entry_value;
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

    @Override // gnu.trove.map.TIntDoubleMap
    public void putAll(Map<? extends Integer, ? extends Double> map) {
        ensureCapacity(map.size());
        for (Map.Entry<? extends Integer, ? extends Double> entry : map.entrySet()) {
            put(entry.getKey().intValue(), entry.getValue().doubleValue());
        }
    }

    @Override // gnu.trove.map.TIntDoubleMap
    public void putAll(TIntDoubleMap map) {
        ensureCapacity(map.size());
        TIntDoubleIterator iter = map.iterator();
        while (iter.hasNext()) {
            iter.advance();
            put(iter.key(), iter.value());
        }
    }

    @Override // gnu.trove.map.TIntDoubleMap
    public double get(int key) {
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

    @Override // gnu.trove.map.TIntDoubleMap
    public double remove(int key) {
        double prev = this.no_entry_value;
        int index = index(key);
        if (index >= 0) {
            prev = this._values[index];
            removeAt(index);
        }
        return prev;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // gnu.trove.impl.hash.TIntDoubleHash, gnu.trove.impl.hash.TPrimitiveHash, gnu.trove.impl.hash.THash
    public void removeAt(int index) {
        this._values[index] = this.no_entry_value;
        super.removeAt(index);
    }

    @Override // gnu.trove.map.TIntDoubleMap
    public TIntSet keySet() {
        return new TKeyView();
    }

    @Override // gnu.trove.map.TIntDoubleMap
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

    @Override // gnu.trove.map.TIntDoubleMap
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

    @Override // gnu.trove.map.TIntDoubleMap
    public TDoubleCollection valueCollection() {
        return new TValueView();
    }

    @Override // gnu.trove.map.TIntDoubleMap
    public double[] values() {
        double[] vals = new double[size()];
        double[] v = this._values;
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

    @Override // gnu.trove.map.TIntDoubleMap
    public double[] values(double[] array) {
        int size = size();
        if (array.length < size) {
            array = new double[size];
        }
        double[] v = this._values;
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

    @Override // gnu.trove.map.TIntDoubleMap
    public boolean containsValue(double val) {
        byte[] states = this._states;
        double[] vals = this._values;
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

    @Override // gnu.trove.map.TIntDoubleMap
    public boolean containsKey(int key) {
        return contains(key);
    }

    @Override // gnu.trove.map.TIntDoubleMap
    public TIntDoubleIterator iterator() {
        return new TIntDoubleHashIterator(this);
    }

    @Override // gnu.trove.map.TIntDoubleMap
    public boolean forEachKey(TIntProcedure procedure) {
        return forEach(procedure);
    }

    @Override // gnu.trove.map.TIntDoubleMap
    public boolean forEachValue(TDoubleProcedure procedure) {
        byte[] states = this._states;
        double[] values = this._values;
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

    @Override // gnu.trove.map.TIntDoubleMap
    public boolean forEachEntry(TIntDoubleProcedure procedure) {
        byte[] states = this._states;
        int[] keys = this._set;
        double[] values = this._values;
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

    @Override // gnu.trove.map.TIntDoubleMap
    public void transformValues(TDoubleFunction function) {
        byte[] states = this._states;
        double[] values = this._values;
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

    @Override // gnu.trove.map.TIntDoubleMap
    public boolean retainEntries(TIntDoubleProcedure procedure) {
        boolean modified = false;
        byte[] states = this._states;
        int[] keys = this._set;
        double[] values = this._values;
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

    @Override // gnu.trove.map.TIntDoubleMap
    public boolean increment(int key) {
        return adjustValue(key, 1.0d);
    }

    @Override // gnu.trove.map.TIntDoubleMap
    public boolean adjustValue(int key, double amount) {
        int index = index(key);
        if (index < 0) {
            return false;
        }
        double[] dArr = this._values;
        dArr[index] = dArr[index] + amount;
        return true;
    }

    /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TIntDoubleHashMap$TKeyView.class */
    protected class TKeyView implements TIntSet {
        protected TKeyView() {
        }

        @Override // gnu.trove.set.TIntSet, gnu.trove.TIntCollection
        public TIntIterator iterator() {
            return new TIntDoubleKeyHashIterator(TIntDoubleHashMap.this);
        }

        @Override // gnu.trove.set.TIntSet, gnu.trove.TIntCollection
        public int getNoEntryValue() {
            return TIntDoubleHashMap.this.no_entry_key;
        }

        @Override // gnu.trove.set.TIntSet, gnu.trove.TIntCollection
        public int size() {
            return TIntDoubleHashMap.this._size;
        }

        @Override // gnu.trove.set.TIntSet, gnu.trove.TIntCollection
        public boolean isEmpty() {
            return 0 == TIntDoubleHashMap.this._size;
        }

        @Override // gnu.trove.set.TIntSet, gnu.trove.TIntCollection
        public boolean contains(int entry) {
            return TIntDoubleHashMap.this.contains(entry);
        }

        @Override // gnu.trove.set.TIntSet, gnu.trove.TIntCollection
        public int[] toArray() {
            return TIntDoubleHashMap.this.keys();
        }

        @Override // gnu.trove.set.TIntSet, gnu.trove.TIntCollection
        public int[] toArray(int[] dest) {
            return TIntDoubleHashMap.this.keys(dest);
        }

        @Override // gnu.trove.set.TIntSet, gnu.trove.TIntCollection
        public boolean add(int entry) {
            throw new UnsupportedOperationException();
        }

        @Override // gnu.trove.set.TIntSet, gnu.trove.TIntCollection
        public boolean remove(int entry) {
            return TIntDoubleHashMap.this.no_entry_value != TIntDoubleHashMap.this.remove(entry);
        }

        @Override // gnu.trove.set.TIntSet, gnu.trove.TIntCollection
        public boolean containsAll(Collection<?> collection) {
            for (Object element : collection) {
                if (element instanceof Integer) {
                    int ele = ((Integer) element).intValue();
                    if (!TIntDoubleHashMap.this.containsKey(ele)) {
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
                if (!TIntDoubleHashMap.this.containsKey(iter.next())) {
                    return false;
                }
            }
            return true;
        }

        @Override // gnu.trove.set.TIntSet, gnu.trove.TIntCollection
        public boolean containsAll(int[] array) {
            for (int element : array) {
                if (!TIntDoubleHashMap.this.contains(element)) {
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
            int[] set = TIntDoubleHashMap.this._set;
            byte[] states = TIntDoubleHashMap.this._states;
            int i = set.length;
            while (true) {
                int i2 = i;
                i--;
                if (i2 > 0) {
                    if (states[i] == 1 && Arrays.binarySearch(array, set[i]) < 0) {
                        TIntDoubleHashMap.this.removeAt(i);
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
            TIntDoubleHashMap.this.clear();
        }

        @Override // gnu.trove.set.TIntSet, gnu.trove.TIntCollection
        public boolean forEach(TIntProcedure procedure) {
            return TIntDoubleHashMap.this.forEachKey(procedure);
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
            int i = TIntDoubleHashMap.this._states.length;
            while (true) {
                int i2 = i;
                i--;
                if (i2 > 0) {
                    if (TIntDoubleHashMap.this._states[i] == 1 && !that.contains(TIntDoubleHashMap.this._set[i])) {
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
            int i = TIntDoubleHashMap.this._states.length;
            while (true) {
                int i2 = i;
                i--;
                if (i2 > 0) {
                    if (TIntDoubleHashMap.this._states[i] == 1) {
                        hashcode += HashFunctions.hash(TIntDoubleHashMap.this._set[i]);
                    }
                } else {
                    return hashcode;
                }
            }
        }

        public String toString() {
            final StringBuilder buf = new StringBuilder("{");
            TIntDoubleHashMap.this.forEachKey(new TIntProcedure() { // from class: gnu.trove.map.hash.TIntDoubleHashMap.TKeyView.1
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

    /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TIntDoubleHashMap$TValueView.class */
    protected class TValueView implements TDoubleCollection {
        protected TValueView() {
        }

        @Override // gnu.trove.TDoubleCollection
        public TDoubleIterator iterator() {
            return new TIntDoubleValueHashIterator(TIntDoubleHashMap.this);
        }

        @Override // gnu.trove.TDoubleCollection
        public double getNoEntryValue() {
            return TIntDoubleHashMap.this.no_entry_value;
        }

        @Override // gnu.trove.TDoubleCollection
        public int size() {
            return TIntDoubleHashMap.this._size;
        }

        @Override // gnu.trove.TDoubleCollection
        public boolean isEmpty() {
            return 0 == TIntDoubleHashMap.this._size;
        }

        @Override // gnu.trove.TDoubleCollection
        public boolean contains(double entry) {
            return TIntDoubleHashMap.this.containsValue(entry);
        }

        @Override // gnu.trove.TDoubleCollection
        public double[] toArray() {
            return TIntDoubleHashMap.this.values();
        }

        @Override // gnu.trove.TDoubleCollection
        public double[] toArray(double[] dest) {
            return TIntDoubleHashMap.this.values(dest);
        }

        @Override // gnu.trove.TDoubleCollection
        public boolean add(double entry) {
            throw new UnsupportedOperationException();
        }

        @Override // gnu.trove.TDoubleCollection
        public boolean remove(double entry) {
            double[] values = TIntDoubleHashMap.this._values;
            int[] set = TIntDoubleHashMap.this._set;
            int i = values.length;
            while (true) {
                int i2 = i;
                i--;
                if (i2 > 0) {
                    if (set[i] != 0 && set[i] != 2 && entry == values[i]) {
                        TIntDoubleHashMap.this.removeAt(i);
                        return true;
                    }
                } else {
                    return false;
                }
            }
        }

        @Override // gnu.trove.TDoubleCollection
        public boolean containsAll(Collection<?> collection) {
            for (Object element : collection) {
                if (element instanceof Double) {
                    double ele = ((Double) element).doubleValue();
                    if (!TIntDoubleHashMap.this.containsValue(ele)) {
                        return false;
                    }
                } else {
                    return false;
                }
            }
            return true;
        }

        @Override // gnu.trove.TDoubleCollection
        public boolean containsAll(TDoubleCollection collection) {
            TDoubleIterator iter = collection.iterator();
            while (iter.hasNext()) {
                if (!TIntDoubleHashMap.this.containsValue(iter.next())) {
                    return false;
                }
            }
            return true;
        }

        @Override // gnu.trove.TDoubleCollection
        public boolean containsAll(double[] array) {
            for (double element : array) {
                if (!TIntDoubleHashMap.this.containsValue(element)) {
                    return false;
                }
            }
            return true;
        }

        @Override // gnu.trove.TDoubleCollection
        public boolean addAll(Collection<? extends Double> collection) {
            throw new UnsupportedOperationException();
        }

        @Override // gnu.trove.TDoubleCollection
        public boolean addAll(TDoubleCollection collection) {
            throw new UnsupportedOperationException();
        }

        @Override // gnu.trove.TDoubleCollection
        public boolean addAll(double[] array) {
            throw new UnsupportedOperationException();
        }

        @Override // gnu.trove.TDoubleCollection
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

        @Override // gnu.trove.TDoubleCollection
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

        @Override // gnu.trove.TDoubleCollection
        public boolean retainAll(double[] array) {
            boolean changed = false;
            Arrays.sort(array);
            double[] values = TIntDoubleHashMap.this._values;
            byte[] states = TIntDoubleHashMap.this._states;
            int i = values.length;
            while (true) {
                int i2 = i;
                i--;
                if (i2 > 0) {
                    if (states[i] == 1 && Arrays.binarySearch(array, values[i]) < 0) {
                        TIntDoubleHashMap.this.removeAt(i);
                        changed = true;
                    }
                } else {
                    return changed;
                }
            }
        }

        @Override // gnu.trove.TDoubleCollection
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

        @Override // gnu.trove.TDoubleCollection
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

        @Override // gnu.trove.TDoubleCollection
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

        @Override // gnu.trove.TDoubleCollection
        public void clear() {
            TIntDoubleHashMap.this.clear();
        }

        @Override // gnu.trove.TDoubleCollection
        public boolean forEach(TDoubleProcedure procedure) {
            return TIntDoubleHashMap.this.forEachValue(procedure);
        }

        public String toString() {
            final StringBuilder buf = new StringBuilder("{");
            TIntDoubleHashMap.this.forEachValue(new TDoubleProcedure() { // from class: gnu.trove.map.hash.TIntDoubleHashMap.TValueView.1
                private boolean first = true;

                @Override // gnu.trove.procedure.TDoubleProcedure
                public boolean execute(double value) {
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
    /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TIntDoubleHashMap$TIntDoubleKeyHashIterator.class */
    public class TIntDoubleKeyHashIterator extends THashPrimitiveIterator implements TIntIterator {
        TIntDoubleKeyHashIterator(TPrimitiveHash hash) {
            super(hash);
        }

        @Override // gnu.trove.iterator.TIntIterator
        public int next() {
            moveToNextIndex();
            return TIntDoubleHashMap.this._set[this._index];
        }

        @Override // gnu.trove.impl.hash.THashPrimitiveIterator, gnu.trove.iterator.TPrimitiveIterator, gnu.trove.iterator.TIterator, java.util.Iterator
        public void remove() {
            if (this._expectedSize != this._hash.size()) {
                throw new ConcurrentModificationException();
            }
            try {
                this._hash.tempDisableAutoCompaction();
                TIntDoubleHashMap.this.removeAt(this._index);
                this._hash.reenableAutoCompaction(false);
                this._expectedSize--;
            } catch (Throwable th) {
                this._hash.reenableAutoCompaction(false);
                throw th;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TIntDoubleHashMap$TIntDoubleValueHashIterator.class */
    public class TIntDoubleValueHashIterator extends THashPrimitiveIterator implements TDoubleIterator {
        TIntDoubleValueHashIterator(TPrimitiveHash hash) {
            super(hash);
        }

        @Override // gnu.trove.iterator.TDoubleIterator
        public double next() {
            moveToNextIndex();
            return TIntDoubleHashMap.this._values[this._index];
        }

        @Override // gnu.trove.impl.hash.THashPrimitiveIterator, gnu.trove.iterator.TPrimitiveIterator, gnu.trove.iterator.TIterator, java.util.Iterator
        public void remove() {
            if (this._expectedSize != this._hash.size()) {
                throw new ConcurrentModificationException();
            }
            try {
                this._hash.tempDisableAutoCompaction();
                TIntDoubleHashMap.this.removeAt(this._index);
                this._hash.reenableAutoCompaction(false);
                this._expectedSize--;
            } catch (Throwable th) {
                this._hash.reenableAutoCompaction(false);
                throw th;
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TIntDoubleHashMap$TIntDoubleHashIterator.class */
    class TIntDoubleHashIterator extends THashPrimitiveIterator implements TIntDoubleIterator {
        TIntDoubleHashIterator(TIntDoubleHashMap map) {
            super(map);
        }

        @Override // gnu.trove.iterator.TAdvancingIterator
        public void advance() {
            moveToNextIndex();
        }

        @Override // gnu.trove.iterator.TIntDoubleIterator
        public int key() {
            return TIntDoubleHashMap.this._set[this._index];
        }

        @Override // gnu.trove.iterator.TIntDoubleIterator
        public double value() {
            return TIntDoubleHashMap.this._values[this._index];
        }

        @Override // gnu.trove.iterator.TIntDoubleIterator
        public double setValue(double val) {
            double old = value();
            TIntDoubleHashMap.this._values[this._index] = val;
            return old;
        }

        @Override // gnu.trove.impl.hash.THashPrimitiveIterator, gnu.trove.iterator.TPrimitiveIterator, gnu.trove.iterator.TIterator, java.util.Iterator
        public void remove() {
            if (this._expectedSize != this._hash.size()) {
                throw new ConcurrentModificationException();
            }
            try {
                this._hash.tempDisableAutoCompaction();
                TIntDoubleHashMap.this.removeAt(this._index);
                this._hash.reenableAutoCompaction(false);
                this._expectedSize--;
            } catch (Throwable th) {
                this._hash.reenableAutoCompaction(false);
                throw th;
            }
        }
    }

    public boolean equals(Object other) {
        if (!(other instanceof TIntDoubleMap)) {
            return false;
        }
        TIntDoubleMap that = (TIntDoubleMap) other;
        if (that.size() != size()) {
            return false;
        }
        double[] values = this._values;
        byte[] states = this._states;
        double this_no_entry_value = getNoEntryValue();
        double that_no_entry_value = that.getNoEntryValue();
        int i = values.length;
        while (true) {
            int i2 = i;
            i--;
            if (i2 > 0) {
                if (states[i] == 1) {
                    int key = this._set[i];
                    double that_value = that.get(key);
                    double this_value = values[i];
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
        forEachEntry(new TIntDoubleProcedure() { // from class: gnu.trove.map.hash.TIntDoubleHashMap.1
            private boolean first = true;

            @Override // gnu.trove.procedure.TIntDoubleProcedure
            public boolean execute(int key, double value) {
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

    @Override // gnu.trove.impl.hash.TIntDoubleHash, gnu.trove.impl.hash.THash, java.io.Externalizable
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
                    out.writeDouble(this._values[i]);
                }
            } else {
                return;
            }
        }
    }

    @Override // gnu.trove.impl.hash.TIntDoubleHash, gnu.trove.impl.hash.THash, java.io.Externalizable
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
                double val = in.readDouble();
                put(key, val);
            } else {
                return;
            }
        }
    }
}
