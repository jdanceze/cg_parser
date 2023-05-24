package org.w3c.dom.html;
/* loaded from: gencallgraphv3.jar:xmlParserAPIs-2.6.2.jar:org/w3c/dom/html/HTMLLabelElement.class */
public interface HTMLLabelElement extends HTMLElement {
    HTMLFormElement getForm();

    String getAccessKey();

    void setAccessKey(String str);

    String getHtmlFor();

    void setHtmlFor(String str);
}
