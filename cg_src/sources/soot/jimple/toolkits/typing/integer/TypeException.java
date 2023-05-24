package soot.jimple.toolkits.typing.integer;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/typing/integer/TypeException.class */
public class TypeException extends Exception {
    private static final long serialVersionUID = -602930090190087993L;

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
