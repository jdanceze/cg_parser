package com.sun.xml.bind.v2.schemagen.xmlschema;

import com.sun.xml.txw2.TypedXmlWriter;
import com.sun.xml.txw2.annotation.XmlAttribute;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/schemagen/xmlschema/Wildcard.class */
public interface Wildcard extends Annotated, TypedXmlWriter {
    @XmlAttribute
    Wildcard processContents(String str);

    @XmlAttribute
    Wildcard namespace(String[] strArr);

    @XmlAttribute
    Wildcard namespace(String str);
}
