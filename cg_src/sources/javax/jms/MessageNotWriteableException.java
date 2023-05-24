package javax.jms;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/jms/MessageNotWriteableException.class */
public class MessageNotWriteableException extends JMSException {
    public MessageNotWriteableException(String reason, String errorCode) {
        super(reason, errorCode);
    }

    public MessageNotWriteableException(String reason) {
        super(reason);
    }
}
