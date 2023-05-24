package gnu.trove.impl.sync;

import gnu.trove.set.TDoubleSet;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/impl/sync/TSynchronizedDoubleSet.class */
public class TSynchronizedDoubleSet extends TSynchronizedDoubleCollection implements TDoubleSet {
    private static final long serialVersionUID = 487447009682186044L;

    public TSynchronizedDoubleSet(TDoubleSet s) {
        super(s);
    }

    public TSynchronizedDoubleSet(TDoubleSet s, Object mutex) {
        super(s, mutex);
    }

    @Override // gnu.trove.TDoubleCollection
    public boolean equals(Object o) {
        boolean equals;
        synchronized (this.mutex) {
            equals = this.c.equals(o);
        }
        return equals;
    }

    @Override // gnu.trove.TDoubleCollection
    public int hashCode() {
        int hashCode;
        synchronized (this.mutex) {
            hashCode = this.c.hashCode();
        }
        return hashCode;
    }
}
