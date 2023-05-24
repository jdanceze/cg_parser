package com.sun.xml.bind.v2.runtime.reflect.opt;

import com.sun.xml.bind.DatatypeConverterImpl;
import com.sun.xml.bind.v2.runtime.reflect.DefaultTransducedAccessor;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/runtime/reflect/opt/TransducedAccessor_method_Boolean.class */
public final class TransducedAccessor_method_Boolean extends DefaultTransducedAccessor {
    @Override // com.sun.xml.bind.v2.runtime.reflect.DefaultTransducedAccessor, com.sun.xml.bind.v2.runtime.reflect.TransducedAccessor
    public String print(Object o) {
        return DatatypeConverterImpl._printBoolean(((Bean) o).get_boolean());
    }

    @Override // com.sun.xml.bind.v2.runtime.reflect.TransducedAccessor
    public void parse(Object o, CharSequence lexical) {
        ((Bean) o).set_boolean(DatatypeConverterImpl._parseBoolean(lexical).booleanValue());
    }

    @Override // com.sun.xml.bind.v2.runtime.reflect.TransducedAccessor
    public boolean hasValue(Object o) {
        return true;
    }
}
