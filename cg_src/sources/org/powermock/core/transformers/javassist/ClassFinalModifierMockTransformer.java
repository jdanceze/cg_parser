package org.powermock.core.transformers.javassist;

import javassist.CtClass;
import javassist.Modifier;
import javassist.bytecode.AttributeInfo;
import javassist.bytecode.ClassFile;
import javassist.bytecode.InnerClassesAttribute;
import org.powermock.core.transformers.TransformStrategy;
/* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/core/transformers/javassist/ClassFinalModifierMockTransformer.class */
public class ClassFinalModifierMockTransformer extends AbstractJavaAssistMockTransformer {
    public ClassFinalModifierMockTransformer(TransformStrategy strategy) {
        super(strategy);
    }

    @Override // org.powermock.core.transformers.javassist.AbstractJavaAssistMockTransformer
    public CtClass transform(CtClass clazz) {
        if (clazz.isInterface()) {
            return clazz;
        }
        if (getStrategy() != TransformStrategy.INST_REDEFINE) {
            if (Modifier.isFinal(clazz.getModifiers())) {
                clazz.setModifiers(clazz.getModifiers() ^ 16);
            }
            ClassFile classFile = clazz.getClassFile2();
            AttributeInfo attribute = classFile.getAttribute("InnerClasses");
            if (attribute != null && (attribute instanceof InnerClassesAttribute)) {
                InnerClassesAttribute ica = (InnerClassesAttribute) attribute;
                String name = classFile.getName();
                int n = ica.tableLength();
                for (int i = 0; i < n; i++) {
                    if (name.equals(ica.innerClass(i))) {
                        int accessFlags = ica.accessFlags(i);
                        if (Modifier.isFinal(accessFlags)) {
                            ica.setAccessFlags(i, accessFlags ^ 16);
                        }
                    }
                }
            }
        }
        return clazz;
    }
}
