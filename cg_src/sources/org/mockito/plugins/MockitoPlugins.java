package org.mockito.plugins;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/plugins/MockitoPlugins.class */
public interface MockitoPlugins {
    <T> T getDefaultPlugin(Class<T> cls);

    MockMaker getInlineMockMaker();
}
