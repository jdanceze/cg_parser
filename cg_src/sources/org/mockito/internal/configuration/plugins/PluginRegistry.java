package org.mockito.internal.configuration.plugins;

import java.util.List;
import org.mockito.internal.creation.instance.InstantiatorProviderAdapter;
import org.mockito.plugins.AnnotationEngine;
import org.mockito.plugins.InstantiatorProvider;
import org.mockito.plugins.InstantiatorProvider2;
import org.mockito.plugins.MemberAccessor;
import org.mockito.plugins.MockMaker;
import org.mockito.plugins.MockResolver;
import org.mockito.plugins.MockitoLogger;
import org.mockito.plugins.PluginSwitch;
import org.mockito.plugins.StackTraceCleanerProvider;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/configuration/plugins/PluginRegistry.class */
public class PluginRegistry {
    private final InstantiatorProvider2 instantiatorProvider;
    private final PluginSwitch pluginSwitch = (PluginSwitch) new PluginLoader(new DefaultPluginSwitch()).loadPlugin(PluginSwitch.class);
    private final MockMaker mockMaker = (MockMaker) new PluginLoader(this.pluginSwitch, "mock-maker-inline").loadPlugin(MockMaker.class);
    private final MemberAccessor memberAccessor = (MemberAccessor) new PluginLoader(this.pluginSwitch, "member-accessor-module").loadPlugin(MemberAccessor.class);
    private final StackTraceCleanerProvider stackTraceCleanerProvider = (StackTraceCleanerProvider) new PluginLoader(this.pluginSwitch).loadPlugin(StackTraceCleanerProvider.class);
    private final AnnotationEngine annotationEngine = (AnnotationEngine) new PluginLoader(this.pluginSwitch).loadPlugin(AnnotationEngine.class);
    private final MockitoLogger mockitoLogger = (MockitoLogger) new PluginLoader(this.pluginSwitch).loadPlugin(MockitoLogger.class);
    private final List<MockResolver> mockResolvers = new PluginLoader(this.pluginSwitch).loadPlugins(MockResolver.class);

    /* JADX INFO: Access modifiers changed from: package-private */
    public PluginRegistry() {
        Object impl = new PluginLoader(this.pluginSwitch).loadPlugin(InstantiatorProvider2.class, InstantiatorProvider.class);
        if (impl instanceof InstantiatorProvider) {
            this.instantiatorProvider = new InstantiatorProviderAdapter((InstantiatorProvider) impl);
        } else {
            this.instantiatorProvider = (InstantiatorProvider2) impl;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public StackTraceCleanerProvider getStackTraceCleanerProvider() {
        return this.stackTraceCleanerProvider;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public MockMaker getMockMaker() {
        return this.mockMaker;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public MemberAccessor getMemberAccessor() {
        return this.memberAccessor;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public InstantiatorProvider2 getInstantiatorProvider() {
        return this.instantiatorProvider;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public AnnotationEngine getAnnotationEngine() {
        return this.annotationEngine;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public MockitoLogger getMockitoLogger() {
        return this.mockitoLogger;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public List<MockResolver> getMockResolvers() {
        return this.mockResolvers;
    }
}
