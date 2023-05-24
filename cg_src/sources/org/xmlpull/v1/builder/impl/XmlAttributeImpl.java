package org.xmlpull.v1.builder.impl;

import org.xmlpull.v1.builder.XmlAttribute;
import org.xmlpull.v1.builder.XmlElement;
import org.xmlpull.v1.builder.XmlNamespace;
/* loaded from: gencallgraphv3.jar:xpp3-1.1.4c.jar:org/xmlpull/v1/builder/impl/XmlAttributeImpl.class */
public class XmlAttributeImpl implements XmlAttribute {
    private XmlElement owner_;
    private String prefix_;
    private XmlNamespace namespace_;
    private String name_;
    private String value_;
    private String type_;
    private boolean default_;

    @Override // org.xmlpull.v1.builder.XmlAttribute
    public Object clone() throws CloneNotSupportedException {
        XmlAttributeImpl cloned = (XmlAttributeImpl) super.clone();
        cloned.owner_ = null;
        cloned.prefix_ = this.prefix_;
        cloned.namespace_ = this.namespace_;
        cloned.name_ = this.name_;
        cloned.value_ = this.value_;
        cloned.default_ = this.default_;
        return cloned;
    }

    XmlAttributeImpl(XmlElement owner, String name, String value) {
        this.type_ = "CDATA";
        this.owner_ = owner;
        this.name_ = name;
        if (value == null) {
            throw new IllegalArgumentException("attribute value can not be null");
        }
        this.value_ = value;
    }

    XmlAttributeImpl(XmlElement owner, XmlNamespace namespace, String name, String value) {
        this(owner, name, value);
        this.namespace_ = namespace;
    }

    XmlAttributeImpl(XmlElement owner, String type, XmlNamespace namespace, String name, String value) {
        this(owner, namespace, name, value);
        this.type_ = type;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public XmlAttributeImpl(XmlElement owner, String type, XmlNamespace namespace, String name, String value, boolean specified) {
        this(owner, namespace, name, value);
        if (type == null) {
            throw new IllegalArgumentException("attribute type can not be null");
        }
        this.type_ = type;
        this.default_ = !specified;
    }

    @Override // org.xmlpull.v1.builder.XmlAttribute
    public XmlElement getOwner() {
        return this.owner_;
    }

    @Override // org.xmlpull.v1.builder.XmlAttribute
    public XmlNamespace getNamespace() {
        return this.namespace_;
    }

    @Override // org.xmlpull.v1.builder.XmlAttribute
    public String getNamespaceName() {
        if (this.namespace_ != null) {
            return this.namespace_.getNamespaceName();
        }
        return null;
    }

    @Override // org.xmlpull.v1.builder.XmlAttribute
    public String getName() {
        return this.name_;
    }

    @Override // org.xmlpull.v1.builder.XmlAttribute
    public String getValue() {
        return this.value_;
    }

    @Override // org.xmlpull.v1.builder.XmlAttribute
    public String getType() {
        return this.type_;
    }

    @Override // org.xmlpull.v1.builder.XmlAttribute
    public boolean isSpecified() {
        return !this.default_;
    }

    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (other != null && (other instanceof XmlAttribute)) {
            XmlAttribute otherAttr = (XmlAttribute) other;
            return getNamespaceName().equals(otherAttr.getNamespaceName()) && getName().equals(otherAttr.getName()) && getValue().equals(otherAttr.getValue());
        }
        return false;
    }

    public String toString() {
        return new StringBuffer().append("name=").append(this.name_).append(" value=").append(this.value_).toString();
    }
}
