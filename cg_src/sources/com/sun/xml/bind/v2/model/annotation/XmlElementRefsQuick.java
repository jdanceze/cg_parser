package com.sun.xml.bind.v2.model.annotation;

import java.lang.annotation.Annotation;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/model/annotation/XmlElementRefsQuick.class */
final class XmlElementRefsQuick extends Quick implements XmlElementRefs {
    private final XmlElementRefs core;

    public XmlElementRefsQuick(Locatable upstream, XmlElementRefs core) {
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
        return new XmlElementRefsQuick(upstream, (XmlElementRefs) core);
    }

    @Override // java.lang.annotation.Annotation
    public Class<XmlElementRefs> annotationType() {
        return XmlElementRefs.class;
    }

    @Override // javax.xml.bind.annotation.XmlElementRefs
    public XmlElementRef[] value() {
        return this.core.value();
    }
}
