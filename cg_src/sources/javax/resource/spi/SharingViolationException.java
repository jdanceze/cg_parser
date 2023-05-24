package javax.resource.spi;

import javax.resource.ResourceException;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/resource/spi/SharingViolationException.class */
public class SharingViolationException extends ResourceException {
    public SharingViolationException() {
    }

    public SharingViolationException(String message) {
        super(message);
    }

    public SharingViolationException(Throwable cause) {
        super(cause);
    }

    public SharingViolationException(String message, Throwable cause) {
        super(message, cause);
    }

    public SharingViolationException(String message, String errorCode) {
        super(message, errorCode);
    }
}
