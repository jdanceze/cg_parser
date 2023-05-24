package com.sun.xml.bind.v2.runtime.unmarshaller;

import com.sun.xml.bind.util.AttributesImpl;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/runtime/unmarshaller/AttributesExImpl.class */
public final class AttributesExImpl extends AttributesImpl implements AttributesEx {
    @Override // com.sun.xml.bind.v2.runtime.unmarshaller.AttributesEx
    public CharSequence getData(int idx) {
        return getValue(idx);
    }

    @Override // com.sun.xml.bind.v2.runtime.unmarshaller.AttributesEx
    public CharSequence getData(String nsUri, String localName) {
        return getValue(nsUri, localName);
    }
}
