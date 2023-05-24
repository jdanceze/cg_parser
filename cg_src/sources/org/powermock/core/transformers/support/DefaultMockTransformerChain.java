package org.powermock.core.transformers.support;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.powermock.core.transformers.ClassWrapper;
import org.powermock.core.transformers.MockTransformer;
import org.powermock.core.transformers.MockTransformerChain;
/* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/core/transformers/support/DefaultMockTransformerChain.class */
public class DefaultMockTransformerChain implements MockTransformerChain {
    private final List<MockTransformer> transformers;

    private DefaultMockTransformerChain(List<MockTransformer> transformers) {
        this.transformers = Collections.unmodifiableList(transformers);
    }

    @Override // org.powermock.core.transformers.MockTransformerChain
    public <T> ClassWrapper<T> transform(ClassWrapper<T> clazz) throws Exception {
        ClassWrapper<T> classWrapper = clazz;
        for (MockTransformer transformer : this.transformers) {
            classWrapper = transformer.transform(classWrapper);
        }
        return classWrapper;
    }

    @Override // org.powermock.core.transformers.MockTransformerChain
    public Collection<MockTransformer> filter(MockTransformerChain.FilterPredicate predicate) {
        ArrayList<MockTransformer> filtered = new ArrayList<>();
        for (MockTransformer transformer : this.transformers) {
            if (predicate.test(transformer)) {
                filtered.add(transformer);
            }
        }
        return filtered;
    }

    public String toString() {
        return "MockTransformerChain{transformers=" + this.transformers + '}';
    }

    public static MockTransformerChainBuilder newBuilder() {
        return new MockTransformerChainBuilder();
    }

    /* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/core/transformers/support/DefaultMockTransformerChain$MockTransformerChainBuilder.class */
    public static class MockTransformerChainBuilder {
        private final List<MockTransformer> transformers;

        private MockTransformerChainBuilder() {
            this.transformers = new ArrayList();
        }

        public MockTransformerChainBuilder append(MockTransformer transformer) {
            this.transformers.add(transformer);
            return this;
        }

        public MockTransformerChainBuilder append(List<MockTransformer> mockTransformerChain) {
            this.transformers.addAll(mockTransformerChain);
            return this;
        }

        public MockTransformerChain build() {
            return new DefaultMockTransformerChain(this.transformers);
        }
    }
}
