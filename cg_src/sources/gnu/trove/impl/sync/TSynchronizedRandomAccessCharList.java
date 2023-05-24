package gnu.trove.impl.sync;

import gnu.trove.list.TCharList;
import java.util.RandomAccess;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/impl/sync/TSynchronizedRandomAccessCharList.class */
public class TSynchronizedRandomAccessCharList extends TSynchronizedCharList implements RandomAccess {
    static final long serialVersionUID = 1530674583602358482L;

    public TSynchronizedRandomAccessCharList(TCharList list) {
        super(list);
    }

    public TSynchronizedRandomAccessCharList(TCharList list, Object mutex) {
        super(list, mutex);
    }

    @Override // gnu.trove.impl.sync.TSynchronizedCharList, gnu.trove.list.TCharList
    public TCharList subList(int fromIndex, int toIndex) {
        TSynchronizedRandomAccessCharList tSynchronizedRandomAccessCharList;
        synchronized (this.mutex) {
            tSynchronizedRandomAccessCharList = new TSynchronizedRandomAccessCharList(this.list.subList(fromIndex, toIndex), this.mutex);
        }
        return tSynchronizedRandomAccessCharList;
    }

    private Object writeReplace() {
        return new TSynchronizedCharList(this.list);
    }
}
