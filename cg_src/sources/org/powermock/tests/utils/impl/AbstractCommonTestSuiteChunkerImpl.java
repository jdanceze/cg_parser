package org.powermock.tests.utils.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import org.powermock.core.classloader.MockClassLoaderFactory;
import org.powermock.core.classloader.annotations.PrepareEverythingForTest;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.PrepareOnlyThisForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.core.transformers.MockTransformer;
import org.powermock.core.transformers.TestClassTransformerBuilder;
import org.powermock.tests.utils.TestChunk;
import org.powermock.tests.utils.TestSuiteChunker;
/* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/tests/utils/impl/AbstractCommonTestSuiteChunkerImpl.class */
public abstract class AbstractCommonTestSuiteChunkerImpl implements TestSuiteChunker {
    protected static final int NOT_INITIALIZED = -1;
    static final int DEFAULT_TEST_LISTENERS_SIZE = 1;
    static final int INTERNAL_INDEX_NOT_FOUND = -1;
    private final List<TestCaseEntry> internalSuites;
    final LinkedHashMap<Integer, List<Integer>> testAtDelegateMapper;
    final Class<?>[] testClasses;
    private int currentTestIndex;

    /* JADX INFO: Access modifiers changed from: protected */
    public AbstractCommonTestSuiteChunkerImpl(Class<?> testClass) throws Exception {
        this(testClass);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public AbstractCommonTestSuiteChunkerImpl(Class<?>... testClasses) throws Exception {
        this.internalSuites = new LinkedList();
        this.testAtDelegateMapper = new LinkedHashMap<>();
        this.currentTestIndex = -1;
        this.testClasses = testClasses;
        for (Class<?> clazz : testClasses) {
            chunkClass(clazz);
        }
    }

    @Override // org.powermock.tests.utils.TestSuiteChunker
    public int getChunkSize() {
        return getTestChunks().size();
    }

    @Override // org.powermock.tests.utils.TestSuiteChunker
    public List<TestChunk> getTestChunks() {
        List<TestChunk> allChunks = new LinkedList<>();
        for (TestCaseEntry entry : this.internalSuites) {
            allChunks.addAll(entry.getTestChunks());
        }
        return allChunks;
    }

    @Override // org.powermock.tests.utils.TestSuiteChunker
    public List<TestChunk> getTestChunksEntries(Class<?> testClass) {
        for (TestCaseEntry entry : this.internalSuites) {
            if (entry.getTestClass().equals(testClass)) {
                return entry.getTestChunks();
            }
        }
        return null;
    }

    @Override // org.powermock.tests.utils.TestSuiteChunker
    public TestChunk getTestChunk(Method method) {
        for (TestChunk testChunk : getTestChunks()) {
            if (testChunk.isMethodToBeExecutedByThisClassloader(method)) {
                return testChunk;
            }
        }
        return null;
    }

    private void chunkClass(Class<?> testClass) throws Exception {
        List<Method> testMethodsForOtherClassLoaders = new ArrayList<>();
        ClassLoader defaultMockLoader = createDefaultMockLoader(testClass, testMethodsForOtherClassLoaders);
        List<Method> currentClassloaderMethods = new LinkedList<>();
        TestChunk testChunkImpl = new TestChunkImpl(defaultMockLoader, currentClassloaderMethods);
        List<TestChunk> testChunks = new LinkedList<>();
        testChunks.add(testChunkImpl);
        this.internalSuites.add(new TestCaseEntry(testClass, testChunks));
        initEntries(this.internalSuites);
        if (!currentClassloaderMethods.isEmpty()) {
            List<TestChunk> allTestChunks = this.internalSuites.get(0).getTestChunks();
            for (TestChunk chunk : allTestChunks.subList(1, allTestChunks.size())) {
                testMethodsForOtherClassLoaders.addAll(chunk.getTestMethodsToBeExecutedByThisClassloader());
            }
        } else if (2 <= this.internalSuites.size() || (1 == this.internalSuites.size() && 2 <= this.internalSuites.get(0).getTestChunks().size())) {
            this.internalSuites.get(0).getTestChunks().remove(0);
        }
    }

    private ClassLoader createDefaultMockLoader(Class<?> testClass, Collection<Method> testMethodsForOtherClassLoaders) {
        MockTransformer extraMockTransformer;
        if (null == testMethodAnnotation()) {
            extraMockTransformer = null;
        } else {
            extraMockTransformer = TestClassTransformerBuilder.forTestClass(testClass).removesTestMethodAnnotation(testMethodAnnotation()).fromMethods(testMethodsForOtherClassLoaders);
        }
        return new MockClassLoaderFactory(testClass).createForClass(extraMockTransformer);
    }

    private void putMethodToChunk(TestCaseEntry testCaseEntry, Class<?> testClass, Method method) {
        if (shouldExecuteTestForMethod(testClass, method)) {
            this.currentTestIndex++;
            if (hasChunkAnnotation(method)) {
                LinkedList<Method> methodsInThisChunk = new LinkedList<>();
                methodsInThisChunk.add(method);
                ClassLoader mockClassloader = createClassLoaderForMethod(testClass, method);
                TestChunkImpl chunk = new TestChunkImpl(mockClassloader, methodsInThisChunk);
                testCaseEntry.getTestChunks().add(chunk);
                updatedIndexes();
                return;
            }
            testCaseEntry.getTestChunks().get(0).getTestMethodsToBeExecutedByThisClassloader().add(method);
            int currentDelegateIndex = this.internalSuites.size() - 1;
            List<Integer> testList = this.testAtDelegateMapper.get(Integer.valueOf(currentDelegateIndex));
            if (testList == null) {
                testList = new LinkedList<>();
                this.testAtDelegateMapper.put(Integer.valueOf(currentDelegateIndex), testList);
            }
            testList.add(Integer.valueOf(this.currentTestIndex));
        }
    }

    private ClassLoader createClassLoaderForMethod(Class<?> testClass, Method method) {
        MockTransformer extraMockTransformer;
        if (null == testMethodAnnotation()) {
            extraMockTransformer = null;
        } else {
            extraMockTransformer = TestClassTransformerBuilder.forTestClass(testClass).bytecodeFrameworkClue(method).removesTestMethodAnnotation(testMethodAnnotation()).fromAllMethodsExcept(method);
        }
        MockClassLoaderFactory classLoaderFactory = new MockClassLoaderFactory(testClass);
        return classLoaderFactory.createForMethod(method, extraMockTransformer);
    }

    protected Class<? extends Annotation> testMethodAnnotation() {
        return null;
    }

    private void initEntries(List<TestCaseEntry> entries) {
        for (TestCaseEntry testCaseEntry : entries) {
            Class<?> testClass = testCaseEntry.getTestClass();
            findMethods(testCaseEntry, testClass);
        }
    }

    private void findMethods(TestCaseEntry testCaseEntry, Class<?> testClass) {
        Method[] allMethods = testClass.getMethods();
        for (Method method : allMethods) {
            putMethodToChunk(testCaseEntry, testClass, method);
        }
        Class<?> testClass2 = testClass.getSuperclass();
        if (!Object.class.equals(testClass2)) {
            findMethods(testCaseEntry, testClass2);
        }
    }

    private boolean hasChunkAnnotation(Method method) {
        return method.isAnnotationPresent(PrepareForTest.class) || method.isAnnotationPresent(SuppressStaticInitializationFor.class) || method.isAnnotationPresent(PrepareOnlyThisForTest.class) || method.isAnnotationPresent(PrepareEverythingForTest.class);
    }

    private void updatedIndexes() {
        List<Integer> testIndexesForThisClassloader = new LinkedList<>();
        testIndexesForThisClassloader.add(Integer.valueOf(this.currentTestIndex));
        this.testAtDelegateMapper.put(Integer.valueOf(this.internalSuites.size()), testIndexesForThisClassloader);
    }
}
