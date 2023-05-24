package javax.servlet.jsp;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/servlet/jsp/JspException.class */
public class JspException extends Exception {
    private Throwable rootCause;

    public JspException() {
    }

    public JspException(String msg) {
        super(msg);
    }

    public JspException(String message, Throwable rootCause) {
        super(message);
        this.rootCause = rootCause;
    }

    public JspException(Throwable rootCause) {
        super(rootCause.getLocalizedMessage());
        this.rootCause = rootCause;
    }

    public Throwable getRootCause() {
        return this.rootCause;
    }
}
