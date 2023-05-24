package org.powermock.modules.junit4.internal.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.junit.Before;
import org.junit.internal.runners.ClassRoadie;
import org.junit.internal.runners.InitializationError;
import org.junit.internal.runners.MethodRoadie;
import org.junit.internal.runners.MethodValidator;
import org.junit.internal.runners.TestClass;
import org.junit.internal.runners.TestMethod;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.manipulation.Filter;
import org.junit.runner.manipulation.Filterable;
import org.junit.runner.manipulation.NoTestsRemainException;
import org.junit.runner.manipulation.Sortable;
import org.junit.runner.manipulation.Sorter;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.powermock.core.classloader.annotations.MockPolicy;
import org.powermock.core.classloader.annotations.PrepareEverythingForTest;
import org.powermock.core.spi.PowerMockTestListener;
import org.powermock.modules.junit4.common.internal.PowerMockJUnitRunnerDelegate;
import org.powermock.modules.junit4.internal.impl.testcaseworkaround.PowerMockJUnit4MethodValidator;
import org.powermock.reflect.Whitebox;
import org.powermock.tests.utils.PowerMockTestNotifier;
import org.powermock.tests.utils.impl.MockPolicyInitializerImpl;
import org.powermock.tests.utils.impl.PowerMockTestNotifierImpl;
import org.powermock.tests.utils.impl.PrepareForTestExtractorImpl;
import org.powermock.tests.utils.impl.StaticConstructorSuppressExtractorImpl;
/* loaded from: gencallgraphv3.jar:powermock-module-junit4-2.0.9.jar:org/powermock/modules/junit4/internal/impl/PowerMockJUnit44RunnerDelegateImpl.class */
public class PowerMockJUnit44RunnerDelegateImpl extends Runner implements Filterable, Sortable, PowerMockJUnitRunnerDelegate {
    private final List<Method> testMethods;
    private final TestClass testClass;
    private final PowerMockTestNotifier powerMockTestNotifier;

    public PowerMockJUnit44RunnerDelegateImpl(Class<?> klass, String[] methodsToRun, PowerMockTestListener[] listeners) throws InitializationError {
        this.powerMockTestNotifier = new PowerMockTestNotifierImpl(listeners == null ? new PowerMockTestListener[0] : listeners);
        this.testClass = new TestClass(klass);
        this.testMethods = getTestMethods(klass, methodsToRun);
        validate();
    }

    public PowerMockJUnit44RunnerDelegateImpl(Class<?> klass, String[] methodsToRun) throws InitializationError {
        this(klass, methodsToRun, null);
    }

    public PowerMockJUnit44RunnerDelegateImpl(Class<?> klass) throws InitializationError {
        this(klass, null);
    }

    protected final List<Method> getTestMethods(Class<?> klass, String[] methodsToRun) {
        if (methodsToRun == null || methodsToRun.length == 0) {
            try {
                return (List) Whitebox.invokeMethod(this.testClass, "getTestMethods", new Object[0]);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }
        List<Method> foundMethods = new LinkedList<>();
        Method[] methods = klass.getMethods();
        for (Method method : methods) {
            for (String methodName : methodsToRun) {
                if (method.getName().equals(methodName)) {
                    foundMethods.add(method);
                }
            }
        }
        return foundMethods;
    }

    protected final void validate() throws InitializationError {
        if (!TestCase.class.isAssignableFrom(this.testClass.getJavaClass())) {
            MethodValidator methodValidator = new PowerMockJUnit4MethodValidator(this.testClass);
            methodValidator.validateMethodsForDefaultRunner();
            methodValidator.assertValid();
        }
    }

    @Override // org.junit.runner.Runner
    public void run(final RunNotifier notifier) {
        new ClassRoadie(notifier, this.testClass, getDescription(), new Runnable() { // from class: org.powermock.modules.junit4.internal.impl.PowerMockJUnit44RunnerDelegateImpl.1
            @Override // java.lang.Runnable
            public void run() {
                PowerMockJUnit44RunnerDelegateImpl.this.runMethods(notifier);
            }
        }).runProtected();
    }

    protected void runMethods(RunNotifier notifier) {
        StaticConstructorSuppressExtractorImpl staticConstructorSuppressExtractorImpl = new StaticConstructorSuppressExtractorImpl();
        Class<?> testType = getTestClass();
        ClassLoader thisClassLoader = getClass().getClassLoader();
        if (!thisClassLoader.equals(testType.getClassLoader())) {
            try {
                testType = thisClassLoader.loadClass(testType.getName());
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("Internal error in PowerMock", e);
            }
        }
        for (Method method : this.testMethods) {
            if (staticConstructorSuppressExtractorImpl.getTestClasses(method) == null) {
                staticConstructorSuppressExtractorImpl.getTestClasses(testType);
            }
            invokeTestMethod(method, notifier);
        }
    }

    @Override // org.junit.runner.Runner, org.junit.runner.Describable
    public Description getDescription() {
        Description spec = Description.createSuiteDescription(getName(), classAnnotations());
        List<Method> testMethods = this.testMethods;
        for (Method method : testMethods) {
            spec.addChild(methodDescription(method));
        }
        return spec;
    }

    protected Annotation[] classAnnotations() {
        return getTestClass().getAnnotations();
    }

    protected String getName() {
        return getTestWrappedClass().getName();
    }

    protected Object createTest() throws Exception {
        return createTestInstance();
    }

    private Object createTestInstance() throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Constructor<?> constructor;
        TestClass testWrappedClass = getTestWrappedClass();
        Class<?> javaClass = testWrappedClass.getJavaClass();
        if (TestCase.class.isAssignableFrom(javaClass)) {
            constructor = TestSuite.getTestConstructor(javaClass.asSubclass(TestCase.class));
            if (constructor.getParameterTypes().length == 1) {
                return constructor.newInstance(javaClass.getSimpleName());
            }
        } else {
            constructor = testWrappedClass.getConstructor();
        }
        return constructor.newInstance(new Object[0]);
    }

    protected void invokeTestMethod(Method method, RunNotifier notifier) {
        Description description = methodDescription(method);
        try {
            Object testInstance = createTest();
            boolean extendsFromTestCase = TestCase.class.isAssignableFrom(this.testClass.getJavaClass());
            TestMethod testMethod = wrapMethod(method);
            createPowerMockRunner(testInstance, testMethod, notifier, description, extendsFromTestCase).run();
        } catch (InvocationTargetException e) {
            testAborted(notifier, description, e.getTargetException());
        } catch (Exception e2) {
            testAborted(notifier, description, e2);
        }
    }

    protected PowerMockJUnit44MethodRunner createPowerMockRunner(Object testInstance, TestMethod testMethod, RunNotifier notifier, Description description, boolean extendsFromTestCase) {
        return new PowerMockJUnit44MethodRunner(testInstance, testMethod, notifier, description, extendsFromTestCase);
    }

    private void testAborted(RunNotifier notifier, Description description, Throwable e) {
        notifier.fireTestStarted(description);
        notifier.fireTestFailure(new Failure(description, e));
        notifier.fireTestFinished(description);
    }

    protected TestMethod wrapMethod(Method method) {
        return new TestMethod(method, this.testClass);
    }

    protected String testName(Method method) {
        return method.getName();
    }

    protected Description methodDescription(Method method) {
        return Description.createTestDescription(getTestWrappedClass().getJavaClass(), testName(method), testAnnotations(method));
    }

    protected Annotation[] testAnnotations(Method method) {
        return method.getAnnotations();
    }

    @Override // org.junit.runner.manipulation.Filterable
    public void filter(Filter filter) throws NoTestsRemainException {
        Iterator<Method> iter = this.testMethods.iterator();
        while (iter.hasNext()) {
            Method method = iter.next();
            if (!filter.shouldRun(methodDescription(method))) {
                iter.remove();
            }
        }
        if (this.testMethods.isEmpty()) {
            throw new NoTestsRemainException();
        }
    }

    @Override // org.junit.runner.manipulation.Sortable
    public void sort(final Sorter sorter) {
        Collections.sort(this.testMethods, new Comparator<Method>() { // from class: org.powermock.modules.junit4.internal.impl.PowerMockJUnit44RunnerDelegateImpl.2
            @Override // java.util.Comparator
            public int compare(Method o1, Method o2) {
                return sorter.compare(PowerMockJUnit44RunnerDelegateImpl.this.methodDescription(o1), PowerMockJUnit44RunnerDelegateImpl.this.methodDescription(o2));
            }
        });
    }

    protected TestClass getTestWrappedClass() {
        return this.testClass;
    }

    @Override // org.powermock.modules.junit4.common.internal.PowerMockJUnitRunnerDelegate
    public int getTestCount() {
        return this.testMethods.size();
    }

    @Override // org.powermock.modules.junit4.common.internal.PowerMockJUnitRunnerDelegate
    public Class<?> getTestClass() {
        return this.testClass.getJavaClass();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: gencallgraphv3.jar:powermock-module-junit4-2.0.9.jar:org/powermock/modules/junit4/internal/impl/PowerMockJUnit44RunnerDelegateImpl$PowerMockJUnit44MethodRunner.class */
    public class PowerMockJUnit44MethodRunner extends MethodRoadie {
        private final Object testInstance;
        private final boolean extendsFromTestCase;
        protected final TestMethod testMethod;

        /* JADX INFO: Access modifiers changed from: protected */
        public PowerMockJUnit44MethodRunner(Object testInstance, TestMethod method, RunNotifier notifier, Description description, boolean extendsFromTestCase) {
            super(testInstance, method, notifier, description);
            this.testInstance = testInstance;
            this.extendsFromTestCase = extendsFromTestCase;
            this.testMethod = method;
        }

        @Override // org.junit.internal.runners.MethodRoadie
        public void runBeforesThenTestThenAfters(Runnable test) {
            executeTest((Method) Whitebox.getInternalState(this.testMethod, Method.class), this.testInstance, test);
        }

        public void executeTest(Method method, Object testInstance, Runnable test) {
            ClassLoader classloader = getClass().getClassLoader();
            Thread currentThread = Thread.currentThread();
            ClassLoader originalClassLoader = currentThread.getContextClassLoader();
            currentThread.setContextClassLoader(classloader);
            new MockPolicyInitializerImpl(PowerMockJUnit44RunnerDelegateImpl.this.testClass.getJavaClass()).initialize(classloader);
            PowerMockJUnit44RunnerDelegateImpl.this.powerMockTestNotifier.notifyBeforeTestMethod(testInstance, method, new Object[0]);
            try {
                super.runBeforesThenTestThenAfters(test);
                currentThread.setContextClassLoader(originalClassLoader);
            } catch (Throwable th) {
                currentThread.setContextClassLoader(originalClassLoader);
                throw th;
            }
        }

        @Override // org.junit.internal.runners.MethodRoadie
        protected void runTestMethod() {
            try {
                try {
                    if (this.extendsFromTestCase) {
                        Method setUp = Whitebox.getMethod(this.testInstance.getClass(), "setUp", new Class[0]);
                        if (!setUp.isAnnotationPresent(Before.class)) {
                            Whitebox.invokeMethod(this.testInstance, "setUp", new Object[0]);
                        }
                    }
                    this.testMethod.invoke(this.testInstance);
                    if (((Boolean) Whitebox.invokeMethod(this.testMethod, "expectsException", new Object[0])).booleanValue()) {
                        addFailure(new AssertionError("Expected exception: " + getExpectedExceptionName(this.testMethod)));
                    }
                    if (this.extendsFromTestCase) {
                        Whitebox.invokeMethod(this.testInstance, "tearDown", new Object[0]);
                    }
                } catch (InvocationTargetException e) {
                    handleInvocationTargetException(this.testMethod, e);
                    if (this.extendsFromTestCase) {
                        Whitebox.invokeMethod(this.testInstance, "tearDown", new Object[0]);
                    }
                }
            } catch (Throwable e2) {
                throw new RuntimeException("Internal error in PowerMock.", e2);
            }
        }

        private void handleInvocationTargetException(TestMethod testMethod, InvocationTargetException e) throws Exception {
            Throwable targetException = e.getTargetException();
            while (true) {
                Throwable actual = targetException;
                if (actual instanceof InvocationTargetException) {
                    targetException = ((InvocationTargetException) actual).getTargetException();
                } else {
                    handleException(testMethod, actual);
                    return;
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public void handleException(TestMethod testMethod, Throwable actualFailure) {
            try {
                String throwableName = actualFailure.getClass().getName();
                if (throwableName.equals("org.junit.internal.AssumptionViolatedException") || throwableName.startsWith("org.junit.Assume$AssumptionViolatedException")) {
                    return;
                }
                if (!((Boolean) Whitebox.invokeMethod(testMethod, "expectsException", new Object[0])).booleanValue()) {
                    String className = actualFailure.getStackTrace()[0].getClassName();
                    Class<?> testClassAsJavaClass = PowerMockJUnit44RunnerDelegateImpl.this.testClass.getJavaClass();
                    if ((actualFailure instanceof NullPointerException) && !testClassAsJavaClass.getName().equals(className) && !className.startsWith("java.lang") && !className.startsWith("org.powermock") && !className.startsWith("org.junit") && !new PrepareForTestExtractorImpl().isPrepared(testClassAsJavaClass, className) && !testClassAsJavaClass.isAnnotationPresent(PrepareEverythingForTest.class)) {
                        if (!new MockPolicyInitializerImpl(testClassAsJavaClass.isAnnotationPresent(MockPolicy.class) ? ((MockPolicy) testClassAsJavaClass.getAnnotation(MockPolicy.class)).value() : null).isPrepared(className)) {
                            Whitebox.setInternalState(actualFailure, "detailMessage", "Perhaps the class " + className + " must be prepared for test?", Throwable.class);
                        }
                    }
                    addFailure(actualFailure);
                } else if (((Boolean) Whitebox.invokeMethod(testMethod, "isUnexpected", actualFailure)).booleanValue()) {
                    String message = "Unexpected exception, expected<" + getExpectedExceptionName(testMethod) + "> but was<" + actualFailure.getClass().getName() + ">";
                    addFailure(new Exception(message, actualFailure));
                }
            } catch (Exception e) {
                throw new RuntimeException("PowerMock internal error: Should never throw exception at this level", e);
            }
        }

        private String getExpectedExceptionName(TestMethod fTestMethod) throws Exception {
            return ((Class) Whitebox.invokeMethod(fTestMethod, "getExpectedException", new Object[0])).getName();
        }
    }
}
