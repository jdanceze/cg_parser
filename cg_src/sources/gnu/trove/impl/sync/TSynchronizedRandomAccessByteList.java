package gnu.trove.impl.sync;

import gnu.trove.list.TByteList;
import java.util.RandomAccess;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/impl/sync/TSynchronizedRandomAccessByteList.class */
public class TSynchronizedRandomAccessByteList extends TSynchronizedByteList implements RandomAccess {
    static final long serialVersionUID = 1530674583602358482L;

    public TSynchronizedRandomAccessByteList(TByteList list) {
        super(list);
    }

    public TSynchronizedRandomAccessByteList(TByteList list, Object mutex) {
        super(list, mutex);
    }

    @Override // gnu.trove.impl.sync.TSynchronizedByteList, gnu.trove.list.TByteList
    public TByteList subList(int fromIndex, int toIndex) {
        TSynchronizedRandomAccessByteList tSynchronizedRandomAccessByteList;
        synchronized (this.mutex) {
            tSynchronizedRandomAccessByteList = new TSynchronizedRandomAccessByteList(this.list.subList(fromIndex, toIndex), this.mutex);
        }
        return tSynchronizedRandomAccessByteList;
    }

    private Object writeReplace() {
        return new TSynchronizedByteList(this.list);
    }
}
