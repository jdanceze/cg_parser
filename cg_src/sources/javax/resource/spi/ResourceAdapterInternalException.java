package javax.resource.spi;

import javax.resource.ResourceException;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/resource/spi/ResourceAdapterInternalException.class */
public class ResourceAdapterInternalException extends ResourceException {
    public ResourceAdapterInternalException() {
    }

    public ResourceAdapterInternalException(String message) {
        super(message);
    }

    public ResourceAdapterInternalException(Throwable cause) {
        super(cause);
    }

    public ResourceAdapterInternalException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResourceAdapterInternalException(String message, String errorCode) {
        super(message, errorCode);
    }
}
