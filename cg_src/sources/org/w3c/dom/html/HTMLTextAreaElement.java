package org.w3c.dom.html;
/* loaded from: gencallgraphv3.jar:xmlParserAPIs-2.6.2.jar:org/w3c/dom/html/HTMLTextAreaElement.class */
public interface HTMLTextAreaElement extends HTMLElement {
    String getDefaultValue();

    void setDefaultValue(String str);

    HTMLFormElement getForm();

    String getAccessKey();

    void setAccessKey(String str);

    int getCols();

    void setCols(int i);

    boolean getDisabled();

    void setDisabled(boolean z);

    String getName();

    void setName(String str);

    boolean getReadOnly();

    void setReadOnly(boolean z);

    int getRows();

    void setRows(int i);

    int getTabIndex();

    void setTabIndex(int i);

    String getType();

    String getValue();

    void setValue(String str);

    void blur();

    void focus();

    void select();
}
