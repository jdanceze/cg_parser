package org.powermock.api.mockito.expectation;

import java.lang.reflect.Method;
import org.mockito.stubbing.Stubber;
/* loaded from: gencallgraphv3.jar:powermock-api-mockito2-2.0.9.jar:org/powermock/api/mockito/expectation/PowerMockitoStubber.class */
public interface PowerMockitoStubber extends Stubber {
    void when(Class<?> cls);

    <T> PrivatelyExpectedArguments when(T t, Method method) throws Exception;

    <T> void when(T t, Object... objArr) throws Exception;

    <T> void when(T t, String str, Object... objArr) throws Exception;

    <T> PrivatelyExpectedArguments when(Class<T> cls, Method method) throws Exception;

    <T> void when(Class<T> cls, Object... objArr) throws Exception;

    <T> void when(Class<T> cls, String str, Object... objArr) throws Exception;
}
