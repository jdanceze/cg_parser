package com.sun.xml.bind.v2.schemagen.xmlschema;

import com.sun.xml.txw2.TypedXmlWriter;
import com.sun.xml.txw2.annotation.XmlElement;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/schemagen/xmlschema/SimpleDerivation.class */
public interface SimpleDerivation extends TypedXmlWriter {
    @XmlElement
    SimpleRestriction restriction();

    @XmlElement
    Union union();

    @XmlElement
    List list();
}
