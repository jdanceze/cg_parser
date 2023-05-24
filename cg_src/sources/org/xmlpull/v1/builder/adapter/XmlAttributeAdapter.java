package org.xmlpull.v1.builder.adapter;

import org.xmlpull.v1.builder.XmlAttribute;
import org.xmlpull.v1.builder.XmlElement;
import org.xmlpull.v1.builder.XmlNamespace;
/* loaded from: gencallgraphv3.jar:xpp3-1.1.4c.jar:org/xmlpull/v1/builder/adapter/XmlAttributeAdapter.class */
public class XmlAttributeAdapter implements XmlAttribute {
    private XmlAttribute target;

    @Override // org.xmlpull.v1.builder.XmlAttribute
    public Object clone() throws CloneNotSupportedException {
        XmlAttributeAdapter ela = (XmlAttributeAdapter) super.clone();
        ela.target = (XmlAttribute) this.target.clone();
        return ela;
    }

    public XmlAttributeAdapter(XmlAttribute target) {
        this.target = target;
    }

    @Override // org.xmlpull.v1.builder.XmlAttribute
    public XmlElement getOwner() {
        return this.target.getOwner();
    }

    @Override // org.xmlpull.v1.builder.XmlAttribute
    public String getNamespaceName() {
        return this.target.getNamespaceName();
    }

    @Override // org.xmlpull.v1.builder.XmlAttribute
    public XmlNamespace getNamespace() {
        return this.target.getNamespace();
    }

    @Override // org.xmlpull.v1.builder.XmlAttribute
    public String getName() {
        return this.target.getName();
    }

    @Override // org.xmlpull.v1.builder.XmlAttribute
    public String getValue() {
        return this.target.getValue();
    }

    @Override // org.xmlpull.v1.builder.XmlAttribute
    public String getType() {
        return this.target.getType();
    }

    @Override // org.xmlpull.v1.builder.XmlAttribute
    public boolean isSpecified() {
        return this.target.isSpecified();
    }
}
