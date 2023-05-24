package org.powermock.tests.utils;

import java.lang.reflect.Method;
import java.util.List;
/* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/tests/utils/TestSuiteChunker.class */
public interface TestSuiteChunker {
    int getChunkSize();

    List<TestChunk> getTestChunks();

    List<TestChunk> getTestChunksEntries(Class<?> cls);

    TestChunk getTestChunk(Method method);

    boolean shouldExecuteTestForMethod(Class<?> cls, Method method);
}
