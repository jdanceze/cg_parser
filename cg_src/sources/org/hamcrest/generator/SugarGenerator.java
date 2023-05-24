package org.hamcrest.generator;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/* loaded from: gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/generator/SugarGenerator.class */
public class SugarGenerator implements Closeable, SugarConfiguration {
    private final List<FactoryWriter> factoryWriters = new ArrayList();
    private final List<FactoryMethod> factoryMethods = new ArrayList();

    @Override // org.hamcrest.generator.SugarConfiguration
    public void addWriter(FactoryWriter factoryWriter) {
        this.factoryWriters.add(factoryWriter);
    }

    @Override // org.hamcrest.generator.SugarConfiguration
    public void addFactoryMethod(FactoryMethod method) {
        this.factoryMethods.add(method);
    }

    @Override // org.hamcrest.generator.SugarConfiguration
    public void addFactoryMethods(Iterable<FactoryMethod> methods) {
        for (FactoryMethod method : methods) {
            addFactoryMethod(method);
        }
    }

    public void generate() throws IOException {
        for (FactoryWriter factoryWriter : this.factoryWriters) {
            factoryWriter.writeHeader();
            for (FactoryMethod factoryMethod : this.factoryMethods) {
                factoryWriter.writeMethod(factoryMethod.getName(), factoryMethod);
            }
            factoryWriter.writeFooter();
            factoryWriter.flush();
        }
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        for (FactoryWriter factoryWriter : this.factoryWriters) {
            factoryWriter.close();
        }
    }
}
