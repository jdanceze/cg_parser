package javax.jms;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/jms/JMSSecurityException.class */
public class JMSSecurityException extends JMSException {
    public JMSSecurityException(String reason, String errorCode) {
        super(reason, errorCode);
    }

    public JMSSecurityException(String reason) {
        super(reason);
    }
}
