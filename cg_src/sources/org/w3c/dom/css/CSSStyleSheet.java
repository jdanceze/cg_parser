package org.w3c.dom.css;

import org.w3c.dom.DOMException;
import org.w3c.dom.stylesheets.StyleSheet;
/* loaded from: gencallgraphv3.jar:xmlParserAPIs-2.6.2.jar:org/w3c/dom/css/CSSStyleSheet.class */
public interface CSSStyleSheet extends StyleSheet {
    CSSRule getOwnerRule();

    CSSRuleList getCssRules();

    int insertRule(String str, int i) throws DOMException;

    void deleteRule(int i) throws DOMException;
}
