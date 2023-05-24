package com.sun.xml.bind.v2.model.runtime;

import com.sun.xml.bind.v2.model.core.LeafInfo;
import com.sun.xml.bind.v2.runtime.Transducer;
import java.lang.reflect.Type;
import javax.xml.namespace.QName;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/model/runtime/RuntimeLeafInfo.class */
public interface RuntimeLeafInfo extends LeafInfo<Type, Class>, RuntimeNonElement {
    <V> Transducer<V> getTransducer();

    Class getClazz();

    QName[] getTypeNames();
}
