package org.powermock;
/* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/PowerMockInternalException.class */
public class PowerMockInternalException extends RuntimeException {
    private static final String MESSAGE = "PowerMock internal error has happened. This exception is thrown in unexpected cases, that normally should never happen. Please, report about the issue to PowerMock issues tacker. ";

    public PowerMockInternalException(Throwable cause) {
        super(MESSAGE, cause);
    }

    public PowerMockInternalException(String message) {
        super(MESSAGE + message);
    }

    public PowerMockInternalException(String message, Throwable cause) {
        super(MESSAGE + message, cause);
    }
}
