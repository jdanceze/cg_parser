package gnu.trove.impl.sync;

import gnu.trove.list.TIntList;
import java.util.RandomAccess;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/impl/sync/TSynchronizedRandomAccessIntList.class */
public class TSynchronizedRandomAccessIntList extends TSynchronizedIntList implements RandomAccess {
    static final long serialVersionUID = 1530674583602358482L;

    public TSynchronizedRandomAccessIntList(TIntList list) {
        super(list);
    }

    public TSynchronizedRandomAccessIntList(TIntList list, Object mutex) {
        super(list, mutex);
    }

    @Override // gnu.trove.impl.sync.TSynchronizedIntList, gnu.trove.list.TIntList
    public TIntList subList(int fromIndex, int toIndex) {
        TSynchronizedRandomAccessIntList tSynchronizedRandomAccessIntList;
        synchronized (this.mutex) {
            tSynchronizedRandomAccessIntList = new TSynchronizedRandomAccessIntList(this.list.subList(fromIndex, toIndex), this.mutex);
        }
        return tSynchronizedRandomAccessIntList;
    }

    private Object writeReplace() {
        return new TSynchronizedIntList(this.list);
    }
}
