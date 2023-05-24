package org.powermock.core.transformers;

import java.util.Collection;
/* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/core/transformers/MockTransformerChain.class */
public interface MockTransformerChain {

    /* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/core/transformers/MockTransformerChain$FilterPredicate.class */
    public interface FilterPredicate {
        boolean test(MockTransformer<?> mockTransformer);
    }

    <T> ClassWrapper<T> transform(ClassWrapper<T> classWrapper) throws Exception;

    Collection<MockTransformer> filter(FilterPredicate filterPredicate);
}
