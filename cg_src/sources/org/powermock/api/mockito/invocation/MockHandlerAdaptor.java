package org.powermock.api.mockito.invocation;

import java.lang.reflect.Method;
import org.mockito.MockingDetails;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.powermock.api.mockito.internal.invocation.InvocationControlAssertionError;
import org.powermock.core.MockRepository;
/* loaded from: gencallgraphv3.jar:powermock-api-mockito2-2.0.9.jar:org/powermock/api/mockito/invocation/MockHandlerAdaptor.class */
public class MockHandlerAdaptor<T> {
    private final T mock;
    private final InvocationFactory invocationFactory = new InvocationFactory();
    private final MockingDetails mockingDetails;

    /* JADX INFO: Access modifiers changed from: package-private */
    public MockHandlerAdaptor(T mock) {
        this.mock = mock;
        this.mockingDetails = Mockito.mockingDetails(mock);
    }

    public Object getMock() {
        return this.mock;
    }

    public MockCreationSettings<?> getMockSettings() {
        return this.mockingDetails.getMockCreationSettings();
    }

    private MockHandler getMockHandler() {
        return this.mockingDetails.getMockHandler();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Object performIntercept(Object mock, Method method, Object[] arguments) throws Throwable {
        Invocation invocation = createInvocation(mock, method, arguments);
        try {
            return getMockHandler().handle(invocation);
        } catch (MockitoAssertionError e) {
            InvocationControlAssertionError.updateErrorMessageForMethodInvocation(e);
            throw e;
        } catch (NotAMockException e2) {
            if (invocation.getMock().getClass().getName().startsWith("java.") && MockRepository.getInstanceMethodInvocationControl(invocation.getMock()) != null) {
                return invocation.callRealMethod();
            }
            throw e2;
        }
    }

    private Invocation createInvocation(Object mock, Method method, Object[] arguments) {
        return this.invocationFactory.createInvocation(mock, method, getMockHandler().getMockSettings(), arguments);
    }
}
