package org.powermock.core.transformers;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
/* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/core/transformers/TestClassTransformer.class */
public abstract class TestClassTransformer<T, M> implements MockTransformer<T> {
    private final Class<?> testClass;
    private final Class<? extends Annotation> testMethodAnnotationType;
    private final MethodSignatureWriter<M> methodSignatureWriter;

    public TestClassTransformer(Class<?> testClass, Class<? extends Annotation> testMethodAnnotationType, MethodSignatureWriter<M> methodSignatureWriter) {
        this.testClass = testClass;
        this.testMethodAnnotationType = testMethodAnnotationType;
        this.methodSignatureWriter = methodSignatureWriter;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public String signatureOf(M method) {
        return this.methodSignatureWriter.signatureFor(method);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public String signatureOf(Method m) {
        return this.methodSignatureWriter.signatureForReflection(m);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Class<? extends Annotation> getTestMethodAnnotationType() {
        return this.testMethodAnnotationType;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Class<?> getTestClass() {
        return this.testClass;
    }
}
