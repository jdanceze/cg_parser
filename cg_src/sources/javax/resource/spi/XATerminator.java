package javax.resource.spi;

import javax.transaction.xa.XAException;
import javax.transaction.xa.Xid;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/resource/spi/XATerminator.class */
public interface XATerminator {
    void commit(Xid xid, boolean z) throws XAException;

    void forget(Xid xid) throws XAException;

    int prepare(Xid xid) throws XAException;

    Xid[] recover(int i) throws XAException;

    void rollback(Xid xid) throws XAException;
}
