package org.powermock.core.classloader;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.powermock.reflect.Whitebox;
/* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/core/classloader/DeferSupportingClassLoader.class */
abstract class DeferSupportingClassLoader extends ClassLoader {
    private final ConcurrentMap<String, SoftReference<Class<?>>> classes = new ConcurrentHashMap();
    private final ConcurrentMap<String, Object> parallelLockMap;
    private final MockClassLoaderConfiguration configuration;
    ClassLoader deferTo;

    protected abstract Class<?> loadClassByThisClassLoader(String str) throws ClassFormatError, ClassNotFoundException;

    /* JADX INFO: Access modifiers changed from: package-private */
    public DeferSupportingClassLoader(ClassLoader classloader, MockClassLoaderConfiguration configuration) {
        this.configuration = configuration;
        if (classloader == null) {
            this.deferTo = ClassLoader.getSystemClassLoader();
        } else {
            this.deferTo = classloader;
        }
        this.parallelLockMap = new ConcurrentHashMap();
    }

    @Override // java.lang.ClassLoader
    public URL getResource(String s) {
        return this.deferTo.getResource(s);
    }

    @Override // java.lang.ClassLoader
    public InputStream getResourceAsStream(String s) {
        return this.deferTo.getResourceAsStream(s);
    }

    @Override // java.lang.ClassLoader
    public Enumeration<URL> getResources(String name) throws IOException {
        if (this.deferTo.equals(getParent())) {
            return this.deferTo.getResources(name);
        }
        return super.getResources(name);
    }

    public MockClassLoaderConfiguration getConfiguration() {
        return this.configuration;
    }

    public void cache(Class<?> cls) {
        if (cls != null) {
            this.classes.put(cls.getName(), new SoftReference<>(cls));
        }
    }

    @Override // java.lang.ClassLoader
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        Class<?> cls;
        synchronized (getClassLoadingLock(name)) {
            Class<?> clazz = findLoadedClass1(name);
            if (clazz == null) {
                clazz = loadClass1(name, resolve);
            }
            cls = clazz;
        }
        return cls;
    }

    protected Object getClassLoadingLock(String className) {
        Object lock = this;
        if (this.parallelLockMap != null) {
            Object newLock = new Object();
            lock = this.parallelLockMap.putIfAbsent(className, newLock);
            if (lock == null) {
                lock = newLock;
            }
        }
        return lock;
    }

    @Override // java.lang.ClassLoader
    protected URL findResource(String name) {
        try {
            return (URL) Whitebox.invokeMethod(this.deferTo, "findResource", name);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override // java.lang.ClassLoader
    protected Enumeration<URL> findResources(String name) throws IOException {
        try {
            return (Enumeration) Whitebox.invokeMethod(this.deferTo, "findResources", name);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Class<?> loadClass1(String name, boolean resolve) throws ClassNotFoundException {
        Class<?> clazz;
        if (shouldDefer(name)) {
            clazz = loadByDeferClassLoader(name);
        } else {
            clazz = loadClassByThisClassLoader(name);
        }
        if (resolve) {
            resolveClass(clazz);
        }
        this.classes.put(name, new SoftReference<>(clazz));
        return clazz;
    }

    private Class<?> loadByDeferClassLoader(String name) throws ClassNotFoundException {
        Class<?> clazz = this.deferTo.loadClass(name);
        return clazz;
    }

    private boolean shouldDefer(String name) {
        return this.configuration.shouldDefer(name);
    }

    private Class<?> findLoadedClass1(String name) {
        SoftReference<Class<?>> reference = this.classes.get(name);
        Class<?> clazz = null;
        if (reference != null) {
            clazz = reference.get();
        }
        if (clazz == null) {
            clazz = findLoadedClass(name);
        }
        return clazz;
    }
}
