package javax.jms;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/jms/InvalidClientIDException.class */
public class InvalidClientIDException extends JMSException {
    public InvalidClientIDException(String reason, String errorCode) {
        super(reason, errorCode);
    }

    public InvalidClientIDException(String reason) {
        super(reason);
    }
}
