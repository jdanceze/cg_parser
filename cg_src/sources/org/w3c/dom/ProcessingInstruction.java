package org.w3c.dom;
/* loaded from: gencallgraphv3.jar:xmlParserAPIs-2.6.2.jar:org/w3c/dom/ProcessingInstruction.class */
public interface ProcessingInstruction extends Node {
    String getTarget();

    String getData();

    void setData(String str) throws DOMException;
}
