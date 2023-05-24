package com.sun.xml.bind.v2.model.annotation;

import java.lang.annotation.Annotation;
import javax.xml.bind.annotation.XmlValue;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/model/annotation/XmlValueQuick.class */
final class XmlValueQuick extends Quick implements XmlValue {
    private final XmlValue core;

    public XmlValueQuick(Locatable upstream, XmlValue core) {
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
        return new XmlValueQuick(upstream, (XmlValue) core);
    }

    @Override // java.lang.annotation.Annotation
    public Class<XmlValue> annotationType() {
        return XmlValue.class;
    }
}
