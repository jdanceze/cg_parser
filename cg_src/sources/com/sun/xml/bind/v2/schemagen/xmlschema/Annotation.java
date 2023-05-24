package com.sun.xml.bind.v2.schemagen.xmlschema;

import com.sun.xml.txw2.TypedXmlWriter;
import com.sun.xml.txw2.annotation.XmlAttribute;
import com.sun.xml.txw2.annotation.XmlElement;
import soot.jimple.Jimple;
@XmlElement(Jimple.ANNOTATION)
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/schemagen/xmlschema/Annotation.class */
public interface Annotation extends TypedXmlWriter {
    @XmlElement
    Appinfo appinfo();

    @XmlElement
    Documentation documentation();

    @XmlAttribute
    Annotation id(String str);
}
