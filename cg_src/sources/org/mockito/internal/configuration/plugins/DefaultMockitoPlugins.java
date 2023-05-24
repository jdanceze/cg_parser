package org.mockito.internal.configuration.plugins;

import java.util.HashMap;
import java.util.Map;
import org.mockito.internal.creation.instance.InstantiatorProvider2Adapter;
import org.mockito.plugins.AnnotationEngine;
import org.mockito.plugins.InstantiatorProvider;
import org.mockito.plugins.InstantiatorProvider2;
import org.mockito.plugins.MemberAccessor;
import org.mockito.plugins.MockMaker;
import org.mockito.plugins.MockitoLogger;
import org.mockito.plugins.MockitoPlugins;
import org.mockito.plugins.PluginSwitch;
import org.mockito.plugins.StackTraceCleanerProvider;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/configuration/plugins/DefaultMockitoPlugins.class */
public class DefaultMockitoPlugins implements MockitoPlugins {
    private static final Map<String, String> DEFAULT_PLUGINS = new HashMap();
    static final String INLINE_ALIAS = "mock-maker-inline";
    static final String MODULE_ALIAS = "member-accessor-module";

    static {
        DEFAULT_PLUGINS.put(PluginSwitch.class.getName(), DefaultPluginSwitch.class.getName());
        DEFAULT_PLUGINS.put(MockMaker.class.getName(), "org.mockito.internal.creation.bytebuddy.ByteBuddyMockMaker");
        DEFAULT_PLUGINS.put(StackTraceCleanerProvider.class.getName(), "org.mockito.internal.exceptions.stacktrace.DefaultStackTraceCleanerProvider");
        DEFAULT_PLUGINS.put(InstantiatorProvider2.class.getName(), "org.mockito.internal.creation.instance.DefaultInstantiatorProvider");
        DEFAULT_PLUGINS.put(AnnotationEngine.class.getName(), "org.mockito.internal.configuration.InjectingAnnotationEngine");
        DEFAULT_PLUGINS.put(INLINE_ALIAS, "org.mockito.internal.creation.bytebuddy.InlineByteBuddyMockMaker");
        DEFAULT_PLUGINS.put(MockitoLogger.class.getName(), "org.mockito.internal.util.ConsoleMockitoLogger");
        DEFAULT_PLUGINS.put(MemberAccessor.class.getName(), "org.mockito.internal.util.reflection.ReflectionMemberAccessor");
        DEFAULT_PLUGINS.put(MODULE_ALIAS, "org.mockito.internal.util.reflection.ModuleMemberAccessor");
    }

    @Override // org.mockito.plugins.MockitoPlugins
    public <T> T getDefaultPlugin(Class<T> pluginType) {
        if (pluginType == InstantiatorProvider.class) {
            String className = DEFAULT_PLUGINS.get(InstantiatorProvider2.class.getName());
            return pluginType.cast(new InstantiatorProvider2Adapter((InstantiatorProvider2) create(InstantiatorProvider2.class, className)));
        }
        String className2 = DEFAULT_PLUGINS.get(pluginType.getName());
        return (T) create(pluginType, className2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String getDefaultPluginClass(String classOrAlias) {
        return DEFAULT_PLUGINS.get(classOrAlias);
    }

    private <T> T create(Class<T> pluginType, String className) {
        if (className == null) {
            throw new IllegalStateException("No default implementation for requested Mockito plugin type: " + pluginType.getName() + "\nIs this a valid Mockito plugin type? If yes, please report this problem to Mockito team.\nOtherwise, please check if you are passing valid plugin type.\nExamples of valid plugin types: MockMaker, StackTraceCleanerProvider.");
        }
        try {
            return pluginType.cast(Class.forName(className).getDeclaredConstructor(new Class[0]).newInstance(new Object[0]));
        } catch (Exception e) {
            throw new IllegalStateException("Internal problem occurred, please report it. Mockito is unable to load the default implementation of class that is a part of Mockito distribution. Failed to load " + pluginType, e);
        }
    }

    @Override // org.mockito.plugins.MockitoPlugins
    public MockMaker getInlineMockMaker() {
        return (MockMaker) create(MockMaker.class, DEFAULT_PLUGINS.get(INLINE_ALIAS));
    }
}
