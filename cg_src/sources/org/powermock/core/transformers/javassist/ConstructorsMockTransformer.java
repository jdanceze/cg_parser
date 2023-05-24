package org.powermock.core.transformers.javassist;

import javassist.CtClass;
import javassist.CtConstructor;
import javassist.Modifier;
import javassist.NotFoundException;
import org.powermock.core.transformers.TransformStrategy;
/* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/core/transformers/javassist/ConstructorsMockTransformer.class */
public class ConstructorsMockTransformer extends AbstractJavaAssistMockTransformer {
    public ConstructorsMockTransformer(TransformStrategy strategy) {
        super(strategy);
    }

    @Override // org.powermock.core.transformers.javassist.AbstractJavaAssistMockTransformer
    public CtClass transform(CtClass clazz) {
        if (clazz.isInterface()) {
            return clazz;
        }
        if (getStrategy() == TransformStrategy.CLASSLOADER) {
            transform(new CtClass[]{clazz});
            try {
                CtClass[] nestedClasses = clazz.getDeclaredClasses();
                transform(nestedClasses);
            } catch (NotFoundException e) {
            }
        }
        return clazz;
    }

    private static void transform(CtClass[] clazzArray) {
        CtConstructor[] declaredConstructors;
        for (CtClass nestedClazz : clazzArray) {
            for (CtConstructor c : nestedClazz.getDeclaredConstructors()) {
                int modifiers = c.getModifiers();
                if (!Modifier.isPublic(modifiers)) {
                    c.setModifiers(Modifier.setPublic(modifiers));
                }
            }
        }
    }
}
