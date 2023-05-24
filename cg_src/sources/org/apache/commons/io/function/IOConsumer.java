package org.apache.commons.io.function;

import java.io.IOException;
import java.util.Objects;
@FunctionalInterface
/* loaded from: gencallgraphv3.jar:commons-io-2.7.jar:org/apache/commons/io/function/IOConsumer.class */
public interface IOConsumer<T> {
    void accept(T t) throws IOException;

    default IOConsumer<T> andThen(IOConsumer<? super T> after) {
        Objects.requireNonNull(after);
        return t -> {
            accept(after);
            after.accept(after);
        };
    }
}
