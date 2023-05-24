package org.powermock.core.transformers;

import java.util.List;
/* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/core/transformers/MockTransformerChainFactory.class */
public interface MockTransformerChainFactory {
    MockTransformerChain createDefaultChain();

    MockTransformerChain createDefaultChain(TransformStrategy transformStrategy);

    MockTransformerChain createDefaultChain(List<MockTransformer> list);

    MockTransformerChain createTestClassChain(MockTransformer mockTransformer);
}
