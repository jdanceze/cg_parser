package org.powermock.core.classloader;

import java.util.concurrent.Callable;
import org.powermock.core.InvocationException;
/* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/core/classloader/ClassloaderWrapper.class */
public class ClassloaderWrapper {
    public static void runWithClass(Runnable runnable) {
        runWithClassClassLoader(ClassloaderWrapper.class.getClassLoader(), runnable);
    }

    public static void runWithClassClassLoader(ClassLoader classLoader, Runnable runnable) {
        ClassLoader originalCL = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(classLoader);
        try {
            runnable.run();
            Thread.currentThread().setContextClassLoader(originalCL);
        } catch (Throwable th) {
            Thread.currentThread().setContextClassLoader(originalCL);
            throw th;
        }
    }

    public static <V> V runWithClass(Callable<V> callable) {
        return (V) runWithClassClassLoader(ClassloaderWrapper.class.getClassLoader(), callable);
    }

    /* JADX WARN: Finally extract failed */
    public static <V> V runWithClassClassLoader(ClassLoader classLoader, Callable<V> callable) {
        ClassLoader originalCL = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(classLoader);
        try {
            try {
                V call = callable.call();
                Thread.currentThread().setContextClassLoader(originalCL);
                return call;
            } catch (Exception e) {
                throw new InvocationException(e);
            }
        } catch (Throwable th) {
            Thread.currentThread().setContextClassLoader(originalCL);
            throw th;
        }
    }
}
