package org.w3c.dom;
/* loaded from: gencallgraphv3.jar:xmlParserAPIs-2.6.2.jar:org/w3c/dom/Attr.class */
public interface Attr extends Node {
    String getName();

    boolean getSpecified();

    String getValue();

    void setValue(String str) throws DOMException;

    Element getOwnerElement();
}
