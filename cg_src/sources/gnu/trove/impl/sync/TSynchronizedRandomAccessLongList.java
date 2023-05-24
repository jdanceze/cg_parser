package gnu.trove.impl.sync;

import gnu.trove.list.TLongList;
import java.util.RandomAccess;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/impl/sync/TSynchronizedRandomAccessLongList.class */
public class TSynchronizedRandomAccessLongList extends TSynchronizedLongList implements RandomAccess {
    static final long serialVersionUID = 1530674583602358482L;

    public TSynchronizedRandomAccessLongList(TLongList list) {
        super(list);
    }

    public TSynchronizedRandomAccessLongList(TLongList list, Object mutex) {
        super(list, mutex);
    }

    @Override // gnu.trove.impl.sync.TSynchronizedLongList, gnu.trove.list.TLongList
    public TLongList subList(int fromIndex, int toIndex) {
        TSynchronizedRandomAccessLongList tSynchronizedRandomAccessLongList;
        synchronized (this.mutex) {
            tSynchronizedRandomAccessLongList = new TSynchronizedRandomAccessLongList(this.list.subList(fromIndex, toIndex), this.mutex);
        }
        return tSynchronizedRandomAccessLongList;
    }

    private Object writeReplace() {
        return new TSynchronizedLongList(this.list);
    }
}
