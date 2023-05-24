package com.sun.xml.bind.v2.model.runtime;

import com.sun.xml.bind.v2.model.core.NonElement;
import com.sun.xml.bind.v2.model.core.ValuePropertyInfo;
import java.lang.reflect.Type;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/model/runtime/RuntimeValuePropertyInfo.class */
public interface RuntimeValuePropertyInfo extends ValuePropertyInfo<Type, Class>, RuntimePropertyInfo, RuntimeNonElementRef {
    @Override // com.sun.xml.bind.v2.model.core.NonElementRef
    NonElement<Type, Class> getTarget();
}
