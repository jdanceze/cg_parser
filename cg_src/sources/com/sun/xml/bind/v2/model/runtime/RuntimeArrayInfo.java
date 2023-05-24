package com.sun.xml.bind.v2.model.runtime;

import com.sun.xml.bind.v2.model.core.ArrayInfo;
import com.sun.xml.bind.v2.model.core.NonElement;
import java.lang.reflect.Type;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/model/runtime/RuntimeArrayInfo.class */
public interface RuntimeArrayInfo extends ArrayInfo<Type, Class>, RuntimeNonElement {
    @Override // com.sun.xml.bind.v2.model.core.TypeInfo
    Type getType();

    @Override // com.sun.xml.bind.v2.model.core.ArrayInfo
    NonElement<Type, Class> getItemType();
}
