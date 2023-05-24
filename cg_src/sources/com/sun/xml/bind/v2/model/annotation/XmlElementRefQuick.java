package com.sun.xml.bind.v2.model.annotation;

import java.lang.annotation.Annotation;
import javax.xml.bind.annotation.XmlElementRef;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/model/annotation/XmlElementRefQuick.class */
final class XmlElementRefQuick extends Quick implements XmlElementRef {
    private final XmlElementRef core;

    public XmlElementRefQuick(Locatable upstream, XmlElementRef core) {
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
        return new XmlElementRefQuick(upstream, (XmlElementRef) core);
    }

    @Override // java.lang.annotation.Annotation
    public Class<XmlElementRef> annotationType() {
        return XmlElementRef.class;
    }

    @Override // javax.xml.bind.annotation.XmlElementRef
    public String name() {
        return this.core.name();
    }

    @Override // javax.xml.bind.annotation.XmlElementRef
    public Class type() {
        return this.core.type();
    }

    @Override // javax.xml.bind.annotation.XmlElementRef
    public String namespace() {
        return this.core.namespace();
    }

    @Override // javax.xml.bind.annotation.XmlElementRef
    public boolean required() {
        return this.core.required();
    }
}
