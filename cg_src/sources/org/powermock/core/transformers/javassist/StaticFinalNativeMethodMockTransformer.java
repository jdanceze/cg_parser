package org.powermock.core.transformers.javassist;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import org.powermock.core.transformers.TransformStrategy;
/* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/core/transformers/javassist/StaticFinalNativeMethodMockTransformer.class */
public class StaticFinalNativeMethodMockTransformer extends MethodMockTransformer {
    public StaticFinalNativeMethodMockTransformer(TransformStrategy strategy) {
        super(strategy);
    }

    @Override // org.powermock.core.transformers.javassist.AbstractJavaAssistMockTransformer
    public CtClass transform(CtClass clazz) throws NotFoundException, CannotCompileException {
        CtMethod[] declaredMethods;
        for (CtMethod m : clazz.getDeclaredMethods()) {
            modifyMethod(m);
        }
        return clazz;
    }
}
