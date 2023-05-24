package org.powermock.modules.junit4.common.internal.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.manipulation.Filter;
import org.junit.runner.manipulation.Filterable;
import org.junit.runner.manipulation.NoTestsRemainException;
import org.junit.runner.manipulation.Sortable;
import org.junit.runner.manipulation.Sorter;
import org.junit.runner.notification.RunNotifier;
import org.powermock.core.spi.PowerMockTestListener;
import org.powermock.core.spi.testresult.TestSuiteResult;
import org.powermock.core.spi.testresult.impl.TestSuiteResultImpl;
import org.powermock.modules.junit4.common.internal.JUnit4TestSuiteChunker;
import org.powermock.modules.junit4.common.internal.PowerMockJUnitRunnerDelegate;
import org.powermock.tests.utils.PowerMockTestNotifier;
import org.powermock.tests.utils.TestChunk;
import org.powermock.tests.utils.impl.AbstractTestSuiteChunkerImpl;
import org.powermock.tests.utils.impl.PowerMockTestNotifierImpl;
/* loaded from: gencallgraphv3.jar:powermock-module-junit4-common-2.0.9.jar:org/powermock/modules/junit4/common/internal/impl/JUnit4TestSuiteChunkerImpl.class */
public class JUnit4TestSuiteChunkerImpl extends AbstractTestSuiteChunkerImpl<PowerMockJUnitRunnerDelegate> implements JUnit4TestSuiteChunker, Filterable, Sortable {
    private static final Class<? extends Annotation> testMethodAnnotation = Test.class;
    private Description description;
    private final Class<? extends PowerMockJUnitRunnerDelegate> runnerDelegateImplementationType;

    @Override // org.powermock.tests.utils.impl.AbstractTestSuiteChunkerImpl
    protected /* bridge */ /* synthetic */ PowerMockJUnitRunnerDelegate createDelegatorFromClassloader(ClassLoader classLoader, Class cls, List list) throws Exception {
        return createDelegatorFromClassloader(classLoader, (Class<?>) cls, (List<Method>) list);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public JUnit4TestSuiteChunkerImpl(Class<?> testClass, Class<? extends PowerMockJUnitRunnerDelegate> runnerDelegateImplementationType) throws Exception {
        super(testClass);
        if (testClass == null) {
            throw new IllegalArgumentException("You must supply a test class");
        }
        if (runnerDelegateImplementationType == null) {
            throw new IllegalArgumentException("Runner delegate type cannot be null.");
        }
        this.runnerDelegateImplementationType = runnerDelegateImplementationType;
        try {
            createTestDelegators(testClass, getTestChunksEntries(testClass));
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            if (cause instanceof Exception) {
                throw ((Exception) cause);
            }
            if (cause instanceof Error) {
                throw ((Error) cause);
            }
            throw new RuntimeException(cause);
        }
    }

    @Override // org.powermock.modules.junit4.common.internal.JUnit4TestSuiteChunker
    public void run(RunNotifier notifier) {
        List<TestChunk> chunkEntries = getTestChunks();
        Iterator<TestChunk> iterator = chunkEntries.iterator();
        if (this.delegates.size() != getChunkSize()) {
            throw new IllegalStateException("Internal error: There must be an equal number of suites and delegates.");
        }
        Class<?> testClass = getTestClasses()[0];
        PowerMockTestListener[] powerMockTestListeners = (PowerMockTestListener[]) getPowerMockTestListenersLoadedByASpecificClassLoader(testClass, getClass().getClassLoader());
        Set<Method> allMethods = new LinkedHashSet<>();
        for (TestChunk testChunk : getTestChunks()) {
            allMethods.addAll(testChunk.getTestMethodsToBeExecutedByThisClassloader());
        }
        Method[] allMethodsAsArray = (Method[]) allMethods.toArray(new Method[allMethods.size()]);
        PowerMockTestNotifier powerMockTestNotifier = new PowerMockTestNotifierImpl(powerMockTestListeners);
        powerMockTestNotifier.notifyBeforeTestSuiteStarted(testClass, allMethodsAsArray);
        int failureCount = 0;
        int successCount = 0;
        int ignoreCount = 0;
        for (T delegate : this.delegates) {
            TestChunk next = iterator.next();
            ClassLoader key = next.getClassLoader();
            PowerMockJUnit4RunListener powerMockListener = new PowerMockJUnit4RunListener(key, powerMockTestNotifier);
            notifier.addListener(powerMockListener);
            ClassLoader originalClassLoader = Thread.currentThread().getContextClassLoader();
            Thread.currentThread().setContextClassLoader(key);
            try {
                delegate.run(notifier);
                Thread.currentThread().setContextClassLoader(originalClassLoader);
                int failureCountForThisPowerMockListener = powerMockListener.getFailureCount();
                int ignoreCountForThisPowerMockListener = powerMockListener.getIgnoreCount();
                failureCount += failureCountForThisPowerMockListener;
                ignoreCount += ignoreCountForThisPowerMockListener;
                successCount += (delegate.getTestCount() - failureCountForThisPowerMockListener) - ignoreCountForThisPowerMockListener;
                notifier.removeListener(powerMockListener);
            } catch (Throwable th) {
                Thread.currentThread().setContextClassLoader(originalClassLoader);
                throw th;
            }
        }
        TestSuiteResult testSuiteResult = new TestSuiteResultImpl(failureCount, successCount, getTestCount(), ignoreCount);
        powerMockTestNotifier.notifyAfterTestSuiteEnded(testClass, allMethodsAsArray, testSuiteResult);
    }

    @Override // org.powermock.tests.utils.TestSuiteChunker
    public boolean shouldExecuteTestForMethod(Class<?> testClass, Method potentialTestMethod) {
        return new JUnit4TestMethodChecker(testClass, potentialTestMethod).isTestMethod();
    }

    @Override // org.powermock.tests.utils.impl.AbstractCommonTestSuiteChunkerImpl
    protected Class<? extends Annotation> testMethodAnnotation() {
        return testMethodAnnotation;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.powermock.tests.utils.impl.AbstractTestSuiteChunkerImpl
    protected PowerMockJUnitRunnerDelegate createDelegatorFromClassloader(ClassLoader classLoader, Class<?> testClass, List<Method> methodsToTest) throws Exception {
        Set<String> methodNames = new HashSet<>();
        for (Method method : methodsToTest) {
            methodNames.add(method.getName());
        }
        Class<?> testClassLoadedByMockedClassLoader = Class.forName(testClass.getName(), false, classLoader);
        Class<?> powerMockTestListenerArrayType = Class.forName(PowerMockTestListener[].class.getName(), false, classLoader);
        Class<?> delegateClass = Class.forName(this.runnerDelegateImplementationType.getName(), false, classLoader);
        Constructor<?> con = delegateClass.getConstructor(Class.class, String[].class, powerMockTestListenerArrayType);
        return (PowerMockJUnitRunnerDelegate) con.newInstance(testClassLoadedByMockedClassLoader, methodNames.toArray(new String[methodNames.size()]), getPowerMockTestListenersLoadedByASpecificClassLoader(testClass, classLoader));
    }

    @Override // org.powermock.tests.utils.RunnerTestSuiteChunker
    public synchronized int getTestCount() {
        if (this.testCount == -1) {
            this.testCount = 0;
            for (T delegate : this.delegates) {
                this.testCount += delegate.getTestCount();
            }
        }
        return this.testCount;
    }

    @Override // org.powermock.modules.junit4.common.internal.JUnit4TestSuiteChunker
    public Description getDescription() {
        if (this.description == null) {
            if (this.delegates.size() == 0) {
                return Description.createTestDescription(getClass(), "no tests in this class");
            }
            PowerMockJUnitRunnerDelegate delegate = (PowerMockJUnitRunnerDelegate) this.delegates.get(0);
            this.description = delegate.getDescription();
            for (int i = 1; i < this.delegates.size(); i++) {
                ArrayList<Description> children = ((PowerMockJUnitRunnerDelegate) this.delegates.get(i)).getDescription().getChildren();
                Iterator<Description> it = children.iterator();
                while (it.hasNext()) {
                    Description methodDescription = it.next();
                    this.description.addChild(methodDescription);
                }
            }
        }
        return this.description;
    }

    @Override // org.junit.runner.manipulation.Filterable
    public void filter(Filter filter) throws NoTestsRemainException {
        for (Object delegate : this.delegates) {
            if (delegate instanceof Filterable) {
                ((Filterable) delegate).filter(filter);
            }
        }
    }

    @Override // org.junit.runner.manipulation.Sortable
    public void sort(Sorter sorter) {
        for (Object delegate : this.delegates) {
            if (delegate instanceof Sortable) {
                ((Sortable) delegate).sort(sorter);
            }
        }
    }
}
