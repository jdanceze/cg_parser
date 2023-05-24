package org.powermock.configuration.support;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.powermock.PowerMockInternalException;
import org.powermock.configuration.Configuration;
/* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/configuration/support/ConfigurationBuilder.class */
class ConfigurationBuilder<T extends Configuration<?>> {
    private final Map<String, String> alias = new HashMap();
    private final Class<T> configurationType;

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <C extends Configuration<?>> ConfigurationBuilder<C> createConfigurationFor(Class<C> configurationType) {
        return new ConfigurationBuilder<>(configurationType);
    }

    private ConfigurationBuilder(Class<T> configurationType) {
        this.configurationType = configurationType;
    }

    ConfigurationBuilder<T> withValueAlias(String alias, String value) {
        this.alias.put(alias, value);
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public T fromFile(String configurationLocation) {
        Properties properties = new PropertiesLoader().load(configurationLocation);
        return fromProperties(properties);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public T fromProperties(Properties properties) {
        ConfigurationCreator configurationCreator = new ConfigurationCreator(this.alias);
        return (T) configurationCreator.create(this.configurationType, properties);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/configuration/support/ConfigurationBuilder$ConfigurationCreator.class */
    public static class ConfigurationCreator {
        private final ValueAliases alias;

        private ConfigurationCreator(Map<String, String> alias) {
            this.alias = new ValueAliases(alias);
        }

        public <T extends Configuration> T create(Class<T> configurationClass, Properties properties) {
            try {
                T configuration = configurationClass.newInstance();
                if (properties != null) {
                    mapConfiguration(configurationClass, configuration, properties);
                }
                return configuration;
            } catch (Exception e) {
                throw new PowerMockInternalException(e);
            }
        }

        private <T extends Configuration> void mapConfiguration(Class<T> configurationClass, T configuration, Properties properties) {
            new ConfigurationMapper(configurationClass, configuration, this.alias).map(properties);
        }
    }
}
