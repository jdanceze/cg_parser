package com.sun.xml.bind.v2.schemagen.xmlschema;

import com.sun.xml.txw2.TypedXmlWriter;
import com.sun.xml.txw2.annotation.XmlAttribute;
import com.sun.xml.txw2.annotation.XmlElement;
@XmlElement("complexContent")
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/schemagen/xmlschema/ComplexContent.class */
public interface ComplexContent extends Annotated, TypedXmlWriter {
    @XmlElement
    ComplexExtension extension();

    @XmlElement
    ComplexRestriction restriction();

    @XmlAttribute
    ComplexContent mixed(boolean z);
}
