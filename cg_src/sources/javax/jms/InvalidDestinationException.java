package javax.jms;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/jms/InvalidDestinationException.class */
public class InvalidDestinationException extends JMSException {
    public InvalidDestinationException(String reason, String errorCode) {
        super(reason, errorCode);
    }

    public InvalidDestinationException(String reason) {
        super(reason);
    }
}
