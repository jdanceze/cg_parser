package org.w3c.dom;
/* loaded from: gencallgraphv3.jar:xmlParserAPIs-2.6.2.jar:org/w3c/dom/NamedNodeMap.class */
public interface NamedNodeMap {
    Node getNamedItem(String str);

    Node setNamedItem(Node node) throws DOMException;

    Node removeNamedItem(String str) throws DOMException;

    Node item(int i);

    int getLength();

    Node getNamedItemNS(String str, String str2);

    Node setNamedItemNS(Node node) throws DOMException;

    Node removeNamedItemNS(String str, String str2) throws DOMException;
}
