package org.powermock.core.classloader;

import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.List;
import org.powermock.core.classloader.annotations.MockPolicy;
import org.powermock.core.classloader.annotations.PrepareEverythingForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.core.classloader.annotations.UseClassPathAdjuster;
import org.powermock.core.spi.PowerMockPolicy;
import org.powermock.core.transformers.MockTransformer;
import org.powermock.tests.utils.ArrayMerger;
import org.powermock.tests.utils.TestClassesExtractor;
import org.powermock.tests.utils.impl.ArrayMergerImpl;
import org.powermock.tests.utils.impl.MockPolicyInitializerImpl;
import org.powermock.tests.utils.impl.PowerMockIgnorePackagesExtractorImpl;
import org.powermock.tests.utils.impl.PrepareForTestExtractorImpl;
import org.powermock.tests.utils.impl.StaticConstructorSuppressExtractorImpl;
/* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/core/classloader/MockClassLoaderFactory.class */
public class MockClassLoaderFactory {
    private final String[] packagesToIgnore;
    private final Class<?> testClass;
    private final TestClassesExtractor prepareForTestExtractor;
    private final TestClassesExtractor suppressionExtractor;
    private final ArrayMerger arrayMerger;

    public MockClassLoaderFactory(Class<?> testClass) {
        this(testClass, new PowerMockIgnorePackagesExtractorImpl().getPackagesToIgnore(testClass));
    }

    public MockClassLoaderFactory(Class<?> testClass, String[] packagesToIgnore) {
        this.testClass = testClass;
        this.prepareForTestExtractor = new PrepareForTestExtractorImpl();
        this.suppressionExtractor = new StaticConstructorSuppressExtractorImpl();
        this.packagesToIgnore = packagesToIgnore;
        this.arrayMerger = new ArrayMergerImpl();
    }

    /* JADX WARN: Multi-variable type inference failed */
    public ClassLoader createForClass(MockTransformer... extraMockTransformer) {
        ByteCodeFramework byteCodeFramework = ByteCodeFramework.getByteCodeFrameworkForTestClass(this.testClass);
        if (this.testClass.isAnnotationPresent(PrepareEverythingForTest.class)) {
            return create(byteCodeFramework, new String[]{"*"}, extraMockTransformer);
        }
        String[] prepareForTestClasses = this.prepareForTestExtractor.getTestClasses(this.testClass);
        String[] suppressStaticClasses = this.suppressionExtractor.getTestClasses(this.testClass);
        return create(byteCodeFramework, (String[]) this.arrayMerger.mergeArrays(String.class, prepareForTestClasses, suppressStaticClasses), extraMockTransformer);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public ClassLoader createForMethod(Method method, MockTransformer... extraMockTransformers) {
        ByteCodeFramework byteCodeFramework = ByteCodeFramework.getByteCodeFrameworkForMethod(this.testClass, method);
        if (method.isAnnotationPresent(PrepareEverythingForTest.class)) {
            String[] classesToLoadByMockClassloader = {"*"};
            return create(byteCodeFramework, classesToLoadByMockClassloader, extraMockTransformers);
        }
        String[] suppressStaticClasses = getStaticSuppressionClasses(method);
        String[] prepareForTestClasses = this.prepareForTestExtractor.getTestClasses(method);
        return create(byteCodeFramework, (String[]) this.arrayMerger.mergeArrays(String.class, prepareForTestClasses, suppressStaticClasses), extraMockTransformers);
    }

    private ClassLoader create(ByteCodeFramework byteCodeFramework, String[] prepareForTestClasses, MockTransformer... extraMockTransformer) {
        ClassLoader mockLoader;
        String[] classesToLoadByMockClassloader = makeSureArrayContainsTestClassName(prepareForTestClasses, this.testClass.getName());
        if (isContextClassLoaderShouldBeUsed(classesToLoadByMockClassloader)) {
            mockLoader = Thread.currentThread().getContextClassLoader();
        } else {
            mockLoader = createMockClassLoader(byteCodeFramework, classesToLoadByMockClassloader, extraMockTransformer);
        }
        return mockLoader;
    }

    private String[] getStaticSuppressionClasses(Method method) {
        String[] testClasses;
        if (method.isAnnotationPresent(SuppressStaticInitializationFor.class)) {
            testClasses = this.suppressionExtractor.getTestClasses(method);
        } else {
            testClasses = this.suppressionExtractor.getTestClasses(this.testClass);
        }
        return testClasses;
    }

    private ClassLoader createMockClassLoader(ByteCodeFramework byteCodeFramework, String[] classesToLoadByMockClassloader, MockTransformer... extraMockTransformer) {
        ClassLoader mockLoader = createWithPrivilegeAccessController(byteCodeFramework, classesToLoadByMockClassloader, extraMockTransformer);
        initialize(mockLoader);
        return mockLoader;
    }

    private ClassLoader createWithPrivilegeAccessController(final ByteCodeFramework byteCodeFramework, final String[] classesToLoadByMockClassloader, final MockTransformer... extraMockTransformer) {
        return (ClassLoader) AccessController.doPrivileged(new PrivilegedAction<MockClassLoader>() { // from class: org.powermock.core.classloader.MockClassLoaderFactory.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            public MockClassLoader run() {
                UseClassPathAdjuster useClassPathAdjuster = (UseClassPathAdjuster) MockClassLoaderFactory.this.testClass.getAnnotation(UseClassPathAdjuster.class);
                return MockClassLoaderFactory.this.createMockClassLoader(byteCodeFramework, classesToLoadByMockClassloader, useClassPathAdjuster, extraMockTransformer);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public MockClassLoader createMockClassLoader(ByteCodeFramework byteCodeFramework, String[] classesToLoadByMockClassloader, UseClassPathAdjuster useClassPathAdjuster, MockTransformer... extraMockTransformer) {
        return MockClassLoaderBuilder.create(byteCodeFramework).forTestClass(this.testClass).addIgnorePackage(this.packagesToIgnore).addClassesToModify(classesToLoadByMockClassloader).addClassPathAdjuster(useClassPathAdjuster).addExtraMockTransformers(extraMockTransformer).build();
    }

    private void initialize(ClassLoader mockLoader) {
        new MockPolicyInitializerImpl(this.testClass).initialize(mockLoader);
    }

    private boolean isContextClassLoaderShouldBeUsed(String[] classesToLoadByMockClassloader) {
        return (classesToLoadByMockClassloader == null || classesToLoadByMockClassloader.length == 0) && !hasMockPolicyProvidedClasses(this.testClass);
    }

    private String[] makeSureArrayContainsTestClassName(String[] arrayOfClassNames, String testClassName) {
        if (null == arrayOfClassNames || 0 == arrayOfClassNames.length) {
            return new String[]{testClassName};
        }
        List<String> modifiedArrayOfClassNames = new ArrayList<>(arrayOfClassNames.length + 1);
        modifiedArrayOfClassNames.add(testClassName);
        for (String className : arrayOfClassNames) {
            if (testClassName.equals(className)) {
                return arrayOfClassNames;
            }
            modifiedArrayOfClassNames.add(className);
        }
        return (String[]) modifiedArrayOfClassNames.toArray(new String[arrayOfClassNames.length + 1]);
    }

    private boolean hasMockPolicyProvidedClasses(Class<?> testClass) {
        boolean hasMockPolicyProvidedClasses = false;
        if (testClass.isAnnotationPresent(MockPolicy.class)) {
            MockPolicy annotation = (MockPolicy) testClass.getAnnotation(MockPolicy.class);
            Class<? extends PowerMockPolicy>[] value = annotation.value();
            hasMockPolicyProvidedClasses = new MockPolicyInitializerImpl(value).needsInitialization();
        }
        return hasMockPolicyProvidedClasses;
    }
}
