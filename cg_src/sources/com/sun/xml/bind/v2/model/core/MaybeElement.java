package com.sun.xml.bind.v2.model.core;

import javax.xml.namespace.QName;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/model/core/MaybeElement.class */
public interface MaybeElement<T, C> extends NonElement<T, C> {
    boolean isElement();

    QName getElementName();

    Element<T, C> asElement();
}
