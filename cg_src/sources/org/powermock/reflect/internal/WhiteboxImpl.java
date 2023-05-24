package org.powermock.reflect.internal;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;
import org.objenesis.instantiator.ObjectInstantiator;
import org.powermock.reflect.exceptions.ConstructorNotFoundException;
import org.powermock.reflect.exceptions.FieldNotFoundException;
import org.powermock.reflect.exceptions.MethodInvocationException;
import org.powermock.reflect.exceptions.MethodNotFoundException;
import org.powermock.reflect.exceptions.TooManyConstructorsFoundException;
import org.powermock.reflect.exceptions.TooManyFieldsFoundException;
import org.powermock.reflect.exceptions.TooManyMethodsFoundException;
import org.powermock.reflect.internal.comparator.ComparatorFactory;
import org.powermock.reflect.internal.matcherstrategies.AllFieldsMatcherStrategy;
import org.powermock.reflect.internal.matcherstrategies.AssignableFromFieldTypeMatcherStrategy;
import org.powermock.reflect.internal.matcherstrategies.AssignableToFieldTypeMatcherStrategy;
import org.powermock.reflect.internal.matcherstrategies.FieldAnnotationMatcherStrategy;
import org.powermock.reflect.internal.matcherstrategies.FieldMatcherStrategy;
import org.powermock.reflect.internal.matcherstrategies.FieldNameMatcherStrategy;
import org.powermock.reflect.internal.primitivesupport.BoxedWrapper;
import org.powermock.reflect.internal.primitivesupport.PrimitiveWrapper;
import org.powermock.reflect.internal.proxy.ProxyFrameworks;
import org.powermock.reflect.internal.proxy.UnproxiedType;
import org.powermock.reflect.matching.FieldMatchingStrategy;
import soot.coffi.Instruction;
import soot.jimple.Jimple;
import sun.misc.Unsafe;
/* loaded from: gencallgraphv3.jar:powermock-reflect-2.0.9.jar:org/powermock/reflect/internal/WhiteboxImpl.class */
public class WhiteboxImpl {
    private static ProxyFrameworks proxyFrameworks = new ProxyFrameworks();
    private static ConcurrentMap<Class, Method[]> allClassMethodsCache = new ConcurrentHashMap();

    public static Method getMethod(Class<?> type, Class<?>... parameterTypes) {
        Method[] methodsToTraverse;
        Method[] methodArr;
        if (parameterTypes == null) {
            parameterTypes = new Class[0];
        }
        List<Method> foundMethods = new LinkedList<>();
        for (Class<?> thisType = type; thisType != null; thisType = thisType.getSuperclass()) {
            if (thisType.isInterface()) {
                methodsToTraverse = getAllPublicMethods(thisType);
            } else {
                methodsToTraverse = thisType.getDeclaredMethods();
            }
            for (Method method : methodsToTraverse) {
                if (checkIfParameterTypesAreSame(method.isVarArgs(), parameterTypes, method.getParameterTypes())) {
                    foundMethods.add(method);
                    if (foundMethods.size() == 1) {
                        method.setAccessible(true);
                    }
                }
            }
            if (foundMethods.size() == 1) {
                return foundMethods.get(0);
            }
            if (foundMethods.size() > 1) {
                break;
            }
        }
        if (foundMethods.isEmpty()) {
            throw new MethodNotFoundException("No method was found with parameter types: [ " + getArgumentTypesAsString(parameterTypes) + " ] in class " + getOriginalUnmockedType(type).getName() + ".");
        }
        throwExceptionWhenMultipleMethodMatchesFound("method name", (Method[]) foundMethods.toArray(new Method[foundMethods.size()]));
        return null;
    }

    public static Method getMethod(Class<?> type, String methodName, Class<?>... parameterTypes) {
        Method[] methodsToTraverse;
        Method[] methodArr;
        if (parameterTypes == null) {
            parameterTypes = new Class[0];
        }
        for (Class<?> thisType = type; thisType != null; thisType = thisType.getSuperclass()) {
            if (thisType.isInterface()) {
                methodsToTraverse = getAllPublicMethods(thisType);
            } else {
                methodsToTraverse = thisType.getDeclaredMethods();
            }
            for (Method method : methodsToTraverse) {
                if (methodName.equals(method.getName()) && checkIfParameterTypesAreSame(method.isVarArgs(), parameterTypes, method.getParameterTypes())) {
                    method.setAccessible(true);
                    return method;
                }
            }
        }
        throwExceptionIfMethodWasNotFound(type, methodName, null, parameterTypes);
        return null;
    }

    public static Field getField(Class<?> type, String fieldName) {
        LinkedList<Class<?>> examine = new LinkedList<>();
        examine.add(type);
        Set<Class<?>> done = new HashSet<>();
        while (!examine.isEmpty()) {
            Class<?> thisType = examine.removeFirst();
            done.add(thisType);
            Field[] declaredField = thisType.getDeclaredFields();
            for (Field field : declaredField) {
                if (fieldName.equals(field.getName())) {
                    field.setAccessible(true);
                    return field;
                }
            }
            Set<Class<?>> potential = new HashSet<>();
            Class<?> clazz = thisType.getSuperclass();
            if (clazz != null) {
                potential.add(thisType.getSuperclass());
            }
            potential.addAll(Arrays.asList(thisType.getInterfaces()));
            potential.removeAll(done);
            examine.addAll(potential);
        }
        throwExceptionIfFieldWasNotFound(type, fieldName, null);
        return null;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static <T> T newInstance(Class<T> classToInstantiate) {
        T newInstance;
        int modifiers = classToInstantiate.getModifiers();
        if (Modifier.isInterface(modifiers)) {
            newInstance = Proxy.newProxyInstance(WhiteboxImpl.class.getClassLoader(), new Class[]{classToInstantiate}, new InvocationHandler() { // from class: org.powermock.reflect.internal.WhiteboxImpl.1
                @Override // java.lang.reflect.InvocationHandler
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    return TypeUtils.getDefaultValue(method.getReturnType());
                }
            });
        } else if (classToInstantiate.isArray()) {
            newInstance = Array.newInstance(classToInstantiate.getComponentType(), 0);
        } else if (Modifier.isAbstract(modifiers)) {
            throw new IllegalArgumentException("Cannot instantiate an abstract class. Please use the ConcreteClassGenerator in PowerMock support to generate a concrete class first.");
        } else {
            Objenesis objenesis = new ObjenesisStd();
            ObjectInstantiator thingyInstantiator = objenesis.getInstantiatorOf(classToInstantiate);
            newInstance = thingyInstantiator.newInstance();
        }
        return newInstance;
    }

    public static java.lang.reflect.Constructor<?> getConstructor(Class<?> type, Class<?>... parameterTypes) {
        Class<?> unmockedType = getOriginalUnmockedType(type);
        try {
            java.lang.reflect.Constructor<?> constructor = unmockedType.getDeclaredConstructor(parameterTypes);
            constructor.setAccessible(true);
            return constructor;
        } catch (Error e) {
            throw e;
        } catch (RuntimeException e2) {
            throw e2;
        } catch (Throwable e3) {
            throw new ConstructorNotFoundException(String.format("Failed to lookup constructor with parameter types [ %s ] in class %s.", getArgumentTypesAsString(parameterTypes), unmockedType.getName()), e3);
        }
    }

    public static void setInternalState(Object object, String fieldName, Object value) {
        Field foundField = findFieldInHierarchy(object, fieldName);
        setField(object, value, foundField);
    }

    public static void setInternalState(Object object, String fieldName, Object[] value) {
        setInternalState(object, fieldName, (Object) value);
    }

    public static void setInternalState(Object object, Class<?> fieldType, Object value) {
        setField(object, value, findFieldInHierarchy(object, new AssignableFromFieldTypeMatcherStrategy(fieldType)));
    }

    public static void setInternalState(Object object, Object value, Object... additionalValues) {
        setField(object, value, findFieldInHierarchy(object, new AssignableFromFieldTypeMatcherStrategy(getType(value))));
        if (additionalValues != null && additionalValues.length > 0) {
            for (Object additionalValue : additionalValues) {
                setField(object, additionalValue, findFieldInHierarchy(object, new AssignableFromFieldTypeMatcherStrategy(getType(additionalValue))));
            }
        }
    }

    public static void setInternalState(Object object, Object value, Class<?> where) {
        setField(object, value, findField(object, new AssignableFromFieldTypeMatcherStrategy(getType(value)), where));
    }

    public static void setInternalState(Object object, Class<?> fieldType, Object value, Class<?> where) {
        if (fieldType == null || where == null) {
            throw new IllegalArgumentException("fieldType and where cannot be null");
        }
        setField(object, value, findFieldOrThrowException(fieldType, where));
    }

    public static void setInternalState(Object object, String fieldName, Object value, Class<?> where) {
        if (object == null || fieldName == null || fieldName.equals("") || fieldName.startsWith(Instruction.argsep)) {
            throw new IllegalArgumentException("object, field name, and \"where\" must not be empty or null.");
        }
        Field field = getField(fieldName, where);
        try {
            field.set(object, value);
        } catch (Exception e) {
            throw new RuntimeException("Internal Error: Failed to set field in method setInternalState.", e);
        }
    }

    public static <T> T getInternalState(Object object, String fieldName) {
        Field foundField = findFieldInHierarchy(object, fieldName);
        try {
            return (T) foundField.get(object);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Internal error: Failed to get field in method getInternalState.", e);
        }
    }

    private static Field findFieldInHierarchy(Object object, String fieldName) {
        return findFieldInHierarchy(object, new FieldNameMatcherStrategy(fieldName));
    }

    private static Field findFieldInHierarchy(Object object, FieldMatcherStrategy strategy) {
        assertObjectInGetInternalStateIsNotNull(object);
        return findSingleFieldUsingStrategy(strategy, object, true, getType(object));
    }

    private static Field findField(Object object, FieldMatcherStrategy strategy, Class<?> where) {
        return findSingleFieldUsingStrategy(strategy, object, false, where);
    }

    private static Field findSingleFieldUsingStrategy(FieldMatcherStrategy strategy, Object object, boolean checkHierarchy, Class<?> startClass) {
        assertObjectInGetInternalStateIsNotNull(object);
        Field foundField = null;
        while (startClass != null) {
            Field[] declaredFields = startClass.getDeclaredFields();
            for (Field field : declaredFields) {
                if (strategy.matches(field) && hasFieldProperModifier(object, field, false)) {
                    if (foundField != null) {
                        throw new TooManyFieldsFoundException("Two or more fields matching " + strategy + ".");
                    }
                    foundField = field;
                }
            }
            if (foundField != null || !checkHierarchy) {
                break;
            }
            startClass = startClass.getSuperclass();
        }
        if (foundField == null) {
            strategy.notFound(startClass, !isClass(object));
        }
        foundField.setAccessible(true);
        return foundField;
    }

    private static Set<Field> findAllFieldsUsingStrategy(FieldMatcherStrategy strategy, Object object, boolean checkHierarchy, boolean onlyInstanceFields, Class<?> startClass) {
        assertObjectInGetInternalStateIsNotNull(object);
        Set<Field> foundFields = new LinkedHashSet<>();
        while (startClass != null) {
            Field[] declaredFields = startClass.getDeclaredFields();
            for (Field field : declaredFields) {
                if (strategy.matches(field) && hasFieldProperModifier(object, field, onlyInstanceFields)) {
                    try {
                        field.setAccessible(true);
                        foundFields.add(field);
                    } catch (Exception e) {
                    }
                }
            }
            if (!checkHierarchy) {
                break;
            }
            startClass = startClass.getSuperclass();
        }
        return Collections.unmodifiableSet(foundFields);
    }

    private static boolean hasFieldProperModifier(Object object, Field field, boolean onlyInstanceFields) {
        if (onlyInstanceFields) {
            return !Modifier.isStatic(field.getModifiers());
        } else if (object instanceof Class) {
            return Modifier.isStatic(field.getModifiers());
        } else {
            return !Modifier.isStatic(field.getModifiers());
        }
    }

    public static <T> T getInternalState(Object object, Class<T> fieldType) {
        Field foundField = findFieldInHierarchy(object, new AssignableToFieldTypeMatcherStrategy(fieldType));
        try {
            return (T) foundField.get(object);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Internal error: Failed to get field in method getInternalState.", e);
        }
    }

    public static <T> T getInternalState(Object object, Class<T> fieldType, Class<?> where) {
        if (object == null) {
            throw new IllegalArgumentException("object and type are not allowed to be null");
        }
        try {
            return (T) findFieldOrThrowException(fieldType, where).get(object);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Internal error: Failed to get field in method getInternalState.", e);
        }
    }

    public static <T> T getInternalState(Object object, String fieldName, Class<?> where) {
        if (object == null || fieldName == null || fieldName.equals("") || fieldName.startsWith(Instruction.argsep)) {
            throw new IllegalArgumentException("object, field name, and \"where\" must not be empty or null.");
        }
        try {
            Field field = where.getDeclaredField(fieldName);
            field.setAccessible(true);
            return (T) field.get(object);
        } catch (NoSuchFieldException e) {
            throw new FieldNotFoundException("Field '" + fieldName + "' was not found in class " + where.getName() + ".");
        } catch (Exception e2) {
            throw new RuntimeException("Internal error: Failed to get field in method getInternalState.", e2);
        }
    }

    public static synchronized <T> T invokeMethod(Object tested, Object... arguments) throws Exception {
        return (T) doInvokeMethod(tested, null, null, arguments);
    }

    public static synchronized <T> T invokeMethod(Class<?> tested, Object... arguments) throws Exception {
        return (T) doInvokeMethod(tested, null, null, arguments);
    }

    public static synchronized <T> T invokeMethod(Object tested, String methodToExecute, Object... arguments) throws Exception {
        return (T) doInvokeMethod(tested, null, methodToExecute, arguments);
    }

    public static synchronized <T> T invokeMethod(Object tested, String methodToExecute, Class<?>[] argumentTypes, Object... arguments) throws Exception {
        Class<?> unmockedType = getType(tested);
        Method method = getMethod(unmockedType, methodToExecute, argumentTypes);
        if (method == null) {
            throwExceptionIfMethodWasNotFound(unmockedType, methodToExecute, null, arguments);
        }
        return (T) performMethodInvocation(tested, method, arguments);
    }

    public static synchronized <T> T invokeMethod(Object tested, String methodToExecute, Class<?> definedIn, Class<?>[] argumentTypes, Object... arguments) throws Exception {
        Method method = getMethod(definedIn, methodToExecute, argumentTypes);
        if (method == null) {
            throwExceptionIfMethodWasNotFound(definedIn, methodToExecute, null, arguments);
        }
        return (T) performMethodInvocation(tested, method, arguments);
    }

    public static synchronized <T> T invokeMethod(Object tested, Class<?> declaringClass, String methodToExecute, Object... arguments) throws Exception {
        return (T) doInvokeMethod(tested, declaringClass, methodToExecute, arguments);
    }

    public static synchronized <T> T invokeMethod(Object object, Class<?> declaringClass, String methodToExecute, Class<?>[] parameterTypes, Object... arguments) throws Exception {
        if (object == null) {
            throw new IllegalArgumentException("object cannot be null");
        }
        Method methodToInvoke = getMethod(declaringClass, methodToExecute, parameterTypes);
        return (T) performMethodInvocation(object, methodToInvoke, arguments);
    }

    public static synchronized <T> T invokeMethod(Class<?> clazz, String methodToExecute, Object... arguments) throws Exception {
        return (T) doInvokeMethod(clazz, null, methodToExecute, arguments);
    }

    private static <T> T doInvokeMethod(Object tested, Class<?> declaringClass, String methodToExecute, Object... arguments) throws Exception {
        Method methodToInvoke = findMethodOrThrowException(tested, declaringClass, methodToExecute, arguments);
        return (T) performMethodInvocation(tested, methodToInvoke, arguments);
    }

    /* JADX WARN: Code restructure failed: missing block: B:67:0x0166, code lost:
        throwExceptionIfMethodWasNotFound(getType(r6), r8, r12, r9);
     */
    /* JADX WARN: Code restructure failed: missing block: B:68:0x0173, code lost:
        return r12;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static java.lang.reflect.Method findMethodOrThrowException(java.lang.Object r6, java.lang.Class<?> r7, java.lang.String r8, java.lang.Object[] r9) {
        /*
            Method dump skipped, instructions count: 372
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: org.powermock.reflect.internal.WhiteboxImpl.findMethodOrThrowException(java.lang.Object, java.lang.Class, java.lang.String, java.lang.Object[]):java.lang.reflect.Method");
    }

    private static Method getMethodWithMostSpecificParameterTypes(Method firstMethodCandidate, Method secondMethodCandidate) {
        Class<?>[] firstMethodCandidateParameterTypes = firstMethodCandidate.getParameterTypes();
        Class<?>[] secondMethodCandidateParameterTypes = secondMethodCandidate.getParameterTypes();
        Method bestMatch = null;
        for (int i = 0; i < firstMethodCandidateParameterTypes.length; i++) {
            Class<?> candidateType1 = toBoxedIfPrimitive(firstMethodCandidateParameterTypes[i]);
            Class<?> candidateType2 = toBoxedIfPrimitive(secondMethodCandidateParameterTypes[i]);
            if (!candidateType1.equals(candidateType2)) {
                Method potentialMatch = null;
                if (candidateType1.isAssignableFrom(candidateType2)) {
                    potentialMatch = secondMethodCandidate;
                } else if (candidateType2.isAssignableFrom(candidateType1)) {
                    potentialMatch = firstMethodCandidate;
                }
                if (potentialMatch == null) {
                    continue;
                } else if (bestMatch != null && !potentialMatch.equals(bestMatch)) {
                    return null;
                } else {
                    bestMatch = potentialMatch;
                }
            }
        }
        return bestMatch;
    }

    private static Class<?> toBoxedIfPrimitive(Class<?> type) {
        return type.isPrimitive() ? BoxedWrapper.getBoxedFromPrimitiveType(type) : type;
    }

    private static Class<?>[] getTypes(Object[] arguments) {
        Class<?>[] classes = new Class[arguments.length];
        for (int i = 0; i < arguments.length; i++) {
            classes[i] = getType(arguments[i]);
        }
        return classes;
    }

    public static Method getBestMethodCandidate(Class<?> cls, String methodName, Class<?>[] signature, boolean exactParameterTypeMatch) {
        Method foundMethod;
        Method[] methods = getMethods(cls, methodName, signature, exactParameterTypeMatch);
        if (methods.length == 1) {
            foundMethod = methods[0];
        } else {
            Arrays.sort(methods, ComparatorFactory.createMethodComparator());
            foundMethod = methods[0];
        }
        return foundMethod;
    }

    public static java.lang.reflect.Constructor<?> findDefaultConstructorOrThrowException(Class<?> type) throws ConstructorNotFoundException {
        if (type == null) {
            throw new IllegalArgumentException("type cannot be null");
        }
        try {
            java.lang.reflect.Constructor<?> declaredConstructor = type.getDeclaredConstructor(new Class[0]);
            return declaredConstructor;
        } catch (NoSuchMethodException e) {
            throw new ConstructorNotFoundException(String.format("Couldn't find a default constructor in %s.", type.getName()));
        }
    }

    public static java.lang.reflect.Constructor<?> findConstructorOrThrowException(Class<?> type) {
        java.lang.reflect.Constructor<?>[] declaredConstructors = filterPowerMockConstructor(type.getDeclaredConstructors());
        if (declaredConstructors.length > 1) {
            throwExceptionWhenMultipleConstructorMatchesFound(declaredConstructors);
        }
        return declaredConstructors[0];
    }

    static java.lang.reflect.Constructor<?>[] filterPowerMockConstructor(java.lang.reflect.Constructor<?>[] declaredConstructors) {
        Set<java.lang.reflect.Constructor<?>> constructors = new HashSet<>();
        for (java.lang.reflect.Constructor<?> constructor : declaredConstructors) {
            Class<?>[] parameterTypes = constructor.getParameterTypes();
            if (parameterTypes.length < 1 || !parameterTypes[parameterTypes.length - 1].getName().equals("org.powermock.core.IndicateReloadClass")) {
                constructors.add(constructor);
            }
        }
        return (java.lang.reflect.Constructor[]) constructors.toArray(new java.lang.reflect.Constructor[constructors.size()]);
    }

    public static java.lang.reflect.Constructor<?> findUniqueConstructorOrThrowException(Class<?> type, Object... arguments) {
        return new ConstructorFinder(type, arguments).findConstructor();
    }

    private static Class<?>[] convertArgumentTypesToPrimitive(Class<?>[] paramTypes, Object[] arguments) {
        Class<?> argumentType;
        Class<?>[] types = new Class[arguments.length];
        for (int i = 0; i < arguments.length; i++) {
            if (arguments[i] == null) {
                argumentType = paramTypes[i];
            } else {
                argumentType = getType(arguments[i]);
            }
            Class<?> primitiveWrapperType = PrimitiveWrapper.getPrimitiveFromWrapperType(argumentType);
            if (primitiveWrapperType == null) {
                types[i] = argumentType;
            } else {
                types[i] = primitiveWrapperType;
            }
        }
        return types;
    }

    public static void throwExceptionIfMethodWasNotFound(Class<?> type, String methodName, Method methodToMock, Object... arguments) {
        if (methodToMock == null) {
            String methodNameData = "";
            if (methodName != null) {
                methodNameData = "with name '" + methodName + "' ";
            }
            throw new MethodNotFoundException("No method found " + methodNameData + "with parameter types: [ " + getArgumentTypesAsString(arguments) + " ] in class " + getOriginalUnmockedType(type).getName() + ".");
        }
    }

    public static void throwExceptionIfFieldWasNotFound(Class<?> type, String fieldName, Field field) {
        if (field == null) {
            throw new FieldNotFoundException("No field was found with name '" + fieldName + "' in class " + getOriginalUnmockedType(type).getName() + ".");
        }
    }

    static void throwExceptionIfConstructorWasNotFound(Class<?> type, java.lang.reflect.Constructor<?> potentialConstructor, Object... arguments) {
        if (potentialConstructor == null) {
            String message = "No constructor found in class '" + getOriginalUnmockedType(type).getName() + "' with parameter types: [ " + getArgumentTypesAsString(arguments) + " ].";
            throw new ConstructorNotFoundException(message);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String getArgumentTypesAsString(Object... arguments) {
        String name;
        StringBuilder argumentsAsString = new StringBuilder();
        if (arguments != null && arguments.length != 0) {
            for (int i = 0; i < arguments.length; i++) {
                Object argument = arguments[i];
                if (argument instanceof Class) {
                    name = ((Class) argument).getName();
                } else if ((argument instanceof Class[]) && arguments.length == 1) {
                    Class<?>[] argumentArray = (Class[]) argument;
                    if (argumentArray.length > 0) {
                        for (int j = 0; j < argumentArray.length; j++) {
                            appendArgument(argumentsAsString, j, argumentArray[j] == null ? Jimple.NULL : getUnproxyType(argumentArray[j]).getName(), argumentArray);
                        }
                        return argumentsAsString.toString();
                    }
                    name = "<none>";
                } else if (argument == null) {
                    name = Jimple.NULL;
                } else {
                    name = getUnproxyType(argument).getName();
                }
                String argumentName = name;
                appendArgument(argumentsAsString, i, argumentName, arguments);
            }
        } else {
            argumentsAsString.append("<none>");
        }
        return argumentsAsString.toString();
    }

    private static void appendArgument(StringBuilder argumentsAsString, int index, String argumentName, Object[] arguments) {
        argumentsAsString.append(argumentName);
        if (index != arguments.length - 1) {
            argumentsAsString.append(", ");
        }
    }

    public static <T> T invokeConstructor(Class<T> classThatContainsTheConstructorToTest, Class<?>[] parameterTypes, Object[] arguments) throws Exception {
        if (parameterTypes != null && arguments != null && parameterTypes.length != arguments.length) {
            throw new IllegalArgumentException("parameterTypes and arguments must have the same length");
        }
        try {
            java.lang.reflect.Constructor<T> constructor = classThatContainsTheConstructorToTest.getDeclaredConstructor(parameterTypes);
            return (T) createInstance(constructor, arguments);
        } catch (Exception e) {
            throw new ConstructorNotFoundException("Could not lookup the constructor", e);
        }
    }

    public static <T> T invokeConstructor(Class<T> classThatContainsTheConstructorToTest, Object... arguments) throws Exception {
        Class<?>[] argumentTypes;
        if (classThatContainsTheConstructorToTest == null) {
            throw new IllegalArgumentException("The class should contain the constructor cannot be null.");
        }
        if (arguments == null) {
            argumentTypes = new Class[0];
        } else {
            argumentTypes = new Class[arguments.length];
            for (int i = 0; i < arguments.length; i++) {
                argumentTypes[i] = getType(arguments[i]);
            }
        }
        java.lang.reflect.Constructor<T> constructor = getBestCandidateConstructor(classThatContainsTheConstructorToTest, argumentTypes, arguments);
        return (T) createInstance(constructor, arguments);
    }

    private static <T> java.lang.reflect.Constructor<T> getBestCandidateConstructor(Class<T> classThatContainsTheConstructorToTest, Class<?>[] argumentTypes, Object[] arguments) {
        java.lang.reflect.Constructor<T> constructor;
        java.lang.reflect.Constructor<T> potentialConstructorWrapped = getPotentialConstructorWrapped(classThatContainsTheConstructorToTest, argumentTypes);
        java.lang.reflect.Constructor<T> potentialConstructorPrimitive = getPotentialConstructorPrimitive(classThatContainsTheConstructorToTest, argumentTypes);
        if (potentialConstructorPrimitive == null && potentialConstructorWrapped == null) {
            constructor = getPotentialVarArgsConstructor(classThatContainsTheConstructorToTest, arguments);
            if (constructor == null) {
                throw new ConstructorNotFoundException("Failed to find a constructor with parameter types: [" + getArgumentTypesAsString(arguments) + "]");
            }
        } else if (potentialConstructorPrimitive == null) {
            constructor = potentialConstructorWrapped;
        } else if (potentialConstructorWrapped == null) {
            constructor = potentialConstructorPrimitive;
        } else if (arguments == null || (arguments.length == 0 && potentialConstructorPrimitive != null)) {
            constructor = potentialConstructorPrimitive;
        } else {
            throw new TooManyConstructorsFoundException("Could not determine which constructor to execute. Please specify the parameter types by hand.");
        }
        return constructor;
    }

    private static <T> java.lang.reflect.Constructor<T> getPotentialConstructorWrapped(Class<T> classThatContainsTheConstructorToTest, Class<?>[] argumentTypes) {
        return new CandidateConstructorSearcher(classThatContainsTheConstructorToTest, argumentTypes).findConstructor();
    }

    private static <T> java.lang.reflect.Constructor<T> getPotentialConstructorPrimitive(Class<T> classThatContainsTheConstructorToTest, Class<?>[] argumentTypes) {
        java.lang.reflect.Constructor<T> potentialConstructorPrimitive = null;
        try {
            Class<?>[] primitiveType = PrimitiveWrapper.toPrimitiveType(argumentTypes);
            if (!argumentTypesEqualsPrimitiveTypes(argumentTypes, primitiveType)) {
                potentialConstructorPrimitive = new CandidateConstructorSearcher(classThatContainsTheConstructorToTest, primitiveType).findConstructor();
            }
        } catch (Exception e) {
        }
        return potentialConstructorPrimitive;
    }

    private static boolean argumentTypesEqualsPrimitiveTypes(Class<?>[] argumentTypes, Class<?>[] primitiveType) {
        for (int index = 0; index < argumentTypes.length; index++) {
            if (!argumentTypes[index].equals(primitiveType[index])) {
                return false;
            }
        }
        return true;
    }

    private static <T> java.lang.reflect.Constructor<T> getPotentialVarArgsConstructor(Class<T> classThatContainsTheConstructorToTest, Object... arguments) {
        for (java.lang.reflect.Constructor<?> constructor : classThatContainsTheConstructorToTest.getDeclaredConstructors()) {
            java.lang.reflect.Constructor<T> possibleVarArgsConstructor = (java.lang.reflect.Constructor<T>) constructor;
            if (possibleVarArgsConstructor.isVarArgs()) {
                if (arguments == null || arguments.length == 0) {
                    return possibleVarArgsConstructor;
                }
                Class<?>[] parameterTypes = possibleVarArgsConstructor.getParameterTypes();
                if (parameterTypes[parameterTypes.length - 1].getComponentType().isAssignableFrom(getType(arguments[0]))) {
                    return possibleVarArgsConstructor;
                }
            }
        }
        return null;
    }

    private static <T> T createInstance(java.lang.reflect.Constructor<T> constructor, Object... arguments) throws Exception {
        if (constructor == null) {
            throw new IllegalArgumentException("Constructor cannot be null");
        }
        constructor.setAccessible(true);
        T createdObject = null;
        try {
            if (constructor.isVarArgs()) {
                Class<?>[] parameterTypes = constructor.getParameterTypes();
                int varArgsIndex = parameterTypes.length - 1;
                Class<?> varArgsType = parameterTypes[varArgsIndex].getComponentType();
                Object varArgsArrayInstance = createAndPopulateVarArgsArray(varArgsType, varArgsIndex, arguments);
                Object[] completeArgumentList = new Object[parameterTypes.length];
                System.arraycopy(arguments, 0, completeArgumentList, 0, varArgsIndex);
                completeArgumentList[completeArgumentList.length - 1] = varArgsArrayInstance;
                createdObject = constructor.newInstance(completeArgumentList);
            } else {
                createdObject = constructor.newInstance(arguments);
            }
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            if (cause instanceof Exception) {
                throw ((Exception) cause);
            }
            if (cause instanceof Error) {
                throw ((Error) cause);
            }
        }
        return createdObject;
    }

    private static Object createAndPopulateVarArgsArray(Class<?> varArgsType, int varArgsStartPosition, Object... arguments) {
        Object arrayInstance = Array.newInstance(varArgsType, arguments.length - varArgsStartPosition);
        for (int i = varArgsStartPosition; i < arguments.length; i++) {
            Array.set(arrayInstance, i - varArgsStartPosition, arguments[i]);
        }
        return arrayInstance;
    }

    public static java.lang.reflect.Constructor<?>[] getAllConstructors(Class<?> clazz) {
        java.lang.reflect.Constructor<?>[] declaredConstructors = clazz.getDeclaredConstructors();
        for (java.lang.reflect.Constructor<?> constructor : declaredConstructors) {
            if (!constructor.isAccessible()) {
                constructor.setAccessible(true);
            }
        }
        return declaredConstructors;
    }

    public static Method[] getAllMethods(Class<?> clazz) {
        Method[] allMethods = allClassMethodsCache.get(clazz);
        if (allMethods == null) {
            allMethods = doGetAllMethods(clazz);
            allClassMethodsCache.put(clazz, allMethods);
        }
        return allMethods;
    }

    private static Method[] doGetAllMethods(Class<?> clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("You must specify a class in order to get the methods.");
        }
        Set<Method> methods = new LinkedHashSet<>();
        Class<?> cls = clazz;
        while (true) {
            final Class<?> thisType = cls;
            if (thisType != null) {
                Method[] declaredMethods = (Method[]) AccessController.doPrivileged(new PrivilegedAction<Method[]>() { // from class: org.powermock.reflect.internal.WhiteboxImpl.2
                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.security.PrivilegedAction
                    public Method[] run() {
                        return thisType.getDeclaredMethods();
                    }
                });
                for (Method method : declaredMethods) {
                    if (!"finalize".equals(method.getName())) {
                        method.setAccessible(true);
                        methods.add(method);
                    }
                }
                Collections.addAll(methods, thisType.getMethods());
                cls = thisType.getSuperclass();
            } else {
                return (Method[]) methods.toArray(new Method[methods.size()]);
            }
        }
    }

    private static Method[] getAllPublicMethods(Class<?> clazz) {
        Method[] methods;
        if (clazz == null) {
            throw new IllegalArgumentException("You must specify a class in order to get the methods.");
        }
        Set<Method> methods2 = new LinkedHashSet<>();
        for (Method method : clazz.getMethods()) {
            method.setAccessible(true);
            methods2.add(method);
        }
        return (Method[]) methods2.toArray(new Method[0]);
    }

    public static Field[] getAllFields(Class<?> clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("You must specify the class that contains the fields");
        }
        Set<Field> fields = new LinkedHashSet<>();
        Class<?> cls = clazz;
        while (true) {
            Class<?> thisType = cls;
            if (thisType != null) {
                Field[] declaredFields = thisType.getDeclaredFields();
                for (Field field : declaredFields) {
                    field.setAccessible(true);
                    fields.add(field);
                }
                cls = thisType.getSuperclass();
            } else {
                return (Field[]) fields.toArray(new Field[fields.size()]);
            }
        }
    }

    public static java.lang.reflect.Constructor<?> getFirstParentConstructor(Class<?> klass) {
        try {
            return getOriginalUnmockedType(klass).getSuperclass().getDeclaredConstructors()[0];
        } catch (Exception e) {
            throw new ConstructorNotFoundException("Failed to lookup constructor.", e);
        }
    }

    public static <T> Method findMethod(Class<T> type, String methodName, Class<?>... parameterTypes) {
        Method[] allMethods;
        if (methodName == null && parameterTypes == null) {
            throw new IllegalArgumentException("You must specify a method name or parameter types.");
        }
        List<Method> matchingMethodsList = new LinkedList<>();
        for (Method method : getAllMethods(type)) {
            if (methodName == null || method.getName().equals(methodName)) {
                if (parameterTypes != null && parameterTypes.length > 0) {
                    Class<?>[] paramTypes = method.getParameterTypes();
                    if (!checkIfParameterTypesAreSame(method.isVarArgs(), parameterTypes, paramTypes)) {
                    }
                }
                matchingMethodsList.add(method);
            }
        }
        Method methodToMock = null;
        if (matchingMethodsList.size() > 0) {
            if (matchingMethodsList.size() == 1) {
                methodToMock = matchingMethodsList.get(0);
            } else if ((parameterTypes != null ? parameterTypes.length : 0) == 0) {
                Iterator<Method> it = matchingMethodsList.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    Method method2 = it.next();
                    if (method2.getParameterTypes().length == 0) {
                        methodToMock = method2;
                        break;
                    }
                }
                if (methodToMock == null) {
                    throwExceptionWhenMultipleMethodMatchesFound("argument parameter types", (Method[]) matchingMethodsList.toArray(new Method[matchingMethodsList.size()]));
                }
            } else {
                throwExceptionWhenMultipleMethodMatchesFound("argument parameter types", (Method[]) matchingMethodsList.toArray(new Method[matchingMethodsList.size()]));
            }
        }
        return methodToMock;
    }

    public static <T> Class<?> getOriginalUnmockedType(Class<T> type) {
        return getUnproxiedType(type).getOriginalType();
    }

    public static <T> UnproxiedType getUnproxiedType(Class<T> type) {
        return proxyFrameworks.getUnproxiedType((Class<?>) type);
    }

    static void throwExceptionWhenMultipleMethodMatchesFound(String helpInfo, Method[] methods) {
        if (methods == null || methods.length < 2) {
            throw new IllegalArgumentException("Internal error: throwExceptionWhenMultipleMethodMatchesFound needs at least two methods.");
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Several matching methods found, please specify the ");
        sb.append(helpInfo);
        sb.append(" so that PowerMock can determine which method you're referring to.\n");
        sb.append("Matching methods in class ").append(methods[0].getDeclaringClass().getName()).append(" were:\n");
        for (Method method : methods) {
            sb.append(method.getReturnType().getName()).append(Instruction.argsep);
            sb.append(method.getName()).append("( ");
            Class<?>[] parameterTypes = method.getParameterTypes();
            for (Class<?> paramType : parameterTypes) {
                sb.append(paramType.getName()).append(".class ");
            }
            sb.append(")\n");
        }
        throw new TooManyMethodsFoundException(sb.toString());
    }

    static void throwExceptionWhenMultipleConstructorMatchesFound(java.lang.reflect.Constructor<?>[] constructors) {
        if (constructors == null || constructors.length < 2) {
            throw new IllegalArgumentException("Internal error: throwExceptionWhenMultipleConstructorMatchesFound needs at least two constructors.");
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Several matching constructors found, please specify the argument parameter types so that PowerMock can determine which method you're referring to.\n");
        sb.append("Matching constructors in class ").append(constructors[0].getDeclaringClass().getName()).append(" were:\n");
        for (java.lang.reflect.Constructor<?> constructor : constructors) {
            sb.append(constructor.getName()).append("( ");
            Class<?>[] parameterTypes = constructor.getParameterTypes();
            for (Class<?> paramType : parameterTypes) {
                sb.append(paramType.getName()).append(".class ");
            }
            sb.append(")\n");
        }
        throw new TooManyConstructorsFoundException(sb.toString());
    }

    public static Method findMethodOrThrowException(Class<?> type, String methodName, Class<?>... parameterTypes) {
        Method methodToMock = findMethod(type, methodName, parameterTypes);
        throwExceptionIfMethodWasNotFound(type, methodName, methodToMock, parameterTypes);
        return methodToMock;
    }

    public static Method[] getMethods(Class<?> clazz, String... methodNames) {
        Method[] allMethods;
        Method[] methodArr;
        if (methodNames == null || methodNames.length == 0) {
            throw new IllegalArgumentException("You must supply at least one method name.");
        }
        List<Method> methodsToMock = new LinkedList<>();
        if (clazz.isInterface()) {
            allMethods = getAllPublicMethods(clazz);
        } else {
            allMethods = getAllMethods(clazz);
        }
        for (Method method : allMethods) {
            for (String methodName : methodNames) {
                if (method.getName().equals(methodName)) {
                    method.setAccessible(true);
                    methodsToMock.add(method);
                }
            }
        }
        Method[] methodArray = (Method[]) methodsToMock.toArray(new Method[0]);
        if (methodArray.length == 0) {
            throw new MethodNotFoundException(String.format("No methods matching the name(s) %s were found in the class hierarchy of %s.", concatenateStrings(methodNames), getType(clazz)));
        }
        return methodArray;
    }

    public static Method[] getMethods(Class<?> clazz, String methodName, Class<?>[] expectedTypes, boolean exactParameterTypeMatch) {
        List<Method> matchingArgumentTypes = new LinkedList<>();
        Method[] methods = getMethods(clazz, methodName);
        for (Method method : methods) {
            Class<?>[] parameterTypes = method.getParameterTypes();
            if (checkIfParameterTypesAreSame(method.isVarArgs(), expectedTypes, parameterTypes) || (!exactParameterTypeMatch && checkIfParameterTypesAreSame(method.isVarArgs(), convertParameterTypesToPrimitive(expectedTypes), convertParameterTypesToPrimitive(parameterTypes)))) {
                matchingArgumentTypes.add(method);
            }
        }
        Method[] methodArray = (Method[]) matchingArgumentTypes.toArray(new Method[0]);
        if (methodArray.length == 0) {
            throw new MethodNotFoundException(String.format("No methods matching the name(s) %s were found in the class hierarchy of %s.", concatenateStrings(methodName), getType(clazz)));
        }
        return (Method[]) matchingArgumentTypes.toArray(new Method[matchingArgumentTypes.size()]);
    }

    public static Field[] getFields(Class<?> clazz, String... fieldNames) {
        Field[] allFields;
        List<Field> fields = new LinkedList<>();
        for (Field field : getAllFields(clazz)) {
            for (String fieldName : fieldNames) {
                if (field.getName().equals(fieldName)) {
                    fields.add(field);
                }
            }
        }
        Field[] fieldArray = (Field[]) fields.toArray(new Field[fields.size()]);
        if (fieldArray.length == 0) {
            throw new FieldNotFoundException(String.format("No fields matching the name(s) %s were found in the class hierarchy of %s.", concatenateStrings(fieldNames), getType(clazz)));
        }
        return fieldArray;
    }

    public static <T> T performMethodInvocation(Object tested, Method methodToInvoke, Object... arguments) throws Exception {
        boolean accessible = methodToInvoke.isAccessible();
        if (!accessible) {
            methodToInvoke.setAccessible(true);
        }
        try {
            try {
                if (isPotentialVarArgsMethod(methodToInvoke, arguments)) {
                    Class<?>[] parameterTypes = methodToInvoke.getParameterTypes();
                    int varArgsIndex = parameterTypes.length - 1;
                    Class<?> varArgsType = parameterTypes[varArgsIndex].getComponentType();
                    Object varArgsArrayInstance = createAndPopulateVarArgsArray(varArgsType, varArgsIndex, arguments);
                    Object[] completeArgumentList = new Object[parameterTypes.length];
                    System.arraycopy(arguments, 0, completeArgumentList, 0, varArgsIndex);
                    completeArgumentList[completeArgumentList.length - 1] = varArgsArrayInstance;
                    T t = (T) methodToInvoke.invoke(tested, completeArgumentList);
                    if (!accessible) {
                        methodToInvoke.setAccessible(false);
                    }
                    return t;
                }
                T t2 = (T) methodToInvoke.invoke(tested, arguments == null ? new Object[]{arguments} : arguments);
                if (!accessible) {
                    methodToInvoke.setAccessible(false);
                }
                return t2;
            } catch (InvocationTargetException e) {
                Throwable cause = e.getCause();
                if (cause instanceof Exception) {
                    throw ((Exception) cause);
                }
                if (cause instanceof Error) {
                    throw ((Error) cause);
                }
                throw new MethodInvocationException(cause);
            }
        } catch (Throwable th) {
            if (!accessible) {
                methodToInvoke.setAccessible(false);
            }
            throw th;
        }
    }

    public static <T> Method[] getAllMethodExcept(Class<T> type, String... methodNames) {
        List<Method> methodsToMock = new LinkedList<>();
        Method[] methods = getAllMethods(type);
        for (Method method : methods) {
            int length = methodNames.length;
            int i = 0;
            while (true) {
                if (i < length) {
                    String methodName = methodNames[i];
                    if (method.getName().equals(methodName)) {
                        break;
                    }
                    i++;
                } else {
                    methodsToMock.add(method);
                    break;
                }
            }
        }
        return (Method[]) methodsToMock.toArray(new Method[0]);
    }

    public static <T> Method[] getAllMethodsExcept(Class<T> type, String methodNameToExclude, Class<?>[] argumentTypes) {
        Method[] methods = getAllMethods(type);
        List<Method> methodList = new ArrayList<>();
        for (Method method : methods) {
            if (method.getName().equals(methodNameToExclude)) {
                if (argumentTypes != null && argumentTypes.length > 0) {
                    Class<?>[] args = method.getParameterTypes();
                    if (args != null && args.length == argumentTypes.length) {
                        for (int i = 0; i < args.length; i++) {
                            if (args[i].isAssignableFrom(getOriginalUnmockedType(argumentTypes[i]))) {
                                break;
                            }
                        }
                    }
                }
            }
            methodList.add(method);
        }
        return (Method[]) methodList.toArray(new Method[0]);
    }

    public static boolean areAllMethodsStatic(Method... methods) {
        for (Method method : methods) {
            if (!Modifier.isStatic(method.getModifiers())) {
                return false;
            }
        }
        return true;
    }

    static boolean areAllArgumentsOfSameType(Object[] arguments) {
        Object object;
        if (arguments == null || arguments.length <= 1) {
            return true;
        }
        int index = 0;
        Object obj = null;
        while (true) {
            object = obj;
            if (object != null || index >= arguments.length) {
                break;
            }
            int i = index;
            index++;
            obj = arguments[i];
        }
        if (object == null) {
            return true;
        }
        Class<?> firstArgumentType = getType(object);
        for (int i2 = index; i2 < arguments.length; i2++) {
            Object argument = arguments[i2];
            if (argument != null && !getType(argument).isAssignableFrom(firstArgumentType)) {
                return false;
            }
        }
        return true;
    }

    static boolean checkArgumentTypesMatchParameterTypes(boolean isVarArgs, Class<?>[] parameterTypes, Object[] arguments) {
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
                if (!isAssignableFrom(parameterTypes[parameterTypes.length - 1], getType(argument))) {
                    return false;
                }
            } else {
                boolean assignableFrom = isAssignableFrom(parameterTypes[i], getType(argument));
                boolean isClass = parameterTypes[i].equals(Class.class) && isClass(argument);
                if (!assignableFrom && !isClass) {
                    return false;
                }
            }
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isAssignableFrom(Class<?> type, Class<?> from) {
        Class<?> primitiveFromWrapperType;
        Class<?> theType = getComponentType(type);
        Class<?> theFrom = getComponentType(from);
        boolean assignableFrom = theType.isAssignableFrom(theFrom);
        if (!assignableFrom && PrimitiveWrapper.hasPrimitiveCounterPart(theFrom) && (primitiveFromWrapperType = PrimitiveWrapper.getPrimitiveFromWrapperType(theFrom)) != null) {
            assignableFrom = theType.isAssignableFrom(primitiveFromWrapperType);
        }
        return assignableFrom;
    }

    private static Class<?> getComponentType(Class<?> type) {
        Class<?> cls = type;
        while (true) {
            Class<?> theType = cls;
            if (theType.isArray()) {
                cls = theType.getComponentType();
            } else {
                return theType;
            }
        }
    }

    public static Class<?> getType(Object object) {
        Class<?> type = null;
        if (isClass(object)) {
            type = (Class) object;
        } else if (object != null) {
            type = object.getClass();
        }
        return type;
    }

    public static Class<?> getUnproxyType(Object object) {
        Class<?> type = null;
        if (isClass(object)) {
            type = (Class) object;
        } else if (object != null) {
            type = object.getClass();
        }
        if (type == null) {
            return null;
        }
        return getOriginalUnmockedType(type);
    }

    public static Class<Object> getInnerClassType(Class<?> declaringClass, String name) throws ClassNotFoundException {
        return Class.forName(declaringClass.getName() + "$" + name);
    }

    public static Class<Object> getLocalClassType(Class<?> declaringClass, int occurrence, String name) throws ClassNotFoundException {
        return Class.forName(declaringClass.getName() + "$" + occurrence + name);
    }

    public static Class<Object> getAnonymousInnerClassType(Class<?> declaringClass, int occurrence) throws ClassNotFoundException {
        return Class.forName(declaringClass.getName() + "$" + occurrence);
    }

    public static Set<Field> getFieldsAnnotatedWith(Object object, Class<? extends Annotation> annotation, Class<? extends Annotation>... additionalAnnotations) {
        Class<? extends Annotation>[] annotations;
        if (additionalAnnotations == null || additionalAnnotations.length == 0) {
            annotations = new Class[]{annotation};
        } else {
            annotations = new Class[additionalAnnotations.length + 1];
            annotations[0] = annotation;
            System.arraycopy(additionalAnnotations, 0, annotations, 1, additionalAnnotations.length);
        }
        return getFieldsAnnotatedWith(object, annotations);
    }

    public static Set<Field> getFieldsAnnotatedWith(Object object, Class<? extends Annotation>[] annotationTypes) {
        return findAllFieldsUsingStrategy(new FieldAnnotationMatcherStrategy(annotationTypes), object, true, false, getType(object));
    }

    public static Set<Field> getFieldsOfType(Object object, Class<?> type) {
        return findAllFieldsUsingStrategy(new AssignableFromFieldTypeMatcherStrategy(type), object, true, false, getType(object));
    }

    public static Set<Field> getAllInstanceFields(Object object) {
        return findAllFieldsUsingStrategy(new AllFieldsMatcherStrategy(), object, true, true, getUnproxyType(object));
    }

    public static Set<Field> getAllStaticFields(Class<?> type) {
        return findAllFieldsUsingStrategy(new AllFieldsMatcherStrategy(), type, false, false, type);
    }

    public static boolean isClass(Object argument) {
        return argument instanceof Class;
    }

    public static boolean checkIfParameterTypesAreSame(boolean isVarArgs, Class<?>[] expectedParameterTypes, Class<?>[] actualParameterTypes) {
        return new ParameterTypesMatcher(isVarArgs, expectedParameterTypes, actualParameterTypes).match();
    }

    private static Field getField(String fieldName, Class<?> where) {
        if (where == null) {
            throw new IllegalArgumentException("where cannot be null");
        }
        try {
            Field field = where.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field;
        } catch (NoSuchFieldException e) {
            throw new FieldNotFoundException("Field '" + fieldName + "' was not found in class " + where.getName() + ".");
        }
    }

    private static Field findFieldOrThrowException(Class<?> fieldType, Class<?> where) {
        if (fieldType == null || where == null) {
            throw new IllegalArgumentException("fieldType and where cannot be null");
        }
        Field field = null;
        Field[] declaredFields = where.getDeclaredFields();
        int length = declaredFields.length;
        int i = 0;
        while (true) {
            if (i >= length) {
                break;
            }
            Field currentField = declaredFields[i];
            currentField.setAccessible(true);
            if (!currentField.getType().equals(fieldType)) {
                i++;
            } else {
                field = currentField;
                break;
            }
        }
        if (field == null) {
            throw new FieldNotFoundException("Cannot find a field of type " + fieldType + "in where.");
        }
        return field;
    }

    private static void setField(Object object, Object value, Field foundField) {
        boolean isStatic = (foundField.getModifiers() & 8) == 8;
        if (isStatic) {
            setStaticFieldUsingUnsafe(foundField, value);
        } else {
            setFieldUsingUnsafe(foundField, object, value);
        }
    }

    private static void setStaticFieldUsingUnsafe(final Field field, final Object newValue) {
        try {
            field.setAccessible(true);
            int fieldModifiersMask = field.getModifiers();
            boolean isFinalModifierPresent = (fieldModifiersMask & 16) == 16;
            if (isFinalModifierPresent) {
                AccessController.doPrivileged(new PrivilegedAction<Object>() { // from class: org.powermock.reflect.internal.WhiteboxImpl.3
                    @Override // java.security.PrivilegedAction
                    public Object run() {
                        try {
                            Unsafe unsafe = WhiteboxImpl.getUnsafe();
                            long offset = unsafe.staticFieldOffset(field);
                            Object base = unsafe.staticFieldBase(field);
                            WhiteboxImpl.setFieldUsingUnsafe(base, field.getType(), offset, newValue, unsafe);
                            return null;
                        } catch (Throwable t) {
                            throw new RuntimeException(t);
                        }
                    }
                });
            } else {
                field.set(null, newValue);
            }
        } catch (IllegalAccessException ex) {
            throw new RuntimeException(ex);
        } catch (IllegalArgumentException ex2) {
            throw new RuntimeException(ex2);
        } catch (SecurityException ex3) {
            throw new RuntimeException(ex3);
        }
    }

    private static void setFieldUsingUnsafe(final Field field, final Object object, final Object newValue) {
        try {
            field.setAccessible(true);
            int fieldModifiersMask = field.getModifiers();
            boolean isFinalModifierPresent = (fieldModifiersMask & 16) == 16;
            if (isFinalModifierPresent) {
                AccessController.doPrivileged(new PrivilegedAction<Object>() { // from class: org.powermock.reflect.internal.WhiteboxImpl.4
                    @Override // java.security.PrivilegedAction
                    public Object run() {
                        try {
                            Unsafe unsafe = WhiteboxImpl.getUnsafe();
                            long offset = unsafe.objectFieldOffset(field);
                            WhiteboxImpl.setFieldUsingUnsafe(object, field.getType(), offset, newValue, unsafe);
                            return null;
                        } catch (Throwable t) {
                            throw new RuntimeException(t);
                        }
                    }
                });
            } else {
                try {
                    field.set(object, newValue);
                } catch (IllegalAccessException ex) {
                    throw new RuntimeException(ex);
                }
            }
        } catch (SecurityException ex2) {
            throw new RuntimeException(ex2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Unsafe getUnsafe() throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
        Field field1 = Unsafe.class.getDeclaredField("theUnsafe");
        field1.setAccessible(true);
        Unsafe unsafe = (Unsafe) field1.get(null);
        return unsafe;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void setFieldUsingUnsafe(Object base, Class type, long offset, Object newValue, Unsafe unsafe) {
        if (type == Integer.TYPE) {
            unsafe.putInt(base, offset, ((Integer) newValue).intValue());
        } else if (type == Short.TYPE) {
            unsafe.putShort(base, offset, ((Short) newValue).shortValue());
        } else if (type == Long.TYPE) {
            unsafe.putLong(base, offset, ((Long) newValue).longValue());
        } else if (type == Byte.TYPE) {
            unsafe.putByte(base, offset, ((Byte) newValue).byteValue());
        } else if (type == Boolean.TYPE) {
            unsafe.putBoolean(base, offset, ((Boolean) newValue).booleanValue());
        } else if (type == Float.TYPE) {
            unsafe.putFloat(base, offset, ((Float) newValue).floatValue());
        } else if (type == Double.TYPE) {
            unsafe.putDouble(base, offset, ((Double) newValue).doubleValue());
        } else if (type == Character.TYPE) {
            unsafe.putChar(base, offset, ((Character) newValue).charValue());
        } else {
            unsafe.putObject(base, offset, newValue);
        }
    }

    private static String concatenateStrings(String... stringsToConcatenate) {
        StringBuilder builder = new StringBuilder();
        int stringsLength = stringsToConcatenate.length;
        for (int i = 0; i < stringsLength; i++) {
            if (i == stringsLength - 1 && stringsLength != 1) {
                builder.append(" or ");
            } else if (i != 0) {
                builder.append(", ");
            }
            builder.append(stringsToConcatenate[i]);
        }
        return builder.toString();
    }

    private static boolean isPotentialVarArgsMethod(Method method, Object[] arguments) {
        return doesParameterTypesMatchForVarArgsInvocation(method.isVarArgs(), method.getParameterTypes(), arguments);
    }

    static boolean doesParameterTypesMatchForVarArgsInvocation(boolean isVarArgs, Class<?>[] parameterTypes, Object[] arguments) {
        if (isVarArgs && arguments != null && arguments.length >= 1 && parameterTypes != null && parameterTypes.length >= 1) {
            Class<?> componentType = parameterTypes[parameterTypes.length - 1].getComponentType();
            Object lastArgument = arguments[arguments.length - 1];
            if (lastArgument != null) {
                Class<?> lastArgumentTypeAsPrimitive = getTypeAsPrimitiveIfWrapped(lastArgument);
                Class<?> varArgsParameterTypeAsPrimitive = getTypeAsPrimitiveIfWrapped(componentType);
                isVarArgs = varArgsParameterTypeAsPrimitive.isAssignableFrom(lastArgumentTypeAsPrimitive);
            }
        }
        return isVarArgs && checkArgumentTypesMatchParameterTypes(isVarArgs, parameterTypes, arguments);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Class<?> getTypeAsPrimitiveIfWrapped(Object object) {
        if (object != null) {
            Class<?> firstArgumentType = getType(object);
            Class<?> firstArgumentTypeAsPrimitive = PrimitiveWrapper.hasPrimitiveCounterPart(firstArgumentType) ? PrimitiveWrapper.getPrimitiveFromWrapperType(firstArgumentType) : firstArgumentType;
            return firstArgumentTypeAsPrimitive;
        }
        return null;
    }

    public static void setInternalStateFromContext(Object object, Object context, Object[] additionalContexts) {
        setInternalStateFromContext(object, context, FieldMatchingStrategy.MATCHING);
        if (additionalContexts != null && additionalContexts.length > 0) {
            for (Object additionContext : additionalContexts) {
                setInternalStateFromContext(object, additionContext, FieldMatchingStrategy.MATCHING);
            }
        }
    }

    public static void setInternalStateFromContext(Object object, Object context, FieldMatchingStrategy strategy) {
        if (isClass(context)) {
            copyState(object, getType(context), strategy);
        } else {
            copyState(object, context, strategy);
        }
    }

    public static void setInternalStateFromContext(Object object, Class<?> context, Class<?>[] additionalContexts) {
        setInternalStateFromContext(object, context, FieldMatchingStrategy.MATCHING);
        if (additionalContexts != null && additionalContexts.length > 0) {
            for (Class<?> additionContext : additionalContexts) {
                setInternalStateFromContext(object, additionContext, FieldMatchingStrategy.MATCHING);
            }
        }
    }

    static void copyState(Object object, Object context, FieldMatchingStrategy strategy) {
        if (object == null) {
            throw new IllegalArgumentException("object to set state cannot be null");
        }
        if (context == null) {
            throw new IllegalArgumentException("context cannot be null");
        }
        if (strategy == null) {
            throw new IllegalArgumentException("strategy cannot be null");
        }
        Set<Field> allFields = isClass(context) ? getAllStaticFields(getType(context)) : getAllInstanceFields(context);
        for (Field field : allFields) {
            try {
                boolean isStaticField = Modifier.isStatic(field.getModifiers());
                setInternalState(isStaticField ? getType(object) : object, field.getType(), field.get(context));
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Internal Error: Failed to get the field value in method setInternalStateFromContext.", e);
            } catch (FieldNotFoundException e2) {
                if (strategy == FieldMatchingStrategy.STRICT) {
                    throw e2;
                }
            }
        }
    }

    private static void assertObjectInGetInternalStateIsNotNull(Object object) {
        if (object == null) {
            throw new IllegalArgumentException("The object containing the field cannot be null");
        }
    }

    private static Class<?>[] convertParameterTypesToPrimitive(Class<?>[] parameterTypes) {
        Class<?>[] converted = new Class[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            Class<?> primitiveWrapperType = PrimitiveWrapper.getPrimitiveFromWrapperType(parameterTypes[i]);
            if (primitiveWrapperType == null) {
                converted[i] = parameterTypes[i];
            } else {
                converted[i] = primitiveWrapperType;
            }
        }
        return converted;
    }

    public static <T> void copyToMock(T from, T mock) {
        copy(from, mock, from.getClass());
    }

    public static <T> void copyToRealObject(T from, T to) {
        copy(from, to, from.getClass());
    }

    private static <T> void copy(T from, T to, Class<?> fromClazz) {
        while (fromClazz != Object.class) {
            copyValues(from, to, fromClazz);
            fromClazz = fromClazz.getSuperclass();
        }
    }

    private static <T> void copyValues(T from, T mock, Class<?> classFrom) {
        Field[] fields = classFrom.getDeclaredFields();
        for (Field field : fields) {
            if (!Modifier.isStatic(field.getModifiers())) {
                boolean accessible = field.isAccessible();
                try {
                    field.setAccessible(true);
                    copyValue(from, mock, field);
                    field.setAccessible(accessible);
                } catch (Exception e) {
                    field.setAccessible(accessible);
                } catch (Throwable th) {
                    field.setAccessible(accessible);
                    throw th;
                }
            }
        }
    }

    private static <T> void copyValue(T from, T to, Field field) throws IllegalAccessException {
        Object value = field.get(from);
        field.set(to, value);
    }
}
