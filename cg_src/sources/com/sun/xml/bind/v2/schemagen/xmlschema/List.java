package com.sun.xml.bind.v2.schemagen.xmlschema;

import com.sun.xml.txw2.TypedXmlWriter;
import com.sun.xml.txw2.annotation.XmlAttribute;
import com.sun.xml.txw2.annotation.XmlElement;
import javax.xml.namespace.QName;
import org.apache.tools.ant.taskdefs.optional.j2ee.HotDeploymentTool;
@XmlElement(HotDeploymentTool.ACTION_LIST)
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/schemagen/xmlschema/List.class */
public interface List extends Annotated, SimpleTypeHost, TypedXmlWriter {
    @XmlAttribute
    List itemType(QName qName);
}
