package org.w3c.dom.css;

import org.w3c.dom.DOMException;
/* loaded from: gencallgraphv3.jar:xmlParserAPIs-2.6.2.jar:org/w3c/dom/css/CSSPageRule.class */
public interface CSSPageRule extends CSSRule {
    String getSelectorText();

    void setSelectorText(String str) throws DOMException;

    CSSStyleDeclaration getStyle();
}
