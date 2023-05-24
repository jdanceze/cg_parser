package javax.xml.rpc;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/xml/rpc/JAXRPCException.class */
public class JAXRPCException extends RuntimeException {
    private Throwable cause;

    public JAXRPCException() {
        this.cause = null;
    }

    public JAXRPCException(String message) {
        super(message);
        this.cause = null;
    }

    public JAXRPCException(String message, Throwable cause) {
        super(message);
        this.cause = cause;
    }

    public JAXRPCException(Throwable cause) {
        super(cause == null ? null : cause.toString());
        this.cause = cause;
    }

    public Throwable getLinkedCause() {
        return this.cause;
    }
}
