package com.sun.xml.bind.v2.model.runtime;

import com.sun.xml.bind.v2.model.core.Element;
import com.sun.xml.bind.v2.model.core.ReferencePropertyInfo;
import java.lang.reflect.Type;
import java.util.Set;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/model/runtime/RuntimeReferencePropertyInfo.class */
public interface RuntimeReferencePropertyInfo extends ReferencePropertyInfo<Type, Class>, RuntimePropertyInfo {
    @Override // com.sun.xml.bind.v2.model.core.ReferencePropertyInfo
    Set<? extends Element<Type, Class>> getElements();
}
