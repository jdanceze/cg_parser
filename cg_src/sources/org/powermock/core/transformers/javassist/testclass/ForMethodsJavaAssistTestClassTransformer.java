package org.powermock.core.transformers.javassist.testclass;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import javassist.CtMethod;
import org.powermock.core.transformers.MethodSignatureWriter;
/* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/core/transformers/javassist/testclass/ForMethodsJavaAssistTestClassTransformer.class */
public class ForMethodsJavaAssistTestClassTransformer extends JavaAssistTestClassTransformer {
    private final Collection<Method> testMethodsThatRunOnOtherClassLoaders;
    private Collection<String> methodsThatRunOnOtherClassLoaders;

    public ForMethodsJavaAssistTestClassTransformer(Class<?> testClass, Class<? extends Annotation> testMethodAnnotation, MethodSignatureWriter<CtMethod> methodSignatureWriter, Collection<Method> testMethodsThatRunOnOtherClassLoaders) {
        super(testClass, testMethodAnnotation, methodSignatureWriter);
        this.testMethodsThatRunOnOtherClassLoaders = testMethodsThatRunOnOtherClassLoaders;
    }

    @Override // org.powermock.core.transformers.javassist.testclass.JavaAssistTestClassTransformer
    protected boolean mustHaveTestAnnotationRemoved(CtMethod method) throws Exception {
        if (null == this.methodsThatRunOnOtherClassLoaders) {
            this.methodsThatRunOnOtherClassLoaders = new HashSet();
            for (Method m : this.testMethodsThatRunOnOtherClassLoaders) {
                this.methodsThatRunOnOtherClassLoaders.add(signatureOf(m));
            }
            this.testMethodsThatRunOnOtherClassLoaders.clear();
        }
        return this.methodsThatRunOnOtherClassLoaders.contains(signatureOf((ForMethodsJavaAssistTestClassTransformer) method));
    }
}
