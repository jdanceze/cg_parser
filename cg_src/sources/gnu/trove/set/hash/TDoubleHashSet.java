package gnu.trove.set.hash;

import com.sun.xml.bind.v2.runtime.reflect.opt.Const;
import gnu.trove.TDoubleCollection;
import gnu.trove.impl.HashFunctions;
import gnu.trove.impl.hash.TDoubleHash;
import gnu.trove.impl.hash.THashPrimitiveIterator;
import gnu.trove.iterator.TDoubleIterator;
import gnu.trove.set.TDoubleSet;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Arrays;
import java.util.Collection;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/set/hash/TDoubleHashSet.class */
public class TDoubleHashSet extends TDoubleHash implements TDoubleSet, Externalizable {
    static final long serialVersionUID = 1;

    public TDoubleHashSet() {
    }

    public TDoubleHashSet(int initialCapacity) {
        super(initialCapacity);
    }

    public TDoubleHashSet(int initialCapacity, float load_factor) {
        super(initialCapacity, load_factor);
    }

    public TDoubleHashSet(int initial_capacity, float load_factor, double no_entry_value) {
        super(initial_capacity, load_factor, no_entry_value);
        if (no_entry_value != Const.default_value_double) {
            Arrays.fill(this._set, no_entry_value);
        }
    }

    public TDoubleHashSet(Collection<? extends Double> collection) {
        this(Math.max(collection.size(), 10));
        addAll(collection);
    }

    public TDoubleHashSet(TDoubleCollection collection) {
        this(Math.max(collection.size(), 10));
        if (collection instanceof TDoubleHashSet) {
            TDoubleHashSet hashset = (TDoubleHashSet) collection;
            this._loadFactor = hashset._loadFactor;
            this.no_entry_value = hashset.no_entry_value;
            if (this.no_entry_value != Const.default_value_double) {
                Arrays.fill(this._set, this.no_entry_value);
            }
            setUp((int) Math.ceil(10.0f / this._loadFactor));
        }
        addAll(collection);
    }

    public TDoubleHashSet(double[] array) {
        this(Math.max(array.length, 10));
        addAll(array);
    }

    @Override // gnu.trove.set.TDoubleSet, gnu.trove.TDoubleCollection
    public TDoubleIterator iterator() {
        return new TDoubleHashIterator(this);
    }

    @Override // gnu.trove.set.TDoubleSet, gnu.trove.TDoubleCollection
    public double[] toArray() {
        double[] result = new double[size()];
        double[] set = this._set;
        byte[] states = this._states;
        int i = states.length;
        int j = 0;
        while (true) {
            int i2 = i;
            i--;
            if (i2 > 0) {
                if (states[i] == 1) {
                    int i3 = j;
                    j++;
                    result[i3] = set[i];
                }
            } else {
                return result;
            }
        }
    }

    @Override // gnu.trove.set.TDoubleSet, gnu.trove.TDoubleCollection
    public double[] toArray(double[] dest) {
        double[] set = this._set;
        byte[] states = this._states;
        int i = states.length;
        int j = 0;
        while (true) {
            int i2 = i;
            i--;
            if (i2 <= 0) {
                break;
            } else if (states[i] == 1) {
                int i3 = j;
                j++;
                dest[i3] = set[i];
            }
        }
        if (dest.length > this._size) {
            dest[this._size] = this.no_entry_value;
        }
        return dest;
    }

    @Override // gnu.trove.set.TDoubleSet, gnu.trove.TDoubleCollection
    public boolean add(double val) {
        int index = insertKey(val);
        if (index < 0) {
            return false;
        }
        postInsertHook(this.consumeFreeSlot);
        return true;
    }

    @Override // gnu.trove.set.TDoubleSet, gnu.trove.TDoubleCollection
    public boolean remove(double val) {
        int index = index(val);
        if (index >= 0) {
            removeAt(index);
            return true;
        }
        return false;
    }

    @Override // gnu.trove.set.TDoubleSet, gnu.trove.TDoubleCollection
    public boolean containsAll(Collection<?> collection) {
        for (Object element : collection) {
            if (element instanceof Double) {
                double c = ((Double) element).doubleValue();
                if (!contains(c)) {
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
            double element = iter.next();
            if (!contains(element)) {
                return false;
            }
        }
        return true;
    }

    @Override // gnu.trove.set.TDoubleSet, gnu.trove.TDoubleCollection
    public boolean containsAll(double[] array) {
        int i = array.length;
        do {
            int i2 = i;
            i--;
            if (i2 <= 0) {
                return true;
            }
        } while (contains(array[i]));
        return false;
    }

    @Override // gnu.trove.set.TDoubleSet, gnu.trove.TDoubleCollection
    public boolean addAll(Collection<? extends Double> collection) {
        boolean changed = false;
        for (Double element : collection) {
            double e = element.doubleValue();
            if (add(e)) {
                changed = true;
            }
        }
        return changed;
    }

    @Override // gnu.trove.set.TDoubleSet, gnu.trove.TDoubleCollection
    public boolean addAll(TDoubleCollection collection) {
        boolean changed = false;
        TDoubleIterator iter = collection.iterator();
        while (iter.hasNext()) {
            double element = iter.next();
            if (add(element)) {
                changed = true;
            }
        }
        return changed;
    }

    @Override // gnu.trove.set.TDoubleSet, gnu.trove.TDoubleCollection
    public boolean addAll(double[] array) {
        boolean changed = false;
        int i = array.length;
        while (true) {
            int i2 = i;
            i--;
            if (i2 > 0) {
                if (add(array[i])) {
                    changed = true;
                }
            } else {
                return changed;
            }
        }
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
        double[] set = this._set;
        byte[] states = this._states;
        this._autoCompactTemporaryDisable = true;
        int i = set.length;
        while (true) {
            int i2 = i;
            i--;
            if (i2 > 0) {
                if (states[i] == 1 && Arrays.binarySearch(array, set[i]) < 0) {
                    removeAt(i);
                    changed = true;
                }
            } else {
                this._autoCompactTemporaryDisable = false;
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

    @Override // gnu.trove.impl.hash.THash, gnu.trove.map.TObjectByteMap
    public void clear() {
        super.clear();
        double[] set = this._set;
        byte[] states = this._states;
        int i = set.length;
        while (true) {
            int i2 = i;
            i--;
            if (i2 > 0) {
                set[i] = this.no_entry_value;
                states[i] = 0;
            } else {
                return;
            }
        }
    }

    @Override // gnu.trove.impl.hash.THash
    protected void rehash(int newCapacity) {
        int oldCapacity = this._set.length;
        double[] oldSet = this._set;
        byte[] oldStates = this._states;
        this._set = new double[newCapacity];
        this._states = new byte[newCapacity];
        int i = oldCapacity;
        while (true) {
            int i2 = i;
            i--;
            if (i2 > 0) {
                if (oldStates[i] == 1) {
                    double o = oldSet[i];
                    insertKey(o);
                }
            } else {
                return;
            }
        }
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
        int i = this._states.length;
        while (true) {
            int i2 = i;
            i--;
            if (i2 > 0) {
                if (this._states[i] == 1 && !that.contains(this._set[i])) {
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
        int i = this._states.length;
        while (true) {
            int i2 = i;
            i--;
            if (i2 > 0) {
                if (this._states[i] == 1) {
                    hashcode += HashFunctions.hash(this._set[i]);
                }
            } else {
                return hashcode;
            }
        }
    }

    public String toString() {
        StringBuilder buffy = new StringBuilder((this._size * 2) + 2);
        buffy.append("{");
        int i = this._states.length;
        int j = 1;
        while (true) {
            int i2 = i;
            i--;
            if (i2 > 0) {
                if (this._states[i] == 1) {
                    buffy.append(this._set[i]);
                    int i3 = j;
                    j++;
                    if (i3 < this._size) {
                        buffy.append(",");
                    }
                }
            } else {
                buffy.append("}");
                return buffy.toString();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/set/hash/TDoubleHashSet$TDoubleHashIterator.class */
    public class TDoubleHashIterator extends THashPrimitiveIterator implements TDoubleIterator {
        private final TDoubleHash _hash;

        public TDoubleHashIterator(TDoubleHash hash) {
            super(hash);
            this._hash = hash;
        }

        @Override // gnu.trove.iterator.TDoubleIterator
        public double next() {
            moveToNextIndex();
            return this._hash._set[this._index];
        }
    }

    @Override // gnu.trove.impl.hash.THash, java.io.Externalizable
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeByte(1);
        super.writeExternal(out);
        out.writeInt(this._size);
        out.writeFloat(this._loadFactor);
        out.writeDouble(this.no_entry_value);
        int i = this._states.length;
        while (true) {
            int i2 = i;
            i--;
            if (i2 > 0) {
                if (this._states[i] == 1) {
                    out.writeDouble(this._set[i]);
                }
            } else {
                return;
            }
        }
    }

    @Override // gnu.trove.impl.hash.THash, java.io.Externalizable
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        int version = in.readByte();
        super.readExternal(in);
        int size = in.readInt();
        if (version >= 1) {
            this._loadFactor = in.readFloat();
            this.no_entry_value = in.readDouble();
            if (this.no_entry_value != Const.default_value_double) {
                Arrays.fill(this._set, this.no_entry_value);
            }
        }
        setUp(size);
        while (true) {
            int i = size;
            size--;
            if (i > 0) {
                double val = in.readDouble();
                add(val);
            } else {
                return;
            }
        }
    }
}
