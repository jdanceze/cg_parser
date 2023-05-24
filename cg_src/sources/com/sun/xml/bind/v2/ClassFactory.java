package com.sun.xml.bind.v2;

import com.sun.xml.bind.Util;
import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/ClassFactory.class */
public final class ClassFactory {
    private static final Class[] emptyClass = new Class[0];
    private static final Object[] emptyObject = new Object[0];
    private static final Logger logger = Util.getClassLogger();
    private static final ThreadLocal<Map<Class, WeakReference<Constructor>>> tls = new ThreadLocal<Map<Class, WeakReference<Constructor>>>() { // from class: com.sun.xml.bind.v2.ClassFactory.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.lang.ThreadLocal
        public Map<Class, WeakReference<Constructor>> initialValue() {
            return new WeakHashMap();
        }
    };

    public static void cleanCache() {
        if (tls != null) {
            try {
                tls.remove();
            } catch (Exception e) {
                logger.log(Level.WARNING, "Unable to clean Thread Local cache of classes used in Unmarshaller: {0}", e.getLocalizedMessage());
            }
        }
    }

    public static <T> T create0(final Class<T> clazz) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        Map<Class, WeakReference<Constructor>> m = tls.get();
        Constructor<T> cons = null;
        WeakReference<Constructor> consRef = m.get(clazz);
        if (consRef != null) {
            cons = consRef.get();
        }
        if (cons == null) {
            if (System.getSecurityManager() == null) {
                cons = tryGetDeclaredConstructor(clazz);
            } else {
                cons = (Constructor) AccessController.doPrivileged(new PrivilegedAction<Constructor<T>>() { // from class: com.sun.xml.bind.v2.ClassFactory.2
                    @Override // java.security.PrivilegedAction
                    public Constructor<T> run() {
                        return ClassFactory.tryGetDeclaredConstructor(clazz);
                    }
                });
            }
            int classMod = clazz.getModifiers();
            if (!Modifier.isPublic(classMod) || !Modifier.isPublic(cons.getModifiers())) {
                try {
                    cons.setAccessible(true);
                } catch (SecurityException e) {
                    logger.log(Level.FINE, "Unable to make the constructor of " + clazz + " accessible", (Throwable) e);
                    throw e;
                }
            }
            m.put(clazz, new WeakReference<>(cons));
        }
        return cons.newInstance(emptyObject);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static <T> Constructor<T> tryGetDeclaredConstructor(Class<T> clazz) {
        NoSuchMethodError exp;
        try {
            return clazz.getDeclaredConstructor(emptyClass);
        } catch (NoSuchMethodException e) {
            logger.log(Level.INFO, "No default constructor found on " + clazz, (Throwable) e);
            if (clazz.getDeclaringClass() != null && !Modifier.isStatic(clazz.getModifiers())) {
                exp = new NoSuchMethodError(Messages.NO_DEFAULT_CONSTRUCTOR_IN_INNER_CLASS.format(clazz.getName()));
            } else {
                exp = new NoSuchMethodError(e.getMessage());
            }
            exp.initCause(e);
            throw exp;
        }
    }

    public static <T> T create(Class<T> clazz) {
        try {
            return (T) create0(clazz);
        } catch (IllegalAccessException e) {
            logger.log(Level.INFO, "failed to create a new instance of " + clazz, (Throwable) e);
            throw new IllegalAccessError(e.toString());
        } catch (InstantiationException e2) {
            logger.log(Level.INFO, "failed to create a new instance of " + clazz, (Throwable) e2);
            throw new InstantiationError(e2.toString());
        } catch (InvocationTargetException e3) {
            Throwable target = e3.getTargetException();
            if (target instanceof RuntimeException) {
                throw ((RuntimeException) target);
            }
            if (target instanceof Error) {
                throw ((Error) target);
            }
            throw new IllegalStateException(target);
        }
    }

    public static Object create(Method method) {
        Throwable errorMsg;
        try {
            return method.invoke(null, emptyObject);
        } catch (ExceptionInInitializerError eie) {
            logger.log(Level.INFO, "failed to create a new instance of " + method.getReturnType().getName(), eie);
            errorMsg = eie;
            NoSuchMethodError exp = new NoSuchMethodError(errorMsg.getMessage());
            exp.initCause(errorMsg);
            throw exp;
        } catch (IllegalAccessException e) {
            logger.log(Level.INFO, "failed to create a new instance of " + method.getReturnType().getName(), (Throwable) e);
            throw new IllegalAccessError(e.toString());
        } catch (IllegalArgumentException iae) {
            logger.log(Level.INFO, "failed to create a new instance of " + method.getReturnType().getName(), iae);
            errorMsg = iae;
            NoSuchMethodError exp2 = new NoSuchMethodError(errorMsg.getMessage());
            exp2.initCause(errorMsg);
            throw exp2;
        } catch (NullPointerException npe) {
            logger.log(Level.INFO, "failed to create a new instance of " + method.getReturnType().getName(), npe);
            errorMsg = npe;
            NoSuchMethodError exp22 = new NoSuchMethodError(errorMsg.getMessage());
            exp22.initCause(errorMsg);
            throw exp22;
        } catch (InvocationTargetException ive) {
            Throwable target = ive.getTargetException();
            if (target instanceof RuntimeException) {
                throw ((RuntimeException) target);
            }
            if (target instanceof Error) {
                throw ((Error) target);
            }
            throw new IllegalStateException(target);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static <T> Class<? extends T> inferImplClass(Class<T> fieldType, Class[] knownImplClasses) {
        if (!fieldType.isInterface()) {
            return fieldType;
        }
        for (Class cls : knownImplClasses) {
            if (fieldType.isAssignableFrom(cls)) {
                return cls.asSubclass(fieldType);
            }
        }
        return null;
    }
}
