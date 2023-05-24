package gnu.trove.impl.unmodifiable;

import gnu.trove.list.TDoubleList;
import java.util.RandomAccess;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/impl/unmodifiable/TUnmodifiableRandomAccessDoubleList.class */
public class TUnmodifiableRandomAccessDoubleList extends TUnmodifiableDoubleList implements RandomAccess {
    private static final long serialVersionUID = -2542308836966382001L;

    public TUnmodifiableRandomAccessDoubleList(TDoubleList list) {
        super(list);
    }

    @Override // gnu.trove.impl.unmodifiable.TUnmodifiableDoubleList, gnu.trove.list.TDoubleList
    public TDoubleList subList(int fromIndex, int toIndex) {
        return new TUnmodifiableRandomAccessDoubleList(this.list.subList(fromIndex, toIndex));
    }

    private Object writeReplace() {
        return new TUnmodifiableDoubleList(this.list);
    }
}
