package org.powermock.core.classloader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.security.ProtectionDomain;
import java.util.Enumeration;
import org.powermock.core.transformers.ClassWrapper;
import org.powermock.core.transformers.ClassWrapperFactory;
import org.powermock.core.transformers.MockTransformerChain;
import org.powermock.core.transformers.javassist.support.JavaAssistClassWrapperFactory;
import org.powermock.core.transformers.support.DefaultMockTransformerChain;
/* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/core/classloader/MockClassLoader.class */
public abstract class MockClassLoader extends DeferSupportingClassLoader {
    public static final String MODIFY_ALL_CLASSES = "*";
    protected ClassMarker classMarker;
    protected ClassWrapperFactory classWrapperFactory;
    private MockTransformerChain mockTransformerChain;

    protected abstract byte[] defineAndTransformClass(String str, ProtectionDomain protectionDomain) throws ClassNotFoundException;

    @Override // org.powermock.core.classloader.DeferSupportingClassLoader
    public /* bridge */ /* synthetic */ void cache(Class cls) {
        super.cache(cls);
    }

    @Override // org.powermock.core.classloader.DeferSupportingClassLoader
    public /* bridge */ /* synthetic */ MockClassLoaderConfiguration getConfiguration() {
        return super.getConfiguration();
    }

    @Override // org.powermock.core.classloader.DeferSupportingClassLoader, java.lang.ClassLoader
    public /* bridge */ /* synthetic */ Enumeration getResources(String str) throws IOException {
        return super.getResources(str);
    }

    @Override // org.powermock.core.classloader.DeferSupportingClassLoader, java.lang.ClassLoader
    public /* bridge */ /* synthetic */ InputStream getResourceAsStream(String str) {
        return super.getResourceAsStream(str);
    }

    @Override // org.powermock.core.classloader.DeferSupportingClassLoader, java.lang.ClassLoader
    public /* bridge */ /* synthetic */ URL getResource(String str) {
        return super.getResource(str);
    }

    protected MockClassLoader(String[] classesToMock, String[] packagesToDefer) {
        this(new MockClassLoaderConfiguration(classesToMock, packagesToDefer), new JavaAssistClassWrapperFactory());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public MockClassLoader(MockClassLoaderConfiguration configuration, ClassWrapperFactory classWrapperFactory) {
        super(MockClassLoader.class.getClassLoader(), configuration);
        this.classWrapperFactory = classWrapperFactory;
        this.mockTransformerChain = DefaultMockTransformerChain.newBuilder().build();
    }

    @Override // org.powermock.core.classloader.DeferSupportingClassLoader
    protected Class<?> loadClassByThisClassLoader(String className) throws ClassFormatError, ClassNotFoundException {
        Class<?> loadedClass;
        Class<?> deferClass = this.deferTo.loadClass(className);
        if (getConfiguration().shouldMockClass(className)) {
            loadedClass = loadMockClass(className, deferClass.getProtectionDomain());
        } else {
            loadedClass = loadUnmockedClass(className, deferClass.getProtectionDomain());
        }
        return loadedClass;
    }

    public void setMockTransformerChain(MockTransformerChain mockTransformerChain) {
        this.mockTransformerChain = mockTransformerChain;
    }

    public MockTransformerChain getMockTransformerChain() {
        return this.mockTransformerChain;
    }

    protected Class<?> loadUnmockedClass(String name, ProtectionDomain protectionDomain) throws ClassNotFoundException {
        String path = name.replace('.', '/').concat(".class");
        URL res = this.deferTo.getResource(path);
        if (res != null) {
            try {
                return defineClass(name, res, protectionDomain);
            } catch (IOException e) {
                throw new ClassNotFoundException(name, e);
            }
        }
        throw new ClassNotFoundException(name);
    }

    private Class<?> defineClass(String name, URL url, ProtectionDomain protectionDomain) throws IOException {
        byte[] b = readClass(url);
        return defineClass(name, b, 0, b.length, protectionDomain);
    }

    private byte[] readClass(URL url) throws IOException {
        URLConnection connection = url.openConnection();
        InputStream in = connection.getInputStream();
        ByteArrayOutputStream tmpOut = null;
        try {
            int contentLength = connection.getContentLength();
            if (contentLength != -1) {
                tmpOut = new ByteArrayOutputStream(contentLength);
            } else {
                tmpOut = new ByteArrayOutputStream(16384);
            }
            byte[] buf = new byte[512];
            while (true) {
                int len = in.read(buf);
                if (len == -1) {
                    break;
                }
                tmpOut.write(buf, 0, len);
            }
            byte[] byteArray = tmpOut.toByteArray();
            in.close();
            if (tmpOut != null) {
                tmpOut.close();
            }
            return byteArray;
        } catch (Throwable th) {
            in.close();
            if (tmpOut != null) {
                tmpOut.close();
            }
            throw th;
        }
    }

    private Class<?> loadMockClass(String name, ProtectionDomain protectionDomain) throws ClassNotFoundException {
        byte[] clazz = defineAndTransformClass(name, protectionDomain);
        return defineClass(name, protectionDomain, clazz);
    }

    public Class<?> defineClass(String name, ProtectionDomain protectionDomain, byte[] clazz) {
        return defineClass(name, clazz, 0, clazz.length, protectionDomain);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public <T> ClassWrapper<T> transformClass(ClassWrapper<T> wrappedType) throws Exception {
        ClassWrapper<T> wrappedType2 = this.mockTransformerChain.transform(wrappedType);
        if (this.classMarker != null) {
            this.classMarker.mark(wrappedType2);
        }
        return wrappedType2;
    }
}
