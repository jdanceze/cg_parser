package org.powermock.core.classloader;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import org.powermock.configuration.GlobalConfiguration;
import org.powermock.core.classloader.annotations.PrepareEverythingForTest;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.PrepareOnlyThisForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.core.classloader.annotations.UseClassPathAdjuster;
import org.powermock.core.classloader.javassist.JavassistMockClassLoader;
import org.powermock.core.transformers.MockTransformerChainFactory;
import org.powermock.core.transformers.javassist.JavassistMockTransformerChainFactory;
/* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/core/classloader/ByteCodeFramework.class */
public enum ByteCodeFramework {
    Javassist { // from class: org.powermock.core.classloader.ByteCodeFramework.1
        @Override // org.powermock.core.classloader.ByteCodeFramework
        MockClassLoader createClassloader(MockClassLoaderConfiguration configuration, UseClassPathAdjuster useClassPathAdjuster) {
            return new JavassistMockClassLoader(configuration, useClassPathAdjuster);
        }

        @Override // org.powermock.core.classloader.ByteCodeFramework
        MockTransformerChainFactory createTransformerChainFactory() {
            return new JavassistMockTransformerChainFactory();
        }
    };

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract MockClassLoader createClassloader(MockClassLoaderConfiguration mockClassLoaderConfiguration, UseClassPathAdjuster useClassPathAdjuster);

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract MockTransformerChainFactory createTransformerChainFactory();

    public static ByteCodeFramework getByteCodeFrameworkForMethod(Class<?> testClass, Method method) {
        ByteCodeFramework byteCodeFramework = getByteCodeFramework(method);
        if (byteCodeFramework == null) {
            byteCodeFramework = getByteCodeFramework(testClass);
        }
        if (byteCodeFramework == null) {
            throw new IllegalArgumentException(String.format("Either method %s or class %s is annotated by PrepareForTest/PrepareEverythingForTest", method.getName(), testClass.getName()));
        }
        return byteCodeFramework;
    }

    public static ByteCodeFramework getByteCodeFrameworkForTestClass(Class<?> testClass) {
        ByteCodeFramework byteCodeFramework = getByteCodeFramework(testClass);
        if (byteCodeFramework == null) {
            byteCodeFramework = GlobalConfiguration.powerMockConfiguration().getByteCodeFramework();
        }
        return byteCodeFramework;
    }

    private static ByteCodeFramework getByteCodeFramework(AnnotatedElement element) {
        if (element.isAnnotationPresent(PrepareForTest.class)) {
            return ((PrepareForTest) element.getAnnotation(PrepareForTest.class)).byteCodeFramework();
        }
        if (element.isAnnotationPresent(PrepareOnlyThisForTest.class)) {
            return ((PrepareOnlyThisForTest) element.getAnnotation(PrepareOnlyThisForTest.class)).byteCodeFramework();
        }
        if (element.isAnnotationPresent(PrepareEverythingForTest.class)) {
            return ((PrepareEverythingForTest) element.getAnnotation(PrepareEverythingForTest.class)).byteCodeFramework();
        }
        if (element.isAnnotationPresent(SuppressStaticInitializationFor.class)) {
            return ((SuppressStaticInitializationFor) element.getAnnotation(SuppressStaticInitializationFor.class)).byteCodeFramework();
        }
        return null;
    }
}
