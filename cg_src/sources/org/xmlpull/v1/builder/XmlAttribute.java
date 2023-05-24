package org.xmlpull.v1.builder;
/* loaded from: gencallgraphv3.jar:xpp3-1.1.4c.jar:org/xmlpull/v1/builder/XmlAttribute.class */
public interface XmlAttribute extends Cloneable {
    Object clone() throws CloneNotSupportedException;

    XmlElement getOwner();

    String getNamespaceName();

    XmlNamespace getNamespace();

    String getName();

    String getValue();

    String getType();

    boolean isSpecified();
}
