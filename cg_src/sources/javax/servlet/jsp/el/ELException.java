package javax.servlet.jsp.el;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/servlet/jsp/el/ELException.class */
public class ELException extends Exception {
    private Throwable mRootCause;

    public ELException() {
    }

    public ELException(String pMessage) {
        super(pMessage);
    }

    public ELException(Throwable pRootCause) {
        super(pRootCause.getLocalizedMessage());
        this.mRootCause = pRootCause;
    }

    public ELException(String pMessage, Throwable pRootCause) {
        super(pMessage);
        this.mRootCause = pRootCause;
    }

    public Throwable getRootCause() {
        return this.mRootCause;
    }
}
