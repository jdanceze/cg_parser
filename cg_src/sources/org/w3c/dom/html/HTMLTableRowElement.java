package org.w3c.dom.html;
/* loaded from: gencallgraphv3.jar:xmlParserAPIs-2.6.2.jar:org/w3c/dom/html/HTMLTableRowElement.class */
public interface HTMLTableRowElement extends HTMLElement {
    int getRowIndex();

    void setRowIndex(int i);

    int getSectionRowIndex();

    void setSectionRowIndex(int i);

    HTMLCollection getCells();

    void setCells(HTMLCollection hTMLCollection);

    String getAlign();

    void setAlign(String str);

    String getBgColor();

    void setBgColor(String str);

    String getCh();

    void setCh(String str);

    String getChOff();

    void setChOff(String str);

    String getVAlign();

    void setVAlign(String str);

    HTMLElement insertCell(int i);

    void deleteCell(int i);
}
