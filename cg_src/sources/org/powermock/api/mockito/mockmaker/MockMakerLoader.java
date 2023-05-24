package org.powermock.api.mockito.mockmaker;

import org.mockito.Mockito;
import org.mockito.plugins.MockMaker;
import org.powermock.configuration.MockitoConfiguration;
/* loaded from: gencallgraphv3.jar:powermock-api-mockito2-2.0.9.jar:org/powermock/api/mockito/mockmaker/MockMakerLoader.class */
class MockMakerLoader {
    /* JADX INFO: Access modifiers changed from: package-private */
    public MockMaker load(MockitoConfiguration mockitoConfiguration) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        if (loader == null) {
            loader = ClassLoader.getSystemClassLoader();
        }
        String mockMakerClassName = mockitoConfiguration.getMockMakerClass();
        try {
            return doLoad(loader, mockMakerClassName);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to load MockMaker implementation: " + mockMakerClassName, e);
        }
    }

    private MockMaker doLoad(ClassLoader loader, String mockMakerClassName) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        if (mockMakerClassName == null) {
            return (MockMaker) Mockito.framework().getPlugins().getDefaultPlugin(MockMaker.class);
        }
        if ("mock-maker-inline".equals(mockMakerClassName)) {
            return Mockito.framework().getPlugins().getInlineMockMaker();
        }
        Class<?> mockMakerClass = loader.loadClass(mockMakerClassName);
        Object mockMaker = mockMakerClass.newInstance();
        return (MockMaker) MockMaker.class.cast(mockMaker);
    }
}
