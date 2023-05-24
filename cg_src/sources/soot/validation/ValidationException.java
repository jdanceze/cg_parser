package soot.validation;
/* loaded from: gencallgraphv3.jar:soot/validation/ValidationException.class */
public class ValidationException extends RuntimeException {
    private Object concerned;
    private String strMessage;
    private String strCompatibilityMessage;
    private boolean warning;
    private static final long serialVersionUID = 1;

    public ValidationException(Object concerned, String strMessage, String strCompatibilityMessage, boolean isWarning) {
        super(strMessage);
        this.strCompatibilityMessage = strCompatibilityMessage;
        this.strMessage = strMessage;
        this.concerned = concerned;
        this.warning = isWarning;
    }

    public ValidationException(Object concerned, String strMessage, String strCompatibilityMessage) {
        this(concerned, strMessage, strCompatibilityMessage, false);
    }

    public ValidationException(Object concerned, String strCompatibilityMessage) {
        this(concerned, strCompatibilityMessage, strCompatibilityMessage, false);
    }

    public boolean isWarning() {
        return this.warning;
    }

    public String getRawMessage() {
        return this.strMessage;
    }

    public Object getConcerned() {
        return this.concerned;
    }

    @Override // java.lang.Throwable
    public String toString() {
        return this.strCompatibilityMessage;
    }
}
