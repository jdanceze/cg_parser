package gnu.trove.impl.hash;

import gnu.trove.iterator.TIterator;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/impl/hash/THashIterator.class */
public abstract class THashIterator<V> implements TIterator, Iterator<V> {
    private final TObjectHash<V> _object_hash;
    protected final THash _hash;
    protected int _expectedSize;
    protected int _index;

    protected abstract V objectAtIndex(int i);

    /* JADX INFO: Access modifiers changed from: protected */
    public THashIterator(TObjectHash<V> hash) {
        this._hash = hash;
        this._expectedSize = this._hash.size();
        this._index = this._hash.capacity();
        this._object_hash = hash;
    }

    @Override // java.util.Iterator
    public V next() {
        moveToNextIndex();
        return objectAtIndex(this._index);
    }

    @Override // gnu.trove.iterator.TIterator, java.util.Iterator
    public boolean hasNext() {
        return nextIndex() >= 0;
    }

    @Override // gnu.trove.iterator.TIterator, java.util.Iterator
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

    protected final int nextIndex() {
        if (this._expectedSize != this._hash.size()) {
            throw new ConcurrentModificationException();
        }
        Object[] set = this._object_hash._set;
        int i = this._index;
        while (true) {
            int i2 = i;
            i--;
            if (i2 <= 0 || (set[i] != TObjectHash.FREE && set[i] != TObjectHash.REMOVED)) {
                break;
            }
        }
        return i;
    }
}
