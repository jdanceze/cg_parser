package org.powermock.core.classloader.javassist;

import java.security.ProtectionDomain;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import org.powermock.core.classloader.MockClassLoader;
import org.powermock.core.classloader.MockClassLoaderConfiguration;
import org.powermock.core.classloader.annotations.UseClassPathAdjuster;
import org.powermock.core.transformers.ClassWrapper;
import org.powermock.core.transformers.javassist.support.JavaAssistClassWrapperFactory;
/* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/core/classloader/javassist/JavassistMockClassLoader.class */
public class JavassistMockClassLoader extends MockClassLoader {
    public static final String CGLIB_ENHANCER = "net.sf.cglib.proxy.Enhancer$EnhancerKey$$KeyFactoryByCGLIB$$";
    public static final String CGLIB_METHOD_WRAPPER = "net.sf.cglib.core.MethodWrapper$MethodWrapperKey$$KeyFactoryByCGLIB";
    private final ClassPool classPool;

    public JavassistMockClassLoader(String[] classesToMock) {
        this(classesToMock, new String[0], null);
    }

    public JavassistMockClassLoader(String[] classesToMock, String[] packagesToDefer, UseClassPathAdjuster useClassPathAdjuster) {
        this(new MockClassLoaderConfiguration(classesToMock, packagesToDefer), useClassPathAdjuster);
    }

    public JavassistMockClassLoader(MockClassLoaderConfiguration configuration) {
        this(configuration, null);
    }

    public JavassistMockClassLoader(MockClassLoaderConfiguration configuration, UseClassPathAdjuster useClassPathAdjuster) {
        super(configuration, new JavaAssistClassWrapperFactory());
        this.classPool = new ClassPoolFactory(useClassPathAdjuster).create();
        this.classMarker = JavaAssistClassMarkerFactory.createClassMarker(this.classPool);
    }

    @Override // org.powermock.core.classloader.MockClassLoader
    protected Class<?> loadUnmockedClass(String name, ProtectionDomain protectionDomain) throws ClassFormatError, ClassNotFoundException {
        byte[] bytes = null;
        try {
            if (!name.startsWith(CGLIB_ENHANCER) && !name.startsWith(CGLIB_METHOD_WRAPPER)) {
                CtClass ctClass = this.classPool.get(name);
                if (ctClass.isFrozen()) {
                    ctClass.defrost();
                }
                bytes = ctClass.toBytecode();
            }
            if (bytes == null) {
                return null;
            }
            return defineClass(name, bytes, 0, bytes.length, protectionDomain);
        } catch (NotFoundException e) {
            return ClassLoader.getSystemClassLoader().loadClass(name);
        } catch (Exception e2) {
            throw new RuntimeException("Failed to loaded class " + name, e2);
        }
    }

    @Override // org.powermock.core.classloader.MockClassLoader
    protected byte[] defineAndTransformClass(String name, ProtectionDomain protectionDomain) {
        ClassPool.doPruning = false;
        try {
            ClassWrapper<CtClass> wrappedType = this.classWrapperFactory.wrap(this.classPool.get(name));
            CtClass type = transformClass(wrappedType).unwrap();
            type.detach();
            byte[] clazz = type.toBytecode();
            return clazz;
        } catch (Exception e) {
            throw new IllegalStateException("Failed to transform class with name " + name + ". Reason: " + e.getMessage(), e);
        }
    }
}
