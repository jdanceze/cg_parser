package org.mockito.internal.util;

import org.mockito.plugins.MockitoLogger;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/util/ConsoleMockitoLogger.class */
public class ConsoleMockitoLogger implements MockitoLogger {
    @Override // org.mockito.plugins.MockitoLogger
    public void log(Object what) {
        System.out.println(what);
    }
}
