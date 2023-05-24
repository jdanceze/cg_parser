package org.powermock.api.mockito.invocation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.concurrent.Callable;
import org.mockito.Mockito;
import org.mockito.invocation.Invocation;
import org.mockito.mock.MockCreationSettings;
import org.powermock.api.support.SafeExceptionRethrower;
import org.powermock.core.MockGateway;
import org.powermock.core.MockRepository;
import org.powermock.reflect.Whitebox;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: gencallgraphv3.jar:powermock-api-mockito2-2.0.9.jar:org/powermock/api/mockito/invocation/InvocationFactory.class */
public class InvocationFactory {
    /* JADX INFO: Access modifiers changed from: package-private */
    public Invocation createInvocation(Object mock, Method method, MockCreationSettings settings, Object... arguments) {
        Callable realMethod = createRealMethod(mock, method, arguments);
        return Mockito.framework().getInvocationFactory().createInvocation(mock, settings, method, realMethod, arguments);
    }

    private Callable createRealMethod(final Object delegator, final Method method, final Object... arguments) {
        return new Callable() { // from class: org.powermock.api.mockito.invocation.InvocationFactory.1
            @Override // java.util.concurrent.Callable
            public Object call() throws Exception {
                Class<?> type = Whitebox.getType(delegator);
                boolean isFinalSystemClass = type.getName().startsWith("java.") && Modifier.isFinal(type.getModifiers());
                if (!isFinalSystemClass) {
                    MockRepository.putAdditionalState(MockGateway.DONT_MOCK_NEXT_CALL, true);
                }
                try {
                    return method.invoke(delegator, arguments);
                } catch (InvocationTargetException e) {
                    SafeExceptionRethrower.safeRethrow(e.getCause());
                    return null;
                }
            }
        };
    }
}
