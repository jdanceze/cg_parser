package com.sun.xml.bind.v2.schemagen.xmlschema;

import com.sun.xml.txw2.TypedXmlWriter;
import com.sun.xml.txw2.annotation.XmlAttribute;
import com.sun.xml.txw2.annotation.XmlElement;
import javax.xml.namespace.QName;
import soot.jimple.Jimple;
@XmlElement("element")
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/schemagen/xmlschema/TopLevelElement.class */
public interface TopLevelElement extends Element, TypedXmlWriter {
    @XmlAttribute(Jimple.FINAL)
    TopLevelElement _final(String[] strArr);

    @XmlAttribute(Jimple.FINAL)
    TopLevelElement _final(String str);

    @XmlAttribute(Jimple.ABSTRACT)
    TopLevelElement _abstract(boolean z);

    @XmlAttribute
    TopLevelElement substitutionGroup(QName qName);

    @XmlAttribute
    TopLevelElement name(String str);
}
