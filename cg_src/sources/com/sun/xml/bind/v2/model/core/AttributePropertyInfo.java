package com.sun.xml.bind.v2.model.core;

import javax.xml.namespace.QName;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/model/core/AttributePropertyInfo.class */
public interface AttributePropertyInfo<T, C> extends PropertyInfo<T, C>, NonElementRef<T, C> {
    @Override // com.sun.xml.bind.v2.model.core.NonElementRef
    NonElement<T, C> getTarget();

    boolean isRequired();

    QName getXmlName();

    @Override // com.sun.xml.bind.v2.model.core.PropertyInfo
    Adapter<T, C> getAdapter();
}
