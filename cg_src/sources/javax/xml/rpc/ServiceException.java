package javax.xml.rpc;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/xml/rpc/ServiceException.class */
public class ServiceException extends Exception {
    private Throwable cause;

    public ServiceException() {
        this.cause = null;
    }

    public ServiceException(String message) {
        super(message);
        this.cause = null;
    }

    public ServiceException(String message, Throwable cause) {
        super(message);
        this.cause = cause;
    }

    public ServiceException(Throwable cause) {
        super(cause == null ? null : cause.toString());
        this.cause = cause;
    }

    public Throwable getLinkedCause() {
        return this.cause;
    }
}
