package gnu.trove.iterator.hash;

import gnu.trove.impl.hash.THashIterator;
import gnu.trove.impl.hash.TObjectHash;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/iterator/hash/TObjectHashIterator.class */
public class TObjectHashIterator<E> extends THashIterator<E> {
    protected final TObjectHash _objectHash;

    public TObjectHashIterator(TObjectHash<E> hash) {
        super(hash);
        this._objectHash = hash;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // gnu.trove.impl.hash.THashIterator
    public E objectAtIndex(int index) {
        E e = (E) this._objectHash._set[index];
        if (e == TObjectHash.FREE || e == TObjectHash.REMOVED) {
            return null;
        }
        return e;
    }
}
