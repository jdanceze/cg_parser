package org.w3c.dom.css;

import org.w3c.dom.DOMException;
/* loaded from: gencallgraphv3.jar:xmlParserAPIs-2.6.2.jar:org/w3c/dom/css/CSSStyleDeclaration.class */
public interface CSSStyleDeclaration {
    String getCssText();

    void setCssText(String str) throws DOMException;

    String getPropertyValue(String str);

    CSSValue getPropertyCSSValue(String str);

    String removeProperty(String str) throws DOMException;

    String getPropertyPriority(String str);

    void setProperty(String str, String str2, String str3) throws DOMException;

    int getLength();

    String item(int i);

    CSSRule getParentRule();
}
