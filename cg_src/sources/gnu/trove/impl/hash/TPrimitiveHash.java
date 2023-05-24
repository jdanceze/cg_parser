package gnu.trove.impl.hash;

import gnu.trove.impl.HashFunctions;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/impl/hash/TPrimitiveHash.class */
public abstract class TPrimitiveHash extends THash {
    static final long serialVersionUID = 1;
    public transient byte[] _states;
    public static final byte FREE = 0;
    public static final byte FULL = 1;
    public static final byte REMOVED = 2;

    public TPrimitiveHash() {
    }

    public TPrimitiveHash(int initialCapacity) {
        this(initialCapacity, 0.5f);
    }

    public TPrimitiveHash(int initialCapacity, float loadFactor) {
        int initialCapacity2 = Math.max(1, initialCapacity);
        this._loadFactor = loadFactor;
        setUp(HashFunctions.fastCeil(initialCapacity2 / loadFactor));
    }

    @Override // gnu.trove.impl.hash.THash
    public int capacity() {
        return this._states.length;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // gnu.trove.impl.hash.THash
    public void removeAt(int index) {
        this._states[index] = 2;
        super.removeAt(index);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // gnu.trove.impl.hash.THash
    public int setUp(int initialCapacity) {
        int capacity = super.setUp(initialCapacity);
        this._states = new byte[capacity];
        return capacity;
    }
}
