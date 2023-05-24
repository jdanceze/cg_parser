package org.mockito.internal.configuration.plugins;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import org.mockito.internal.util.collections.Iterables;
import org.mockito.plugins.PluginSwitch;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/configuration/plugins/PluginInitializer.class */
class PluginInitializer {
    private final PluginSwitch pluginSwitch;
    private final String alias;
    private final DefaultMockitoPlugins plugins;

    /* JADX INFO: Access modifiers changed from: package-private */
    public PluginInitializer(PluginSwitch pluginSwitch, String alias, DefaultMockitoPlugins plugins) {
        this.pluginSwitch = pluginSwitch;
        this.alias = alias;
        this.plugins = plugins;
    }

    public <T> T loadImpl(Class<T> service) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        if (loader == null) {
            loader = ClassLoader.getSystemClassLoader();
        }
        try {
            Enumeration<URL> resources = loader.getResources("mockito-extensions/" + service.getName());
            try {
                String classOrAlias = new PluginFinder(this.pluginSwitch).findPluginClass(Iterables.toIterable(resources));
                if (classOrAlias != null) {
                    if (classOrAlias.equals(this.alias)) {
                        classOrAlias = this.plugins.getDefaultPluginClass(this.alias);
                    }
                    Class<?> pluginClass = loader.loadClass(classOrAlias);
                    Object plugin = pluginClass.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
                    return service.cast(plugin);
                }
                return null;
            } catch (Exception e) {
                throw new IllegalStateException("Failed to load " + service + " implementation declared in " + resources, e);
            }
        } catch (IOException e2) {
            throw new IllegalStateException("Failed to load " + service, e2);
        }
    }

    public <T> List<T> loadImpls(Class<T> service) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        if (loader == null) {
            loader = ClassLoader.getSystemClassLoader();
        }
        try {
            Enumeration<URL> resources = loader.getResources("mockito-extensions/" + service.getName());
            try {
                List<String> classesOrAliases = new PluginFinder(this.pluginSwitch).findPluginClasses(Iterables.toIterable(resources));
                List<T> impls = new ArrayList<>();
                for (String classOrAlias : classesOrAliases) {
                    if (classOrAlias.equals(this.alias)) {
                        classOrAlias = this.plugins.getDefaultPluginClass(this.alias);
                    }
                    Class<?> pluginClass = loader.loadClass(classOrAlias);
                    Object plugin = pluginClass.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
                    impls.add(service.cast(plugin));
                }
                return impls;
            } catch (Exception e) {
                throw new IllegalStateException("Failed to load " + service + " implementation declared in " + resources, e);
            }
        } catch (IOException e2) {
            throw new IllegalStateException("Failed to load " + service, e2);
        }
    }
}
