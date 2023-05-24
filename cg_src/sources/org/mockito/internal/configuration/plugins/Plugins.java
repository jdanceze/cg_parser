package org.mockito.internal.configuration.plugins;

import java.util.List;
import org.mockito.plugins.AnnotationEngine;
import org.mockito.plugins.InstantiatorProvider2;
import org.mockito.plugins.MemberAccessor;
import org.mockito.plugins.MockMaker;
import org.mockito.plugins.MockResolver;
import org.mockito.plugins.MockitoLogger;
import org.mockito.plugins.MockitoPlugins;
import org.mockito.plugins.StackTraceCleanerProvider;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/configuration/plugins/Plugins.class */
public class Plugins {
    private static final PluginRegistry registry = new PluginRegistry();

    public static StackTraceCleanerProvider getStackTraceCleanerProvider() {
        return registry.getStackTraceCleanerProvider();
    }

    public static MockMaker getMockMaker() {
        return registry.getMockMaker();
    }

    public static MemberAccessor getMemberAccessor() {
        return registry.getMemberAccessor();
    }

    public static InstantiatorProvider2 getInstantiatorProvider() {
        return registry.getInstantiatorProvider();
    }

    public static AnnotationEngine getAnnotationEngine() {
        return registry.getAnnotationEngine();
    }

    public static MockitoLogger getMockitoLogger() {
        return registry.getMockitoLogger();
    }

    public static List<MockResolver> getMockResolvers() {
        return registry.getMockResolvers();
    }

    public static MockitoPlugins getPlugins() {
        return new DefaultMockitoPlugins();
    }
}
