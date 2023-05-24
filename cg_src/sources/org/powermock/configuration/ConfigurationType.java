package org.powermock.configuration;
/* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/configuration/ConfigurationType.class */
public enum ConfigurationType {
    Mockito("mockito", MockitoConfiguration.class),
    PowerMock("powermock", PowerMockConfiguration.class);
    
    private final String prefix;
    private final Class<? extends Configuration> configurationClass;

    ConfigurationType(String prefix, Class cls) {
        this.prefix = prefix;
        this.configurationClass = cls;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public static <T extends Configuration> ConfigurationType forClass(Class<T> configurationClass) {
        ConfigurationType[] values;
        for (ConfigurationType configurationType : values()) {
            if (configurationType.configurationClass.isAssignableFrom(configurationClass)) {
                return configurationType;
            }
        }
        return null;
    }
}
