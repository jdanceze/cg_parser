package javax.jms;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/jms/ResourceAllocationException.class */
public class ResourceAllocationException extends JMSException {
    public ResourceAllocationException(String reason, String errorCode) {
        super(reason, errorCode);
    }

    public ResourceAllocationException(String reason) {
        super(reason);
    }
}
