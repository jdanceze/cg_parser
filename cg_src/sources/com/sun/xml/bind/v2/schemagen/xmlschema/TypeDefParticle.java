package com.sun.xml.bind.v2.schemagen.xmlschema;

import com.sun.xml.txw2.TypedXmlWriter;
import com.sun.xml.txw2.annotation.XmlElement;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/schemagen/xmlschema/TypeDefParticle.class */
public interface TypeDefParticle extends TypedXmlWriter {
    @XmlElement
    ExplicitGroup all();

    @XmlElement
    ExplicitGroup sequence();

    @XmlElement
    ExplicitGroup choice();
}
