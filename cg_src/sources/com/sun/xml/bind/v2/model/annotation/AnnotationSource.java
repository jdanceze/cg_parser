package com.sun.xml.bind.v2.model.annotation;

import java.lang.annotation.Annotation;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/model/annotation/AnnotationSource.class */
public interface AnnotationSource {
    <A extends Annotation> A readAnnotation(Class<A> cls);

    boolean hasAnnotation(Class<? extends Annotation> cls);
}
