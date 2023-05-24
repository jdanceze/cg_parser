package gnu.trove.impl.hash;

import gnu.trove.impl.Constants;
import gnu.trove.impl.HashFunctions;
import gnu.trove.procedure.TCharProcedure;
import java.util.Arrays;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/impl/hash/TCharHash.class */
public abstract class TCharHash extends TPrimitiveHash {
    static final long serialVersionUID = 1;
    public transient char[] _set;
    protected char no_entry_value;
    protected boolean consumeFreeSlot;

    public TCharHash() {
        this.no_entry_value = Constants.DEFAULT_CHAR_NO_ENTRY_VALUE;
        if (this.no_entry_value != 0) {
            Arrays.fill(this._set, this.no_entry_value);
        }
    }

    public TCharHash(int initialCapacity) {
        super(initialCapacity);
        this.no_entry_value = Constants.DEFAULT_CHAR_NO_ENTRY_VALUE;
        if (this.no_entry_value != 0) {
            Arrays.fill(this._set, this.no_entry_value);
        }
    }

    public TCharHash(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
        this.no_entry_value = Constants.DEFAULT_CHAR_NO_ENTRY_VALUE;
        if (this.no_entry_value != 0) {
            Arrays.fill(this._set, this.no_entry_value);
        }
    }

    public TCharHash(int initialCapacity, float loadFactor, char no_entry_value) {
        super(initialCapacity, loadFactor);
        this.no_entry_value = no_entry_value;
        if (no_entry_value != 0) {
            Arrays.fill(this._set, no_entry_value);
        }
    }

    public char getNoEntryValue() {
        return this.no_entry_value;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // gnu.trove.impl.hash.TPrimitiveHash, gnu.trove.impl.hash.THash
    public int setUp(int initialCapacity) {
        int capacity = super.setUp(initialCapacity);
        this._set = new char[capacity];
        return capacity;
    }

    public boolean contains(char val) {
        return index(val) >= 0;
    }

    public boolean forEach(TCharProcedure procedure) {
        byte[] states = this._states;
        char[] set = this._set;
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
        this._set[index] = this.no_entry_value;
        super.removeAt(index);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int index(char val) {
        byte[] states = this._states;
        char[] set = this._set;
        int length = states.length;
        int hash = HashFunctions.hash((int) val) & Integer.MAX_VALUE;
        int index = hash % length;
        byte state = states[index];
        if (state == 0) {
            return -1;
        }
        if (state == 1 && set[index] == val) {
            return index;
        }
        return indexRehashed(val, index, hash, state);
    }

    int indexRehashed(char key, int index, int hash, byte state) {
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
    public int insertKey(char val) {
        int hash = HashFunctions.hash((int) val) & Integer.MAX_VALUE;
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

    int insertKeyRehash(char val, int index, int hash, byte state) {
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

    void insertKeyAt(int index, char val) {
        this._set[index] = val;
        this._states[index] = 1;
    }
}
