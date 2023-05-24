package org.mockito.internal.framework;

import org.mockito.MockitoFramework;
import org.mockito.internal.configuration.plugins.Plugins;
import org.mockito.internal.invocation.DefaultInvocationFactory;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.util.Checks;
import org.mockito.invocation.InvocationFactory;
import org.mockito.listeners.MockitoListener;
import org.mockito.plugins.InlineMockMaker;
import org.mockito.plugins.MockMaker;
import org.mockito.plugins.MockitoPlugins;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/framework/DefaultMockitoFramework.class */
public class DefaultMockitoFramework implements MockitoFramework {
    @Override // org.mockito.MockitoFramework
    public MockitoFramework addListener(MockitoListener listener) {
        Checks.checkNotNull(listener, "listener");
        ThreadSafeMockingProgress.mockingProgress().addListener(listener);
        return this;
    }

    @Override // org.mockito.MockitoFramework
    public MockitoFramework removeListener(MockitoListener listener) {
        Checks.checkNotNull(listener, "listener");
        ThreadSafeMockingProgress.mockingProgress().removeListener(listener);
        return this;
    }

    @Override // org.mockito.MockitoFramework
    public MockitoPlugins getPlugins() {
        return Plugins.getPlugins();
    }

    @Override // org.mockito.MockitoFramework
    public InvocationFactory getInvocationFactory() {
        return new DefaultInvocationFactory();
    }

    private InlineMockMaker getInlineMockMaker() {
        MockMaker mockMaker = Plugins.getMockMaker();
        if (mockMaker instanceof InlineMockMaker) {
            return (InlineMockMaker) mockMaker;
        }
        return null;
    }

    @Override // org.mockito.MockitoFramework
    public void clearInlineMocks() {
        InlineMockMaker mockMaker = getInlineMockMaker();
        if (mockMaker != null) {
            mockMaker.clearAllMocks();
        }
    }

    @Override // org.mockito.MockitoFramework
    public void clearInlineMock(Object mock) {
        InlineMockMaker mockMaker = getInlineMockMaker();
        if (mockMaker != null) {
            mockMaker.clearMock(mock);
        }
    }
}
