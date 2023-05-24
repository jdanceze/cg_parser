package com.sun.xml.bind.v2.runtime.reflect;

import com.sun.xml.bind.v2.model.nav.Navigator;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.logging.Level;
import java.util.logging.Logger;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/runtime/reflect/Utils.class */
final class Utils {
    private static final Logger LOGGER = Logger.getLogger(Utils.class.getName());
    static final Navigator<Type, Class, Field, Method> REFLECTION_NAVIGATOR;

    static {
        try {
            final Class refNav = Class.forName("com.sun.xml.bind.v2.model.nav.ReflectionNavigator");
            Method getInstance = (Method) AccessController.doPrivileged(new PrivilegedAction<Method>() { // from class: com.sun.xml.bind.v2.runtime.reflect.Utils.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                public Method run() {
                    try {
                        Method getInstance2 = refNav.getDeclaredMethod("getInstance", new Class[0]);
                        getInstance2.setAccessible(true);
                        return getInstance2;
                    } catch (NoSuchMethodException e) {
                        throw new IllegalStateException("ReflectionNavigator.getInstance can't be found");
                    }
                }
            });
            REFLECTION_NAVIGATOR = (Navigator) getInstance.invoke(null, new Object[0]);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Can't find ReflectionNavigator class");
        } catch (IllegalAccessException e2) {
            throw new IllegalStateException("ReflectionNavigator.getInstance method is inaccessible");
        } catch (SecurityException e3) {
            LOGGER.log(Level.FINE, "Unable to access ReflectionNavigator.getInstance", (Throwable) e3);
            throw e3;
        } catch (InvocationTargetException e4) {
            throw new IllegalStateException("ReflectionNavigator.getInstance throws the exception");
        }
    }

    private Utils() {
    }
}
