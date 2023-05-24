package org.apache.commons.logging.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.apache.commons.logging.LogFactory;
/* loaded from: gencallgraphv3.jar:commons-logging-1.1.1.jar:org/apache/commons/logging/impl/ServletContextCleaner.class */
public class ServletContextCleaner implements ServletContextListener {
    private Class[] RELEASE_SIGNATURE;
    static Class class$java$lang$ClassLoader;

    public ServletContextCleaner() {
        Class cls;
        Class[] clsArr = new Class[1];
        if (class$java$lang$ClassLoader == null) {
            cls = class$("java.lang.ClassLoader");
            class$java$lang$ClassLoader = cls;
        } else {
            cls = class$java$lang$ClassLoader;
        }
        clsArr[0] = cls;
        this.RELEASE_SIGNATURE = clsArr;
    }

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    @Override // javax.servlet.ServletContextListener
    public void contextDestroyed(ServletContextEvent sce) {
        ClassLoader tccl = Thread.currentThread().getContextClassLoader();
        Object[] params = {tccl};
        ClassLoader loader = tccl;
        while (loader != null) {
            try {
                Class logFactoryClass = loader.loadClass(LogFactory.FACTORY_PROPERTY);
                Method releaseMethod = logFactoryClass.getMethod("release", this.RELEASE_SIGNATURE);
                releaseMethod.invoke(null, params);
                loader = logFactoryClass.getClassLoader().getParent();
            } catch (ClassNotFoundException e) {
                loader = null;
            } catch (IllegalAccessException e2) {
                System.err.println("LogFactory instance found which is not accessable!");
                loader = null;
            } catch (NoSuchMethodException e3) {
                System.err.println("LogFactory instance found which does not support release method!");
                loader = null;
            } catch (InvocationTargetException e4) {
                System.err.println("LogFactory instance release method failed!");
                loader = null;
            }
        }
        LogFactory.release(tccl);
    }

    @Override // javax.servlet.ServletContextListener
    public void contextInitialized(ServletContextEvent sce) {
    }
}
