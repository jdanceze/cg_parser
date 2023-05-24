package com.sun.xml.bind.v2.model.core;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/model/core/EnumLeafInfo.class */
public interface EnumLeafInfo<T, C> extends LeafInfo<T, C> {
    C getClazz();

    NonElement<T, C> getBaseType();

    Iterable<? extends EnumConstant> getConstants();
}
