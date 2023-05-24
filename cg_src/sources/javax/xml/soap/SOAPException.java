package javax.xml.soap;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/xml/soap/SOAPException.class */
public class SOAPException extends Exception {
    private Throwable cause;

    public SOAPException() {
        this.cause = null;
    }

    public SOAPException(String str) {
        super(str);
        this.cause = null;
    }

    public SOAPException(String str, Throwable th) {
        super(str);
        initCause(th);
    }

    public SOAPException(Throwable th) {
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
}
