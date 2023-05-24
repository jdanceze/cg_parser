package javax.resource.spi;

import javax.resource.ResourceException;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/resource/spi/ResourceAllocationException.class */
public class ResourceAllocationException extends ResourceException {
    public ResourceAllocationException() {
    }

    public ResourceAllocationException(String message) {
        super(message);
    }

    public ResourceAllocationException(Throwable cause) {
        super(cause);
    }

    public ResourceAllocationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResourceAllocationException(String message, String errorCode) {
        super(message, errorCode);
    }
}
