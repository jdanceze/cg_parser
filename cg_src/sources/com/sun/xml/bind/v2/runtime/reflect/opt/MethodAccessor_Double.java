package com.sun.xml.bind.v2.runtime.reflect.opt;

import com.sun.xml.bind.v2.runtime.reflect.Accessor;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/runtime/reflect/opt/MethodAccessor_Double.class */
public class MethodAccessor_Double extends Accessor {
    public MethodAccessor_Double() {
        super(Double.class);
    }

    @Override // com.sun.xml.bind.v2.runtime.reflect.Accessor
    public Object get(Object bean) {
        return Double.valueOf(((Bean) bean).get_double());
    }

    @Override // com.sun.xml.bind.v2.runtime.reflect.Accessor
    public void set(Object bean, Object value) {
        ((Bean) bean).set_double(value == null ? Const.default_value_double : ((Double) value).doubleValue());
    }
}
