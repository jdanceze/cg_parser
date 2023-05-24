package javax.transaction;

import java.rmi.RemoteException;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/transaction/TransactionRolledbackException.class */
public class TransactionRolledbackException extends RemoteException {
    public TransactionRolledbackException() {
    }

    public TransactionRolledbackException(String msg) {
        super(msg);
    }
}
