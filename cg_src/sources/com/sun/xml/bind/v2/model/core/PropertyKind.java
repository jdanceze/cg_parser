package com.sun.xml.bind.v2.model.core;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/model/core/PropertyKind.class */
public enum PropertyKind {
    VALUE(true, false, Integer.MAX_VALUE),
    ATTRIBUTE(false, false, Integer.MAX_VALUE),
    ELEMENT(true, true, 0),
    REFERENCE(false, true, 1),
    MAP(false, true, 2);
    
    public final boolean canHaveXmlMimeType;
    public final boolean isOrdered;
    public final int propertyIndex;

    PropertyKind(boolean canHaveExpectedContentType, boolean isOrdered, int propertyIndex) {
        this.canHaveXmlMimeType = canHaveExpectedContentType;
        this.isOrdered = isOrdered;
        this.propertyIndex = propertyIndex;
    }
}
