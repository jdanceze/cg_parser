package gnu.trove.impl.unmodifiable;

import gnu.trove.set.TFloatSet;
import java.io.Serializable;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/impl/unmodifiable/TUnmodifiableFloatSet.class */
public class TUnmodifiableFloatSet extends TUnmodifiableFloatCollection implements TFloatSet, Serializable {
    private static final long serialVersionUID = -9215047833775013803L;

    public TUnmodifiableFloatSet(TFloatSet s) {
        super(s);
    }

    @Override // gnu.trove.TFloatCollection
    public boolean equals(Object o) {
        return o == this || this.c.equals(o);
    }

    @Override // gnu.trove.TFloatCollection
    public int hashCode() {
        return this.c.hashCode();
    }
}
