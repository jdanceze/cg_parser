package javax.resource.spi;

import javax.resource.ResourceException;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/resource/spi/CommException.class */
public class CommException extends ResourceException {
    public CommException() {
    }

    public CommException(String message) {
        super(message);
    }

    public CommException(Throwable cause) {
        super(cause);
    }

    public CommException(String message, Throwable cause) {
        super(message, cause);
    }

    public CommException(String message, String errorCode) {
        super(message, errorCode);
    }
}
