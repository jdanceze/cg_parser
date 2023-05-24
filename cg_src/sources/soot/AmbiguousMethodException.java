package soot;
/* loaded from: gencallgraphv3.jar:soot/AmbiguousMethodException.class */
public class AmbiguousMethodException extends RuntimeException {
    private static final long serialVersionUID = -3200937620978653123L;

    public AmbiguousMethodException(String methodName, String className) {
        super(String.format("Ambiguous method name %s in class %s", methodName, className));
    }
}
