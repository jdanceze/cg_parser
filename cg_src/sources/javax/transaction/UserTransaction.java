package javax.transaction;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/transaction/UserTransaction.class */
public interface UserTransaction {
    void begin() throws NotSupportedException, SystemException;

    void commit() throws RollbackException, HeuristicMixedException, HeuristicRollbackException, SecurityException, IllegalStateException, SystemException;

    void rollback() throws IllegalStateException, SecurityException, SystemException;

    void setRollbackOnly() throws IllegalStateException, SystemException;

    int getStatus() throws SystemException;

    void setTransactionTimeout(int i) throws SystemException;
}
