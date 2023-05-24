package org.powermock.core.transformers.javassist;

import javassist.CtClass;
import org.powermock.core.transformers.ClassWrapper;
import org.powermock.core.transformers.MockTransformer;
import org.powermock.core.transformers.TransformStrategy;
/* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/core/transformers/javassist/AbstractJavaAssistMockTransformer.class */
public abstract class AbstractJavaAssistMockTransformer implements MockTransformer<CtClass> {
    private final TransformStrategy strategy;

    public abstract CtClass transform(CtClass ctClass) throws Exception;

    public AbstractJavaAssistMockTransformer(TransformStrategy strategy) {
        this.strategy = strategy;
    }

    @Override // org.powermock.core.transformers.MockTransformer
    public ClassWrapper<CtClass> transform(ClassWrapper<CtClass> clazz) throws Exception {
        CtClass classImpl;
        if ((clazz.unwrap() instanceof CtClass) && (classImpl = clazz.unwrap()) != null) {
            transform(classImpl);
        }
        return clazz;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public TransformStrategy getStrategy() {
        return this.strategy;
    }
}
