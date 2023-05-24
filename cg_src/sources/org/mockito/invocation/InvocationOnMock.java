package org.mockito.invocation;

import java.io.Serializable;
import java.lang.reflect.Method;
import org.mockito.NotExtensible;
@NotExtensible
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/invocation/InvocationOnMock.class */
public interface InvocationOnMock extends Serializable {
    Object getMock();

    Method getMethod();

    Object[] getArguments();

    <T> T getArgument(int i);

    <T> T getArgument(int i, Class<T> cls);

    Object callRealMethod() throws Throwable;
}
