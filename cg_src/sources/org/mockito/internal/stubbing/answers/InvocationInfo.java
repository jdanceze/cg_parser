package org.mockito.internal.stubbing.answers;

import java.lang.reflect.Method;
import org.mockito.internal.invocation.AbstractAwareMethod;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.Primitives;
import org.mockito.internal.util.reflection.GenericMetadataSupport;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.mock.MockCreationSettings;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/stubbing/answers/InvocationInfo.class */
public class InvocationInfo implements AbstractAwareMethod {
    private final Method method;
    private final InvocationOnMock invocation;

    public InvocationInfo(InvocationOnMock theInvocation) {
        this.method = theInvocation.getMethod();
        this.invocation = theInvocation;
    }

    public boolean isValidException(Throwable throwable) {
        Class<?>[] exceptions = this.method.getExceptionTypes();
        Class<?> throwableClass = throwable.getClass();
        for (Class<?> exception : exceptions) {
            if (exception.isAssignableFrom(throwableClass)) {
                return true;
            }
        }
        return false;
    }

    public boolean isValidReturnType(Class<?> clazz) {
        if (this.method.getReturnType().isPrimitive() || clazz.isPrimitive()) {
            return Primitives.primitiveTypeOf(clazz) == Primitives.primitiveTypeOf(this.method.getReturnType());
        }
        return this.method.getReturnType().isAssignableFrom(clazz);
    }

    public boolean isVoid() {
        MockCreationSettings mockSettings = MockUtil.getMockHandler(this.invocation.getMock()).getMockSettings();
        Class<?> returnType = GenericMetadataSupport.inferFrom(mockSettings.getTypeToMock()).resolveGenericReturnType(this.method).rawType();
        return returnType == Void.TYPE || returnType == Void.class;
    }

    public String printMethodReturnType() {
        return this.method.getReturnType().getSimpleName();
    }

    public String getMethodName() {
        return this.method.getName();
    }

    public boolean returnsPrimitive() {
        return this.method.getReturnType().isPrimitive();
    }

    public Method getMethod() {
        return this.method;
    }

    public boolean isDeclaredOnInterface() {
        return this.method.getDeclaringClass().isInterface();
    }

    @Override // org.mockito.internal.invocation.AbstractAwareMethod
    public boolean isAbstract() {
        return (this.method.getModifiers() & 1024) != 0;
    }
}
