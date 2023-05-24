package com.sun.xml.bind.v2.runtime.unmarshaller;

import com.sun.xml.bind.v2.runtime.unmarshaller.UnmarshallingContext;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/runtime/unmarshaller/Discarder.class */
public final class Discarder extends Loader {
    public static final Loader INSTANCE = new Discarder();

    private Discarder() {
        super(false);
    }

    @Override // com.sun.xml.bind.v2.runtime.unmarshaller.Loader
    public void childElement(UnmarshallingContext.State state, TagName ea) {
        state.setTarget(null);
        state.setLoader(this);
    }
}
