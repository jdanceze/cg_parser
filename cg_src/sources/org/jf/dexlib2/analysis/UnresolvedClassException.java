package org.jf.dexlib2.analysis;

import org.jf.util.ExceptionWithContext;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/analysis/UnresolvedClassException.class */
public class UnresolvedClassException extends ExceptionWithContext {
    public UnresolvedClassException(Throwable cause) {
        super(cause);
    }

    public UnresolvedClassException(Throwable cause, String message, Object... formatArgs) {
        super(cause, message, formatArgs);
    }

    public UnresolvedClassException(String message, Object... formatArgs) {
        super(message, formatArgs);
    }
}
