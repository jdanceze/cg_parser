package com.sun.xml.bind.v2.model.core;

import java.util.Collection;
import java.util.Set;
import javax.xml.namespace.QName;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/model/core/ReferencePropertyInfo.class */
public interface ReferencePropertyInfo<T, C> extends PropertyInfo<T, C> {
    Set<? extends Element<T, C>> getElements();

    @Override // com.sun.xml.bind.v2.model.core.PropertyInfo
    Collection<? extends TypeInfo<T, C>> ref();

    QName getXmlName();

    boolean isCollectionNillable();

    boolean isCollectionRequired();

    boolean isMixed();

    WildcardMode getWildcard();

    C getDOMHandler();

    boolean isRequired();

    @Override // com.sun.xml.bind.v2.model.core.PropertyInfo
    Adapter<T, C> getAdapter();
}
