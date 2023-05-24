package com.sun.xml.bind.v2.schemagen.xmlschema;

import com.sun.xml.txw2.TypedXmlWriter;
import com.sun.xml.txw2.annotation.XmlAttribute;
import com.sun.xml.txw2.annotation.XmlElement;
@XmlElement("schema")
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/schemagen/xmlschema/Schema.class */
public interface Schema extends SchemaTop, TypedXmlWriter {
    @XmlElement
    Annotation annotation();

    @XmlElement("import")
    Import _import();

    @XmlAttribute
    Schema targetNamespace(String str);

    @XmlAttribute(ns = "http://www.w3.org/XML/1998/namespace")
    Schema lang(String str);

    @XmlAttribute
    Schema id(String str);

    @XmlAttribute
    Schema elementFormDefault(String str);

    @XmlAttribute
    Schema attributeFormDefault(String str);

    @XmlAttribute
    Schema blockDefault(String[] strArr);

    @XmlAttribute
    Schema blockDefault(String str);

    @XmlAttribute
    Schema finalDefault(String[] strArr);

    @XmlAttribute
    Schema finalDefault(String str);

    @XmlAttribute
    Schema version(String str);
}
