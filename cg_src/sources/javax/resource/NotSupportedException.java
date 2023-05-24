package javax.resource;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/resource/NotSupportedException.class */
public class NotSupportedException extends ResourceException {
    public NotSupportedException() {
    }

    public NotSupportedException(String message) {
        super(message);
    }

    public NotSupportedException(Throwable cause) {
        super(cause);
    }

    public NotSupportedException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotSupportedException(String message, String errorCode) {
        super(message, errorCode);
    }
}
