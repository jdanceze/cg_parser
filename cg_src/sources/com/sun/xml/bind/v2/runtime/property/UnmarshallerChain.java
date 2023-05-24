package com.sun.xml.bind.v2.runtime.property;

import com.sun.xml.bind.v2.runtime.JAXBContextImpl;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/runtime/property/UnmarshallerChain.class */
public final class UnmarshallerChain {
    private int offset = 0;
    public final JAXBContextImpl context;

    public UnmarshallerChain(JAXBContextImpl context) {
        this.context = context;
    }

    public int allocateOffset() {
        int i = this.offset;
        this.offset = i + 1;
        return i;
    }

    public int getScopeSize() {
        return this.offset;
    }
}
