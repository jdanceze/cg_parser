package javax.transaction;

import java.rmi.RemoteException;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/transaction/TransactionRequiredException.class */
public class TransactionRequiredException extends RemoteException {
    public TransactionRequiredException() {
    }

    public TransactionRequiredException(String msg) {
        super(msg);
    }
}
