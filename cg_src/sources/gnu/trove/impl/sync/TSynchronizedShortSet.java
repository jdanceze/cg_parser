package gnu.trove.impl.sync;

import gnu.trove.set.TShortSet;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/impl/sync/TSynchronizedShortSet.class */
public class TSynchronizedShortSet extends TSynchronizedShortCollection implements TShortSet {
    private static final long serialVersionUID = 487447009682186044L;

    public TSynchronizedShortSet(TShortSet s) {
        super(s);
    }

    public TSynchronizedShortSet(TShortSet s, Object mutex) {
        super(s, mutex);
    }

    @Override // gnu.trove.TShortCollection
    public boolean equals(Object o) {
        boolean equals;
        synchronized (this.mutex) {
            equals = this.c.equals(o);
        }
        return equals;
    }

    @Override // gnu.trove.TShortCollection
    public int hashCode() {
        int hashCode;
        synchronized (this.mutex) {
            hashCode = this.c.hashCode();
        }
        return hashCode;
    }
}
