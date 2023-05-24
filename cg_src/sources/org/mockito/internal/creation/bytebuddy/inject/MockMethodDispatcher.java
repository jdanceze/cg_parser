package org.mockito.internal.creation.bytebuddy.inject;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/creation/bytebuddy/inject/MockMethodDispatcher.raw */
public abstract class MockMethodDispatcher {
    private static final ConcurrentMap<String, MockMethodDispatcher> DISPATCHERS;

    public abstract Callable<?> handle(Object obj, Method method, Object[] objArr) throws Throwable;

    public abstract Callable<?> handleStatic(Class<?> cls, Method method, Object[] objArr) throws Throwable;

    public abstract Object handleConstruction(Class<?> cls, Object obj, Object[] objArr, String[] strArr);

    public abstract boolean isMock(Object obj);

    public abstract boolean isMocked(Object obj);

    public abstract boolean isMockedStatic(Class<?> cls);

    public abstract boolean isOverridden(Object obj, Method method);

    public abstract boolean isConstructorMock(Class<?> cls);

    static {
        ClassLoader classLoader = MockMethodDispatcher.class.getClassLoader();
        if (classLoader != null) {
            throw new IllegalStateException(MockMethodDispatcher.class.getName() + " is not loaded by the bootstrap class loader but by an instance of " + classLoader.getClass().getName() + ".\n\nThis causes the inline mock maker to not work as expected. Please contact the maintainer of this class loader implementation to assure that this class is never loaded by another class loader. The bootstrap class loader must always be queried first for this class for Mockito's inline mock maker to function correctly.");
        }
        DISPATCHERS = new ConcurrentHashMap();
    }

    public static MockMethodDispatcher get(String identifier, Object mock) {
        if (mock == DISPATCHERS) {
            return null;
        }
        return DISPATCHERS.get(identifier);
    }

    public static MockMethodDispatcher getStatic(String identifier, Class<?> type) {
        if (MockMethodDispatcher.class.isAssignableFrom(type) || type == ConcurrentHashMap.class) {
            return null;
        }
        return DISPATCHERS.get(identifier);
    }

    public static void set(String identifier, MockMethodDispatcher dispatcher) {
        DISPATCHERS.putIfAbsent(identifier, dispatcher);
    }

    public static boolean isConstructorMock(String identifier, Class<?> type) {
        return DISPATCHERS.get(identifier).isConstructorMock(type);
    }

    public static Object handleConstruction(String identifier, Class<?> type, Object object, Object[] arguments, String[] parameterTypeNames) {
        return DISPATCHERS.get(identifier).handleConstruction(type, object, arguments, parameterTypeNames);
    }
}
