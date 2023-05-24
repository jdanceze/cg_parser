package com.sun.xml.bind.util;

import com.sun.xml.bind.ValidationEventLocatorEx;
import javax.xml.bind.helpers.ValidationEventLocatorImpl;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/util/ValidationEventLocatorExImpl.class */
public class ValidationEventLocatorExImpl extends ValidationEventLocatorImpl implements ValidationEventLocatorEx {
    private final String fieldName;

    public ValidationEventLocatorExImpl(Object target, String fieldName) {
        super(target);
        this.fieldName = fieldName;
    }

    @Override // com.sun.xml.bind.ValidationEventLocatorEx
    public String getFieldName() {
        return this.fieldName;
    }

    @Override // javax.xml.bind.helpers.ValidationEventLocatorImpl
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("[url=");
        buf.append(getURL());
        buf.append(",line=");
        buf.append(getLineNumber());
        buf.append(",column=");
        buf.append(getColumnNumber());
        buf.append(",node=");
        buf.append(getNode());
        buf.append(",object=");
        buf.append(getObject());
        buf.append(",field=");
        buf.append(getFieldName());
        buf.append("]");
        return buf.toString();
    }
}
