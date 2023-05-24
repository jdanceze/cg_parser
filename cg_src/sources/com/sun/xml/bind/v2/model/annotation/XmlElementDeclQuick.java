package com.sun.xml.bind.v2.model.annotation;

import java.lang.annotation.Annotation;
import javax.xml.bind.annotation.XmlElementDecl;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/model/annotation/XmlElementDeclQuick.class */
final class XmlElementDeclQuick extends Quick implements XmlElementDecl {
    private final XmlElementDecl core;

    public XmlElementDeclQuick(Locatable upstream, XmlElementDecl core) {
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
        return new XmlElementDeclQuick(upstream, (XmlElementDecl) core);
    }

    @Override // java.lang.annotation.Annotation
    public Class<XmlElementDecl> annotationType() {
        return XmlElementDecl.class;
    }

    @Override // javax.xml.bind.annotation.XmlElementDecl
    public String name() {
        return this.core.name();
    }

    @Override // javax.xml.bind.annotation.XmlElementDecl
    public Class scope() {
        return this.core.scope();
    }

    @Override // javax.xml.bind.annotation.XmlElementDecl
    public String namespace() {
        return this.core.namespace();
    }

    @Override // javax.xml.bind.annotation.XmlElementDecl
    public String defaultValue() {
        return this.core.defaultValue();
    }

    @Override // javax.xml.bind.annotation.XmlElementDecl
    public String substitutionHeadNamespace() {
        return this.core.substitutionHeadNamespace();
    }

    @Override // javax.xml.bind.annotation.XmlElementDecl
    public String substitutionHeadName() {
        return this.core.substitutionHeadName();
    }
}
