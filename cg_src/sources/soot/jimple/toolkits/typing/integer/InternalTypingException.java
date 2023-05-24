package soot.jimple.toolkits.typing.integer;

import org.apache.commons.cli.HelpFormatter;
import soot.Type;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/typing/integer/InternalTypingException.class */
class InternalTypingException extends RuntimeException {
    private static final long serialVersionUID = 1874994601632508834L;
    private final Type unexpectedType;

    public InternalTypingException() {
        this.unexpectedType = null;
    }

    public InternalTypingException(Type unexpectedType) {
        this.unexpectedType = unexpectedType;
    }

    public Type getUnexpectedType() {
        return this.unexpectedType;
    }

    @Override // java.lang.Throwable
    public String getMessage() {
        Object[] objArr = new Object[2];
        objArr[0] = this.unexpectedType;
        objArr[1] = this.unexpectedType == null ? HelpFormatter.DEFAULT_OPT_PREFIX : this.unexpectedType.getClass().getSimpleName();
        return String.format("Unexpected type %s (%s)", objArr);
    }
}
