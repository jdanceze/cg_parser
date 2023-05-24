package com.sun.xml.bind.v2.model.runtime;

import com.sun.xml.bind.v2.model.core.ClassInfo;
import com.sun.xml.bind.v2.model.core.ElementInfo;
import com.sun.xml.bind.v2.model.core.ElementPropertyInfo;
import com.sun.xml.bind.v2.model.core.NonElement;
import java.lang.reflect.Type;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/model/runtime/RuntimeElementInfo.class */
public interface RuntimeElementInfo extends ElementInfo<Type, Class>, RuntimeElement {
    @Override // com.sun.xml.bind.v2.model.core.Element
    ClassInfo<Type, Class> getScope();

    @Override // com.sun.xml.bind.v2.model.core.ElementInfo
    ElementPropertyInfo<Type, Class> getProperty();

    @Override // com.sun.xml.bind.v2.model.core.ElementInfo, com.sun.xml.bind.v2.model.core.TypeInfo
    Type getType();

    @Override // com.sun.xml.bind.v2.model.core.ElementInfo
    NonElement<Type, Class> getContentType();
}
