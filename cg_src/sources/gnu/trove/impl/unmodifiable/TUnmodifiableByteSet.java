package gnu.trove.impl.unmodifiable;

import gnu.trove.set.TByteSet;
import java.io.Serializable;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/impl/unmodifiable/TUnmodifiableByteSet.class */
public class TUnmodifiableByteSet extends TUnmodifiableByteCollection implements TByteSet, Serializable {
    private static final long serialVersionUID = -9215047833775013803L;

    public TUnmodifiableByteSet(TByteSet s) {
        super(s);
    }

    @Override // gnu.trove.TByteCollection
    public boolean equals(Object o) {
        return o == this || this.c.equals(o);
    }

    @Override // gnu.trove.TByteCollection
    public int hashCode() {
        return this.c.hashCode();
    }
}
