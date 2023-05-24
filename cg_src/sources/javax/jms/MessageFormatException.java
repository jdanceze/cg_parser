package javax.jms;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/jms/MessageFormatException.class */
public class MessageFormatException extends JMSException {
    public MessageFormatException(String reason, String errorCode) {
        super(reason, errorCode);
    }

    public MessageFormatException(String reason) {
        super(reason);
    }
}
