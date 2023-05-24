package com.sun.xml.bind.v2.model.annotation;

import java.lang.annotation.Annotation;
import javax.xml.bind.annotation.XmlSchemaType;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/model/annotation/XmlSchemaTypeQuick.class */
final class XmlSchemaTypeQuick extends Quick implements XmlSchemaType {
    private final XmlSchemaType core;

    public XmlSchemaTypeQuick(Locatable upstream, XmlSchemaType core) {
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
        return new XmlSchemaTypeQuick(upstream, (XmlSchemaType) core);
    }

    @Override // java.lang.annotation.Annotation
    public Class<XmlSchemaType> annotationType() {
        return XmlSchemaType.class;
    }

    @Override // javax.xml.bind.annotation.XmlSchemaType
    public String name() {
        return this.core.name();
    }

    @Override // javax.xml.bind.annotation.XmlSchemaType
    public Class type() {
        return this.core.type();
    }

    @Override // javax.xml.bind.annotation.XmlSchemaType
    public String namespace() {
        return this.core.namespace();
    }
}
