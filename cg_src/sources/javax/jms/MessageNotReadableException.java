package javax.jms;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/jms/MessageNotReadableException.class */
public class MessageNotReadableException extends JMSException {
    public MessageNotReadableException(String reason, String errorCode) {
        super(reason, errorCode);
    }

    public MessageNotReadableException(String reason) {
        super(reason);
    }
}
