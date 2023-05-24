package org.mockito.internal.session;

import org.mockito.plugins.MockitoLogger;
import org.mockito.session.MockitoSessionLogger;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/session/MockitoLoggerAdapter.class */
class MockitoLoggerAdapter implements MockitoLogger {
    private final MockitoSessionLogger logger;

    /* JADX INFO: Access modifiers changed from: package-private */
    public MockitoLoggerAdapter(MockitoSessionLogger logger) {
        this.logger = logger;
    }

    @Override // org.mockito.plugins.MockitoLogger
    public void log(Object what) {
        this.logger.log(String.valueOf(what));
    }
}
