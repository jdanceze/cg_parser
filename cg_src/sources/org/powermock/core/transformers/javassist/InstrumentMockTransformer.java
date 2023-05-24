package org.powermock.core.transformers.javassist;

import javassist.CannotCompileException;
import javassist.CtClass;
import org.powermock.core.MockGateway;
import org.powermock.core.transformers.TransformStrategy;
import org.powermock.core.transformers.javassist.support.PowerMockExpressionEditor;
/* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/core/transformers/javassist/InstrumentMockTransformer.class */
public class InstrumentMockTransformer extends AbstractJavaAssistMockTransformer {
    private Class<?> mockGetawayClass;

    public InstrumentMockTransformer(TransformStrategy strategy) {
        super(strategy);
        this.mockGetawayClass = MockGateway.class;
    }

    @Override // org.powermock.core.transformers.javassist.AbstractJavaAssistMockTransformer
    public CtClass transform(CtClass clazz) throws CannotCompileException {
        clazz.instrument(new PowerMockExpressionEditor(getStrategy(), clazz, this.mockGetawayClass));
        return clazz;
    }
}
