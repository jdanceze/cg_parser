package org.powermock.tests.utils;

import java.lang.reflect.AnnotatedElement;
/* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/tests/utils/TestClassesExtractor.class */
public interface TestClassesExtractor {
    String[] getTestClasses(AnnotatedElement annotatedElement);

    boolean isPrepared(AnnotatedElement annotatedElement, String str);
}
