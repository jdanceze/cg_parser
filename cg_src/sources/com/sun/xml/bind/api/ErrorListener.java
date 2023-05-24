package com.sun.xml.bind.api;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/api/ErrorListener.class */
public interface ErrorListener extends ErrorHandler {
    @Override // org.xml.sax.ErrorHandler
    void error(SAXParseException sAXParseException);

    @Override // org.xml.sax.ErrorHandler
    void fatalError(SAXParseException sAXParseException);

    @Override // org.xml.sax.ErrorHandler
    void warning(SAXParseException sAXParseException);

    void info(SAXParseException sAXParseException);
}
