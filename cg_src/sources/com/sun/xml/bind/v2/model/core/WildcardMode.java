package com.sun.xml.bind.v2.model.core;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/model/core/WildcardMode.class */
public enum WildcardMode {
    STRICT(false, true),
    SKIP(true, false),
    LAX(true, true);
    
    public final boolean allowDom;
    public final boolean allowTypedObject;

    WildcardMode(boolean allowDom, boolean allowTypedObject) {
        this.allowDom = allowDom;
        this.allowTypedObject = allowTypedObject;
    }
}
