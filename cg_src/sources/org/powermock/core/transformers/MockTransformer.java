package org.powermock.core.transformers;
/* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/core/transformers/MockTransformer.class */
public interface MockTransformer<T> {
    ClassWrapper<T> transform(ClassWrapper<T> classWrapper) throws Exception;
}
