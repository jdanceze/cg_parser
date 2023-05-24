package com.sun.xml.bind.v2.model.core;

import java.util.List;
import javax.xml.namespace.QName;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/model/core/ElementPropertyInfo.class */
public interface ElementPropertyInfo<T, C> extends PropertyInfo<T, C> {
    List<? extends TypeRef<T, C>> getTypes();

    QName getXmlName();

    boolean isCollectionRequired();

    boolean isCollectionNillable();

    boolean isValueList();

    boolean isRequired();

    @Override // com.sun.xml.bind.v2.model.core.PropertyInfo
    Adapter<T, C> getAdapter();
}
