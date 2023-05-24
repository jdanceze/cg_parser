package com.sun.xml.bind.v2.runtime.property;

import com.sun.istack.localization.Localizable;
import com.sun.xml.bind.v2.runtime.unmarshaller.ChildLoader;
import com.sun.xml.bind.v2.util.QNameMap;
import javax.xml.namespace.QName;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/runtime/property/StructureLoaderBuilder.class */
public interface StructureLoaderBuilder {
    public static final QName TEXT_HANDLER = new QName(Localizable.NOT_LOCALIZABLE, "text");
    public static final QName CATCH_ALL = new QName(Localizable.NOT_LOCALIZABLE, "catchAll");

    void buildChildElementUnmarshallers(UnmarshallerChain unmarshallerChain, QNameMap<ChildLoader> qNameMap);
}
