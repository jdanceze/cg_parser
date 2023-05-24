package org.jf.dexlib2.analysis;

import org.jf.util.ExceptionWithContext;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/analysis/AnalysisException.class */
public class AnalysisException extends ExceptionWithContext {
    public int codeAddress;

    public AnalysisException(Throwable cause) {
        super(cause);
    }

    public AnalysisException(Throwable cause, String message, Object... formatArgs) {
        super(cause, message, formatArgs);
    }

    public AnalysisException(String message, Object... formatArgs) {
        super(message, formatArgs);
    }
}
