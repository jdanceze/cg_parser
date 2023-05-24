package org.w3c.dom;
/* loaded from: gencallgraphv3.jar:xmlParserAPIs-2.6.2.jar:org/w3c/dom/Document.class */
public interface Document extends Node {
    DocumentType getDoctype();

    DOMImplementation getImplementation();

    Element getDocumentElement();

    Element createElement(String str) throws DOMException;

    DocumentFragment createDocumentFragment();

    Text createTextNode(String str);

    Comment createComment(String str);

    CDATASection createCDATASection(String str) throws DOMException;

    ProcessingInstruction createProcessingInstruction(String str, String str2) throws DOMException;

    Attr createAttribute(String str) throws DOMException;

    EntityReference createEntityReference(String str) throws DOMException;

    NodeList getElementsByTagName(String str);

    Node importNode(Node node, boolean z) throws DOMException;

    Element createElementNS(String str, String str2) throws DOMException;

    Attr createAttributeNS(String str, String str2) throws DOMException;

    NodeList getElementsByTagNameNS(String str, String str2);

    Element getElementById(String str);
}
