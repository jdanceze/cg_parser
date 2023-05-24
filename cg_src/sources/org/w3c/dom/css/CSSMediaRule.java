package org.w3c.dom.css;

import org.w3c.dom.DOMException;
import org.w3c.dom.stylesheets.MediaList;
/* loaded from: gencallgraphv3.jar:xmlParserAPIs-2.6.2.jar:org/w3c/dom/css/CSSMediaRule.class */
public interface CSSMediaRule extends CSSRule {
    MediaList getMedia();

    CSSRuleList getCssRules();

    int insertRule(String str, int i) throws DOMException;

    void deleteRule(int i) throws DOMException;
}
