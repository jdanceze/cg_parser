package javax.resource.spi.work;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/resource/spi/work/WorkCompletedException.class */
public class WorkCompletedException extends WorkException {
    public WorkCompletedException() {
    }

    public WorkCompletedException(String message) {
        super(message);
    }

    public WorkCompletedException(Throwable cause) {
        super(cause);
    }

    public WorkCompletedException(String message, Throwable cause) {
        super(message, cause);
    }

    public WorkCompletedException(String message, String errorCode) {
        super(message, errorCode);
    }
}
