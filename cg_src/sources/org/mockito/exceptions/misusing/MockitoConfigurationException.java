package org.mockito.exceptions.misusing;

import org.mockito.exceptions.base.MockitoException;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/exceptions/misusing/MockitoConfigurationException.class */
public class MockitoConfigurationException extends MockitoException {
    private static final long serialVersionUID = 1;

    public MockitoConfigurationException(String message) {
        super(message);
    }

    public MockitoConfigurationException(String message, Exception cause) {
        super(message, cause);
    }
}
