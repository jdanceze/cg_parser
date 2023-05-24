package com.sun.xml.bind.v2.model.runtime;

import com.sun.xml.bind.v2.model.core.NonElement;
import com.sun.xml.bind.v2.model.core.PropertyInfo;
import com.sun.xml.bind.v2.model.core.TypeRef;
import java.lang.reflect.Type;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/model/runtime/RuntimeTypeRef.class */
public interface RuntimeTypeRef extends TypeRef<Type, Class>, RuntimeNonElementRef {
    @Override // com.sun.xml.bind.v2.model.core.NonElementRef
    NonElement<Type, Class> getTarget();

    @Override // com.sun.xml.bind.v2.model.core.NonElementRef
    PropertyInfo<Type, Class> getSource();
}
