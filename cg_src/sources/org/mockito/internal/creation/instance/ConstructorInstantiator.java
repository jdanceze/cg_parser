package org.mockito.internal.creation.instance;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import org.mockito.internal.configuration.plugins.Plugins;
import org.mockito.internal.util.Primitives;
import org.mockito.internal.util.StringUtil;
import org.mockito.plugins.MemberAccessor;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/creation/instance/ConstructorInstantiator.class */
public class ConstructorInstantiator implements org.mockito.creation.instance.Instantiator {
    private final boolean hasOuterClassInstance;
    private final Object[] constructorArgs;

    public ConstructorInstantiator(boolean hasOuterClassInstance, Object... constructorArgs) {
        this.hasOuterClassInstance = hasOuterClassInstance;
        this.constructorArgs = constructorArgs;
    }

    @Override // org.mockito.creation.instance.Instantiator
    public <T> T newInstance(Class<T> cls) {
        return (T) withParams(cls, this.constructorArgs);
    }

    private <T> T withParams(Class<T> cls, Object... params) {
        Constructor<?>[] declaredConstructors;
        List<Constructor<?>> matchingConstructors = new LinkedList<>();
        try {
            for (Constructor<?> constructor : cls.getDeclaredConstructors()) {
                Class<?>[] types = constructor.getParameterTypes();
                if (paramsMatch(types, params)) {
                    evaluateConstructor(matchingConstructors, constructor);
                }
            }
            if (matchingConstructors.size() == 1) {
                return (T) invokeConstructor(matchingConstructors.get(0), params);
            }
            if (matchingConstructors.size() == 0) {
                throw noMatchingConstructor(cls);
            }
            throw multipleMatchingConstructors(cls, matchingConstructors);
        } catch (Exception e) {
            throw paramsException(cls, e);
        }
    }

    private static <T> T invokeConstructor(Constructor<?> constructor, Object... params) throws java.lang.InstantiationException, IllegalAccessException, InvocationTargetException {
        MemberAccessor accessor = Plugins.getMemberAccessor();
        return (T) accessor.newInstance(constructor, params);
    }

    private org.mockito.creation.instance.InstantiationException paramsException(Class<?> cls, Exception e) {
        return new org.mockito.creation.instance.InstantiationException(StringUtil.join("Unable to create instance of '" + cls.getSimpleName() + "'.", "Please ensure the target class has " + constructorArgsString() + " and executes cleanly."), e);
    }

    private String constructorArgTypes() {
        int argPos = 0;
        if (this.hasOuterClassInstance) {
            argPos = 0 + 1;
        }
        String[] constructorArgTypes = new String[this.constructorArgs.length - argPos];
        for (int i = argPos; i < this.constructorArgs.length; i++) {
            constructorArgTypes[i - argPos] = this.constructorArgs[i] == null ? null : this.constructorArgs[i].getClass().getName();
        }
        return Arrays.toString(constructorArgTypes);
    }

    private org.mockito.creation.instance.InstantiationException noMatchingConstructor(Class<?> cls) {
        String constructorString = constructorArgsString();
        String outerInstanceHint = "";
        if (this.hasOuterClassInstance) {
            outerInstanceHint = " and provided outer instance is correct";
        }
        return new org.mockito.creation.instance.InstantiationException(StringUtil.join("Unable to create instance of '" + cls.getSimpleName() + "'.", "Please ensure that the target class has " + constructorString + outerInstanceHint + "."), null);
    }

    private String constructorArgsString() {
        String constructorString;
        if (this.constructorArgs.length == 0 || (this.hasOuterClassInstance && this.constructorArgs.length == 1)) {
            constructorString = "a 0-arg constructor";
        } else {
            constructorString = "a constructor that matches these argument types: " + constructorArgTypes();
        }
        return constructorString;
    }

    private org.mockito.creation.instance.InstantiationException multipleMatchingConstructors(Class<?> cls, List<Constructor<?>> constructors) {
        return new org.mockito.creation.instance.InstantiationException(StringUtil.join("Unable to create instance of '" + cls.getSimpleName() + "'.", "Multiple constructors could be matched to arguments of types " + constructorArgTypes() + ":", StringUtil.join("", " - ", constructors), "If you believe that Mockito could do a better job deciding on which constructor to use, please let us know.", "Ticket 685 contains the discussion and a workaround for ambiguous constructors using inner class.", "See https://github.com/mockito/mockito/issues/685"), null);
    }

    private static boolean paramsMatch(Class<?>[] types, Object[] params) {
        if (params.length != types.length) {
            return false;
        }
        for (int i = 0; i < params.length; i++) {
            if (params[i] == null) {
                if (types[i].isPrimitive()) {
                    return false;
                }
            } else if (types[i].isPrimitive() || types[i].isInstance(params[i])) {
                if (types[i].isPrimitive() && !types[i].equals(Primitives.primitiveTypeOf(params[i].getClass()))) {
                    return false;
                }
            } else {
                return false;
            }
        }
        return true;
    }

    private void evaluateConstructor(List<Constructor<?>> matchingConstructors, Constructor<?> constructor) {
        boolean newHasBetterParam = false;
        boolean existingHasBetterParam = false;
        Class<?>[] paramTypes = constructor.getParameterTypes();
        for (int i = 0; i < paramTypes.length; i++) {
            Class<?> paramType = paramTypes[i];
            if (!paramType.isPrimitive()) {
                for (Constructor<?> existingCtor : matchingConstructors) {
                    Class<?> existingParamType = existingCtor.getParameterTypes()[i];
                    if (paramType != existingParamType) {
                        if (paramType.isAssignableFrom(existingParamType)) {
                            existingHasBetterParam = true;
                        } else {
                            newHasBetterParam = true;
                        }
                    }
                }
            }
        }
        if (!existingHasBetterParam) {
            matchingConstructors.clear();
        }
        if (newHasBetterParam || !existingHasBetterParam) {
            matchingConstructors.add(constructor);
        }
    }
}
