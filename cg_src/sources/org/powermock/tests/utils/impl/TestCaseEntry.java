package org.powermock.tests.utils.impl;

import java.util.List;
import org.powermock.tests.utils.TestChunk;
/* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/tests/utils/impl/TestCaseEntry.class */
public class TestCaseEntry {
    private final List<TestChunk> testChunks;
    private final Class<?> testClass;

    public TestCaseEntry(Class<?> testClass, List<TestChunk> chunks) {
        this.testClass = testClass;
        this.testChunks = chunks;
    }

    public List<TestChunk> getTestChunks() {
        return this.testChunks;
    }

    public Class<?> getTestClass() {
        return this.testClass;
    }
}
