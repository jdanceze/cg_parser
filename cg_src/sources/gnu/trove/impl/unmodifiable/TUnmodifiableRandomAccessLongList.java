package gnu.trove.impl.unmodifiable;

import gnu.trove.list.TLongList;
import java.util.RandomAccess;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/impl/unmodifiable/TUnmodifiableRandomAccessLongList.class */
public class TUnmodifiableRandomAccessLongList extends TUnmodifiableLongList implements RandomAccess {
    private static final long serialVersionUID = -2542308836966382001L;

    public TUnmodifiableRandomAccessLongList(TLongList list) {
        super(list);
    }

    @Override // gnu.trove.impl.unmodifiable.TUnmodifiableLongList, gnu.trove.list.TLongList
    public TLongList subList(int fromIndex, int toIndex) {
        return new TUnmodifiableRandomAccessLongList(this.list.subList(fromIndex, toIndex));
    }

    private Object writeReplace() {
        return new TUnmodifiableLongList(this.list);
    }
}
