package org.mockito.internal.stubbing.answers;

import java.io.Serializable;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/stubbing/answers/ThrowsException.class */
public class ThrowsException extends AbstractThrowsException implements Serializable {
    private static final long serialVersionUID = 1128820328555183980L;
    private final Throwable throwable;

    public ThrowsException(Throwable throwable) {
        this.throwable = throwable;
    }

    @Override // org.mockito.internal.stubbing.answers.AbstractThrowsException
    protected Throwable getThrowable() {
        return this.throwable;
    }
}
