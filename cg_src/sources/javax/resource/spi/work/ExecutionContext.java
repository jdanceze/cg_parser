package javax.resource.spi.work;

import javax.resource.NotSupportedException;
import javax.transaction.xa.Xid;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/resource/spi/work/ExecutionContext.class */
public class ExecutionContext {
    private Xid xid;
    private long transactionTimeout = -1;

    public void setXid(Xid xid) {
        this.xid = xid;
    }

    public Xid getXid() {
        return this.xid;
    }

    public void setTransactionTimeout(long timeout) throws NotSupportedException {
        if (timeout > 0) {
            this.transactionTimeout = timeout;
            return;
        }
        throw new NotSupportedException("Illegal timeout value");
    }

    public long getTransactionTimeout() {
        return this.transactionTimeout;
    }
}
