package org.mockito.internal.invocation;

import java.lang.reflect.Method;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/invocation/MockitoMethod.class */
public interface MockitoMethod extends AbstractAwareMethod {
    String getName();

    Class<?> getReturnType();

    Class<?>[] getParameterTypes();

    Class<?>[] getExceptionTypes();

    boolean isVarArgs();

    Method getJavaMethod();
}
