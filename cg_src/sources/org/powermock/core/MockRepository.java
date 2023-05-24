package org.powermock.core;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.powermock.core.spi.MethodInvocationControl;
import org.powermock.core.spi.NewInvocationControl;
/* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/core/MockRepository.class */
public class MockRepository {
    private static Set<Object> objectsToAutomaticallyReplayAndVerify = new IdentityHashSet();
    private static Map<Class<?>, NewInvocationControl<?>> newSubstitutions = new HashMap();
    private static Map<Class<?>, MethodInvocationControl> classMocks = new HashMap();
    private static Map<Object, MethodInvocationControl> instanceMocks = new ListMap();
    private static Map<Method, Object> substituteReturnValues = new HashMap();
    private static Map<Method, InvocationHandler> methodProxies = new HashMap();
    private static Set<String> suppressStaticInitializers = new HashSet();
    private static Map<String, Object> additionalState = new HashMap();
    private static final Set<Constructor<?>> suppressConstructor = new HashSet();
    private static final Set<Method> suppressMethod = new HashSet();
    private static final Set<Field> suppressField = new HashSet();
    private static final Set<String> suppressFieldTypes = new HashSet();
    private static final Set<Runnable> afterMethodRunners = new HashSet();

    public static synchronized void clear() {
        newSubstitutions.clear();
        classMocks.clear();
        instanceMocks.clear();
        objectsToAutomaticallyReplayAndVerify.clear();
        additionalState.clear();
        suppressConstructor.clear();
        suppressMethod.clear();
        substituteReturnValues.clear();
        suppressField.clear();
        suppressFieldTypes.clear();
        methodProxies.clear();
        for (Runnable runnable : afterMethodRunners) {
            runnable.run();
        }
        afterMethodRunners.clear();
    }

    public static void remove(Object mock) {
        if (mock instanceof Class) {
            if (newSubstitutions.containsKey(mock)) {
                newSubstitutions.remove(mock);
            }
            if (classMocks.containsKey(mock)) {
                classMocks.remove(mock);
            }
        } else if (instanceMocks.containsKey(mock)) {
            instanceMocks.remove(mock);
        }
    }

    public static synchronized MethodInvocationControl getStaticMethodInvocationControl(Class<?> type) {
        return classMocks.get(type);
    }

    public static synchronized MethodInvocationControl putStaticMethodInvocationControl(Class<?> type, MethodInvocationControl invocationControl) {
        return classMocks.put(type, invocationControl);
    }

    public static synchronized MethodInvocationControl removeClassMethodInvocationControl(Class<?> type) {
        return classMocks.remove(type);
    }

    public static synchronized MethodInvocationControl getInstanceMethodInvocationControl(Object instance) {
        return instanceMocks.get(instance);
    }

    public static synchronized MethodInvocationControl putInstanceMethodInvocationControl(Object instance, MethodInvocationControl invocationControl) {
        return instanceMocks.put(instance, invocationControl);
    }

    public static synchronized MethodInvocationControl removeInstanceMethodInvocationControl(Class<?> type) {
        return classMocks.remove(type);
    }

    public static synchronized NewInvocationControl<?> getNewInstanceControl(Class<?> type) {
        return newSubstitutions.get(type);
    }

    public static synchronized NewInvocationControl<?> putNewInstanceControl(Class<?> type, NewInvocationControl<?> control) {
        return newSubstitutions.put(type, control);
    }

    public static synchronized void addSuppressStaticInitializer(String className) {
        suppressStaticInitializers.add(className);
    }

    public static synchronized void removeSuppressStaticInitializer(String className) {
        suppressStaticInitializers.remove(className);
    }

    public static synchronized boolean shouldSuppressStaticInitializerFor(String className) {
        return suppressStaticInitializers.contains(className);
    }

    public static synchronized Set<Object> getObjectsToAutomaticallyReplayAndVerify() {
        return Collections.unmodifiableSet(objectsToAutomaticallyReplayAndVerify);
    }

    public static synchronized void addObjectsToAutomaticallyReplayAndVerify(Object... objects) {
        Collections.addAll(objectsToAutomaticallyReplayAndVerify, objects);
    }

    public static synchronized Object putAdditionalState(String key, Object value) {
        return additionalState.put(key, value);
    }

    public static synchronized Object removeAdditionalState(String key) {
        return additionalState.remove(key);
    }

    public static synchronized InvocationHandler removeMethodProxy(Method method) {
        return methodProxies.remove(method);
    }

    public static synchronized <T> T getAdditionalState(String key) {
        return (T) additionalState.get(key);
    }

    public static synchronized void addMethodToSuppress(Method method) {
        suppressMethod.add(method);
    }

    public static synchronized void addFieldToSuppress(Field field) {
        suppressField.add(field);
    }

    public static synchronized void addFieldTypeToSuppress(String fieldType) {
        suppressFieldTypes.add(fieldType);
    }

    public static synchronized void addConstructorToSuppress(Constructor<?> constructor) {
        suppressConstructor.add(constructor);
    }

    public static synchronized boolean hasMethodProxy(Method method) {
        return methodProxies.containsKey(method);
    }

    public static synchronized boolean shouldSuppressMethod(Method method, Class<?> objectType) throws ClassNotFoundException {
        for (Method suppressedMethod : suppressMethod) {
            Class<?> suppressedMethodClass = suppressedMethod.getDeclaringClass();
            if (suppressedMethodClass.getClass().isAssignableFrom(objectType.getClass()) && suppressedMethod.getName().equals(method.getName()) && ClassLocator.getCallerClass().getName().equals(suppressedMethodClass.getName())) {
                return true;
            }
        }
        return false;
    }

    public static synchronized boolean shouldSuppressField(Field field) {
        return suppressField.contains(field) || suppressFieldTypes.contains(field.getType().getName());
    }

    public static synchronized boolean shouldSuppressConstructor(Constructor<?> constructor) {
        return suppressConstructor.contains(constructor);
    }

    public static synchronized boolean shouldStubMethod(Method method) {
        return substituteReturnValues.containsKey(method);
    }

    public static synchronized Object getMethodToStub(Method method) {
        return substituteReturnValues.get(method);
    }

    public static synchronized Object putMethodToStub(Method method, Object value) {
        return substituteReturnValues.put(method, value);
    }

    public static synchronized InvocationHandler getMethodProxy(Method method) {
        return methodProxies.get(method);
    }

    public static synchronized InvocationHandler putMethodProxy(Method method, InvocationHandler invocationHandler) {
        return methodProxies.put(method, invocationHandler);
    }

    public static synchronized void addAfterMethodRunner(Runnable runnable) {
        afterMethodRunners.add(runnable);
    }
}
