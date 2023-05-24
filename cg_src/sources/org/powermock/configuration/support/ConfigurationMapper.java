package org.powermock.configuration.support;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.Properties;
import org.powermock.PowerMockInternalException;
import org.powermock.configuration.Configuration;
import org.powermock.configuration.ConfigurationType;
/* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/configuration/support/ConfigurationMapper.class */
class ConfigurationMapper<T extends Configuration> {
    private final Class<T> configurationClass;
    private final T configuration;
    private final ValueAliases aliases;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ConfigurationMapper(Class<T> configurationClass, T configuration, ValueAliases aliases) {
        this.configurationClass = configurationClass;
        this.configuration = configuration;
        this.aliases = aliases;
    }

    public void map(Properties properties) {
        try {
            BeanInfo info = Introspector.getBeanInfo(this.configurationClass, Object.class);
            PropertyDescriptor[] propertyDescriptors = info.getPropertyDescriptors();
            for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
                if (propertyDescriptor.getWriteMethod() != null) {
                    mapProperty(propertyDescriptor, properties);
                }
            }
        } catch (Exception e) {
            throw new PowerMockInternalException(e);
        }
    }

    private void mapProperty(PropertyDescriptor propertyDescriptor, Properties properties) {
        ConfigurationKey key = new ConfigurationKey(ConfigurationType.forClass(this.configurationClass), propertyDescriptor.getName());
        String value = this.aliases.findValue((String) properties.get(key.toString()));
        PropertyWriter.forProperty(propertyDescriptor).writeProperty(propertyDescriptor, this.configuration, value);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/configuration/support/ConfigurationMapper$ConfigurationKey.class */
    public static class ConfigurationKey {
        private final ConfigurationType configurationType;
        private final String name;

        private ConfigurationKey(ConfigurationType configurationType, String name) {
            this.configurationType = configurationType;
            this.name = name;
        }

        public String toString() {
            StringBuilder key = new StringBuilder();
            if (this.configurationType.getPrefix() != null) {
                key.append(this.configurationType.getPrefix());
                key.append(".");
            }
            for (int i = 0; i < this.name.length(); i++) {
                char c = this.name.charAt(i);
                if (Character.isUpperCase(c)) {
                    key.append('-');
                    key.append(Character.toLowerCase(c));
                } else {
                    key.append(c);
                }
            }
            return key.toString();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/configuration/support/ConfigurationMapper$PropertyWriter.class */
    public enum PropertyWriter {
        ArrayWriter { // from class: org.powermock.configuration.support.ConfigurationMapper.PropertyWriter.1
            @Override // org.powermock.configuration.support.ConfigurationMapper.PropertyWriter
            public void writeProperty(PropertyDescriptor pd, Object target, String value) {
                if (value != null) {
                    try {
                        String[] array = value.split(",");
                        pd.getWriteMethod().invoke(target, array);
                    } catch (Exception e) {
                        throw new PowerMockInternalException(e);
                    }
                }
            }
        },
        StringWriter { // from class: org.powermock.configuration.support.ConfigurationMapper.PropertyWriter.2
            @Override // org.powermock.configuration.support.ConfigurationMapper.PropertyWriter
            public void writeProperty(PropertyDescriptor pd, Object target, String value) {
                if (value != null) {
                    try {
                        pd.getWriteMethod().invoke(target, value);
                    } catch (Exception e) {
                        throw new PowerMockInternalException(e);
                    }
                }
            }
        },
        EnumWriter { // from class: org.powermock.configuration.support.ConfigurationMapper.PropertyWriter.3
            @Override // org.powermock.configuration.support.ConfigurationMapper.PropertyWriter
            public void writeProperty(PropertyDescriptor pd, Object target, String value) {
                if (value != null) {
                    try {
                        Class<Enum<?>> enumClass = pd.getPropertyType();
                        Enum<?>[] constants = enumClass.getEnumConstants();
                        for (Enum<?> constant : constants) {
                            if (value.equals(constant.name())) {
                                pd.getWriteMethod().invoke(target, constant);
                                return;
                            }
                        }
                        throw new PowerMockInternalException(String.format("Find unknown enum constant `%s` for type `%s` during reading configuration.", value, enumClass));
                    } catch (Exception e) {
                        throw new PowerMockInternalException(e);
                    }
                }
            }
        };

        abstract void writeProperty(PropertyDescriptor propertyDescriptor, Object obj, String str);

        /* JADX INFO: Access modifiers changed from: private */
        public static PropertyWriter forProperty(PropertyDescriptor pd) {
            if (String[].class.isAssignableFrom(pd.getPropertyType())) {
                return ArrayWriter;
            }
            if (Enum.class.isAssignableFrom(pd.getPropertyType())) {
                return EnumWriter;
            }
            return StringWriter;
        }
    }
}
