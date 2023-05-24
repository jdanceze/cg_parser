package com.sun.xml.bind.v2.model.core;

import com.sun.xml.bind.v2.model.annotation.Locatable;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/model/core/TypeInfo.class */
public interface TypeInfo<T, C> extends Locatable {
    T getType();

    boolean canBeReferencedByIDREF();
}
