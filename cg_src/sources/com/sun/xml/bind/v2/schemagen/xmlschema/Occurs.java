package com.sun.xml.bind.v2.schemagen.xmlschema;

import com.sun.xml.txw2.TypedXmlWriter;
import com.sun.xml.txw2.annotation.XmlAttribute;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/schemagen/xmlschema/Occurs.class */
public interface Occurs extends TypedXmlWriter {
    @XmlAttribute
    Occurs minOccurs(int i);

    @XmlAttribute
    Occurs maxOccurs(String str);

    @XmlAttribute
    Occurs maxOccurs(int i);
}
