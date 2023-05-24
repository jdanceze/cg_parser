package org.powermock.api.mockito.internal.configuration;

import org.mockito.internal.configuration.InjectingAnnotationEngine;
import org.mockito.internal.configuration.plugins.Plugins;
import org.powermock.api.mockito.internal.mockcreation.DefaultMockCreator;
/* loaded from: gencallgraphv3.jar:powermock-api-mockito2-2.0.9.jar:org/powermock/api/mockito/internal/configuration/PowerMockitoInjectingAnnotationEngine.class */
public class PowerMockitoInjectingAnnotationEngine extends InjectingAnnotationEngine {
    public void process(Class<?> context, Object testClass) {
        new PowerMockitoSpyAnnotationEngine().process(context, testClass);
        preLoadPluginLoader();
        injectMocks(testClass);
    }

    private void preLoadPluginLoader() {
        ClassLoader originalCL = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(DefaultMockCreator.class.getClassLoader());
        try {
            Plugins.getMockMaker();
            Thread.currentThread().setContextClassLoader(originalCL);
        } catch (Throwable th) {
            Thread.currentThread().setContextClassLoader(originalCL);
            throw th;
        }
    }
}
