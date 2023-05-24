package org.powermock.tests.utils;

import java.util.List;
/* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/tests/utils/RunnerTestSuiteChunker.class */
public interface RunnerTestSuiteChunker extends TestSuiteChunker {
    void createTestDelegators(Class<?> cls, List<TestChunk> list) throws Exception;

    int getTestCount();
}
