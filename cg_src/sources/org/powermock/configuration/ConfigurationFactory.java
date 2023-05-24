package org.powermock.configuration;
/* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/configuration/ConfigurationFactory.class */
public interface ConfigurationFactory {
    <T extends Configuration<T>> T create(Class<T> cls);
}
