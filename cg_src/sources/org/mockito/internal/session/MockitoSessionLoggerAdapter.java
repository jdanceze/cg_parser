package org.mockito.internal.session;

import org.mockito.plugins.MockitoLogger;
import org.mockito.session.MockitoSessionLogger;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/session/MockitoSessionLoggerAdapter.class */
public class MockitoSessionLoggerAdapter implements MockitoSessionLogger {
    private final MockitoLogger logger;

    public MockitoSessionLoggerAdapter(MockitoLogger logger) {
        this.logger = logger;
    }

    @Override // org.mockito.session.MockitoSessionLogger
    public void log(String hint) {
        this.logger.log(hint);
    }
}
