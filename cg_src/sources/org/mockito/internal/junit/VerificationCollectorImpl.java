package org.mockito.internal.junit;

import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.progress.MockingProgressImpl;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.junit.VerificationCollector;
import org.mockito.verification.VerificationMode;
import org.mockito.verification.VerificationStrategy;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/junit/VerificationCollectorImpl.class */
public class VerificationCollectorImpl implements VerificationCollector {
    private StringBuilder builder;
    private int numberOfFailures;

    public VerificationCollectorImpl() {
        resetBuilder();
    }

    @Override // org.junit.rules.TestRule
    public Statement apply(final Statement base, Description description) {
        return new Statement() { // from class: org.mockito.internal.junit.VerificationCollectorImpl.1
            @Override // org.junit.runners.model.Statement
            public void evaluate() throws Throwable {
                try {
                    VerificationCollectorImpl.this.assertLazily();
                    base.evaluate();
                    VerificationCollectorImpl.this.collectAndReport();
                    ThreadSafeMockingProgress.mockingProgress().setVerificationStrategy(MockingProgressImpl.getDefaultVerificationStrategy());
                } catch (Throwable th) {
                    ThreadSafeMockingProgress.mockingProgress().setVerificationStrategy(MockingProgressImpl.getDefaultVerificationStrategy());
                    throw th;
                }
            }
        };
    }

    @Override // org.mockito.junit.VerificationCollector
    public void collectAndReport() throws MockitoAssertionError {
        ThreadSafeMockingProgress.mockingProgress().setVerificationStrategy(MockingProgressImpl.getDefaultVerificationStrategy());
        if (this.numberOfFailures > 0) {
            String error = this.builder.toString();
            resetBuilder();
            throw new MockitoAssertionError(error);
        }
    }

    @Override // org.mockito.junit.VerificationCollector
    public VerificationCollector assertLazily() {
        ThreadSafeMockingProgress.mockingProgress().setVerificationStrategy(new VerificationStrategy() { // from class: org.mockito.internal.junit.VerificationCollectorImpl.2
            @Override // org.mockito.verification.VerificationStrategy
            public VerificationMode maybeVerifyLazily(VerificationMode mode) {
                return new VerificationWrapper(mode);
            }
        });
        return this;
    }

    private void resetBuilder() {
        this.builder = new StringBuilder().append("There were multiple verification failures:");
        this.numberOfFailures = 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void append(String message) {
        this.numberOfFailures++;
        this.builder.append('\n').append(this.numberOfFailures).append(". ").append(message.trim()).append('\n');
    }

    /* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/junit/VerificationCollectorImpl$VerificationWrapper.class */
    private class VerificationWrapper implements VerificationMode {
        private final VerificationMode delegate;

        private VerificationWrapper(VerificationMode delegate) {
            this.delegate = delegate;
        }

        @Override // org.mockito.verification.VerificationMode
        public void verify(VerificationData data) {
            try {
                this.delegate.verify(data);
            } catch (AssertionError error) {
                VerificationCollectorImpl.this.append(error.getMessage());
            }
        }

        @Override // org.mockito.verification.VerificationMode
        public VerificationMode description(String description) {
            throw new IllegalStateException("Should not fail in this mode");
        }
    }
}
