package org.w3c.dom.html;
/* loaded from: gencallgraphv3.jar:xmlParserAPIs-2.6.2.jar:org/w3c/dom/html/HTMLTableElement.class */
public interface HTMLTableElement extends HTMLElement {
    HTMLTableCaptionElement getCaption();

    void setCaption(HTMLTableCaptionElement hTMLTableCaptionElement);

    HTMLTableSectionElement getTHead();

    void setTHead(HTMLTableSectionElement hTMLTableSectionElement);

    HTMLTableSectionElement getTFoot();

    void setTFoot(HTMLTableSectionElement hTMLTableSectionElement);

    HTMLCollection getRows();

    HTMLCollection getTBodies();

    String getAlign();

    void setAlign(String str);

    String getBgColor();

    void setBgColor(String str);

    String getBorder();

    void setBorder(String str);

    String getCellPadding();

    void setCellPadding(String str);

    String getCellSpacing();

    void setCellSpacing(String str);

    String getFrame();

    void setFrame(String str);

    String getRules();

    void setRules(String str);

    String getSummary();

    void setSummary(String str);

    String getWidth();

    void setWidth(String str);

    HTMLElement createTHead();

    void deleteTHead();

    HTMLElement createTFoot();

    void deleteTFoot();

    HTMLElement createCaption();

    void deleteCaption();

    HTMLElement insertRow(int i);

    void deleteRow(int i);
}
