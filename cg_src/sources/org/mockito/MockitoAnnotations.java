package org.mockito;

import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.internal.util.StringUtil;
import org.mockito.plugins.AnnotationEngine;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/MockitoAnnotations.class */
public class MockitoAnnotations {
    public static AutoCloseable openMocks(Object testClass) {
        if (testClass == null) {
            throw new MockitoException("testClass cannot be null. For info how to use @Mock annotations see examples in javadoc for MockitoAnnotations class");
        }
        AnnotationEngine annotationEngine = new GlobalConfiguration().tryGetPluginAnnotationEngine();
        return annotationEngine.process(testClass.getClass(), testClass);
    }

    @Deprecated
    public static void initMocks(Object testClass) {
        try {
            openMocks(testClass).close();
        } catch (Exception e) {
            throw new MockitoException(StringUtil.join("Failed to release mocks", "", "This should not happen unless you are using a third-part mock maker"), e);
        }
    }
}
