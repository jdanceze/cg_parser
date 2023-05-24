package javax.resource;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/resource/ResourceException.class */
public class ResourceException extends Exception {
    private String errorCode;
    private Exception linkedException;

    public ResourceException() {
    }

    public ResourceException(String message) {
        super(message);
    }

    public ResourceException(Throwable cause) {
        super(cause);
    }

    public ResourceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResourceException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return this.errorCode;
    }

    public Exception getLinkedException() {
        return this.linkedException;
    }

    public void setLinkedException(Exception ex) {
        this.linkedException = ex;
    }

    @Override // java.lang.Throwable
    public String toString() {
        return getMessage();
    }
}
