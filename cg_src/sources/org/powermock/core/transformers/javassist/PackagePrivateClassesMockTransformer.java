package org.powermock.core.transformers.javassist;

import javassist.CtClass;
import javassist.Modifier;
import javassist.NotFoundException;
import org.powermock.core.transformers.TransformStrategy;
/* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/core/transformers/javassist/PackagePrivateClassesMockTransformer.class */
public class PackagePrivateClassesMockTransformer extends AbstractJavaAssistMockTransformer {
    public PackagePrivateClassesMockTransformer(TransformStrategy strategy) {
        super(strategy);
    }

    @Override // org.powermock.core.transformers.javassist.AbstractJavaAssistMockTransformer
    public CtClass transform(CtClass clazz) {
        String name = clazz.getName();
        if (getStrategy() != TransformStrategy.INST_REDEFINE) {
            transform(clazz, name);
        }
        return clazz;
    }

    private static void transform(CtClass clazz, String name) {
        try {
            int modifiers = clazz.getModifiers();
            if (Modifier.isPackage(modifiers) && isNotSystemClass(name) && (!clazz.isInterface() || clazz.getDeclaringClass() == null)) {
                clazz.setModifiers(Modifier.setPublic(modifiers));
            }
        } catch (NotFoundException e) {
        }
    }

    private static boolean isNotSystemClass(String name) {
        return !name.startsWith("java.");
    }
}
