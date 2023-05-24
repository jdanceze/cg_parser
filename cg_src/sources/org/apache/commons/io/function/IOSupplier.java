package org.apache.commons.io.function;

import java.io.IOException;
@FunctionalInterface
/* loaded from: gencallgraphv3.jar:commons-io-2.7.jar:org/apache/commons/io/function/IOSupplier.class */
public interface IOSupplier<T> {
    T get() throws IOException;
}
