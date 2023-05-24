package com.sun.xml.bind.v2.schemagen.episode;

import com.sun.xml.txw2.TypedXmlWriter;
import com.sun.xml.txw2.annotation.XmlAttribute;
import com.sun.xml.txw2.annotation.XmlElement;
@XmlElement("bindings")
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/schemagen/episode/Bindings.class */
public interface Bindings extends TypedXmlWriter {
    @XmlElement
    Bindings bindings();

    @XmlElement("class")
    Klass klass();

    Klass typesafeEnumClass();

    @XmlElement
    SchemaBindings schemaBindings();

    @XmlAttribute
    void scd(String str);

    @XmlAttribute
    void version(String str);
}
