package com.sun.xml.bind.v2.runtime.reflect.opt;

import com.sun.xml.bind.v2.runtime.reflect.Accessor;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/runtime/reflect/opt/MethodAccessor_Boolean.class */
public class MethodAccessor_Boolean extends Accessor {
    public MethodAccessor_Boolean() {
        super(Boolean.class);
    }

    @Override // com.sun.xml.bind.v2.runtime.reflect.Accessor
    public Object get(Object bean) {
        return Boolean.valueOf(((Bean) bean).get_boolean());
    }

    @Override // com.sun.xml.bind.v2.runtime.reflect.Accessor
    public void set(Object bean, Object value) {
        ((Bean) bean).set_boolean(value == null ? false : ((Boolean) value).booleanValue());
    }
}
