package javax.resource.spi;

import javax.resource.ResourceException;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/resource/spi/LocalTransactionException.class */
public class LocalTransactionException extends ResourceException {
    public LocalTransactionException() {
    }

    public LocalTransactionException(String message) {
        super(message);
    }

    public LocalTransactionException(Throwable cause) {
        super(cause);
    }

    public LocalTransactionException(String message, Throwable cause) {
        super(message, cause);
    }

    public LocalTransactionException(String message, String errorCode) {
        super(message, errorCode);
    }
}
