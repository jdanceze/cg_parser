package gnu.trove.impl.unmodifiable;

import gnu.trove.set.TLongSet;
import java.io.Serializable;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/impl/unmodifiable/TUnmodifiableLongSet.class */
public class TUnmodifiableLongSet extends TUnmodifiableLongCollection implements TLongSet, Serializable {
    private static final long serialVersionUID = -9215047833775013803L;

    public TUnmodifiableLongSet(TLongSet s) {
        super(s);
    }

    @Override // gnu.trove.TLongCollection
    public boolean equals(Object o) {
        return o == this || this.c.equals(o);
    }

    @Override // gnu.trove.TLongCollection
    public int hashCode() {
        return this.c.hashCode();
    }
}
