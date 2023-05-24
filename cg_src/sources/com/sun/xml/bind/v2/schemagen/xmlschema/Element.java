package com.sun.xml.bind.v2.schemagen.xmlschema;

import com.sun.xml.txw2.TypedXmlWriter;
import com.sun.xml.txw2.annotation.XmlAttribute;
import javax.xml.namespace.QName;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/schemagen/xmlschema/Element.class */
public interface Element extends Annotated, ComplexTypeHost, FixedOrDefault, SimpleTypeHost, TypedXmlWriter {
    @XmlAttribute
    Element type(QName qName);

    @XmlAttribute
    Element block(String[] strArr);

    @XmlAttribute
    Element block(String str);

    @XmlAttribute
    Element nillable(boolean z);
}
