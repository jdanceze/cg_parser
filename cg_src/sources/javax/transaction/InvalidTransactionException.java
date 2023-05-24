package javax.transaction;

import java.rmi.RemoteException;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/transaction/InvalidTransactionException.class */
public class InvalidTransactionException extends RemoteException {
    public InvalidTransactionException() {
    }

    public InvalidTransactionException(String msg) {
        super(msg);
    }
}
