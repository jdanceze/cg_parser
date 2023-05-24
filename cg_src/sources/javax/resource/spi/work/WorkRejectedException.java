package javax.resource.spi.work;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/resource/spi/work/WorkRejectedException.class */
public class WorkRejectedException extends WorkException {
    public WorkRejectedException() {
    }

    public WorkRejectedException(String message) {
        super(message);
    }

    public WorkRejectedException(Throwable cause) {
        super(cause);
    }

    public WorkRejectedException(String message, Throwable cause) {
        super(message, cause);
    }

    public WorkRejectedException(String message, String errorCode) {
        super(message, errorCode);
    }
}
