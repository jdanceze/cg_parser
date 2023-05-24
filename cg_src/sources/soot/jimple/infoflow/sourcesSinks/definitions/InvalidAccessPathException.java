package soot.jimple.infoflow.sourcesSinks.definitions;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/sourcesSinks/definitions/InvalidAccessPathException.class */
public class InvalidAccessPathException extends RuntimeException {
    private static final long serialVersionUID = -4996789547058608944L;

    public InvalidAccessPathException() {
    }

    public InvalidAccessPathException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public InvalidAccessPathException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidAccessPathException(String message) {
        super(message);
    }

    public InvalidAccessPathException(Throwable cause) {
        super(cause);
    }
}
