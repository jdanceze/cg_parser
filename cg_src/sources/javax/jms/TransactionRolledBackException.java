package javax.jms;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/jms/TransactionRolledBackException.class */
public class TransactionRolledBackException extends JMSException {
    public TransactionRolledBackException(String reason, String errorCode) {
        super(reason, errorCode);
    }

    public TransactionRolledBackException(String reason) {
        super(reason);
    }
}
