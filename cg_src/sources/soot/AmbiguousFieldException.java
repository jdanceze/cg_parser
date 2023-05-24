package soot;
/* loaded from: gencallgraphv3.jar:soot/AmbiguousFieldException.class */
public class AmbiguousFieldException extends RuntimeException {
    private static final long serialVersionUID = -1713255335762612121L;

    public AmbiguousFieldException(String fieldName, String className) {
        super(String.format("Ambiguous field name %s in class %s", fieldName, className));
    }
}
