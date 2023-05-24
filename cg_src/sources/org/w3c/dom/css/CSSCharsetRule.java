package org.w3c.dom.css;

import org.w3c.dom.DOMException;
/* loaded from: gencallgraphv3.jar:xmlParserAPIs-2.6.2.jar:org/w3c/dom/css/CSSCharsetRule.class */
public interface CSSCharsetRule extends CSSRule {
    String getEncoding();

    void setEncoding(String str) throws DOMException;
}
