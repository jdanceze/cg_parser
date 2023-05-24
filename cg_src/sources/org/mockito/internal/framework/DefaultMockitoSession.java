package org.mockito.internal.framework;

import java.util.ArrayList;
import java.util.List;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.MockitoSession;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.exceptions.misusing.RedundantListenerException;
import org.mockito.internal.exceptions.Reporter;
import org.mockito.internal.junit.TestFinishedEvent;
import org.mockito.internal.junit.UniversalTestListener;
import org.mockito.plugins.MockitoLogger;
import org.mockito.quality.Strictness;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/framework/DefaultMockitoSession.class */
public class DefaultMockitoSession implements MockitoSession {
    private final String name;
    private final UniversalTestListener listener;
    private final List<AutoCloseable> closeables = new ArrayList();

    public DefaultMockitoSession(List<Object> testClassInstances, String name, Strictness strictness, MockitoLogger logger) {
        this.name = name;
        this.listener = new UniversalTestListener(strictness, logger);
        try {
            Mockito.framework().addListener(this.listener);
        } catch (RedundantListenerException e) {
            Reporter.unfinishedMockingSession();
        }
        try {
            for (Object testClassInstance : testClassInstances) {
                this.closeables.add(MockitoAnnotations.openMocks(testClassInstance));
            }
        } catch (RuntimeException e2) {
            try {
                release();
            } catch (Throwable t) {
                e2.addSuppressed(t);
            }
            this.listener.setListenerDirty();
            throw e2;
        }
    }

    @Override // org.mockito.MockitoSession
    public void setStrictness(Strictness strictness) {
        this.listener.setStrictness(strictness);
    }

    @Override // org.mockito.MockitoSession
    public void finishMocking() {
        finishMocking(null);
    }

    @Override // org.mockito.MockitoSession
    public void finishMocking(final Throwable failure) {
        try {
            Mockito.framework().removeListener(this.listener);
            this.listener.testFinished(new TestFinishedEvent() { // from class: org.mockito.internal.framework.DefaultMockitoSession.1
                @Override // org.mockito.internal.junit.TestFinishedEvent
                public Throwable getFailure() {
                    return failure;
                }

                @Override // org.mockito.internal.junit.TestFinishedEvent
                public String getTestName() {
                    return DefaultMockitoSession.this.name;
                }
            });
            if (failure == null) {
                Mockito.validateMockitoUsage();
            }
        } finally {
            release();
        }
    }

    private void release() {
        for (AutoCloseable closeable : this.closeables) {
            try {
                closeable.close();
            } catch (Exception e) {
                throw new MockitoException("Failed to release " + closeable, e);
            }
        }
    }
}
