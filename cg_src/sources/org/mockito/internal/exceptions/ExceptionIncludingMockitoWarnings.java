package org.mockito.internal.exceptions;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/exceptions/ExceptionIncludingMockitoWarnings.class */
public class ExceptionIncludingMockitoWarnings extends RuntimeException {
    private static final long serialVersionUID = -5925150219446765679L;

    public ExceptionIncludingMockitoWarnings(String message, Throwable throwable) {
        super(message, throwable);
    }
}
