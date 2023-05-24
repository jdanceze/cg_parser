package com.sun.xml.bind.v2.model.runtime;

import com.sun.xml.bind.v2.model.core.MapPropertyInfo;
import com.sun.xml.bind.v2.model.core.NonElement;
import java.lang.reflect.Type;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/model/runtime/RuntimeMapPropertyInfo.class */
public interface RuntimeMapPropertyInfo extends RuntimePropertyInfo, MapPropertyInfo<Type, Class> {
    @Override // com.sun.xml.bind.v2.model.core.MapPropertyInfo
    NonElement<Type, Class> getKeyType();

    @Override // com.sun.xml.bind.v2.model.core.MapPropertyInfo
    NonElement<Type, Class> getValueType();
}
