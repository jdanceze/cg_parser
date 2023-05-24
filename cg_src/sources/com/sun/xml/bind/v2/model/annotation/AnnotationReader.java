package com.sun.xml.bind.v2.model.annotation;

import com.sun.istack.Nullable;
import com.sun.xml.bind.v2.model.core.ErrorHandler;
import java.lang.annotation.Annotation;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/model/annotation/AnnotationReader.class */
public interface AnnotationReader<T, C, F, M> {
    void setErrorHandler(ErrorHandler errorHandler);

    <A extends Annotation> A getFieldAnnotation(Class<A> cls, F f, Locatable locatable);

    boolean hasFieldAnnotation(Class<? extends Annotation> cls, F f);

    boolean hasClassAnnotation(C c, Class<? extends Annotation> cls);

    Annotation[] getAllFieldAnnotations(F f, Locatable locatable);

    <A extends Annotation> A getMethodAnnotation(Class<A> cls, M m, M m2, Locatable locatable);

    boolean hasMethodAnnotation(Class<? extends Annotation> cls, String str, M m, M m2, Locatable locatable);

    Annotation[] getAllMethodAnnotations(M m, Locatable locatable);

    <A extends Annotation> A getMethodAnnotation(Class<A> cls, M m, Locatable locatable);

    boolean hasMethodAnnotation(Class<? extends Annotation> cls, M m);

    @Nullable
    <A extends Annotation> A getMethodParameterAnnotation(Class<A> cls, M m, int i, Locatable locatable);

    @Nullable
    <A extends Annotation> A getClassAnnotation(Class<A> cls, C c, Locatable locatable);

    @Nullable
    <A extends Annotation> A getPackageAnnotation(Class<A> cls, C c, Locatable locatable);

    T getClassValue(Annotation annotation, String str);

    T[] getClassArrayValue(Annotation annotation, String str);
}
