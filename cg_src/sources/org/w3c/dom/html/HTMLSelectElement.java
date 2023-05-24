package org.w3c.dom.html;
/* loaded from: gencallgraphv3.jar:xmlParserAPIs-2.6.2.jar:org/w3c/dom/html/HTMLSelectElement.class */
public interface HTMLSelectElement extends HTMLElement {
    String getType();

    int getSelectedIndex();

    void setSelectedIndex(int i);

    String getValue();

    void setValue(String str);

    int getLength();

    HTMLFormElement getForm();

    HTMLCollection getOptions();

    boolean getDisabled();

    void setDisabled(boolean z);

    boolean getMultiple();

    void setMultiple(boolean z);

    String getName();

    void setName(String str);

    int getSize();

    void setSize(int i);

    int getTabIndex();

    void setTabIndex(int i);

    void add(HTMLElement hTMLElement, HTMLElement hTMLElement2);

    void remove(int i);

    void blur();

    void focus();
}
