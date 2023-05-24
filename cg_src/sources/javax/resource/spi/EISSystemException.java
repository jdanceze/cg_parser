package javax.resource.spi;

import javax.resource.ResourceException;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/resource/spi/EISSystemException.class */
public class EISSystemException extends ResourceException {
    public EISSystemException() {
    }

    public EISSystemException(String message) {
        super(message);
    }

    public EISSystemException(Throwable cause) {
        super(cause);
    }

    public EISSystemException(String message, Throwable cause) {
        super(message, cause);
    }

    public EISSystemException(String message, String errorCode) {
        super(message, errorCode);
    }
}
