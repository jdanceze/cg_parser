package org.w3c.dom;
/* loaded from: gencallgraphv3.jar:xmlParserAPIs-2.6.2.jar:org/w3c/dom/Element.class */
public interface Element extends Node {
    String getTagName();

    String getAttribute(String str);

    void setAttribute(String str, String str2) throws DOMException;

    void removeAttribute(String str) throws DOMException;

    Attr getAttributeNode(String str);

    Attr setAttributeNode(Attr attr) throws DOMException;

    Attr removeAttributeNode(Attr attr) throws DOMException;

    NodeList getElementsByTagName(String str);

    String getAttributeNS(String str, String str2);

    void setAttributeNS(String str, String str2, String str3) throws DOMException;

    void removeAttributeNS(String str, String str2) throws DOMException;

    Attr getAttributeNodeNS(String str, String str2);

    Attr setAttributeNodeNS(Attr attr) throws DOMException;

    NodeList getElementsByTagNameNS(String str, String str2);

    boolean hasAttribute(String str);

    boolean hasAttributeNS(String str, String str2);
}
