package org.powermock.configuration;

import org.powermock.configuration.support.ConfigurationFactoryImpl;
/* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/configuration/GlobalConfiguration.class */
public final class GlobalConfiguration {
    private static ConfigurationFactory configurationFactory = new ConfigurationFactoryImpl();
    private static final ThreadLocal<MockitoConfiguration> MOCKITO_CONFIGURATION = new ThreadLocal<>();
    private static final ThreadLocal<PowerMockConfiguration> POWER_MOCK_CONFIGURATION = new ThreadLocal<>();

    public static MockitoConfiguration mockitoConfiguration() {
        return new GlobalConfiguration().getMockitoConfiguration();
    }

    public static PowerMockConfiguration powerMockConfiguration() {
        return new GlobalConfiguration().getPowerMockConfiguration();
    }

    public static void clear() {
        configurationFactory = new ConfigurationFactoryImpl();
        MOCKITO_CONFIGURATION.remove();
        POWER_MOCK_CONFIGURATION.remove();
    }

    public static void setConfigurationFactory(ConfigurationFactory configurationFactory2) {
        configurationFactory = configurationFactory2;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private GlobalConfiguration() {
        if (MOCKITO_CONFIGURATION.get() == null) {
            MOCKITO_CONFIGURATION.set(createConfig(MockitoConfiguration.class));
        }
        if (POWER_MOCK_CONFIGURATION.get() == null) {
            POWER_MOCK_CONFIGURATION.set(createConfig(PowerMockConfiguration.class));
        }
    }

    private PowerMockConfiguration getPowerMockConfiguration() {
        return POWER_MOCK_CONFIGURATION.get();
    }

    private MockitoConfiguration getMockitoConfiguration() {
        return MOCKITO_CONFIGURATION.get();
    }

    private <T extends Configuration<T>> T createConfig(Class<T> configurationClass) {
        return (T) configurationFactory.create(configurationClass);
    }
}
