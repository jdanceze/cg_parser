package com.sun.xml.bind;

import javax.xml.bind.annotation.adapters.XmlAdapter;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/AnyTypeAdapter.class */
public final class AnyTypeAdapter extends XmlAdapter<Object, Object> {
    @Override // javax.xml.bind.annotation.adapters.XmlAdapter
    public Object unmarshal(Object v) {
        return v;
    }

    @Override // javax.xml.bind.annotation.adapters.XmlAdapter
    public Object marshal(Object v) {
        return v;
    }
}
