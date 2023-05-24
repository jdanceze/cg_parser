package com.sun.xml.fastinfoset.stax.events;

import javax.xml.namespace.QName;
import javax.xml.stream.events.Attribute;
/* loaded from: gencallgraphv3.jar:FastInfoset-1.2.15.jar:com/sun/xml/fastinfoset/stax/events/AttributeBase.class */
public class AttributeBase extends EventBase implements Attribute {
    private QName _QName;
    private String _value;
    private String _attributeType;
    private boolean _specified;

    public AttributeBase() {
        super(10);
        this._attributeType = null;
        this._specified = false;
    }

    public AttributeBase(String name, String value) {
        super(10);
        this._attributeType = null;
        this._specified = false;
        this._QName = new QName(name);
        this._value = value;
    }

    public AttributeBase(QName qname, String value) {
        this._attributeType = null;
        this._specified = false;
        this._QName = qname;
        this._value = value;
    }

    public AttributeBase(String prefix, String localName, String value) {
        this(prefix, null, localName, value, null);
    }

    public AttributeBase(String prefix, String namespaceURI, String localName, String value, String attributeType) {
        this._attributeType = null;
        this._specified = false;
        this._QName = new QName(namespaceURI, localName, prefix == null ? "" : prefix);
        this._value = value;
        this._attributeType = attributeType == null ? "CDATA" : attributeType;
    }

    public void setName(QName name) {
        this._QName = name;
    }

    public QName getName() {
        return this._QName;
    }

    public void setValue(String value) {
        this._value = value;
    }

    public String getLocalName() {
        return this._QName.getLocalPart();
    }

    public String getValue() {
        return this._value;
    }

    public void setAttributeType(String attributeType) {
        this._attributeType = attributeType;
    }

    public String getDTDType() {
        return this._attributeType;
    }

    public boolean isSpecified() {
        return this._specified;
    }

    public void setSpecified(boolean isSpecified) {
        this._specified = isSpecified;
    }

    public String toString() {
        String prefix = this._QName.getPrefix();
        if (!Util.isEmptyString(prefix)) {
            return prefix + ":" + this._QName.getLocalPart() + "='" + this._value + "'";
        }
        return this._QName.getLocalPart() + "='" + this._value + "'";
    }
}
