package gnu.trove.impl.hash;

import gnu.trove.iterator.TPrimitiveIterator;
import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/impl/hash/THashPrimitiveIterator.class */
public abstract class THashPrimitiveIterator implements TPrimitiveIterator {
    protected final TPrimitiveHash _hash;
    protected int _expectedSize;
    protected int _index;

    public THashPrimitiveIterator(TPrimitiveHash hash) {
        this._hash = hash;
        this._expectedSize = this._hash.size();
        this._index = this._hash.capacity();
    }

    protected final int nextIndex() {
        if (this._expectedSize != this._hash.size()) {
            throw new ConcurrentModificationException();
        }
        byte[] states = this._hash._states;
        int i = this._index;
        do {
            int i2 = i;
            i--;
            if (i2 <= 0) {
                break;
            }
        } while (states[i] != 1);
        return i;
    }

    @Override // gnu.trove.iterator.TPrimitiveIterator, gnu.trove.iterator.TIterator, java.util.Iterator
    public boolean hasNext() {
        return nextIndex() >= 0;
    }

    @Override // gnu.trove.iterator.TPrimitiveIterator, gnu.trove.iterator.TIterator, java.util.Iterator
    public void remove() {
        if (this._expectedSize != this._hash.size()) {
            throw new ConcurrentModificationException();
        }
        try {
            this._hash.tempDisableAutoCompaction();
            this._hash.removeAt(this._index);
            this._hash.reenableAutoCompaction(false);
            this._expectedSize--;
        } catch (Throwable th) {
            this._hash.reenableAutoCompaction(false);
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void moveToNextIndex() {
        int nextIndex = nextIndex();
        this._index = nextIndex;
        if (nextIndex < 0) {
            throw new NoSuchElementException();
        }
    }
}
