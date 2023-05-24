package gnu.trove.impl.unmodifiable;

import gnu.trove.list.TByteList;
import java.util.RandomAccess;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/impl/unmodifiable/TUnmodifiableRandomAccessByteList.class */
public class TUnmodifiableRandomAccessByteList extends TUnmodifiableByteList implements RandomAccess {
    private static final long serialVersionUID = -2542308836966382001L;

    public TUnmodifiableRandomAccessByteList(TByteList list) {
        super(list);
    }

    @Override // gnu.trove.impl.unmodifiable.TUnmodifiableByteList, gnu.trove.list.TByteList
    public TByteList subList(int fromIndex, int toIndex) {
        return new TUnmodifiableRandomAccessByteList(this.list.subList(fromIndex, toIndex));
    }

    private Object writeReplace() {
        return new TUnmodifiableByteList(this.list);
    }
}
