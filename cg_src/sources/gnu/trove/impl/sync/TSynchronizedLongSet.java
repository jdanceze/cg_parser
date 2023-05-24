package gnu.trove.impl.sync;

import gnu.trove.set.TLongSet;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/impl/sync/TSynchronizedLongSet.class */
public class TSynchronizedLongSet extends TSynchronizedLongCollection implements TLongSet {
    private static final long serialVersionUID = 487447009682186044L;

    public TSynchronizedLongSet(TLongSet s) {
        super(s);
    }

    public TSynchronizedLongSet(TLongSet s, Object mutex) {
        super(s, mutex);
    }

    @Override // gnu.trove.TLongCollection
    public boolean equals(Object o) {
        boolean equals;
        synchronized (this.mutex) {
            equals = this.c.equals(o);
        }
        return equals;
    }

    @Override // gnu.trove.TLongCollection
    public int hashCode() {
        int hashCode;
        synchronized (this.mutex) {
            hashCode = this.c.hashCode();
        }
        return hashCode;
    }
}
