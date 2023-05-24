package org.powermock.core.transformers.javassist;

import javassist.CtClass;
import javassist.CtField;
import javassist.Modifier;
import org.powermock.core.transformers.TransformStrategy;
/* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/core/transformers/javassist/StaticFinalFieldsMockTransformer.class */
public class StaticFinalFieldsMockTransformer extends AbstractJavaAssistMockTransformer {
    public StaticFinalFieldsMockTransformer(TransformStrategy strategy) {
        super(strategy);
    }

    @Override // org.powermock.core.transformers.javassist.AbstractJavaAssistMockTransformer
    public CtClass transform(CtClass clazz) {
        CtField[] declaredFields;
        if (clazz.isInterface()) {
            return clazz;
        }
        if (getStrategy() != TransformStrategy.INST_REDEFINE) {
            for (CtField f : clazz.getDeclaredFields()) {
                int modifiers = f.getModifiers();
                if (Modifier.isFinal(modifiers) && Modifier.isStatic(modifiers)) {
                    f.setModifiers(modifiers ^ 16);
                }
            }
        }
        return clazz;
    }
}
