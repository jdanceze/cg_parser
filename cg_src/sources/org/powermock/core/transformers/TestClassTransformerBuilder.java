package org.powermock.core.transformers;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collection;
import org.powermock.core.classloader.ByteCodeFramework;
import org.powermock.core.transformers.javassist.testclass.ForMethodsJavaAssistTestClassTransformer;
import org.powermock.core.transformers.javassist.testclass.FromAllMethodsExceptJavaAssistTestClassTransformer;
/* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/core/transformers/TestClassTransformerBuilder.class */
public class TestClassTransformerBuilder {
    private final Class<?> testClass;

    public static TestClassTransformerBuilder forTestClass(Class<?> testClass) {
        return new TestClassTransformerBuilder(testClass);
    }

    private TestClassTransformerBuilder(Class<?> testClass) {
        this.testClass = testClass;
    }

    public RemovesTestMethodAnnotation removesTestMethodAnnotation(Class<? extends Annotation> testMethodAnnotation) {
        return new RemovesTestMethodAnnotation(this.testClass, testMethodAnnotation, ByteCodeFramework.getByteCodeFrameworkForTestClass(this.testClass));
    }

    public TestClassTransformerBuilderWithClue bytecodeFrameworkClue(Method method) {
        return new TestClassTransformerBuilderWithClue(this.testClass, method);
    }

    /* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/core/transformers/TestClassTransformerBuilder$TestClassTransformerBuilderWithClue.class */
    public static class TestClassTransformerBuilderWithClue {
        private final Class<?> testClass;
        private final Method method;

        private TestClassTransformerBuilderWithClue(Class<?> testClass, Method method) {
            this.testClass = testClass;
            this.method = method;
        }

        public RemovesTestMethodAnnotation removesTestMethodAnnotation(Class<? extends Annotation> testMethodAnnotation) {
            return new RemovesTestMethodAnnotation(this.testClass, testMethodAnnotation, ByteCodeFramework.getByteCodeFrameworkForMethod(this.testClass, this.method));
        }
    }

    /* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/core/transformers/TestClassTransformerBuilder$RemovesTestMethodAnnotation.class */
    public static class RemovesTestMethodAnnotation {
        private final Class<? extends Annotation> testMethodAnnotation;
        private final Class<?> testClass;
        private final ByteCodeFramework byteCodeFramework;

        private RemovesTestMethodAnnotation(Class<?> testClass, Class<? extends Annotation> testMethodAnnotation, ByteCodeFramework byteCodeFramework) {
            this.testClass = testClass;
            this.testMethodAnnotation = testMethodAnnotation;
            this.byteCodeFramework = byteCodeFramework;
        }

        public TestClassTransformer fromMethods(Collection<Method> testMethodsThatRunOnOtherClassLoaders) {
            switch (this.byteCodeFramework) {
                case Javassist:
                    return new ForMethodsJavaAssistTestClassTransformer(this.testClass, this.testMethodAnnotation, MethodSignatures.Javassist.methodSignatureWriter(), testMethodsThatRunOnOtherClassLoaders);
                default:
                    throw new IllegalArgumentException(String.format("Unknown bytecode framework `%s`", this.byteCodeFramework));
            }
        }

        public TestClassTransformer fromAllMethodsExcept(Method singleMethodToRunOnTargetClassLoader) {
            switch (this.byteCodeFramework) {
                case Javassist:
                    return new FromAllMethodsExceptJavaAssistTestClassTransformer(this.testClass, this.testMethodAnnotation, MethodSignatures.Javassist.methodSignatureWriter(), singleMethodToRunOnTargetClassLoader);
                default:
                    throw new IllegalArgumentException(String.format("Unknown bytecode framework `%s`", this.byteCodeFramework));
            }
        }
    }
}
