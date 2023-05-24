package soot.jimple.toolkits.typing;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/typing/TypeException.class */
public class TypeException extends Exception {
    private static final long serialVersionUID = -2484942383485179989L;

    public TypeException(String message) {
        super(message);
        if (message == null) {
            throw new InternalTypingException();
        }
    }

    public TypeException(String message, Throwable cause) {
        super(message, cause);
        if (message == null) {
            throw new InternalTypingException();
        }
    }
}
