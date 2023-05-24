package gnu.trove.impl.unmodifiable;

import gnu.trove.list.TFloatList;
import java.util.RandomAccess;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/impl/unmodifiable/TUnmodifiableRandomAccessFloatList.class */
public class TUnmodifiableRandomAccessFloatList extends TUnmodifiableFloatList implements RandomAccess {
    private static final long serialVersionUID = -2542308836966382001L;

    public TUnmodifiableRandomAccessFloatList(TFloatList list) {
        super(list);
    }

    @Override // gnu.trove.impl.unmodifiable.TUnmodifiableFloatList, gnu.trove.list.TFloatList
    public TFloatList subList(int fromIndex, int toIndex) {
        return new TUnmodifiableRandomAccessFloatList(this.list.subList(fromIndex, toIndex));
    }

    private Object writeReplace() {
        return new TUnmodifiableFloatList(this.list);
    }
}
