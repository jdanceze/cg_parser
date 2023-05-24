package javax.jms;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/jms/TransactionInProgressException.class */
public class TransactionInProgressException extends JMSException {
    public TransactionInProgressException(String reason, String errorCode) {
        super(reason, errorCode);
    }

    public TransactionInProgressException(String reason) {
        super(reason);
    }
}
