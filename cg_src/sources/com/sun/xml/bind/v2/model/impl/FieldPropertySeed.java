package com.sun.xml.bind.v2.model.impl;

import com.sun.xml.bind.v2.model.annotation.Locatable;
import com.sun.xml.bind.v2.runtime.Location;
import java.lang.annotation.Annotation;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/model/impl/FieldPropertySeed.class */
public class FieldPropertySeed<TypeT, ClassDeclT, FieldT, MethodT> implements PropertySeed<TypeT, ClassDeclT, FieldT, MethodT> {
    protected final FieldT field;
    private ClassInfoImpl<TypeT, ClassDeclT, FieldT, MethodT> parent;

    /* JADX INFO: Access modifiers changed from: package-private */
    public FieldPropertySeed(ClassInfoImpl<TypeT, ClassDeclT, FieldT, MethodT> classInfo, FieldT field) {
        this.parent = classInfo;
        this.field = field;
    }

    @Override // com.sun.xml.bind.v2.model.annotation.AnnotationSource
    public <A extends Annotation> A readAnnotation(Class<A> a) {
        return (A) this.parent.reader().getFieldAnnotation(a, this.field, this);
    }

    @Override // com.sun.xml.bind.v2.model.annotation.AnnotationSource
    public boolean hasAnnotation(Class<? extends Annotation> annotationType) {
        return this.parent.reader().hasFieldAnnotation(annotationType, this.field);
    }

    @Override // com.sun.xml.bind.v2.model.impl.PropertySeed
    public String getName() {
        return this.parent.nav().getFieldName(this.field);
    }

    @Override // com.sun.xml.bind.v2.model.impl.PropertySeed
    public TypeT getRawType() {
        return this.parent.nav().getFieldType(this.field);
    }

    @Override // com.sun.xml.bind.v2.model.annotation.Locatable
    public Locatable getUpstream() {
        return this.parent;
    }

    @Override // com.sun.xml.bind.v2.model.annotation.Locatable
    public Location getLocation() {
        return this.parent.nav().getFieldLocation(this.field);
    }
}
