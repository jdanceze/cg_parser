package org.powermock.configuration.support;

import java.io.IOException;
import java.util.List;
import java.util.Properties;
import org.powermock.configuration.support.PropertiesFinder;
import org.powermock.utils.StringJoiner;
/* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/configuration/support/PropertiesLoader.class */
class PropertiesLoader {
    /* JADX INFO: Access modifiers changed from: package-private */
    public Properties load(String propertiesFile) {
        if (propertiesFile == null) {
            return null;
        }
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        try {
            List<PropertiesFinder.ConfigurationSource> configurations = new PropertiesFinder(classLoader).find(propertiesFile);
            return loadProperties(configurations, propertiesFile);
        } catch (Exception e) {
            return null;
        }
    }

    private Properties loadProperties(List<PropertiesFinder.ConfigurationSource> configurations, String propertiesFile) throws IOException {
        if (configurations.size() == 0) {
            return null;
        }
        if (configurations.size() > 1) {
            printWarning(configurations, propertiesFile);
        }
        return loadPropertiesFromFile(configurations.get(0));
    }

    private void printWarning(List<PropertiesFinder.ConfigurationSource> configurations, String propertiesFile) {
        System.err.printf("Properties file %s is found in %s places: %s. Which one will be used is undefined. Please, remove duplicated configuration file (or second PowerMock jar file) from class path to have stable tests.", propertiesFile, Integer.valueOf(configurations.size()), StringJoiner.join(configurations));
    }

    private Properties loadPropertiesFromFile(PropertiesFinder.ConfigurationSource configurationSource) throws IOException {
        Properties properties = new Properties();
        properties.load(configurationSource.inputStream());
        return properties;
    }
}
