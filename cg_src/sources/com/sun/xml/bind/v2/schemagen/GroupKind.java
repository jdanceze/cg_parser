package com.sun.xml.bind.v2.schemagen;

import com.sun.xml.bind.v2.schemagen.xmlschema.ContentModelContainer;
import com.sun.xml.bind.v2.schemagen.xmlschema.Particle;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/schemagen/GroupKind.class */
public enum GroupKind {
    ALL("all"),
    SEQUENCE("sequence"),
    CHOICE("choice");
    
    private final String name;

    GroupKind(String name) {
        this.name = name;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Particle write(ContentModelContainer parent) {
        return (Particle) parent._element(this.name, Particle.class);
    }
}
