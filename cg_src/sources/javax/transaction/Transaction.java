package javax.transaction;

import javax.transaction.xa.XAResource;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/transaction/Transaction.class */
public interface Transaction {
    void commit() throws RollbackException, HeuristicMixedException, HeuristicRollbackException, SecurityException, IllegalStateException, SystemException;

    boolean delistResource(XAResource xAResource, int i) throws IllegalStateException, SystemException;

    boolean enlistResource(XAResource xAResource) throws RollbackException, IllegalStateException, SystemException;

    int getStatus() throws SystemException;

    void registerSynchronization(Synchronization synchronization) throws RollbackException, IllegalStateException, SystemException;

    void rollback() throws IllegalStateException, SystemException;

    void setRollbackOnly() throws IllegalStateException, SystemException;
}
