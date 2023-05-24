package com.sun.xml.bind.unmarshaller;

import com.sun.xml.bind.v2.runtime.unmarshaller.LocatorEx;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/unmarshaller/InfosetScanner.class */
public interface InfosetScanner<XmlNode> {
    void scan(XmlNode xmlnode) throws SAXException;

    void setContentHandler(ContentHandler contentHandler);

    ContentHandler getContentHandler();

    XmlNode getCurrentElement();

    LocatorEx getLocator();
}
