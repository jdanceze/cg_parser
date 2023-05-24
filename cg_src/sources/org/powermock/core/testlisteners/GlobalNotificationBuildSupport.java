package org.powermock.core.testlisteners;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
/* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/core/testlisteners/GlobalNotificationBuildSupport.class */
public class GlobalNotificationBuildSupport {
    private static final Map<String, Callback> testSuiteCallbacks = new ConcurrentHashMap();
    private static final Map<Class<?>, Callback> initiatedTestSuiteClasses = new ConcurrentHashMap();
    private static final ThreadLocal<Class<?>> pendingInitiatedTestClass = new ThreadLocal<>();

    /* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/core/testlisteners/GlobalNotificationBuildSupport$Callback.class */
    public interface Callback {
        void suiteClassInitiated(Class<?> cls);

        void testInstanceCreated(Object obj);
    }

    public static void prepareTestSuite(String testClassName, Callback callback) {
        if (testSuiteCallbacks.containsKey(testClassName)) {
            throw new IllegalStateException("Needs to wait for concurrent test-suite execution to start!");
        }
        testSuiteCallbacks.put(testClassName, callback);
        Class<?> initiatedTestClass = pendingInitiatedTestClass.get();
        if (null != initiatedTestClass && initiatedTestClass.getName().equals(testClassName)) {
            System.err.println("Detected late test-suite preparation of already initiated test-" + initiatedTestClass);
            testClassInitiated(initiatedTestClass);
        }
    }

    public static void testClassInitiated(Class<?> testClass) {
        if (!initiatedTestSuiteClasses.containsKey(testClass)) {
            Callback callback = testSuiteCallbacks.get(testClass.getName());
            if (null == callback) {
                pendingInitiatedTestClass.set(testClass);
                return;
            }
            initiatedTestSuiteClasses.put(testClass, callback);
            callback.suiteClassInitiated(testClass);
            pendingInitiatedTestClass.set(null);
        }
    }

    private static int countInitializersInTrace(String className) {
        StackTraceElement[] stackTrace;
        int initializerCount = 0;
        for (StackTraceElement ste : Thread.currentThread().getStackTrace()) {
            if ("<init>".equals(ste.getMethodName()) && className.equals(ste.getClassName())) {
                initializerCount++;
                if (2 <= initializerCount) {
                    return 2;
                }
            }
        }
        return initializerCount;
    }

    public static void testInstanceCreated(Object testInstance) {
        Class<?> cls = testInstance.getClass();
        while (true) {
            Class<?> c = cls;
            if (Object.class != c) {
                Callback callback = initiatedTestSuiteClasses.get(c);
                if (null == callback) {
                    cls = c.getSuperclass();
                } else if (1 == countInitializersInTrace(c.getName())) {
                    callback.testInstanceCreated(testInstance);
                    return;
                } else {
                    return;
                }
            } else {
                return;
            }
        }
    }

    public static void closeTestSuite(Class<?> testClass) {
        Callback callback = initiatedTestSuiteClasses.remove(testClass);
        if (null != callback && !initiatedTestSuiteClasses.values().contains(callback)) {
            testSuiteCallbacks.values().remove(callback);
        }
    }

    public static void closePendingTestSuites(Callback callback) {
        testSuiteCallbacks.values().remove(callback);
        initiatedTestSuiteClasses.values().removeAll(Collections.singleton(callback));
    }
}
