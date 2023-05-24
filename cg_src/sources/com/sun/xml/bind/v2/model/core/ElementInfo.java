package com.sun.xml.bind.v2.model.core;

import java.util.Collection;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/model/core/ElementInfo.class */
public interface ElementInfo<T, C> extends Element<T, C> {
    ElementPropertyInfo<T, C> getProperty();

    NonElement<T, C> getContentType();

    T getContentInMemoryType();

    @Override // com.sun.xml.bind.v2.model.core.TypeInfo
    T getType();

    @Override // com.sun.xml.bind.v2.model.core.Element
    ElementInfo<T, C> getSubstitutionHead();

    Collection<? extends ElementInfo<T, C>> getSubstitutionMembers();
}
