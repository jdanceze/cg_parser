package gnu.trove.impl.sync;

import gnu.trove.list.TShortList;
import java.util.RandomAccess;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/impl/sync/TSynchronizedRandomAccessShortList.class */
public class TSynchronizedRandomAccessShortList extends TSynchronizedShortList implements RandomAccess {
    static final long serialVersionUID = 1530674583602358482L;

    public TSynchronizedRandomAccessShortList(TShortList list) {
        super(list);
    }

    public TSynchronizedRandomAccessShortList(TShortList list, Object mutex) {
        super(list, mutex);
    }

    @Override // gnu.trove.impl.sync.TSynchronizedShortList, gnu.trove.list.TShortList
    public TShortList subList(int fromIndex, int toIndex) {
        TSynchronizedRandomAccessShortList tSynchronizedRandomAccessShortList;
        synchronized (this.mutex) {
            tSynchronizedRandomAccessShortList = new TSynchronizedRandomAccessShortList(this.list.subList(fromIndex, toIndex), this.mutex);
        }
        return tSynchronizedRandomAccessShortList;
    }

    private Object writeReplace() {
        return new TSynchronizedShortList(this.list);
    }
}
