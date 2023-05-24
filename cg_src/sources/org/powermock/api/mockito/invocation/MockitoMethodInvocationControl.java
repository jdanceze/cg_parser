package org.powermock.api.mockito.invocation;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.powermock.api.mockito.internal.invocation.InvocationControlAssertionError;
import org.powermock.api.mockito.internal.stubbing.MockitoRealMethodInvocation;
import org.powermock.core.MockGateway;
import org.powermock.core.spi.MethodInvocationControl;
/* loaded from: gencallgraphv3.jar:powermock-api-mockito2-2.0.9.jar:org/powermock/api/mockito/invocation/MockitoMethodInvocationControl.class */
public class MockitoMethodInvocationControl<T> implements MethodInvocationControl {
    private final Set<Method> mockedMethods;
    private final Object delegator;
    private final MockHandlerAdaptor<T> mockHandlerAdaptor;

    public MockitoMethodInvocationControl(Object delegator, T mockInstance, Method... methodsToMock) {
        this.mockHandlerAdaptor = new MockHandlerAdaptor<>(mockInstance);
        this.mockedMethods = toSet(methodsToMock);
        this.delegator = delegator;
    }

    @Override // org.powermock.core.spi.MethodInvocationControl
    public boolean isMocked(Method method) {
        return this.mockedMethods == null || this.mockedMethods.contains(method);
    }

    @Override // java.lang.reflect.InvocationHandler
    public Object invoke(Object mock, Method method, Object[] arguments) throws Throwable {
        Object returnValue;
        if (isCanBeHandledByMockito(method) && hasBeenCaughtByMockitoProxy()) {
            returnValue = MockGateway.PROCEED;
        } else {
            if (mock instanceof Class) {
                returnValue = this.mockHandlerAdaptor.performIntercept(this.mockHandlerAdaptor.getMockSettings().getTypeToMock(), method, arguments);
            } else {
                returnValue = this.mockHandlerAdaptor.performIntercept(mock, method, arguments);
            }
            if (returnValue == null) {
                return MockGateway.SUPPRESS;
            }
        }
        return returnValue;
    }

    private boolean isCanBeHandledByMockito(Method method) {
        int modifiers = method.getModifiers();
        return (!hasDelegator() || Modifier.isPrivate(modifiers) || Modifier.isFinal(modifiers) || Modifier.isStatic(modifiers)) ? false : true;
    }

    private boolean hasBeenCaughtByMockitoProxy() {
        return MockitoRealMethodInvocation.isHandledByMockito();
    }

    @Override // org.powermock.core.spi.DefaultBehavior
    public Object replay(Object... mocks) {
        throw new IllegalStateException("Internal error: No such thing as replay exists in Mockito.");
    }

    @Override // org.powermock.core.spi.DefaultBehavior
    public Object reset(Object... mocks) {
        throw new IllegalStateException("Internal error: No such thing as reset exists in Mockito.");
    }

    public void verifyNoMoreInteractions() {
        try {
            Mockito.verifyNoMoreInteractions(getMockHandlerAdaptor().getMock());
        } catch (Exception e) {
            throw new RuntimeException("PowerMock internal error", e);
        } catch (MockitoAssertionError e2) {
            InvocationControlAssertionError.updateErrorMessageForVerifyNoMoreInteractions(e2);
            throw e2;
        }
    }

    private Set<Method> toSet(Method... methods) {
        if (methods == null) {
            return null;
        }
        return new HashSet(Arrays.asList(methods));
    }

    private boolean hasDelegator() {
        return this.delegator != null;
    }

    public MockHandlerAdaptor<T> getMockHandlerAdaptor() {
        return this.mockHandlerAdaptor;
    }
}
