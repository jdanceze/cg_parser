package org.powermock.reflect.internal;
/* loaded from: gencallgraphv3.jar:powermock-reflect-2.0.9.jar:org/powermock/reflect/internal/ParametersMatcher.class */
class ParametersMatcher {
    private final Class<?>[] parameterTypes;
    private final Object[] arguments;
    private boolean isVarArgs;

    public ParametersMatcher(boolean isVarArgs, Class<?>[] parameterTypes, Object[] arguments) {
        this.isVarArgs = isVarArgs;
        this.parameterTypes = parameterTypes;
        this.arguments = arguments;
    }

    public boolean match() {
        if (this.arguments != null && this.parameterTypes.length == this.arguments.length) {
            if (this.parameterTypes.length == 0) {
                return true;
            }
            return checkArgumentTypesMatchParameterTypes(this.isVarArgs, this.parameterTypes, this.arguments);
        } else if (doesParameterTypesMatchForVarArgsInvocation(this.arguments)) {
            return true;
        } else {
            return false;
        }
    }

    boolean checkArgumentTypesMatchParameterTypes(boolean isVarArgs, Class<?>[] parameterTypes, Object[] arguments) {
        int index;
        if (parameterTypes == null) {
            throw new IllegalArgumentException("parameter types cannot be null");
        }
        if (!isVarArgs && arguments.length != parameterTypes.length) {
            return false;
        }
        for (int i = 0; i < arguments.length; i++) {
            Object argument = arguments[i];
            if (argument == null) {
                if (i >= parameterTypes.length) {
                    index = parameterTypes.length - 1;
                } else {
                    index = i;
                }
                Class<?> type = parameterTypes[index];
                if (type.isPrimitive()) {
                    return false;
                }
            } else if (i >= parameterTypes.length) {
                if (!WhiteboxImpl.isAssignableFrom(parameterTypes[parameterTypes.length - 1], WhiteboxImpl.getType(argument))) {
                    return false;
                }
            } else {
                boolean assignableFrom = WhiteboxImpl.isAssignableFrom(parameterTypes[i], WhiteboxImpl.getType(argument));
                boolean isClass = parameterTypes[i].equals(Class.class) && WhiteboxImpl.isClass(argument);
                if (!assignableFrom && !isClass) {
                    return false;
                }
            }
        }
        return true;
    }

    boolean doesParameterTypesMatchForVarArgsInvocation(Object[] arguments) {
        if (this.isVarArgs && arguments != null && arguments.length >= 1 && this.parameterTypes != null && this.parameterTypes.length >= 1) {
            Class<?> componentType = this.parameterTypes[this.parameterTypes.length - 1].getComponentType();
            Object lastArgument = arguments[arguments.length - 1];
            if (lastArgument != null) {
                Class<?> lastArgumentTypeAsPrimitive = WhiteboxImpl.getTypeAsPrimitiveIfWrapped(lastArgument);
                Class<?> varArgsParameterTypeAsPrimitive = WhiteboxImpl.getTypeAsPrimitiveIfWrapped(componentType);
                this.isVarArgs = varArgsParameterTypeAsPrimitive.isAssignableFrom(lastArgumentTypeAsPrimitive);
            }
        }
        return this.isVarArgs && checkArgumentTypesMatchParameterTypes(this.isVarArgs, this.parameterTypes, arguments);
    }
}
