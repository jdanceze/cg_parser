package org.powermock.tests.utils.impl;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.powermock.core.classloader.annotations.PowerMockListener;
import org.powermock.core.spi.PowerMockTestListener;
import org.powermock.reflect.Whitebox;
import org.powermock.tests.utils.RunnerTestSuiteChunker;
import org.powermock.tests.utils.TestChunk;
/* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/tests/utils/impl/AbstractTestSuiteChunkerImpl.class */
public abstract class AbstractTestSuiteChunkerImpl<T> extends AbstractCommonTestSuiteChunkerImpl implements RunnerTestSuiteChunker {
    protected final Set<Class<?>> delegatesCreatedForTheseClasses;
    protected final List<T> delegates;
    protected volatile int testCount;

    protected abstract T createDelegatorFromClassloader(ClassLoader classLoader, Class<?> cls, List<Method> list) throws Exception;

    /* JADX INFO: Access modifiers changed from: protected */
    public AbstractTestSuiteChunkerImpl(Class<?> testClass) throws Exception {
        super(testClass);
        this.delegatesCreatedForTheseClasses = new LinkedHashSet();
        this.delegates = new ArrayList();
        this.testCount = -1;
    }

    protected AbstractTestSuiteChunkerImpl(Class<?>... testClasses) throws Exception {
        super(testClasses);
        this.delegatesCreatedForTheseClasses = new LinkedHashSet();
        this.delegates = new ArrayList();
        this.testCount = -1;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Object getPowerMockTestListenersLoadedByASpecificClassLoader(Class<?> clazz, ClassLoader classLoader) {
        int defaultListenerSize = 1;
        Class<?> annotationEnablerClass = null;
        try {
            try {
                annotationEnablerClass = Class.forName("org.powermock.api.extension.listener.AnnotationEnabler", false, classLoader);
            } catch (ClassNotFoundException e) {
                throw new IllegalStateException("PowerMock internal error: Failed to load class.", e);
            }
        } catch (ClassNotFoundException e2) {
            defaultListenerSize = 0;
        }
        Class<?> powerMockTestListenerType = Class.forName(PowerMockTestListener.class.getName(), false, classLoader);
        Object testListeners = null;
        if (clazz.isAnnotationPresent(PowerMockListener.class)) {
            PowerMockListener annotation = (PowerMockListener) clazz.getAnnotation(PowerMockListener.class);
            Class<? extends PowerMockTestListener>[] powerMockTestListeners = annotation.value();
            if (powerMockTestListeners.length > 0) {
                testListeners = Array.newInstance(powerMockTestListenerType, powerMockTestListeners.length + defaultListenerSize);
                for (int i = 0; i < powerMockTestListeners.length; i++) {
                    String testListenerClassName = powerMockTestListeners[i].getName();
                    Class<?> listenerTypeLoadedByClassLoader = Class.forName(testListenerClassName, false, classLoader);
                    Array.set(testListeners, i, Whitebox.newInstance(listenerTypeLoadedByClassLoader));
                }
            }
        } else {
            testListeners = Array.newInstance(powerMockTestListenerType, defaultListenerSize);
        }
        if (annotationEnablerClass != null) {
            Array.set(testListeners, Array.getLength(testListeners) - 1, Whitebox.newInstance(annotationEnablerClass));
        }
        return testListeners;
    }

    @Override // org.powermock.tests.utils.RunnerTestSuiteChunker
    public final void createTestDelegators(Class<?> testClass, List<TestChunk> chunks) throws Exception {
        for (TestChunk chunk : chunks) {
            ClassLoader classLoader = chunk.getClassLoader();
            List<Method> methodsToTest = chunk.getTestMethodsToBeExecutedByThisClassloader();
            T runnerDelegator = createDelegatorFromClassloader(classLoader, testClass, methodsToTest);
            this.delegates.add(runnerDelegator);
        }
        this.delegatesCreatedForTheseClasses.add(testClass);
    }

    public int getInternalTestIndex(int originalTestIndex) {
        Set<Map.Entry<Integer, List<Integer>>> delegatorEntrySet = this.testAtDelegateMapper.entrySet();
        for (Map.Entry<Integer, List<Integer>> entry : delegatorEntrySet) {
            List<Integer> testIndexesForThisDelegate = entry.getValue();
            int internalIndex = testIndexesForThisDelegate.indexOf(Integer.valueOf(originalTestIndex));
            if (internalIndex != -1) {
                return internalIndex;
            }
        }
        return -1;
    }

    public int getDelegatorIndex(int testIndex) {
        int delegatorIndex = -1;
        Set<Map.Entry<Integer, List<Integer>>> entrySet = this.testAtDelegateMapper.entrySet();
        Iterator<Map.Entry<Integer, List<Integer>>> it = entrySet.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            Map.Entry<Integer, List<Integer>> entry = it.next();
            if (entry.getValue().contains(Integer.valueOf(testIndex))) {
                delegatorIndex = entry.getKey().intValue();
                break;
            }
        }
        if (delegatorIndex == -1) {
            throw new RuntimeException("Internal error: Failed to find the delegator index.");
        }
        return delegatorIndex;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Class<?>[] getTestClasses() {
        return this.testClasses;
    }
}
