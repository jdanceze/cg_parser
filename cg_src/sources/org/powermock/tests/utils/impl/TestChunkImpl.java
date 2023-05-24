package org.powermock.tests.utils.impl;

import java.lang.reflect.Method;
import java.util.List;
import org.powermock.tests.utils.TestChunk;
/* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/tests/utils/impl/TestChunkImpl.class */
public class TestChunkImpl implements TestChunk {
    private final ClassLoader classLoader;
    private final List<Method> testMethodsToBeExecutedByThisClassloader;

    public TestChunkImpl(ClassLoader classLoader, List<Method> testMethodsToBeExecutedByThisClassloader) {
        this.classLoader = classLoader;
        this.testMethodsToBeExecutedByThisClassloader = testMethodsToBeExecutedByThisClassloader;
    }

    @Override // org.powermock.tests.utils.TestChunk
    public ClassLoader getClassLoader() {
        return this.classLoader;
    }

    @Override // org.powermock.tests.utils.TestChunk
    public List<Method> getTestMethodsToBeExecutedByThisClassloader() {
        return this.testMethodsToBeExecutedByThisClassloader;
    }

    @Override // org.powermock.tests.utils.TestChunk
    public boolean isMethodToBeExecutedByThisClassloader(Method method) {
        return this.testMethodsToBeExecutedByThisClassloader.contains(method);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Classloader = ").append(this.classLoader).append("\n");
        sb.append("Methods:\n");
        for (Method method : this.testMethodsToBeExecutedByThisClassloader) {
            sb.append("  ").append(method).append("\n");
        }
        return sb.toString();
    }
}
