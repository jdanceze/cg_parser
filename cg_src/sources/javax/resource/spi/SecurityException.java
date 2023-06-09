package javax.resource.spi;

import javax.resource.ResourceException;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/resource/spi/SecurityException.class */
public class SecurityException extends ResourceException {
    public SecurityException() {
    }

    public SecurityException(String message) {
        super(message);
    }

    public SecurityException(Throwable cause) {
        super(cause);
    }

    public SecurityException(String message, Throwable cause) {
        super(message, cause);
    }

    public SecurityException(String message, String errorCode) {
        super(message, errorCode);
    }
}
