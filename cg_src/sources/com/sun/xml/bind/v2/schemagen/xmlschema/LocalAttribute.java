package com.sun.xml.bind.v2.schemagen.xmlschema;

import com.sun.xml.txw2.TypedXmlWriter;
import com.sun.xml.txw2.annotation.XmlAttribute;
import com.sun.xml.txw2.annotation.XmlElement;
import javax.xml.namespace.QName;
@XmlElement("attribute")
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/schemagen/xmlschema/LocalAttribute.class */
public interface LocalAttribute extends Annotated, AttributeType, FixedOrDefault, TypedXmlWriter {
    @XmlAttribute
    LocalAttribute form(String str);

    @XmlAttribute
    LocalAttribute name(String str);

    @XmlAttribute
    LocalAttribute ref(QName qName);

    @XmlAttribute
    LocalAttribute use(String str);
}
