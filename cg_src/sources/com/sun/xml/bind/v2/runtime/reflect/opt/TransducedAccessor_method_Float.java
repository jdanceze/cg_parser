package com.sun.xml.bind.v2.runtime.reflect.opt;

import com.sun.xml.bind.DatatypeConverterImpl;
import com.sun.xml.bind.v2.runtime.reflect.DefaultTransducedAccessor;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/runtime/reflect/opt/TransducedAccessor_method_Float.class */
public final class TransducedAccessor_method_Float extends DefaultTransducedAccessor {
    @Override // com.sun.xml.bind.v2.runtime.reflect.DefaultTransducedAccessor, com.sun.xml.bind.v2.runtime.reflect.TransducedAccessor
    public String print(Object o) {
        return DatatypeConverterImpl._printFloat(((Bean) o).get_float());
    }

    @Override // com.sun.xml.bind.v2.runtime.reflect.TransducedAccessor
    public void parse(Object o, CharSequence lexical) {
        ((Bean) o).set_float(DatatypeConverterImpl._parseFloat(lexical));
    }

    @Override // com.sun.xml.bind.v2.runtime.reflect.TransducedAccessor
    public boolean hasValue(Object o) {
        return true;
    }
}
