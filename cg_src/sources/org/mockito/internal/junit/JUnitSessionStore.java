package org.mockito.internal.junit;

import org.junit.runners.model.Statement;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.MockitoSession;
import org.mockito.internal.session.MockitoSessionLoggerAdapter;
import org.mockito.plugins.MockitoLogger;
import org.mockito.quality.Strictness;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/junit/JUnitSessionStore.class */
class JUnitSessionStore {
    private final MockitoLogger logger;
    private MockitoSession session;
    protected Strictness strictness;

    /* JADX INFO: Access modifiers changed from: package-private */
    public JUnitSessionStore(MockitoLogger logger, Strictness strictness) {
        this.logger = logger;
        this.strictness = strictness;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Statement createStatement(final Statement base, final String methodName, final Object target) {
        return new Statement() { // from class: org.mockito.internal.junit.JUnitSessionStore.1
            @Override // org.junit.runners.model.Statement
            public void evaluate() throws Throwable {
                AutoCloseable closeable;
                if (JUnitSessionStore.this.session == null) {
                    JUnitSessionStore.this.session = Mockito.mockitoSession().name(methodName).strictness(JUnitSessionStore.this.strictness).logger(new MockitoSessionLoggerAdapter(JUnitSessionStore.this.logger)).initMocks(target).startMocking();
                    closeable = null;
                } else {
                    closeable = MockitoAnnotations.openMocks(target);
                }
                Throwable testFailure = evaluateSafely(base);
                JUnitSessionStore.this.session.finishMocking(testFailure);
                if (closeable != null) {
                    closeable.close();
                }
                if (testFailure != null) {
                    throw testFailure;
                }
            }

            private Throwable evaluateSafely(Statement base2) {
                try {
                    base2.evaluate();
                    return null;
                } catch (Throwable throwable) {
                    return throwable;
                }
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setStrictness(Strictness strictness) {
        this.strictness = strictness;
        if (this.session != null) {
            this.session.setStrictness(strictness);
        }
    }
}
