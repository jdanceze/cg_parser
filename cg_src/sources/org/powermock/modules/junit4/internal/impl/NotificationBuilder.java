package org.powermock.modules.junit4.internal.impl;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.IdentityHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.powermock.core.spi.testresult.Result;
import org.powermock.core.spi.testresult.TestMethodResult;
import org.powermock.tests.utils.PowerMockTestNotifier;
import org.powermock.tests.utils.impl.MockPolicyInitializerImpl;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: gencallgraphv3.jar:powermock-module-junit4-2.0.9.jar:org/powermock/modules/junit4/internal/impl/NotificationBuilder.class */
public class NotificationBuilder {
    private final Method[] testMethods;
    private final List<?> pendingTestInstances;
    private final PowerMockTestNotifier powerMockTestNotifier;
    private Description currentDescription;
    private Object currentTestInstance;
    private String testClassName;
    private Object latestTestInstance;
    private Method latestMethod;
    private static final Pattern methodDisplayNameRgx = Pattern.compile("(^[^\\(\\[]++)|([^(]+(?=\\)))");
    private static final Object[] unsupportedMethodArgs = new Object[0];
    private DetectedTestRunBehaviour behaviour = DetectedTestRunBehaviour.PENDING;
    private final Map<Object, List<Method>> methodsPerInstance = new IdentityHashMap<Object, List<Method>>() { // from class: org.powermock.modules.junit4.internal.impl.NotificationBuilder.1
        @Override // java.util.IdentityHashMap, java.util.AbstractMap, java.util.Map
        public List<Method> get(Object key) {
            if (!containsKey(key)) {
                put(key, new LinkedList());
            }
            return (List) super.get(key);
        }
    };
    private final Map<Description, OngoingTestRun> ongoingTestRuns = new IdentityHashMap();

    /* loaded from: gencallgraphv3.jar:powermock-module-junit4-2.0.9.jar:org/powermock/modules/junit4/internal/impl/NotificationBuilder$DetectedTestRunBehaviour.class */
    enum DetectedTestRunBehaviour {
        PENDING,
        START_FIRES_FIRST,
        TEST_INSTANCE_CREATED_FIRST,
        ALL_TESTINSTANCES_ARE_CREATED_FIRST,
        TEST_INSTANCES_ARE_REUSED,
        INCONSISTENT_BEHAVIOUR
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:powermock-module-junit4-2.0.9.jar:org/powermock/modules/junit4/internal/impl/NotificationBuilder$OngoingTestRun.class */
    public class OngoingTestRun implements TestMethodResult {
        final Description testDescription;
        final Object testInstance;
        final Method testMethod;
        private Result result;

        OngoingTestRun(Description testDescription, Object testInstance) {
            this.testDescription = testDescription;
            this.testInstance = testInstance;
            this.testMethod = NotificationBuilder.this.determineTestMethod(testDescription);
            NotificationBuilder.this.pendingTestInstances.remove(testInstance);
            Class<?> testClass = testClass();
            new MockPolicyInitializerImpl(testClass).initialize(testClass.getClassLoader());
            NotificationBuilder.this.powerMockTestNotifier.notifyBeforeTestMethod(testInstance, this.testMethod, NotificationBuilder.unsupportedMethodArgs);
            NotificationBuilder.this.ongoingTestRuns.put(testDescription, this);
        }

        final Class<?> testClass() {
            if (null == NotificationBuilder.this.testClassName) {
                return this.testInstance.getClass();
            }
            try {
                return Class.forName(NotificationBuilder.this.testClassName, false, this.testInstance.getClass().getClassLoader());
            } catch (ClassNotFoundException e) {
                return this.testInstance.getClass();
            }
        }

        void report(Result result) {
            if ((null != this.result && Result.SUCCESSFUL == result) || this.result == result) {
                return;
            }
            if (null != this.result) {
                new IllegalStateException("Will report an unexpected result-notification " + result + " after previously received notification " + this.result).printStackTrace();
            }
            this.result = result;
            NotificationBuilder.this.powerMockTestNotifier.notifyAfterTestMethod(this.testInstance, this.testMethod, NotificationBuilder.unsupportedMethodArgs, this);
        }

        @Override // org.powermock.core.spi.testresult.TestMethodResult
        public Result getResult() {
            return this.result;
        }
    }

    public NotificationBuilder(Method[] testMethods, PowerMockTestNotifier notifier, List<?> pendingTestInstances) {
        this.testMethods = testMethods;
        this.pendingTestInstances = pendingTestInstances;
        this.powerMockTestNotifier = notifier;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Method determineTestMethod(Description d) {
        Method[] methodArr;
        Matcher matchMethodName = methodDisplayNameRgx.matcher(d.getDisplayName());
        matchMethodName.find();
        String methodName = matchMethodName.group();
        boolean latestTestMethodCanBeRepeated = false;
        for (Method m : this.testMethods) {
            if (m.getName().equals(methodName)) {
                if (m == this.latestMethod) {
                    latestTestMethodCanBeRepeated = true;
                } else {
                    this.latestMethod = m;
                    return m;
                }
            }
        }
        if (latestTestMethodCanBeRepeated) {
            return this.latestMethod;
        }
        new IllegalArgumentException("Unable to determine method-name from description=" + d + "; - ignored").printStackTrace();
        return null;
    }

    private Class<?> reloadParamType(Class<?> testClass, Class<?> typeToReload) {
        if (typeToReload.isPrimitive() || testClass.getClassLoader() == typeToReload.getClassLoader()) {
            return typeToReload;
        }
        if (typeToReload.isArray()) {
            Class<?> newComponentType = reloadParamType(testClass, typeToReload.getComponentType());
            if (newComponentType == typeToReload.getComponentType()) {
                return typeToReload;
            }
            return Array.newInstance(newComponentType, 0).getClass();
        }
        try {
            return Class.forName(typeToReload.getName(), true, testClass.getClassLoader());
        } catch (ClassNotFoundException ex) {
            throw new Error(ex);
        }
    }

    private Method reloadMethod(Class<?> testClass, Method m) {
        if (testClass.getClassLoader() == m.getDeclaringClass().getClassLoader()) {
            return m;
        }
        if (!m.getDeclaringClass().getName().equals(testClass.getName())) {
            return reloadMethod(testClass.getSuperclass(), m);
        }
        Class[] paramTypes = m.getParameterTypes();
        for (int i = 0; i < paramTypes.length; i++) {
            paramTypes[i] = reloadParamType(testClass, paramTypes[i]);
        }
        try {
            return testClass.getDeclaredMethod(m.getName(), paramTypes);
        } catch (NoSuchMethodException ex) {
            throw new Error(ex);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void testSuiteStarted(Class<?> testClass) {
        for (int i = 0; i < this.testMethods.length; i++) {
            this.testMethods[i] = reloadMethod(testClass, this.testMethods[i]);
        }
        this.powerMockTestNotifier.notifyBeforeTestSuiteStarted(testClass, this.testMethods);
        this.testClassName = testClass.getName();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public void testStartHasBeenFired(Description d) {
        OngoingTestRun oldTestRun = this.ongoingTestRuns.get(d);
        if (null != oldTestRun && null != oldTestRun.getResult()) {
            throw new IllegalStateException("Fired testrun is already running: " + d);
        }
        this.currentDescription = d;
        switch (this.behaviour) {
            case PENDING:
                this.behaviour = DetectedTestRunBehaviour.START_FIRES_FIRST;
                return;
            case START_FIRES_FIRST:
                return;
            case TEST_INSTANCE_CREATED_FIRST:
                if (this.currentTestInstance == this.latestTestInstance) {
                    this.behaviour = DetectedTestRunBehaviour.TEST_INSTANCES_ARE_REUSED;
                    break;
                }
                break;
            case TEST_INSTANCES_ARE_REUSED:
                break;
            case ALL_TESTINSTANCES_ARE_CREATED_FIRST:
                System.err.println("Notifications are not supported when all test-instances are created first!");
                return;
            default:
                throw new AssertionError();
        }
        this.latestTestInstance = this.currentTestInstance;
        this.methodsPerInstance.get(this.currentTestInstance).add(new OngoingTestRun(d, this.currentTestInstance).testMethod);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void testInstanceCreated(Object newTestInstance) {
        switch (this.behaviour) {
            case PENDING:
                this.behaviour = DetectedTestRunBehaviour.TEST_INSTANCE_CREATED_FIRST;
                this.currentTestInstance = newTestInstance;
                return;
            case START_FIRES_FIRST:
                this.latestTestInstance = newTestInstance;
                this.currentTestInstance = newTestInstance;
                this.latestMethod = determineTestMethod(this.currentDescription);
                this.methodsPerInstance.get(newTestInstance).add(new OngoingTestRun(this.currentDescription, newTestInstance).testMethod);
                return;
            case TEST_INSTANCE_CREATED_FIRST:
                if (this.methodsPerInstance.isEmpty()) {
                    this.behaviour = DetectedTestRunBehaviour.ALL_TESTINSTANCES_ARE_CREATED_FIRST;
                    return;
                } else if (this.currentTestInstance == this.latestTestInstance) {
                    this.currentTestInstance = newTestInstance;
                    return;
                } else {
                    this.behaviour = DetectedTestRunBehaviour.INCONSISTENT_BEHAVIOUR;
                    return;
                }
            case TEST_INSTANCES_ARE_REUSED:
            default:
                throw new AssertionError("Unknown behaviour: " + this.behaviour);
            case ALL_TESTINSTANCES_ARE_CREATED_FIRST:
            case INCONSISTENT_BEHAVIOUR:
                System.err.println("Notifications are not supported for behaviour " + this.behaviour);
                return;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void testIgnored(Description d) {
        if (!notify(d, Result.IGNORED) && DetectedTestRunBehaviour.TEST_INSTANCE_CREATED_FIRST == this.behaviour && this.currentTestInstance != this.latestTestInstance) {
            this.currentTestInstance = this.latestTestInstance;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void assumptionFailed(Description d) {
        notify(d, Result.IGNORED);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void failure(Failure f) {
        notify(f.getDescription(), Result.FAILED);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void testFinished(Description d) {
        notify(d, Result.SUCCESSFUL);
    }

    private boolean notify(Description d, Result result) {
        OngoingTestRun testRun = this.ongoingTestRuns.get(d);
        if (null == testRun) {
            return false;
        }
        testRun.report(result);
        return true;
    }
}
