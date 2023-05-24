package org.powermock.configuration.support;

import java.util.Properties;
import org.powermock.configuration.Configuration;
import org.powermock.configuration.ConfigurationFactory;
import org.powermock.utils.Asserts;
/* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/configuration/support/ConfigurationFactoryImpl.class */
public class ConfigurationFactoryImpl implements ConfigurationFactory {
    private static final String USER_CONFIGURATION = "org/powermock/extensions/configuration.properties";
    private static final String DEFAULT_CONFIGURATION = "org/powermock/default.properties";
    private final String userConfigurationLocation;
    private final String defaultConfigurationLocation;

    public ConfigurationFactoryImpl() {
        this(USER_CONFIGURATION, DEFAULT_CONFIGURATION);
    }

    ConfigurationFactoryImpl(String userConfigurationLocation, String defaultConfigurationLocation) {
        this.userConfigurationLocation = userConfigurationLocation;
        this.defaultConfigurationLocation = defaultConfigurationLocation;
    }

    ConfigurationFactoryImpl(String defaultConfigurationLocation) {
        this(USER_CONFIGURATION, defaultConfigurationLocation);
    }

    @Override // org.powermock.configuration.ConfigurationFactory
    public <T extends Configuration<T>> T create(Class<T> configurationType) {
        Configuration readEnvironmentConfiguration = readEnvironmentConfiguration(configurationType);
        return (T) readDefault(configurationType).merge(readUserConfiguration(configurationType).merge(readEnvironmentConfiguration));
    }

    private <T extends Configuration<T>> T readEnvironmentConfiguration(Class<T> configurationType) {
        Properties properties = new Properties();
        properties.putAll(System.getenv());
        return (T) ConfigurationBuilder.createConfigurationFor(configurationType).fromProperties(properties);
    }

    private <T extends Configuration> T readDefault(Class<T> configurationType) {
        T configuration = (T) ConfigurationBuilder.createConfigurationFor(configurationType).fromFile(getDefaultConfigurationLocation());
        Asserts.internalAssertNotNull(configuration, "Default configuration is null.");
        return configuration;
    }

    private <T extends Configuration> T readUserConfiguration(Class<T> configurationType) {
        return (T) ConfigurationBuilder.createConfigurationFor(configurationType).fromFile(getUserConfigurationLocation());
    }

    private String getDefaultConfigurationLocation() {
        return this.defaultConfigurationLocation;
    }

    private String getUserConfigurationLocation() {
        return this.userConfigurationLocation;
    }
}
