package org.powermock.core.transformers.javassist;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtConstructor;
import org.powermock.core.MockGateway;
import org.powermock.core.transformers.TransformStrategy;
/* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/core/transformers/javassist/SuppressStaticInitializerMockTransformer.class */
public class SuppressStaticInitializerMockTransformer extends AbstractJavaAssistMockTransformer {
    public SuppressStaticInitializerMockTransformer(TransformStrategy strategy) {
        super(strategy);
    }

    @Override // org.powermock.core.transformers.javassist.AbstractJavaAssistMockTransformer
    public CtClass transform(CtClass clazz) throws CannotCompileException {
        if (getStrategy() == TransformStrategy.CLASSLOADER && MockGateway.staticConstructorCall(clazz.getName()) != MockGateway.PROCEED) {
            CtConstructor classInitializer = clazz.makeClassInitializer();
            classInitializer.setBody("{}");
        }
        return clazz;
    }
}
