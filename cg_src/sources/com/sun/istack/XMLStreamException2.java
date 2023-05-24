package com.sun.istack;

import javax.xml.stream.Location;
import javax.xml.stream.XMLStreamException;
/* loaded from: gencallgraphv3.jar:istack-commons-runtime-3.0.7.jar:com/sun/istack/XMLStreamException2.class */
public class XMLStreamException2 extends XMLStreamException {
    public XMLStreamException2(String msg) {
        super(msg);
    }

    public XMLStreamException2(Throwable th) {
        super(th);
    }

    public XMLStreamException2(String msg, Throwable th) {
        super(msg, th);
    }

    public XMLStreamException2(String msg, Location location) {
        super(msg, location);
    }

    public XMLStreamException2(String msg, Location location, Throwable th) {
        super(msg, location, th);
    }

    public Throwable getCause() {
        return getNestedException();
    }
}
