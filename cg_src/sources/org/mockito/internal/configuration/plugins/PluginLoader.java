package org.mockito.internal.configuration.plugins;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Collections;
import java.util.List;
import org.mockito.plugins.PluginSwitch;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/configuration/plugins/PluginLoader.class */
class PluginLoader {
    private final DefaultMockitoPlugins plugins;
    private final PluginInitializer initializer;

    PluginLoader(DefaultMockitoPlugins plugins, PluginInitializer initializer) {
        this.plugins = plugins;
        this.initializer = initializer;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public PluginLoader(PluginSwitch pluginSwitch) {
        this(new DefaultMockitoPlugins(), new PluginInitializer(pluginSwitch, null, new DefaultMockitoPlugins()));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Deprecated
    public PluginLoader(PluginSwitch pluginSwitch, String alias) {
        this(new DefaultMockitoPlugins(), new PluginInitializer(pluginSwitch, alias, new DefaultMockitoPlugins()));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public <T> T loadPlugin(Class<T> pluginType) {
        return (T) loadPlugin(pluginType, null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public <PreferredType, AlternateType> Object loadPlugin(final Class<PreferredType> preferredPluginType, final Class<AlternateType> alternatePluginType) {
        Object loadImpl;
        try {
            Object loadImpl2 = this.initializer.loadImpl(preferredPluginType);
            if (loadImpl2 != null) {
                return loadImpl2;
            }
            if (alternatePluginType != null && (loadImpl = this.initializer.loadImpl(alternatePluginType)) != null) {
                return loadImpl;
            }
            return this.plugins.getDefaultPlugin(preferredPluginType);
        } catch (Throwable t) {
            return Proxy.newProxyInstance(preferredPluginType.getClassLoader(), new Class[]{preferredPluginType}, new InvocationHandler() { // from class: org.mockito.internal.configuration.plugins.PluginLoader.1
                @Override // java.lang.reflect.InvocationHandler
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    throw new IllegalStateException("Could not initialize plugin: " + preferredPluginType + " (alternate: " + alternatePluginType + ")", t);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public <T> List<T> loadPlugins(final Class<T> pluginType) {
        try {
            return this.initializer.loadImpls(pluginType);
        } catch (Throwable t) {
            return Collections.singletonList(Proxy.newProxyInstance(pluginType.getClassLoader(), new Class[]{pluginType}, new InvocationHandler() { // from class: org.mockito.internal.configuration.plugins.PluginLoader.2
                @Override // java.lang.reflect.InvocationHandler
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    throw new IllegalStateException("Could not initialize plugin: " + pluginType, t);
                }
            }));
        }
    }
}
