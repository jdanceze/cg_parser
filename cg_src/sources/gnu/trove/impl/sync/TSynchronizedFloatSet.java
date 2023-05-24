package gnu.trove.impl.sync;

import gnu.trove.set.TFloatSet;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/impl/sync/TSynchronizedFloatSet.class */
public class TSynchronizedFloatSet extends TSynchronizedFloatCollection implements TFloatSet {
    private static final long serialVersionUID = 487447009682186044L;

    public TSynchronizedFloatSet(TFloatSet s) {
        super(s);
    }

    public TSynchronizedFloatSet(TFloatSet s, Object mutex) {
        super(s, mutex);
    }

    @Override // gnu.trove.TFloatCollection
    public boolean equals(Object o) {
        boolean equals;
        synchronized (this.mutex) {
            equals = this.c.equals(o);
        }
        return equals;
    }

    @Override // gnu.trove.TFloatCollection
    public int hashCode() {
        int hashCode;
        synchronized (this.mutex) {
            hashCode = this.c.hashCode();
        }
        return hashCode;
    }
}
