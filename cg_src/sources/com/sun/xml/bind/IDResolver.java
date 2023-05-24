package com.sun.xml.bind;

import java.util.concurrent.Callable;
import javax.xml.bind.ValidationEventHandler;
import org.xml.sax.SAXException;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/IDResolver.class */
public abstract class IDResolver {
    public abstract void bind(String str, Object obj) throws SAXException;

    public abstract Callable<?> resolve(String str, Class cls) throws SAXException;

    public void startDocument(ValidationEventHandler eventHandler) throws SAXException {
    }

    public void endDocument() throws SAXException {
    }
}
