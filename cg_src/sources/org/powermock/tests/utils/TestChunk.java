package org.powermock.tests.utils;

import java.lang.reflect.Method;
import java.util.List;
/* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/tests/utils/TestChunk.class */
public interface TestChunk {
    ClassLoader getClassLoader();

    List<Method> getTestMethodsToBeExecutedByThisClassloader();

    boolean isMethodToBeExecutedByThisClassloader(Method method);
}
