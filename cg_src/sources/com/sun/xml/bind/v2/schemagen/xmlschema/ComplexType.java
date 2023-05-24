package com.sun.xml.bind.v2.schemagen.xmlschema;

import com.sun.xml.txw2.TypedXmlWriter;
import com.sun.xml.txw2.annotation.XmlAttribute;
import com.sun.xml.txw2.annotation.XmlElement;
import soot.jimple.Jimple;
@XmlElement("complexType")
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/schemagen/xmlschema/ComplexType.class */
public interface ComplexType extends Annotated, ComplexTypeModel, TypedXmlWriter {
    @XmlAttribute(Jimple.FINAL)
    ComplexType _final(String[] strArr);

    @XmlAttribute(Jimple.FINAL)
    ComplexType _final(String str);

    @XmlAttribute
    ComplexType block(String[] strArr);

    @XmlAttribute
    ComplexType block(String str);

    @XmlAttribute(Jimple.ABSTRACT)
    ComplexType _abstract(boolean z);

    @XmlAttribute
    ComplexType name(String str);
}
