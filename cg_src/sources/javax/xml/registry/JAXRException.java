package javax.xml.registry;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/xml/registry/JAXRException.class */
public class JAXRException extends Exception implements JAXRResponse {
    protected Throwable cause;

    public JAXRException() {
        this.cause = null;
    }

    public JAXRException(String str) {
        super(str);
        this.cause = null;
    }

    public JAXRException(String str, Throwable th) {
        super(str);
        initCause(th);
    }

    public JAXRException(Throwable th) {
        super(th.toString());
        initCause(th);
    }

    @Override // java.lang.Throwable
    public String getMessage() {
        String message = super.getMessage();
        return (message != null || this.cause == null) ? message : this.cause.getMessage();
    }

    @Override // java.lang.Throwable
    public Throwable getCause() {
        return this.cause;
    }

    @Override // java.lang.Throwable
    public synchronized Throwable initCause(Throwable th) {
        if (this.cause != null) {
            throw new IllegalStateException("Can't override cause");
        }
        if (th == this) {
            throw new IllegalArgumentException("Self-causation not permitted");
        }
        this.cause = th;
        return this;
    }

    @Override // javax.xml.registry.JAXRResponse
    public String getRequestId() {
        return null;
    }

    @Override // javax.xml.registry.JAXRResponse
    public int getStatus() {
        return 0;
    }

    @Override // javax.xml.registry.JAXRResponse
    public boolean isAvailable() throws JAXRException {
        return true;
    }
}
