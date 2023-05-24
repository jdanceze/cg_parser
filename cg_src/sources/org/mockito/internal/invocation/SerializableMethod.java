package org.mockito.internal.invocation;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Arrays;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.creation.SuspendMethod;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/invocation/SerializableMethod.class */
public class SerializableMethod implements Serializable, MockitoMethod {
    private static final long serialVersionUID = 6005610965006048445L;
    private final Class<?> declaringClass;
    private final String methodName;
    private final Class<?>[] parameterTypes;
    private final Class<?> returnType;
    private final Class<?>[] exceptionTypes;
    private final boolean isVarArgs;
    private final boolean isAbstract;
    private volatile transient Method method;

    public SerializableMethod(Method method) {
        this.method = method;
        this.declaringClass = method.getDeclaringClass();
        this.methodName = method.getName();
        this.parameterTypes = SuspendMethod.trimSuspendParameterTypes(method.getParameterTypes());
        this.returnType = method.getReturnType();
        this.exceptionTypes = method.getExceptionTypes();
        this.isVarArgs = method.isVarArgs();
        this.isAbstract = (method.getModifiers() & 1024) != 0;
    }

    @Override // org.mockito.internal.invocation.MockitoMethod
    public String getName() {
        return this.methodName;
    }

    @Override // org.mockito.internal.invocation.MockitoMethod
    public Class<?> getReturnType() {
        return this.returnType;
    }

    @Override // org.mockito.internal.invocation.MockitoMethod
    public Class<?>[] getParameterTypes() {
        return this.parameterTypes;
    }

    @Override // org.mockito.internal.invocation.MockitoMethod
    public Class<?>[] getExceptionTypes() {
        return this.exceptionTypes;
    }

    @Override // org.mockito.internal.invocation.MockitoMethod
    public boolean isVarArgs() {
        return this.isVarArgs;
    }

    @Override // org.mockito.internal.invocation.AbstractAwareMethod
    public boolean isAbstract() {
        return this.isAbstract;
    }

    @Override // org.mockito.internal.invocation.MockitoMethod
    public Method getJavaMethod() {
        if (this.method != null) {
            return this.method;
        }
        try {
            this.method = this.declaringClass.getDeclaredMethod(this.methodName, this.parameterTypes);
            return this.method;
        } catch (NoSuchMethodException e) {
            String message = String.format("The method %1$s.%2$s does not exists and you should not get to this point.\nPlease report this as a defect with an example of how to reproduce it.", this.declaringClass, this.methodName);
            throw new MockitoException(message, e);
        } catch (SecurityException e2) {
            String message2 = String.format("The method %1$s.%2$s is probably private or protected and cannot be mocked.\nPlease report this as a defect with an example of how to reproduce it.", this.declaringClass, this.methodName);
            throw new MockitoException(message2, e2);
        }
    }

    public int hashCode() {
        return 1;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj != null && getClass() == obj.getClass()) {
            SerializableMethod other = (SerializableMethod) obj;
            if (this.declaringClass == null) {
                if (other.declaringClass != null) {
                    return false;
                }
            } else if (!this.declaringClass.equals(other.declaringClass)) {
                return false;
            }
            if (this.methodName == null) {
                if (other.methodName != null) {
                    return false;
                }
            } else if (!this.methodName.equals(other.methodName)) {
                return false;
            }
            if (Arrays.equals(this.parameterTypes, other.parameterTypes)) {
                return this.returnType == null ? other.returnType == null : this.returnType.equals(other.returnType);
            }
            return false;
        }
        return false;
    }
}
