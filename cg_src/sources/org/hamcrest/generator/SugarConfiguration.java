package org.hamcrest.generator;
/* loaded from: gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/generator/SugarConfiguration.class */
public interface SugarConfiguration {
    void addWriter(FactoryWriter factoryWriter);

    void addFactoryMethod(FactoryMethod factoryMethod);

    void addFactoryMethods(Iterable<FactoryMethod> iterable);
}
