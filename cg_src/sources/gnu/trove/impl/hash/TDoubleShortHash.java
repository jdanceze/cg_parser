package gnu.trove.impl.hash;

import com.sun.xml.bind.v2.runtime.reflect.opt.Const;
import gnu.trove.impl.HashFunctions;
import gnu.trove.procedure.TDoubleProcedure;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/impl/hash/TDoubleShortHash.class */
public abstract class TDoubleShortHash extends TPrimitiveHash {
    static final long serialVersionUID = 1;
    public transient double[] _set;
    protected double no_entry_key;
    protected short no_entry_value;
    protected boolean consumeFreeSlot;

    public TDoubleShortHash() {
        this.no_entry_key = Const.default_value_double;
        this.no_entry_value = (short) 0;
    }

    public TDoubleShortHash(int initialCapacity) {
        super(initialCapacity);
        this.no_entry_key = Const.default_value_double;
        this.no_entry_value = (short) 0;
    }

    public TDoubleShortHash(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
        this.no_entry_key = Const.default_value_double;
        this.no_entry_value = (short) 0;
    }

    public TDoubleShortHash(int initialCapacity, float loadFactor, double no_entry_key, short no_entry_value) {
        super(initialCapacity, loadFactor);
        this.no_entry_key = no_entry_key;
        this.no_entry_value = no_entry_value;
    }

    public double getNoEntryKey() {
        return this.no_entry_key;
    }

    public short getNoEntryValue() {
        return this.no_entry_value;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // gnu.trove.impl.hash.TPrimitiveHash, gnu.trove.impl.hash.THash
    public int setUp(int initialCapacity) {
        int capacity = super.setUp(initialCapacity);
        this._set = new double[capacity];
        return capacity;
    }

    public boolean contains(double val) {
        return index(val) >= 0;
    }

    public boolean forEach(TDoubleProcedure procedure) {
        byte[] states = this._states;
        double[] set = this._set;
        int i = set.length;
        while (true) {
            int i2 = i;
            i--;
            if (i2 > 0) {
                if (states[i] == 1 && !procedure.execute(set[i])) {
                    return false;
                }
            } else {
                return true;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // gnu.trove.impl.hash.TPrimitiveHash, gnu.trove.impl.hash.THash
    public void removeAt(int index) {
        this._set[index] = this.no_entry_key;
        super.removeAt(index);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int index(double key) {
        byte[] states = this._states;
        double[] set = this._set;
        int length = states.length;
        int hash = HashFunctions.hash(key) & Integer.MAX_VALUE;
        int index = hash % length;
        byte state = states[index];
        if (state == 0) {
            return -1;
        }
        if (state == 1 && set[index] == key) {
            return index;
        }
        return indexRehashed(key, index, hash, state);
    }

    int indexRehashed(double key, int index, int hash, byte state) {
        int length = this._set.length;
        int probe = 1 + (hash % (length - 2));
        do {
            index -= probe;
            if (index < 0) {
                index += length;
            }
            byte state2 = this._states[index];
            if (state2 == 0) {
                return -1;
            }
            if (key == this._set[index] && state2 != 2) {
                return index;
            }
        } while (index != index);
        return -1;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int insertKey(double val) {
        int hash = HashFunctions.hash(val) & Integer.MAX_VALUE;
        int index = hash % this._states.length;
        byte state = this._states[index];
        this.consumeFreeSlot = false;
        if (state == 0) {
            this.consumeFreeSlot = true;
            insertKeyAt(index, val);
            return index;
        } else if (state == 1 && this._set[index] == val) {
            return (-index) - 1;
        } else {
            return insertKeyRehash(val, index, hash, state);
        }
    }

    int insertKeyRehash(double val, int index, int hash, byte state) {
        int length = this._set.length;
        int probe = 1 + (hash % (length - 2));
        int firstRemoved = -1;
        do {
            if (state == 2 && firstRemoved == -1) {
                firstRemoved = index;
            }
            index -= probe;
            if (index < 0) {
                index += length;
            }
            state = this._states[index];
            if (state == 0) {
                if (firstRemoved != -1) {
                    insertKeyAt(firstRemoved, val);
                    return firstRemoved;
                }
                this.consumeFreeSlot = true;
                insertKeyAt(index, val);
                return index;
            } else if (state == 1 && this._set[index] == val) {
                return (-index) - 1;
            }
        } while (index != index);
        if (firstRemoved != -1) {
            insertKeyAt(firstRemoved, val);
            return firstRemoved;
        }
        throw new IllegalStateException("No free or removed slots available. Key set full?!!");
    }

    void insertKeyAt(int index, double val) {
        this._set[index] = val;
        this._states[index] = 1;
    }

    protected int XinsertKey(double key) {
        byte[] states = this._states;
        double[] set = this._set;
        int length = states.length;
        int hash = HashFunctions.hash(key) & Integer.MAX_VALUE;
        int index = hash % length;
        byte state = states[index];
        this.consumeFreeSlot = false;
        if (state == 0) {
            this.consumeFreeSlot = true;
            set[index] = key;
            states[index] = 1;
            return index;
        } else if (state == 1 && set[index] == key) {
            return (-index) - 1;
        } else {
            int probe = 1 + (hash % (length - 2));
            if (state != 2) {
                do {
                    index -= probe;
                    if (index < 0) {
                        index += length;
                    }
                    state = states[index];
                    if (state != 1) {
                        break;
                    }
                } while (set[index] != key);
            }
            if (state == 2) {
                int firstRemoved = index;
                while (state != 0 && (state == 2 || set[index] != key)) {
                    index -= probe;
                    if (index < 0) {
                        index += length;
                    }
                    state = states[index];
                }
                if (state == 1) {
                    return (-index) - 1;
                }
                set[index] = key;
                states[index] = 1;
                return firstRemoved;
            } else if (state == 1) {
                return (-index) - 1;
            } else {
                this.consumeFreeSlot = true;
                set[index] = key;
                states[index] = 1;
                return index;
            }
        }
    }

    @Override // gnu.trove.impl.hash.THash, java.io.Externalizable
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeByte(0);
        super.writeExternal(out);
        out.writeDouble(this.no_entry_key);
        out.writeShort(this.no_entry_value);
    }

    @Override // gnu.trove.impl.hash.THash, java.io.Externalizable
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        in.readByte();
        super.readExternal(in);
        this.no_entry_key = in.readDouble();
        this.no_entry_value = in.readShort();
    }
}
