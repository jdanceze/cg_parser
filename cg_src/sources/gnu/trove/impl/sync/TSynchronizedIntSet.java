package gnu.trove.impl.sync;

import gnu.trove.set.TIntSet;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/impl/sync/TSynchronizedIntSet.class */
public class TSynchronizedIntSet extends TSynchronizedIntCollection implements TIntSet {
    private static final long serialVersionUID = 487447009682186044L;

    public TSynchronizedIntSet(TIntSet s) {
        super(s);
    }

    public TSynchronizedIntSet(TIntSet s, Object mutex) {
        super(s, mutex);
    }

    @Override // gnu.trove.TIntCollection
    public boolean equals(Object o) {
        boolean equals;
        synchronized (this.mutex) {
            equals = this.c.equals(o);
        }
        return equals;
    }

    @Override // gnu.trove.TIntCollection
    public int hashCode() {
        int hashCode;
        synchronized (this.mutex) {
            hashCode = this.c.hashCode();
        }
        return hashCode;
    }
}
