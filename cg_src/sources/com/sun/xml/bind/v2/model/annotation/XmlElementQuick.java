package com.sun.xml.bind.v2.model.annotation;

import java.lang.annotation.Annotation;
import javax.xml.bind.annotation.XmlElement;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/model/annotation/XmlElementQuick.class */
final class XmlElementQuick extends Quick implements XmlElement {
    private final XmlElement core;

    public XmlElementQuick(Locatable upstream, XmlElement core) {
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
        return new XmlElementQuick(upstream, (XmlElement) core);
    }

    @Override // java.lang.annotation.Annotation
    public Class<XmlElement> annotationType() {
        return XmlElement.class;
    }

    @Override // javax.xml.bind.annotation.XmlElement
    public String name() {
        return this.core.name();
    }

    @Override // javax.xml.bind.annotation.XmlElement
    public Class type() {
        return this.core.type();
    }

    @Override // javax.xml.bind.annotation.XmlElement
    public String namespace() {
        return this.core.namespace();
    }

    @Override // javax.xml.bind.annotation.XmlElement
    public String defaultValue() {
        return this.core.defaultValue();
    }

    @Override // javax.xml.bind.annotation.XmlElement
    public boolean required() {
        return this.core.required();
    }

    @Override // javax.xml.bind.annotation.XmlElement
    public boolean nillable() {
        return this.core.nillable();
    }
}
