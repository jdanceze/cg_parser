package org.powermock.reflect.internal;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: gencallgraphv3.jar:powermock-reflect-2.0.9.jar:org/powermock/reflect/internal/Constructor.class */
public class Constructor {
    private final Class<?>[] parameterTypes;
    private java.lang.reflect.Constructor constructor;
    private boolean isVarArgs;

    /* JADX INFO: Access modifiers changed from: package-private */
    public Constructor(java.lang.reflect.Constructor constructor) {
        this.constructor = constructor;
        this.parameterTypes = constructor.getParameterTypes();
        this.isVarArgs = constructor.isVarArgs();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean canBeInvokeWith(Object[] arguments) {
        return new ParametersMatcher(this.isVarArgs, this.parameterTypes, arguments).match();
    }

    public java.lang.reflect.Constructor<?> getJavaConstructor() {
        return this.constructor;
    }

    public boolean isVarArg() {
        return this.isVarArgs;
    }
}
