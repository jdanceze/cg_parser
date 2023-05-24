package com.sun.xml.bind.v2.model.core;

import javax.xml.namespace.QName;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/model/core/NonElement.class */
public interface NonElement<T, C> extends TypeInfo<T, C> {
    public static final QName ANYTYPE_NAME = new QName("http://www.w3.org/2001/XMLSchema", "anyType");

    QName getTypeName();

    boolean isSimpleType();
}
