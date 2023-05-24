package org.powermock.configuration.support;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
/* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/configuration/support/PropertiesFinder.class */
class PropertiesFinder {
    private final ClassLoader classLoader;

    /* JADX INFO: Access modifiers changed from: package-private */
    public PropertiesFinder(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public List<ConfigurationSource> find(String configurationFile) throws IOException, URISyntaxException {
        List<ConfigurationSource> configurations = new ArrayList<>();
        Enumeration<URL> resources = this.classLoader.getResources(configurationFile);
        while (resources.hasMoreElements()) {
            URL candidate = resources.nextElement();
            configurations.add(new ConfigurationSource(candidate.getFile(), candidate.openStream()));
        }
        return configurations;
    }

    /* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/configuration/support/PropertiesFinder$ConfigurationSource.class */
    static class ConfigurationSource {
        private final String location;
        private final InputStream inputStream;

        ConfigurationSource(String location, InputStream inputStream) {
            this.location = location;
            this.inputStream = inputStream;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public InputStream inputStream() {
            return this.inputStream;
        }

        String getLocation() {
            return this.location;
        }

        public String toString() {
            return "ConfigurationSource{location='" + this.location + '}';
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            ConfigurationSource that = (ConfigurationSource) o;
            return getLocation() != null ? getLocation().equals(that.getLocation()) : that.getLocation() == null;
        }

        public int hashCode() {
            if (getLocation() != null) {
                return getLocation().hashCode();
            }
            return 0;
        }
    }
}
