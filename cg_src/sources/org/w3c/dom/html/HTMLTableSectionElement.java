package org.w3c.dom.html;
/* loaded from: gencallgraphv3.jar:xmlParserAPIs-2.6.2.jar:org/w3c/dom/html/HTMLTableSectionElement.class */
public interface HTMLTableSectionElement extends HTMLElement {
    String getAlign();

    void setAlign(String str);

    String getCh();

    void setCh(String str);

    String getChOff();

    void setChOff(String str);

    String getVAlign();

    void setVAlign(String str);

    HTMLCollection getRows();

    HTMLElement insertRow(int i);

    void deleteRow(int i);
}
