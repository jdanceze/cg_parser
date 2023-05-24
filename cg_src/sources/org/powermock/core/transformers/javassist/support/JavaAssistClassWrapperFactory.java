package org.powermock.core.transformers.javassist.support;

import javassist.CtClass;
import org.powermock.core.transformers.ClassWrapper;
import org.powermock.core.transformers.ClassWrapperFactory;
/* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/core/transformers/javassist/support/JavaAssistClassWrapperFactory.class */
public class JavaAssistClassWrapperFactory implements ClassWrapperFactory<CtClass> {
    @Override // org.powermock.core.transformers.ClassWrapperFactory
    public ClassWrapper<CtClass> wrap(CtClass ctClass) {
        return new JavaAssistClassWrapper(ctClass);
    }

    /* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/core/transformers/javassist/support/JavaAssistClassWrapperFactory$JavaAssistClassWrapper.class */
    public static class JavaAssistClassWrapper implements ClassWrapper<CtClass> {
        private final CtClass ctClass;

        private JavaAssistClassWrapper(CtClass ctClass) {
            this.ctClass = ctClass;
        }

        @Override // org.powermock.core.transformers.ClassWrapper
        public boolean isInterface() {
            return this.ctClass.isInterface();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // org.powermock.core.transformers.ClassWrapper
        public CtClass unwrap() {
            return this.ctClass;
        }

        @Override // org.powermock.core.transformers.ClassWrapper
        public ClassWrapper<CtClass> wrap(CtClass original) {
            return new JavaAssistClassWrapper(original);
        }
    }
}
