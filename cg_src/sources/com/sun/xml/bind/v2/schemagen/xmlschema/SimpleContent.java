package com.sun.xml.bind.v2.schemagen.xmlschema;

import com.sun.xml.txw2.TypedXmlWriter;
import com.sun.xml.txw2.annotation.XmlElement;
@XmlElement("simpleContent")
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/schemagen/xmlschema/SimpleContent.class */
public interface SimpleContent extends Annotated, TypedXmlWriter {
    @XmlElement
    SimpleExtension extension();

    @XmlElement
    SimpleRestriction restriction();
}
