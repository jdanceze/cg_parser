package gnu.trove.impl.sync;

import gnu.trove.list.TFloatList;
import java.util.RandomAccess;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/impl/sync/TSynchronizedRandomAccessFloatList.class */
public class TSynchronizedRandomAccessFloatList extends TSynchronizedFloatList implements RandomAccess {
    static final long serialVersionUID = 1530674583602358482L;

    public TSynchronizedRandomAccessFloatList(TFloatList list) {
        super(list);
    }

    public TSynchronizedRandomAccessFloatList(TFloatList list, Object mutex) {
        super(list, mutex);
    }

    @Override // gnu.trove.impl.sync.TSynchronizedFloatList, gnu.trove.list.TFloatList
    public TFloatList subList(int fromIndex, int toIndex) {
        TSynchronizedRandomAccessFloatList tSynchronizedRandomAccessFloatList;
        synchronized (this.mutex) {
            tSynchronizedRandomAccessFloatList = new TSynchronizedRandomAccessFloatList(this.list.subList(fromIndex, toIndex), this.mutex);
        }
        return tSynchronizedRandomAccessFloatList;
    }

    private Object writeReplace() {
        return new TSynchronizedFloatList(this.list);
    }
}
