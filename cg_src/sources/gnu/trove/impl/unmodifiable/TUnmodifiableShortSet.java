package gnu.trove.impl.unmodifiable;

import gnu.trove.set.TShortSet;
import java.io.Serializable;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/impl/unmodifiable/TUnmodifiableShortSet.class */
public class TUnmodifiableShortSet extends TUnmodifiableShortCollection implements TShortSet, Serializable {
    private static final long serialVersionUID = -9215047833775013803L;

    public TUnmodifiableShortSet(TShortSet s) {
        super(s);
    }

    @Override // gnu.trove.TShortCollection
    public boolean equals(Object o) {
        return o == this || this.c.equals(o);
    }

    @Override // gnu.trove.TShortCollection
    public int hashCode() {
        return this.c.hashCode();
    }
}
