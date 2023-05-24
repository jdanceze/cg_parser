package com.sun.xml.bind.v2.model.core;

import javax.xml.namespace.QName;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/model/core/TypeRef.class */
public interface TypeRef<T, C> extends NonElementRef<T, C> {
    QName getTagName();

    boolean isNillable();

    String getDefaultValue();
}
