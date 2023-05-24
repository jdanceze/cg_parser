package org.mockito.internal.configuration;

import java.io.Serializable;
import org.mockito.configuration.AnnotationEngine;
import org.mockito.configuration.DefaultMockitoConfiguration;
import org.mockito.configuration.IMockitoConfiguration;
import org.mockito.internal.configuration.plugins.Plugins;
import org.mockito.stubbing.Answer;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/configuration/GlobalConfiguration.class */
public class GlobalConfiguration implements IMockitoConfiguration, Serializable {
    private static final long serialVersionUID = -2860353062105505938L;
    private static final ThreadLocal<IMockitoConfiguration> GLOBAL_CONFIGURATION = new ThreadLocal<>();

    IMockitoConfiguration getIt() {
        return GLOBAL_CONFIGURATION.get();
    }

    public GlobalConfiguration() {
        if (GLOBAL_CONFIGURATION.get() == null) {
            GLOBAL_CONFIGURATION.set(createConfig());
        }
    }

    private IMockitoConfiguration createConfig() {
        IMockitoConfiguration defaultConfiguration = new DefaultMockitoConfiguration();
        IMockitoConfiguration config = new ClassPathLoader().loadConfiguration();
        if (config != null) {
            return config;
        }
        return defaultConfiguration;
    }

    public static void validate() {
        new GlobalConfiguration();
    }

    @Override // org.mockito.configuration.IMockitoConfiguration
    public AnnotationEngine getAnnotationEngine() {
        return GLOBAL_CONFIGURATION.get().getAnnotationEngine();
    }

    public org.mockito.plugins.AnnotationEngine tryGetPluginAnnotationEngine() {
        IMockitoConfiguration configuration = GLOBAL_CONFIGURATION.get();
        if (configuration.getClass() == DefaultMockitoConfiguration.class) {
            return Plugins.getAnnotationEngine();
        }
        return configuration.getAnnotationEngine();
    }

    @Override // org.mockito.configuration.IMockitoConfiguration
    public boolean cleansStackTrace() {
        return GLOBAL_CONFIGURATION.get().cleansStackTrace();
    }

    @Override // org.mockito.configuration.IMockitoConfiguration
    public boolean enableClassCache() {
        return GLOBAL_CONFIGURATION.get().enableClassCache();
    }

    @Override // org.mockito.configuration.IMockitoConfiguration
    public Answer<Object> getDefaultAnswer() {
        return GLOBAL_CONFIGURATION.get().getDefaultAnswer();
    }
}
