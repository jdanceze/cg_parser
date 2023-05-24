package org.w3c.dom;
/* loaded from: gencallgraphv3.jar:xmlParserAPIs-2.6.2.jar:org/w3c/dom/DOMImplementation.class */
public interface DOMImplementation {
    boolean hasFeature(String str, String str2);

    DocumentType createDocumentType(String str, String str2, String str3) throws DOMException;

    Document createDocument(String str, String str2, DocumentType documentType) throws DOMException;
}
