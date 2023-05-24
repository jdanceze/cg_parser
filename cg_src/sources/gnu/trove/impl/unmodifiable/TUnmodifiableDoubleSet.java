package gnu.trove.impl.unmodifiable;

import gnu.trove.set.TDoubleSet;
import java.io.Serializable;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/impl/unmodifiable/TUnmodifiableDoubleSet.class */
public class TUnmodifiableDoubleSet extends TUnmodifiableDoubleCollection implements TDoubleSet, Serializable {
    private static final long serialVersionUID = -9215047833775013803L;

    public TUnmodifiableDoubleSet(TDoubleSet s) {
        super(s);
    }

    @Override // gnu.trove.TDoubleCollection
    public boolean equals(Object o) {
        return o == this || this.c.equals(o);
    }

    @Override // gnu.trove.TDoubleCollection
    public int hashCode() {
        return this.c.hashCode();
    }
}
