package javax.transaction;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/transaction/TransactionManager.class */
public interface TransactionManager {
    void begin() throws NotSupportedException, SystemException;

    void commit() throws RollbackException, HeuristicMixedException, HeuristicRollbackException, SecurityException, IllegalStateException, SystemException;

    int getStatus() throws SystemException;

    Transaction getTransaction() throws SystemException;

    void resume(Transaction transaction) throws InvalidTransactionException, IllegalStateException, SystemException;

    void rollback() throws IllegalStateException, SecurityException, SystemException;

    void setRollbackOnly() throws IllegalStateException, SystemException;

    void setTransactionTimeout(int i) throws SystemException;

    Transaction suspend() throws SystemException;
}
