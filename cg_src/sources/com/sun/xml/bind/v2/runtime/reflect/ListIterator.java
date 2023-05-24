package com.sun.xml.bind.v2.runtime.reflect;

import javax.xml.bind.JAXBException;
import org.xml.sax.SAXException;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/runtime/reflect/ListIterator.class */
public interface ListIterator<E> {
    boolean hasNext();

    E next() throws SAXException, JAXBException;
}
