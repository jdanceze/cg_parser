package gnu.trove.impl.unmodifiable;

import gnu.trove.set.TIntSet;
import java.io.Serializable;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/impl/unmodifiable/TUnmodifiableIntSet.class */
public class TUnmodifiableIntSet extends TUnmodifiableIntCollection implements TIntSet, Serializable {
    private static final long serialVersionUID = -9215047833775013803L;

    public TUnmodifiableIntSet(TIntSet s) {
        super(s);
    }

    @Override // gnu.trove.TIntCollection
    public boolean equals(Object o) {
        return o == this || this.c.equals(o);
    }

    @Override // gnu.trove.TIntCollection
    public int hashCode() {
        return this.c.hashCode();
    }
}
