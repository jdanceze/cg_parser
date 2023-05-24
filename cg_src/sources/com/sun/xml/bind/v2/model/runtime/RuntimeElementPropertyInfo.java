package com.sun.xml.bind.v2.model.runtime;

import com.sun.xml.bind.v2.model.core.ElementPropertyInfo;
import com.sun.xml.bind.v2.model.core.TypeInfo;
import com.sun.xml.bind.v2.model.core.TypeRef;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/model/runtime/RuntimeElementPropertyInfo.class */
public interface RuntimeElementPropertyInfo extends ElementPropertyInfo<Type, Class>, RuntimePropertyInfo {
    @Override // com.sun.xml.bind.v2.model.core.PropertyInfo
    Collection<? extends TypeInfo<Type, Class>> ref();

    @Override // com.sun.xml.bind.v2.model.core.ElementPropertyInfo
    List<? extends TypeRef<Type, Class>> getTypes();
}
