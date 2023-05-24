package gnu.trove.impl.sync;

import gnu.trove.list.TDoubleList;
import java.util.RandomAccess;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/impl/sync/TSynchronizedRandomAccessDoubleList.class */
public class TSynchronizedRandomAccessDoubleList extends TSynchronizedDoubleList implements RandomAccess {
    static final long serialVersionUID = 1530674583602358482L;

    public TSynchronizedRandomAccessDoubleList(TDoubleList list) {
        super(list);
    }

    public TSynchronizedRandomAccessDoubleList(TDoubleList list, Object mutex) {
        super(list, mutex);
    }

    @Override // gnu.trove.impl.sync.TSynchronizedDoubleList, gnu.trove.list.TDoubleList
    public TDoubleList subList(int fromIndex, int toIndex) {
        TSynchronizedRandomAccessDoubleList tSynchronizedRandomAccessDoubleList;
        synchronized (this.mutex) {
            tSynchronizedRandomAccessDoubleList = new TSynchronizedRandomAccessDoubleList(this.list.subList(fromIndex, toIndex), this.mutex);
        }
        return tSynchronizedRandomAccessDoubleList;
    }

    private Object writeReplace() {
        return new TSynchronizedDoubleList(this.list);
    }
}
