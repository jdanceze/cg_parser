package gnu.trove.impl.hash;

import gnu.trove.impl.HashFunctions;
import gnu.trove.impl.PrimeFinder;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/impl/hash/THash.class */
public abstract class THash implements Externalizable {
    static final long serialVersionUID = -1792948471915530295L;
    protected static final float DEFAULT_LOAD_FACTOR = 0.5f;
    protected static final int DEFAULT_CAPACITY = 10;
    protected transient int _size;
    protected transient int _free;
    protected float _loadFactor;
    protected int _maxSize;
    protected int _autoCompactRemovesRemaining;
    protected float _autoCompactionFactor;
    protected transient boolean _autoCompactTemporaryDisable;

    public abstract int capacity();

    protected abstract void rehash(int i);

    public THash() {
        this(10, 0.5f);
    }

    public THash(int initialCapacity) {
        this(initialCapacity, 0.5f);
    }

    public THash(int initialCapacity, float loadFactor) {
        this._autoCompactTemporaryDisable = false;
        this._loadFactor = loadFactor;
        this._autoCompactionFactor = loadFactor;
        setUp(HashFunctions.fastCeil(initialCapacity / loadFactor));
    }

    public boolean isEmpty() {
        return 0 == this._size;
    }

    public int size() {
        return this._size;
    }

    public void ensureCapacity(int desiredCapacity) {
        if (desiredCapacity > this._maxSize - size()) {
            rehash(PrimeFinder.nextPrime(Math.max(size() + 1, HashFunctions.fastCeil((desiredCapacity + size()) / this._loadFactor) + 1)));
            computeMaxSize(capacity());
        }
    }

    public void compact() {
        rehash(PrimeFinder.nextPrime(Math.max(this._size + 1, HashFunctions.fastCeil(size() / this._loadFactor) + 1)));
        computeMaxSize(capacity());
        if (this._autoCompactionFactor != 0.0f) {
            computeNextAutoCompactionAmount(size());
        }
    }

    public void setAutoCompactionFactor(float factor) {
        if (factor < 0.0f) {
            throw new IllegalArgumentException("Factor must be >= 0: " + factor);
        }
        this._autoCompactionFactor = factor;
    }

    public float getAutoCompactionFactor() {
        return this._autoCompactionFactor;
    }

    public final void trimToSize() {
        compact();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void removeAt(int index) {
        this._size--;
        if (this._autoCompactionFactor != 0.0f) {
            this._autoCompactRemovesRemaining--;
            if (!this._autoCompactTemporaryDisable && this._autoCompactRemovesRemaining <= 0) {
                compact();
            }
        }
    }

    public void clear() {
        this._size = 0;
        this._free = capacity();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int setUp(int initialCapacity) {
        int capacity = PrimeFinder.nextPrime(initialCapacity);
        computeMaxSize(capacity);
        computeNextAutoCompactionAmount(initialCapacity);
        return capacity;
    }

    public void tempDisableAutoCompaction() {
        this._autoCompactTemporaryDisable = true;
    }

    public void reenableAutoCompaction(boolean check_for_compaction) {
        this._autoCompactTemporaryDisable = false;
        if (check_for_compaction && this._autoCompactRemovesRemaining <= 0 && this._autoCompactionFactor != 0.0f) {
            compact();
        }
    }

    protected void computeMaxSize(int capacity) {
        this._maxSize = Math.min(capacity - 1, (int) (capacity * this._loadFactor));
        this._free = capacity - this._size;
    }

    protected void computeNextAutoCompactionAmount(int size) {
        if (this._autoCompactionFactor != 0.0f) {
            this._autoCompactRemovesRemaining = (int) ((size * this._autoCompactionFactor) + 0.5f);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void postInsertHook(boolean usedFreeSlot) {
        if (usedFreeSlot) {
            this._free--;
        }
        int i = this._size + 1;
        this._size = i;
        if (i > this._maxSize || this._free == 0) {
            int newCapacity = this._size > this._maxSize ? PrimeFinder.nextPrime(capacity() << 1) : capacity();
            rehash(newCapacity);
            computeMaxSize(capacity());
        }
    }

    protected int calculateGrownCapacity() {
        return capacity() << 1;
    }

    @Override // java.io.Externalizable
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeByte(0);
        out.writeFloat(this._loadFactor);
        out.writeFloat(this._autoCompactionFactor);
    }

    @Override // java.io.Externalizable
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        in.readByte();
        float old_factor = this._loadFactor;
        this._loadFactor = in.readFloat();
        this._autoCompactionFactor = in.readFloat();
        if (old_factor != this._loadFactor) {
            setUp((int) Math.ceil(10.0f / this._loadFactor));
        }
    }
}
