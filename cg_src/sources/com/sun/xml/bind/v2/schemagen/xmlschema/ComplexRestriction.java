package com.sun.xml.bind.v2.schemagen.xmlschema;

import com.sun.xml.txw2.TypedXmlWriter;
import com.sun.xml.txw2.annotation.XmlAttribute;
import com.sun.xml.txw2.annotation.XmlElement;
import javax.xml.namespace.QName;
@XmlElement("restriction")
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/schemagen/xmlschema/ComplexRestriction.class */
public interface ComplexRestriction extends Annotated, AttrDecls, TypeDefParticle, TypedXmlWriter {
    @XmlAttribute
    ComplexRestriction base(QName qName);
}
