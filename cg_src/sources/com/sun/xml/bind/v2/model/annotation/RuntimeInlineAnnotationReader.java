package com.sun.xml.bind.v2.model.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/model/annotation/RuntimeInlineAnnotationReader.class */
public final class RuntimeInlineAnnotationReader extends AbstractInlineAnnotationReaderImpl<Type, Class, Field, Method> implements RuntimeAnnotationReader {
    private final Map<Class<? extends Annotation>, Map<Package, Annotation>> packageCache = new HashMap();

    @Override // com.sun.xml.bind.v2.model.annotation.AnnotationReader
    public /* bridge */ /* synthetic */ Annotation getPackageAnnotation(Class cls, Class cls2, Locatable locatable) {
        return getPackageAnnotation2((Class<Annotation>) cls, cls2, locatable);
    }

    @Override // com.sun.xml.bind.v2.model.annotation.AnnotationReader
    public /* bridge */ /* synthetic */ Annotation getClassAnnotation(Class cls, Class cls2, Locatable locatable) {
        return getClassAnnotation2((Class<Annotation>) cls, cls2, locatable);
    }

    @Override // com.sun.xml.bind.v2.model.annotation.AnnotationReader
    public /* bridge */ /* synthetic */ Annotation getMethodParameterAnnotation(Class cls, Method method, int i, Locatable locatable) {
        return getMethodParameterAnnotation2((Class<Annotation>) cls, method, i, locatable);
    }

    @Override // com.sun.xml.bind.v2.model.annotation.AnnotationReader
    public /* bridge */ /* synthetic */ boolean hasMethodAnnotation(Class cls, Method method) {
        return hasMethodAnnotation2((Class<? extends Annotation>) cls, method);
    }

    @Override // com.sun.xml.bind.v2.model.annotation.AnnotationReader
    public /* bridge */ /* synthetic */ Annotation getMethodAnnotation(Class cls, Method method, Locatable locatable) {
        return getMethodAnnotation2((Class<Annotation>) cls, method, locatable);
    }

    @Override // com.sun.xml.bind.v2.model.annotation.AnnotationReader
    public /* bridge */ /* synthetic */ boolean hasClassAnnotation(Class cls, Class cls2) {
        return hasClassAnnotation2(cls, (Class<? extends Annotation>) cls2);
    }

    @Override // com.sun.xml.bind.v2.model.annotation.AnnotationReader
    public /* bridge */ /* synthetic */ boolean hasFieldAnnotation(Class cls, Field field) {
        return hasFieldAnnotation2((Class<? extends Annotation>) cls, field);
    }

    @Override // com.sun.xml.bind.v2.model.annotation.AnnotationReader
    public /* bridge */ /* synthetic */ Annotation getFieldAnnotation(Class cls, Field field, Locatable locatable) {
        return getFieldAnnotation2((Class<Annotation>) cls, field, locatable);
    }

    /* renamed from: getFieldAnnotation  reason: avoid collision after fix types in other method */
    public <A extends Annotation> A getFieldAnnotation2(Class<A> annotation, Field field, Locatable srcPos) {
        return (A) LocatableAnnotation.create(field.getAnnotation(annotation), srcPos);
    }

    /* renamed from: hasFieldAnnotation  reason: avoid collision after fix types in other method */
    public boolean hasFieldAnnotation2(Class<? extends Annotation> annotationType, Field field) {
        return field.isAnnotationPresent(annotationType);
    }

    /* renamed from: hasClassAnnotation  reason: avoid collision after fix types in other method */
    public boolean hasClassAnnotation2(Class clazz, Class<? extends Annotation> annotationType) {
        return clazz.isAnnotationPresent(annotationType);
    }

    @Override // com.sun.xml.bind.v2.model.annotation.AnnotationReader
    public Annotation[] getAllFieldAnnotations(Field field, Locatable srcPos) {
        Annotation[] r = field.getAnnotations();
        for (int i = 0; i < r.length; i++) {
            r[i] = LocatableAnnotation.create(r[i], srcPos);
        }
        return r;
    }

    /* renamed from: getMethodAnnotation  reason: avoid collision after fix types in other method */
    public <A extends Annotation> A getMethodAnnotation2(Class<A> annotation, Method method, Locatable srcPos) {
        return (A) LocatableAnnotation.create(method.getAnnotation(annotation), srcPos);
    }

    /* renamed from: hasMethodAnnotation  reason: avoid collision after fix types in other method */
    public boolean hasMethodAnnotation2(Class<? extends Annotation> annotation, Method method) {
        return method.isAnnotationPresent(annotation);
    }

    @Override // com.sun.xml.bind.v2.model.annotation.AnnotationReader
    public Annotation[] getAllMethodAnnotations(Method method, Locatable srcPos) {
        Annotation[] r = method.getAnnotations();
        for (int i = 0; i < r.length; i++) {
            r[i] = LocatableAnnotation.create(r[i], srcPos);
        }
        return r;
    }

    /* renamed from: getMethodParameterAnnotation  reason: avoid collision after fix types in other method */
    public <A extends Annotation> A getMethodParameterAnnotation2(Class<A> annotation, Method method, int paramIndex, Locatable srcPos) {
        Annotation[] pa = method.getParameterAnnotations()[paramIndex];
        for (Annotation a : pa) {
            if (a.annotationType() == annotation) {
                return (A) LocatableAnnotation.create(a, srcPos);
            }
        }
        return null;
    }

    /* renamed from: getClassAnnotation  reason: avoid collision after fix types in other method */
    public <A extends Annotation> A getClassAnnotation2(Class<A> a, Class clazz, Locatable srcPos) {
        return (A) LocatableAnnotation.create(clazz.getAnnotation(a), srcPos);
    }

    /* renamed from: getPackageAnnotation  reason: avoid collision after fix types in other method */
    public <A extends Annotation> A getPackageAnnotation2(Class<A> a, Class clazz, Locatable srcPos) {
        Package p = clazz.getPackage();
        if (p == null) {
            return null;
        }
        Map<Package, Annotation> cache = this.packageCache.get(a);
        if (cache == null) {
            cache = new HashMap<>();
            this.packageCache.put(a, cache);
        }
        if (cache.containsKey(p)) {
            return (A) cache.get(p);
        }
        A ann = (A) LocatableAnnotation.create(p.getAnnotation(a), srcPos);
        cache.put(p, ann);
        return ann;
    }

    @Override // com.sun.xml.bind.v2.model.annotation.AnnotationReader
    /* renamed from: getClassValue */
    public Type getClassValue2(Annotation a, String name) {
        try {
            return (Class) a.annotationType().getMethod(name, new Class[0]).invoke(a, new Object[0]);
        } catch (IllegalAccessException e) {
            throw new IllegalAccessError(e.getMessage());
        } catch (NoSuchMethodException e2) {
            throw new NoSuchMethodError(e2.getMessage());
        } catch (InvocationTargetException e3) {
            throw new InternalError(Messages.CLASS_NOT_FOUND.format(a.annotationType(), e3.getMessage()));
        }
    }

    @Override // com.sun.xml.bind.v2.model.annotation.AnnotationReader
    /* renamed from: getClassArrayValue */
    public Type[] getClassArrayValue2(Annotation a, String name) {
        try {
            return (Class[]) a.annotationType().getMethod(name, new Class[0]).invoke(a, new Object[0]);
        } catch (IllegalAccessException e) {
            throw new IllegalAccessError(e.getMessage());
        } catch (NoSuchMethodException e2) {
            throw new NoSuchMethodError(e2.getMessage());
        } catch (InvocationTargetException e3) {
            throw new InternalError(e3.getMessage());
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.sun.xml.bind.v2.model.annotation.AbstractInlineAnnotationReaderImpl
    public String fullName(Method m) {
        return m.getDeclaringClass().getName() + '#' + m.getName();
    }
}
