package org.w3c.dom.html;
/* loaded from: gencallgraphv3.jar:xmlParserAPIs-2.6.2.jar:org/w3c/dom/html/HTMLOptionElement.class */
public interface HTMLOptionElement extends HTMLElement {
    HTMLFormElement getForm();

    boolean getDefaultSelected();

    void setDefaultSelected(boolean z);

    String getText();

    int getIndex();

    void setIndex(int i);

    boolean getDisabled();

    void setDisabled(boolean z);

    String getLabel();

    void setLabel(String str);

    boolean getSelected();

    String getValue();

    void setValue(String str);
}
