package com.sun.xml.bind.v2.model.annotation;

import com.sun.xml.bind.v2.runtime.Location;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/model/annotation/LocatableAnnotation.class */
public class LocatableAnnotation implements InvocationHandler, Locatable, Location {
    private final Annotation core;
    private final Locatable upstream;
    private static final Map<Class, Quick> quicks = new HashMap();

    public static <A extends Annotation> A create(A annotation, Locatable parentSourcePos) {
        if (annotation == null) {
            return null;
        }
        Class type = annotation.annotationType();
        if (quicks.containsKey(type)) {
            return quicks.get(type).newInstance(parentSourcePos, annotation);
        }
        ClassLoader cl = SecureLoader.getClassClassLoader(LocatableAnnotation.class);
        try {
            Class loadableT = Class.forName(type.getName(), false, cl);
            if (loadableT != type) {
                return annotation;
            }
            return (A) Proxy.newProxyInstance(cl, new Class[]{type, Locatable.class}, new LocatableAnnotation(annotation, parentSourcePos));
        } catch (ClassNotFoundException e) {
            return annotation;
        } catch (IllegalArgumentException e2) {
            return annotation;
        }
    }

    LocatableAnnotation(Annotation core, Locatable upstream) {
        this.core = core;
        this.upstream = upstream;
    }

    @Override // com.sun.xml.bind.v2.model.annotation.Locatable
    public Locatable getUpstream() {
        return this.upstream;
    }

    @Override // com.sun.xml.bind.v2.model.annotation.Locatable
    public Location getLocation() {
        return this;
    }

    @Override // java.lang.reflect.InvocationHandler
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        try {
            if (method.getDeclaringClass() == Locatable.class) {
                return method.invoke(this, args);
            }
            if (Modifier.isStatic(method.getModifiers())) {
                throw new IllegalArgumentException();
            }
            return method.invoke(this.core, args);
        } catch (InvocationTargetException e) {
            if (e.getTargetException() != null) {
                throw e.getTargetException();
            }
            throw e;
        }
    }

    @Override // com.sun.xml.bind.v2.runtime.Location
    public String toString() {
        return this.core.toString();
    }

    static {
        Quick[] all;
        for (Quick q : Init.getAll()) {
            quicks.put(q.annotationType(), q);
        }
    }
}
