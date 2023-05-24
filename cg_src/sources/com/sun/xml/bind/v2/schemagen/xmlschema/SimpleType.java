package com.sun.xml.bind.v2.schemagen.xmlschema;

import com.sun.xml.txw2.TypedXmlWriter;
import com.sun.xml.txw2.annotation.XmlAttribute;
import com.sun.xml.txw2.annotation.XmlElement;
import soot.jimple.Jimple;
@XmlElement("simpleType")
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/schemagen/xmlschema/SimpleType.class */
public interface SimpleType extends Annotated, SimpleDerivation, TypedXmlWriter {
    @XmlAttribute(Jimple.FINAL)
    SimpleType _final(String str);

    @XmlAttribute(Jimple.FINAL)
    SimpleType _final(String[] strArr);

    @XmlAttribute
    SimpleType name(String str);
}
