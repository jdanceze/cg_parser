package com.sun.xml.bind.v2.schemagen.episode;

import com.sun.xml.txw2.TypedXmlWriter;
import com.sun.xml.txw2.annotation.XmlAttribute;
import com.sun.xml.txw2.annotation.XmlElement;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/schemagen/episode/SchemaBindings.class */
public interface SchemaBindings extends TypedXmlWriter {
    @XmlAttribute
    void map(boolean z);

    @XmlElement("package")
    Package _package();
}
