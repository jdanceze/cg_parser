package org.w3c.dom;
/* loaded from: gencallgraphv3.jar:xmlParserAPIs-2.6.2.jar:org/w3c/dom/DocumentType.class */
public interface DocumentType extends Node {
    String getName();

    NamedNodeMap getEntities();

    NamedNodeMap getNotations();

    String getPublicId();

    String getSystemId();

    String getInternalSubset();
}
