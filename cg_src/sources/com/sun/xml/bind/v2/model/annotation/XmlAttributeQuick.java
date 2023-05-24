package com.sun.xml.bind.v2.model.annotation;

import java.lang.annotation.Annotation;
import javax.xml.bind.annotation.XmlAttribute;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/model/annotation/XmlAttributeQuick.class */
final class XmlAttributeQuick extends Quick implements XmlAttribute {
    private final XmlAttribute core;

    public XmlAttributeQuick(Locatable upstream, XmlAttribute core) {
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
        return new XmlAttributeQuick(upstream, (XmlAttribute) core);
    }

    @Override // java.lang.annotation.Annotation
    public Class<XmlAttribute> annotationType() {
        return XmlAttribute.class;
    }

    @Override // javax.xml.bind.annotation.XmlAttribute
    public String name() {
        return this.core.name();
    }

    @Override // javax.xml.bind.annotation.XmlAttribute
    public String namespace() {
        return this.core.namespace();
    }

    @Override // javax.xml.bind.annotation.XmlAttribute
    public boolean required() {
        return this.core.required();
    }
}
