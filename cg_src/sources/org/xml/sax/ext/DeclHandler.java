package org.xml.sax.ext;

import org.xml.sax.SAXException;
/* loaded from: gencallgraphv3.jar:xmlParserAPIs-2.6.2.jar:org/xml/sax/ext/DeclHandler.class */
public interface DeclHandler {
    void elementDecl(String str, String str2) throws SAXException;

    void attributeDecl(String str, String str2, String str3, String str4, String str5) throws SAXException;

    void internalEntityDecl(String str, String str2) throws SAXException;

    void externalEntityDecl(String str, String str2, String str3) throws SAXException;
}
