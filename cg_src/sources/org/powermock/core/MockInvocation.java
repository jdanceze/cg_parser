package org.powermock.core;

import java.lang.reflect.Method;
import org.powermock.core.spi.MethodInvocationControl;
import org.powermock.reflect.exceptions.MethodNotFoundException;
import org.powermock.reflect.internal.WhiteboxImpl;
import org.powermock.reflect.internal.proxy.UnproxiedType;
/* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/core/MockInvocation.class */
class MockInvocation {
    private Object object;
    private String methodName;
    private Class<?>[] sig;
    private Class<?> objectType;
    private MethodInvocationControl methodInvocationControl;
    private Method method;

    /* JADX INFO: Access modifiers changed from: package-private */
    public MockInvocation(Object object, String methodName, Class<?>... sig) {
        this.object = object;
        this.methodName = methodName;
        this.sig = sig;
        init();
    }

    private void init() {
        if (this.object instanceof Class) {
            this.objectType = (Class) this.object;
            this.methodInvocationControl = MockRepository.getStaticMethodInvocationControl(this.objectType);
        } else {
            Class<?> type = this.object.getClass();
            UnproxiedType unproxiedType = WhiteboxImpl.getUnproxiedType(type);
            this.objectType = unproxiedType.getOriginalType();
            this.methodInvocationControl = MockRepository.getInstanceMethodInvocationControl(this.object);
        }
        this.method = findMethodToInvoke(this.methodName, this.sig, this.objectType);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Class<?> getObjectType() {
        return this.objectType;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public MethodInvocationControl getMethodInvocationControl() {
        return this.methodInvocationControl;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Method getMethod() {
        return this.method;
    }

    private static Method findMethodToInvoke(String methodName, Class<?>[] sig, Class<?> objectType) {
        Method method;
        try {
            method = WhiteboxImpl.getBestMethodCandidate(objectType, methodName, sig, true);
        } catch (MethodNotFoundException e) {
            try {
                method = WhiteboxImpl.getMethod(Class.class, methodName, sig);
            } catch (MethodNotFoundException e2) {
                throw e;
            }
        }
        return method;
    }
}
