package org.powermock.core.transformers.javassist.testclass;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import javassist.CtMethod;
import org.powermock.core.transformers.MethodSignatureWriter;
/* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/core/transformers/javassist/testclass/FromAllMethodsExceptJavaAssistTestClassTransformer.class */
public class FromAllMethodsExceptJavaAssistTestClassTransformer extends JavaAssistTestClassTransformer {
    private final String targetMethodSignature;

    public FromAllMethodsExceptJavaAssistTestClassTransformer(Class<?> testClass, Class<? extends Annotation> testMethodAnnotation, MethodSignatureWriter<CtMethod> signatureWriter, Method methodToExclude) {
        super(testClass, testMethodAnnotation, signatureWriter);
        this.targetMethodSignature = signatureWriter.signatureForReflection(methodToExclude);
    }

    @Override // org.powermock.core.transformers.javassist.testclass.JavaAssistTestClassTransformer
    protected boolean mustHaveTestAnnotationRemoved(CtMethod method) throws Exception {
        return !signatureOf((FromAllMethodsExceptJavaAssistTestClassTransformer) method).equals(this.targetMethodSignature);
    }
}
