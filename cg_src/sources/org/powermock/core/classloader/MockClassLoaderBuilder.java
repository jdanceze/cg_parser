package org.powermock.core.classloader;

import java.util.ArrayList;
import java.util.List;
import org.powermock.core.classloader.annotations.UseClassPathAdjuster;
import org.powermock.core.transformers.MockTransformer;
import org.powermock.core.transformers.MockTransformerChain;
import org.powermock.core.transformers.MockTransformerChainFactory;
import org.powermock.core.transformers.TestClassAwareTransformer;
import org.powermock.core.transformers.support.FilterPredicates;
import org.powermock.utils.ArrayUtil;
import org.powermock.utils.Asserts;
/* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/core/classloader/MockClassLoaderBuilder.class */
public class MockClassLoaderBuilder {
    private final MockTransformerChainFactory transformerChainFactory;
    private final List<MockTransformer> extraMockTransformers = new ArrayList();
    private final ByteCodeFramework byteCodeFramework;
    private String[] packagesToIgnore;
    private String[] classesToModify;
    private UseClassPathAdjuster useClassPathAdjuster;
    private Class<?> testClass;

    public static MockClassLoaderBuilder create(ByteCodeFramework byteCodeFramework) {
        return new MockClassLoaderBuilder(byteCodeFramework);
    }

    private MockClassLoaderBuilder(ByteCodeFramework byteCodeFramework) {
        this.byteCodeFramework = byteCodeFramework;
        this.transformerChainFactory = byteCodeFramework.createTransformerChainFactory();
    }

    public MockClassLoader build() {
        Asserts.internalAssertNotNull(this.testClass, "Test class is null during building classloader. ");
        MockClassLoaderConfiguration configuration = new MockClassLoaderConfiguration(this.classesToModify, this.packagesToIgnore);
        MockClassLoader classLoader = this.byteCodeFramework.createClassloader(configuration, this.useClassPathAdjuster);
        classLoader.setMockTransformerChain(createTransformerChain());
        return classLoader;
    }

    private MockTransformerChain createTransformerChain() {
        MockTransformerChain mockTransformerChain = this.transformerChainFactory.createDefaultChain(this.extraMockTransformers);
        Iterable<MockTransformer> testAwareTransformer = mockTransformerChain.filter(FilterPredicates.isInstanceOf(TestClassAwareTransformer.class));
        for (MockTransformer transformer : testAwareTransformer) {
            ((TestClassAwareTransformer) transformer).setTestClass(this.testClass);
        }
        return mockTransformerChain;
    }

    public MockClassLoaderBuilder addIgnorePackage(String[] packagesToIgnore) {
        this.packagesToIgnore = (String[]) ArrayUtil.addAll(this.packagesToIgnore, packagesToIgnore);
        return this;
    }

    public MockClassLoaderBuilder addClassesToModify(String[] classesToModify) {
        this.classesToModify = (String[]) ArrayUtil.addAll(this.classesToModify, classesToModify);
        return this;
    }

    public MockClassLoaderBuilder addExtraMockTransformers(MockTransformer... mockTransformers) {
        if (mockTransformers != null) {
            for (MockTransformer mockTransformer : mockTransformers) {
                if (mockTransformer != null) {
                    this.extraMockTransformers.add(mockTransformer);
                }
            }
        }
        return this;
    }

    public MockClassLoaderBuilder addClassPathAdjuster(UseClassPathAdjuster useClassPathAdjuster) {
        this.useClassPathAdjuster = useClassPathAdjuster;
        return this;
    }

    public MockClassLoaderBuilder forTestClass(Class<?> testClass) {
        this.testClass = testClass;
        return this;
    }
}
