package org.hamcrest.generator;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
/* loaded from: gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/generator/FactoryWriter.class */
public interface FactoryWriter extends Closeable, Flushable {
    void writeHeader() throws IOException;

    void writeMethod(String str, FactoryMethod factoryMethod) throws IOException;

    void writeFooter() throws IOException;
}
