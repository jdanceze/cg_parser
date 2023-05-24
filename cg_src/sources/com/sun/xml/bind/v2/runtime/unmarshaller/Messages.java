package com.sun.xml.bind.v2.runtime.unmarshaller;

import java.text.MessageFormat;
import java.util.ResourceBundle;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/runtime/unmarshaller/Messages.class */
enum Messages {
    UNRESOLVED_IDREF,
    UNEXPECTED_ELEMENT,
    UNEXPECTED_TEXT,
    NOT_A_QNAME,
    UNRECOGNIZED_TYPE_NAME,
    UNRECOGNIZED_TYPE_NAME_MAYBE,
    UNABLE_TO_CREATE_MAP,
    UNINTERNED_STRINGS,
    ERRORS_LIMIT_EXCEEDED;
    
    private static final ResourceBundle rb = ResourceBundle.getBundle(Messages.class.getName());

    @Override // java.lang.Enum
    public String toString() {
        return format(new Object[0]);
    }

    public String format(Object... args) {
        return MessageFormat.format(rb.getString(name()), args);
    }
}
