package com.sun.xml.bind.v2.model.runtime;

import com.sun.xml.bind.v2.model.core.AttributePropertyInfo;
import com.sun.xml.bind.v2.model.core.NonElement;
import java.lang.reflect.Type;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/model/runtime/RuntimeAttributePropertyInfo.class */
public interface RuntimeAttributePropertyInfo extends AttributePropertyInfo<Type, Class>, RuntimePropertyInfo, RuntimeNonElementRef {
    @Override // com.sun.xml.bind.v2.model.core.AttributePropertyInfo, com.sun.xml.bind.v2.model.core.NonElementRef
    NonElement<Type, Class> getTarget();
}
