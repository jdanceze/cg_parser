package org.w3c.dom.css;

import org.w3c.dom.Element;
import org.w3c.dom.stylesheets.DocumentStyle;
/* loaded from: gencallgraphv3.jar:xmlParserAPIs-2.6.2.jar:org/w3c/dom/css/DocumentCSS.class */
public interface DocumentCSS extends DocumentStyle {
    CSSStyleDeclaration getOverrideStyle(Element element, String str);
}
