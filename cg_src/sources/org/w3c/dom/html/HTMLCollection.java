package org.w3c.dom.html;

import org.w3c.dom.Node;
/* loaded from: gencallgraphv3.jar:xmlParserAPIs-2.6.2.jar:org/w3c/dom/html/HTMLCollection.class */
public interface HTMLCollection {
    int getLength();

    Node item(int i);

    Node namedItem(String str);
}
