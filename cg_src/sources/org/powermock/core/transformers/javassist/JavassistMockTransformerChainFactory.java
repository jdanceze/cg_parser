package org.powermock.core.transformers.javassist;

import java.util.List;
import org.powermock.core.transformers.MockTransformer;
import org.powermock.core.transformers.MockTransformerChain;
import org.powermock.core.transformers.MockTransformerChainFactory;
import org.powermock.core.transformers.TransformStrategy;
import org.powermock.core.transformers.support.DefaultMockTransformerChain;
/* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/core/transformers/javassist/JavassistMockTransformerChainFactory.class */
public class JavassistMockTransformerChainFactory implements MockTransformerChainFactory {
    private static final TransformStrategy DEFAULT = TransformStrategy.CLASSLOADER;

    @Override // org.powermock.core.transformers.MockTransformerChainFactory
    public MockTransformerChain createDefaultChain() {
        return createDefaultChain(DEFAULT);
    }

    @Override // org.powermock.core.transformers.MockTransformerChainFactory
    public MockTransformerChain createDefaultChain(TransformStrategy transformStrategy) {
        return createDefaultChainBuilder(transformStrategy).build();
    }

    @Override // org.powermock.core.transformers.MockTransformerChainFactory
    public MockTransformerChain createDefaultChain(List<MockTransformer> extraMockTransformers) {
        return createDefaultChainBuilder(DEFAULT).append(extraMockTransformers).build();
    }

    @Override // org.powermock.core.transformers.MockTransformerChainFactory
    public MockTransformerChain createTestClassChain(MockTransformer testClassTransformer) {
        return createDefaultChainBuilder(DEFAULT).append(testClassTransformer).build();
    }

    private DefaultMockTransformerChain.MockTransformerChainBuilder createDefaultChainBuilder(TransformStrategy transformStrategy) {
        return DefaultMockTransformerChain.newBuilder().append(new ClassFinalModifierMockTransformer(transformStrategy)).append(new ConstructorsMockTransformer(transformStrategy)).append(new InstrumentMockTransformer(transformStrategy)).append(new PackagePrivateClassesMockTransformer(transformStrategy)).append(new StaticFinalFieldsMockTransformer(transformStrategy)).append(new StaticFinalNativeMethodMockTransformer(transformStrategy)).append(new SuppressStaticInitializerMockTransformer(transformStrategy)).append(new MethodSizeMockTransformer(transformStrategy));
    }
}
