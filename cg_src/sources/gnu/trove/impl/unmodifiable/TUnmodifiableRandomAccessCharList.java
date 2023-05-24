package gnu.trove.impl.unmodifiable;

import gnu.trove.list.TCharList;
import java.util.RandomAccess;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/impl/unmodifiable/TUnmodifiableRandomAccessCharList.class */
public class TUnmodifiableRandomAccessCharList extends TUnmodifiableCharList implements RandomAccess {
    private static final long serialVersionUID = -2542308836966382001L;

    public TUnmodifiableRandomAccessCharList(TCharList list) {
        super(list);
    }

    @Override // gnu.trove.impl.unmodifiable.TUnmodifiableCharList, gnu.trove.list.TCharList
    public TCharList subList(int fromIndex, int toIndex) {
        return new TUnmodifiableRandomAccessCharList(this.list.subList(fromIndex, toIndex));
    }

    private Object writeReplace() {
        return new TUnmodifiableCharList(this.list);
    }
}
