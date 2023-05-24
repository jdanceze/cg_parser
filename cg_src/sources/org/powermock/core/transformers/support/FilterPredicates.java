package org.powermock.core.transformers.support;

import org.powermock.core.transformers.MockTransformer;
import org.powermock.core.transformers.MockTransformerChain;
/* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/core/transformers/support/FilterPredicates.class */
public class FilterPredicates {
    public static MockTransformerChain.FilterPredicate isInstanceOf(final Class<?> klass) {
        return new MockTransformerChain.FilterPredicate() { // from class: org.powermock.core.transformers.support.FilterPredicates.1
            @Override // org.powermock.core.transformers.MockTransformerChain.FilterPredicate
            public boolean test(MockTransformer<?> mockTransformer) {
                return klass.isAssignableFrom(mockTransformer.getClass());
            }
        };
    }
}
