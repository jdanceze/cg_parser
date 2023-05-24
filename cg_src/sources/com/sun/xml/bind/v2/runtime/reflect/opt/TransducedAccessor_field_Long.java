package com.sun.xml.bind.v2.runtime.reflect.opt;

import com.sun.xml.bind.DatatypeConverterImpl;
import com.sun.xml.bind.v2.runtime.reflect.DefaultTransducedAccessor;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/runtime/reflect/opt/TransducedAccessor_field_Long.class */
public final class TransducedAccessor_field_Long extends DefaultTransducedAccessor {
    @Override // com.sun.xml.bind.v2.runtime.reflect.DefaultTransducedAccessor, com.sun.xml.bind.v2.runtime.reflect.TransducedAccessor
    public String print(Object o) {
        return DatatypeConverterImpl._printLong(((Bean) o).f_long);
    }

    @Override // com.sun.xml.bind.v2.runtime.reflect.TransducedAccessor
    public void parse(Object o, CharSequence lexical) {
        ((Bean) o).f_long = DatatypeConverterImpl._parseLong(lexical);
    }

    @Override // com.sun.xml.bind.v2.runtime.reflect.TransducedAccessor
    public boolean hasValue(Object o) {
        return true;
    }
}
