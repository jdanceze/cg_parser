package gnu.trove.impl.sync;

import gnu.trove.set.TCharSet;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/impl/sync/TSynchronizedCharSet.class */
public class TSynchronizedCharSet extends TSynchronizedCharCollection implements TCharSet {
    private static final long serialVersionUID = 487447009682186044L;

    public TSynchronizedCharSet(TCharSet s) {
        super(s);
    }

    public TSynchronizedCharSet(TCharSet s, Object mutex) {
        super(s, mutex);
    }

    @Override // gnu.trove.TCharCollection
    public boolean equals(Object o) {
        boolean equals;
        synchronized (this.mutex) {
            equals = this.c.equals(o);
        }
        return equals;
    }

    @Override // gnu.trove.TCharCollection
    public int hashCode() {
        int hashCode;
        synchronized (this.mutex) {
            hashCode = this.c.hashCode();
        }
        return hashCode;
    }
}
