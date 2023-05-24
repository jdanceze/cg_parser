package gnu.trove.impl.unmodifiable;

import gnu.trove.set.TCharSet;
import java.io.Serializable;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/impl/unmodifiable/TUnmodifiableCharSet.class */
public class TUnmodifiableCharSet extends TUnmodifiableCharCollection implements TCharSet, Serializable {
    private static final long serialVersionUID = -9215047833775013803L;

    public TUnmodifiableCharSet(TCharSet s) {
        super(s);
    }

    @Override // gnu.trove.TCharCollection
    public boolean equals(Object o) {
        return o == this || this.c.equals(o);
    }

    @Override // gnu.trove.TCharCollection
    public int hashCode() {
        return this.c.hashCode();
    }
}
