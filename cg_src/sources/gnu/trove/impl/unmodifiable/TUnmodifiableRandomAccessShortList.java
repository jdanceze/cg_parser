package gnu.trove.impl.unmodifiable;

import gnu.trove.list.TShortList;
import java.util.RandomAccess;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/impl/unmodifiable/TUnmodifiableRandomAccessShortList.class */
public class TUnmodifiableRandomAccessShortList extends TUnmodifiableShortList implements RandomAccess {
    private static final long serialVersionUID = -2542308836966382001L;

    public TUnmodifiableRandomAccessShortList(TShortList list) {
        super(list);
    }

    @Override // gnu.trove.impl.unmodifiable.TUnmodifiableShortList, gnu.trove.list.TShortList
    public TShortList subList(int fromIndex, int toIndex) {
        return new TUnmodifiableRandomAccessShortList(this.list.subList(fromIndex, toIndex));
    }

    private Object writeReplace() {
        return new TUnmodifiableShortList(this.list);
    }
}
