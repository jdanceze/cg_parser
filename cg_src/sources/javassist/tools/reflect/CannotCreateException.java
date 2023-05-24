package javassist.tools.reflect;
/* loaded from: gencallgraphv3.jar:javassist-3.28.0-GA.jar:javassist/tools/reflect/CannotCreateException.class */
public class CannotCreateException extends Exception {
    private static final long serialVersionUID = 1;

    public CannotCreateException(String s) {
        super(s);
    }

    public CannotCreateException(Exception e) {
        super("by " + e.toString());
    }
}
