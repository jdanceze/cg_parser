package gnu.trove.impl.unmodifiable;

import gnu.trove.list.TIntList;
import java.util.RandomAccess;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/impl/unmodifiable/TUnmodifiableRandomAccessIntList.class */
public class TUnmodifiableRandomAccessIntList extends TUnmodifiableIntList implements RandomAccess {
    private static final long serialVersionUID = -2542308836966382001L;

    public TUnmodifiableRandomAccessIntList(TIntList list) {
        super(list);
    }

    @Override // gnu.trove.impl.unmodifiable.TUnmodifiableIntList, gnu.trove.list.TIntList
    public TIntList subList(int fromIndex, int toIndex) {
        return new TUnmodifiableRandomAccessIntList(this.list.subList(fromIndex, toIndex));
    }

    private Object writeReplace() {
        return new TUnmodifiableIntList(this.list);
    }
}
