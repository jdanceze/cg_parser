package com.sun.xml.bind.v2.model.runtime;

import com.sun.xml.bind.v2.model.core.NonElement;
import com.sun.xml.bind.v2.runtime.Transducer;
import java.lang.reflect.Type;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/model/runtime/RuntimeNonElement.class */
public interface RuntimeNonElement extends NonElement<Type, Class>, RuntimeTypeInfo {
    <V> Transducer<V> getTransducer();
}
