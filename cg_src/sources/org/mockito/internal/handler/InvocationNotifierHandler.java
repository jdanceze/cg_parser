package org.mockito.internal.handler;

import java.util.List;
import org.mockito.internal.exceptions.Reporter;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.InvocationContainer;
import org.mockito.invocation.MockHandler;
import org.mockito.listeners.InvocationListener;
import org.mockito.mock.MockCreationSettings;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/handler/InvocationNotifierHandler.class */
public class InvocationNotifierHandler<T> implements MockHandler<T> {
    private final List<InvocationListener> invocationListeners;
    private final MockHandler<T> mockHandler;

    public InvocationNotifierHandler(MockHandler<T> mockHandler, MockCreationSettings<T> settings) {
        this.mockHandler = mockHandler;
        this.invocationListeners = settings.getInvocationListeners();
    }

    @Override // org.mockito.invocation.MockHandler
    public Object handle(Invocation invocation) throws Throwable {
        try {
            Object returnedValue = this.mockHandler.handle(invocation);
            notifyMethodCall(invocation, returnedValue);
            return returnedValue;
        } catch (Throwable t) {
            notifyMethodCallException(invocation, t);
            throw t;
        }
    }

    private void notifyMethodCall(Invocation invocation, Object returnValue) {
        for (InvocationListener listener : this.invocationListeners) {
            try {
                listener.reportInvocation(new NotifiedMethodInvocationReport(invocation, returnValue));
            } catch (Throwable listenerThrowable) {
                throw Reporter.invocationListenerThrewException(listener, listenerThrowable);
            }
        }
    }

    private void notifyMethodCallException(Invocation invocation, Throwable exception) {
        for (InvocationListener listener : this.invocationListeners) {
            try {
                listener.reportInvocation(new NotifiedMethodInvocationReport(invocation, exception));
            } catch (Throwable listenerThrowable) {
                throw Reporter.invocationListenerThrewException(listener, listenerThrowable);
            }
        }
    }

    @Override // org.mockito.invocation.MockHandler
    public MockCreationSettings<T> getMockSettings() {
        return this.mockHandler.getMockSettings();
    }

    @Override // org.mockito.invocation.MockHandler
    public InvocationContainer getInvocationContainer() {
        return this.mockHandler.getInvocationContainer();
    }
}
