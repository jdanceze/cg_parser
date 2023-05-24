package org.powermock.core.transformers;
/* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/core/transformers/ClassWrapper.class */
public interface ClassWrapper<T> {
    boolean isInterface();

    T unwrap();

    ClassWrapper<T> wrap(T t);
}
