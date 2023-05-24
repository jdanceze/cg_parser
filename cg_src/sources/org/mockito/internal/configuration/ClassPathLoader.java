package org.mockito.internal.configuration;

import org.mockito.configuration.IMockitoConfiguration;
import org.mockito.exceptions.misusing.MockitoConfigurationException;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/configuration/ClassPathLoader.class */
public class ClassPathLoader {
    public static final String MOCKITO_CONFIGURATION_CLASS_NAME = "org.mockito.configuration.MockitoConfiguration";

    public IMockitoConfiguration loadConfiguration() {
        try {
            Class<?> configClass = Class.forName(MOCKITO_CONFIGURATION_CLASS_NAME);
            try {
                return (IMockitoConfiguration) configClass.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
            } catch (ClassCastException e) {
                throw new MockitoConfigurationException("MockitoConfiguration class must implement " + IMockitoConfiguration.class.getName() + " interface.", e);
            } catch (Exception e2) {
                throw new MockitoConfigurationException("Unable to instantiate org.mockito.configuration.MockitoConfiguration class. Does it have a safe, no-arg constructor?", e2);
            }
        } catch (ClassNotFoundException e3) {
            return null;
        }
    }
}
