package com.sun.istack;

import org.xml.sax.SAXException;
/* loaded from: gencallgraphv3.jar:istack-commons-runtime-3.0.7.jar:com/sun/istack/SAXException2.class */
public class SAXException2 extends SAXException {
    public SAXException2(String message) {
        super(message);
    }

    public SAXException2(Exception e) {
        super(e);
    }

    public SAXException2(String message, Exception e) {
        super(message, e);
    }

    @Override // java.lang.Throwable
    public Throwable getCause() {
        return getException();
    }
}
