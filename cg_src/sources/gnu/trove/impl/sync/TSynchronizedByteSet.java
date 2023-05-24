package gnu.trove.impl.sync;

import gnu.trove.set.TByteSet;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/impl/sync/TSynchronizedByteSet.class */
public class TSynchronizedByteSet extends TSynchronizedByteCollection implements TByteSet {
    private static final long serialVersionUID = 487447009682186044L;

    public TSynchronizedByteSet(TByteSet s) {
        super(s);
    }

    public TSynchronizedByteSet(TByteSet s, Object mutex) {
        super(s, mutex);
    }

    @Override // gnu.trove.TByteCollection
    public boolean equals(Object o) {
        boolean equals;
        synchronized (this.mutex) {
            equals = this.c.equals(o);
        }
        return equals;
    }

    @Override // gnu.trove.TByteCollection
    public int hashCode() {
        int hashCode;
        synchronized (this.mutex) {
            hashCode = this.c.hashCode();
        }
        return hashCode;
    }
}
