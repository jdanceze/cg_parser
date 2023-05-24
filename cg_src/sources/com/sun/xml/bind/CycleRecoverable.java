package com.sun.xml.bind;

import javax.xml.bind.Marshaller;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/CycleRecoverable.class */
public interface CycleRecoverable {

    /* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/CycleRecoverable$Context.class */
    public interface Context {
        Marshaller getMarshaller();
    }

    Object onCycleDetected(Context context);
}
