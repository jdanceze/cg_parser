package gnu.trove.set.hash;

import gnu.trove.TShortCollection;
import gnu.trove.impl.HashFunctions;
import gnu.trove.impl.hash.THashPrimitiveIterator;
import gnu.trove.impl.hash.TShortHash;
import gnu.trove.iterator.TShortIterator;
import gnu.trove.set.TShortSet;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Arrays;
import java.util.Collection;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/set/hash/TShortHashSet.class */
public class TShortHashSet extends TShortHash implements TShortSet, Externalizable {
    static final long serialVersionUID = 1;

    public TShortHashSet() {
    }

    public TShortHashSet(int initialCapacity) {
        super(initialCapacity);
    }

    public TShortHashSet(int initialCapacity, float load_factor) {
        super(initialCapacity, load_factor);
    }

    public TShortHashSet(int initial_capacity, float load_factor, short no_entry_value) {
        super(initial_capacity, load_factor, no_entry_value);
        if (no_entry_value != 0) {
            Arrays.fill(this._set, no_entry_value);
        }
    }

    public TShortHashSet(Collection<? extends Short> collection) {
        this(Math.max(collection.size(), 10));
        addAll(collection);
    }

    public TShortHashSet(TShortCollection collection) {
        this(Math.max(collection.size(), 10));
        if (collection instanceof TShortHashSet) {
            TShortHashSet hashset = (TShortHashSet) collection;
            this._loadFactor = hashset._loadFactor;
            this.no_entry_value = hashset.no_entry_value;
            if (this.no_entry_value != 0) {
                Arrays.fill(this._set, this.no_entry_value);
            }
            setUp((int) Math.ceil(10.0f / this._loadFactor));
        }
        addAll(collection);
    }

    public TShortHashSet(short[] array) {
        this(Math.max(array.length, 10));
        addAll(array);
    }

    @Override // gnu.trove.set.TShortSet, gnu.trove.TShortCollection
    public TShortIterator iterator() {
        return new TShortHashIterator(this);
    }

    @Override // gnu.trove.set.TShortSet, gnu.trove.TShortCollection
    public short[] toArray() {
        short[] result = new short[size()];
        short[] set = this._set;
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

    @Override // gnu.trove.set.TShortSet, gnu.trove.TShortCollection
    public short[] toArray(short[] dest) {
        short[] set = this._set;
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

    @Override // gnu.trove.set.TShortSet, gnu.trove.TShortCollection
    public boolean add(short val) {
        int index = insertKey(val);
        if (index < 0) {
            return false;
        }
        postInsertHook(this.consumeFreeSlot);
        return true;
    }

    @Override // gnu.trove.set.TShortSet, gnu.trove.TShortCollection
    public boolean remove(short val) {
        int index = index(val);
        if (index >= 0) {
            removeAt(index);
            return true;
        }
        return false;
    }

    @Override // gnu.trove.set.TShortSet, gnu.trove.TShortCollection
    public boolean containsAll(Collection<?> collection) {
        for (Object element : collection) {
            if (element instanceof Short) {
                short c = ((Short) element).shortValue();
                if (!contains(c)) {
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
            short element = iter.next();
            if (!contains(element)) {
                return false;
            }
        }
        return true;
    }

    @Override // gnu.trove.set.TShortSet, gnu.trove.TShortCollection
    public boolean containsAll(short[] array) {
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

    @Override // gnu.trove.set.TShortSet, gnu.trove.TShortCollection
    public boolean addAll(Collection<? extends Short> collection) {
        boolean changed = false;
        for (Short element : collection) {
            short e = element.shortValue();
            if (add(e)) {
                changed = true;
            }
        }
        return changed;
    }

    @Override // gnu.trove.set.TShortSet, gnu.trove.TShortCollection
    public boolean addAll(TShortCollection collection) {
        boolean changed = false;
        TShortIterator iter = collection.iterator();
        while (iter.hasNext()) {
            short element = iter.next();
            if (add(element)) {
                changed = true;
            }
        }
        return changed;
    }

    @Override // gnu.trove.set.TShortSet, gnu.trove.TShortCollection
    public boolean addAll(short[] array) {
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
        short[] set = this._set;
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

    @Override // gnu.trove.impl.hash.THash, gnu.trove.map.TObjectByteMap
    public void clear() {
        super.clear();
        short[] set = this._set;
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
        short[] oldSet = this._set;
        byte[] oldStates = this._states;
        this._set = new short[newCapacity];
        this._states = new byte[newCapacity];
        int i = oldCapacity;
        while (true) {
            int i2 = i;
            i--;
            if (i2 > 0) {
                if (oldStates[i] == 1) {
                    short o = oldSet[i];
                    insertKey(o);
                }
            } else {
                return;
            }
        }
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

    @Override // gnu.trove.set.TShortSet, gnu.trove.TShortCollection
    public int hashCode() {
        int hashcode = 0;
        int i = this._states.length;
        while (true) {
            int i2 = i;
            i--;
            if (i2 > 0) {
                if (this._states[i] == 1) {
                    hashcode += HashFunctions.hash((int) this._set[i]);
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
                    buffy.append((int) this._set[i]);
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
    /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/set/hash/TShortHashSet$TShortHashIterator.class */
    public class TShortHashIterator extends THashPrimitiveIterator implements TShortIterator {
        private final TShortHash _hash;

        public TShortHashIterator(TShortHash hash) {
            super(hash);
            this._hash = hash;
        }

        @Override // gnu.trove.iterator.TShortIterator
        public short next() {
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
        out.writeShort(this.no_entry_value);
        int i = this._states.length;
        while (true) {
            int i2 = i;
            i--;
            if (i2 > 0) {
                if (this._states[i] == 1) {
                    out.writeShort(this._set[i]);
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
            this.no_entry_value = in.readShort();
            if (this.no_entry_value != 0) {
                Arrays.fill(this._set, this.no_entry_value);
            }
        }
        setUp(size);
        while (true) {
            int i = size;
            size--;
            if (i > 0) {
                short val = in.readShort();
                add(val);
            } else {
                return;
            }
        }
    }
}
