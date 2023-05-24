package com.sun.xml.bind.v2.model.core;

import javax.xml.namespace.QName;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/model/core/MapPropertyInfo.class */
public interface MapPropertyInfo<T, C> extends PropertyInfo<T, C> {
    QName getXmlName();

    boolean isCollectionNillable();

    NonElement<T, C> getKeyType();

    NonElement<T, C> getValueType();
}
