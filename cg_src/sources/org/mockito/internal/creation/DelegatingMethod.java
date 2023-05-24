package org.mockito.internal.creation;

import java.lang.reflect.Method;
import org.mockito.internal.invocation.MockitoMethod;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/creation/DelegatingMethod.class */
public class DelegatingMethod implements MockitoMethod {
    private final Method method;
    private final Class<?>[] parameterTypes;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !DelegatingMethod.class.desiredAssertionStatus();
    }

    public DelegatingMethod(Method method) {
        if (!$assertionsDisabled && method == null) {
            throw new AssertionError("Method cannot be null");
        }
        this.method = method;
        this.parameterTypes = SuspendMethod.trimSuspendParameterTypes(method.getParameterTypes());
    }

    @Override // org.mockito.internal.invocation.MockitoMethod
    public Class<?>[] getExceptionTypes() {
        return this.method.getExceptionTypes();
    }

    @Override // org.mockito.internal.invocation.MockitoMethod
    public Method getJavaMethod() {
        return this.method;
    }

    @Override // org.mockito.internal.invocation.MockitoMethod
    public String getName() {
        return this.method.getName();
    }

    @Override // org.mockito.internal.invocation.MockitoMethod
    public Class<?>[] getParameterTypes() {
        return this.parameterTypes;
    }

    @Override // org.mockito.internal.invocation.MockitoMethod
    public Class<?> getReturnType() {
        return this.method.getReturnType();
    }

    @Override // org.mockito.internal.invocation.MockitoMethod
    public boolean isVarArgs() {
        return this.method.isVarArgs();
    }

    @Override // org.mockito.internal.invocation.AbstractAwareMethod
    public boolean isAbstract() {
        return (this.method.getModifiers() & 1024) != 0;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof DelegatingMethod) {
            DelegatingMethod that = (DelegatingMethod) o;
            return this.method.equals(that.method);
        }
        return this.method.equals(o);
    }

    public int hashCode() {
        return this.method.hashCode();
    }
}
