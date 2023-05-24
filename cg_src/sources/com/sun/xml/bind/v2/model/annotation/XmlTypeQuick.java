package com.sun.xml.bind.v2.model.annotation;

import java.lang.annotation.Annotation;
import javax.xml.bind.annotation.XmlType;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/model/annotation/XmlTypeQuick.class */
final class XmlTypeQuick extends Quick implements XmlType {
    private final XmlType core;

    public XmlTypeQuick(Locatable upstream, XmlType core) {
        super(upstream);
        this.core = core;
    }

    @Override // com.sun.xml.bind.v2.model.annotation.Quick
    protected Annotation getAnnotation() {
        return this.core;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.sun.xml.bind.v2.model.annotation.Quick
    public Quick newInstance(Locatable upstream, Annotation core) {
        return new XmlTypeQuick(upstream, (XmlType) core);
    }

    @Override // java.lang.annotation.Annotation
    public Class<XmlType> annotationType() {
        return XmlType.class;
    }

    @Override // javax.xml.bind.annotation.XmlType
    public String name() {
        return this.core.name();
    }

    @Override // javax.xml.bind.annotation.XmlType
    public String namespace() {
        return this.core.namespace();
    }

    @Override // javax.xml.bind.annotation.XmlType
    public String[] propOrder() {
        return this.core.propOrder();
    }

    @Override // javax.xml.bind.annotation.XmlType
    public Class factoryClass() {
        return this.core.factoryClass();
    }

    @Override // javax.xml.bind.annotation.XmlType
    public String factoryMethod() {
        return this.core.factoryMethod();
    }
}
