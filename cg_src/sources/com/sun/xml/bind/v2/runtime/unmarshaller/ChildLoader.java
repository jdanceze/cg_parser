package com.sun.xml.bind.v2.runtime.unmarshaller;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/runtime/unmarshaller/ChildLoader.class */
public final class ChildLoader {
    public final Loader loader;
    public final Receiver receiver;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !ChildLoader.class.desiredAssertionStatus();
    }

    public ChildLoader(Loader loader, Receiver receiver) {
        if (!$assertionsDisabled && loader == null) {
            throw new AssertionError();
        }
        this.loader = loader;
        this.receiver = receiver;
    }
}
