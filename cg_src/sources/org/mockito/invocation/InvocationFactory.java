package org.mockito.invocation;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.concurrent.Callable;
import org.mockito.Incubating;
import org.mockito.mock.MockCreationSettings;
@Incubating
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/invocation/InvocationFactory.class */
public interface InvocationFactory {

    /* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/invocation/InvocationFactory$RealMethodBehavior.class */
    public interface RealMethodBehavior<R> extends Serializable {
        R call() throws Throwable;
    }

    @Deprecated
    Invocation createInvocation(Object obj, MockCreationSettings mockCreationSettings, Method method, Callable callable, Object... objArr);

    @Incubating
    Invocation createInvocation(Object obj, MockCreationSettings mockCreationSettings, Method method, RealMethodBehavior realMethodBehavior, Object... objArr);
}
