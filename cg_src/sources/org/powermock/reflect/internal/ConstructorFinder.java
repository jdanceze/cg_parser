package org.powermock.reflect.internal;

import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;
import org.powermock.reflect.exceptions.ConstructorNotFoundException;
import org.powermock.reflect.exceptions.TooManyConstructorsFoundException;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: gencallgraphv3.jar:powermock-reflect-2.0.9.jar:org/powermock/reflect/internal/ConstructorFinder.class */
public class ConstructorFinder {
    private Class<?> type;
    private Object[] arguments;
    private Class<?> unmockedType;
    private Constructor potentialConstructor;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ConstructorFinder(Class<?> type, Object... arguments) {
        if (type == null) {
            throw new IllegalArgumentException("Class type cannot be null.");
        }
        this.type = type;
        this.arguments = arguments;
        this.unmockedType = WhiteboxImpl.getOriginalUnmockedType(type);
        if (isNestedClass() && arguments != null) {
            addArgumentForNestedClass();
        }
    }

    public java.lang.reflect.Constructor findConstructor() {
        lookupPotentialConstructor();
        throwExceptionIfConstructorWasNotFound();
        return this.potentialConstructor.getJavaConstructor();
    }

    private void lookupPotentialConstructor() {
        Set<Constructor> constructors = getDeclaredConstructorsWithoutPowerMockConstructor();
        for (Constructor constructor : constructors) {
            if (constructor.canBeInvokeWith(this.arguments)) {
                setPotentialConstructor(constructor);
            }
            if (isVarArgConstructorFound()) {
                return;
            }
        }
    }

    private boolean isVarArgConstructorFound() {
        return this.potentialConstructor != null && this.potentialConstructor.isVarArg();
    }

    private void setPotentialConstructor(Constructor constructor) {
        if (this.potentialConstructor == null) {
            this.potentialConstructor = constructor;
        } else {
            throwExceptionWhenMultipleConstructorMatchesFound(new java.lang.reflect.Constructor[]{this.potentialConstructor.getJavaConstructor(), constructor.getJavaConstructor()});
        }
    }

    public void throwExceptionWhenMultipleConstructorMatchesFound(java.lang.reflect.Constructor[] constructors) {
        if (constructors == null || constructors.length < 2) {
            throw new IllegalArgumentException("Internal error: throwExceptionWhenMultipleConstructorMatchesFound needs at least two constructors.");
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Several matching constructors found, please specify the argument parameter types so that PowerMock can determine which method you're referring to.\n");
        sb.append("Matching constructors in class ").append(constructors[0].getDeclaringClass().getName()).append(" were:\n");
        for (java.lang.reflect.Constructor constructor : constructors) {
            sb.append(constructor.getName()).append("( ");
            Class<?>[] parameterTypes = constructor.getParameterTypes();
            for (Class<?> paramType : parameterTypes) {
                sb.append(paramType.getName()).append(".class ");
            }
            sb.append(")\n");
        }
        throw new TooManyConstructorsFoundException(sb.toString());
    }

    private void addArgumentForNestedClass() {
        Object[] argumentsForLocalClass = new Object[this.arguments.length + 1];
        argumentsForLocalClass[0] = this.unmockedType.getEnclosingClass();
        System.arraycopy(this.arguments, 0, argumentsForLocalClass, 1, this.arguments.length);
        this.arguments = argumentsForLocalClass;
    }

    private boolean isNestedClass() {
        return (this.unmockedType.isLocalClass() || this.unmockedType.isAnonymousClass() || this.unmockedType.isMemberClass()) && !Modifier.isStatic(this.unmockedType.getModifiers());
    }

    private Set<Constructor> getDeclaredConstructorsWithoutPowerMockConstructor() {
        java.lang.reflect.Constructor<?>[] declaredConstructors;
        Set<Constructor> constructors = new HashSet<>();
        for (java.lang.reflect.Constructor<?> constructor : this.unmockedType.getDeclaredConstructors()) {
            if (!isPowerMockConstructor(constructor.getParameterTypes())) {
                constructors.add(new Constructor(constructor));
            }
        }
        return constructors;
    }

    private boolean isPowerMockConstructor(Class<?>[] parameterTypes) {
        return parameterTypes.length >= 1 && parameterTypes[parameterTypes.length - 1].getName().equals("org.powermock.core.IndicateReloadClass");
    }

    private void throwExceptionIfConstructorWasNotFound() {
        if (this.potentialConstructor == null) {
            String message = "No constructor found in class '" + WhiteboxImpl.getOriginalUnmockedType(this.type).getName() + "' with parameter types: [ " + WhiteboxImpl.getArgumentTypesAsString(this.arguments) + " ].";
            throw new ConstructorNotFoundException(message);
        }
    }
}
