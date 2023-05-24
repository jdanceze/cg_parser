package org.w3c.dom.stylesheets;

import org.w3c.dom.DOMException;
/* loaded from: gencallgraphv3.jar:xmlParserAPIs-2.6.2.jar:org/w3c/dom/stylesheets/MediaList.class */
public interface MediaList {
    String getMediaText();

    void setMediaText(String str) throws DOMException;

    int getLength();

    String item(int i);

    void deleteMedium(String str) throws DOMException;

    void appendMedium(String str) throws DOMException;
}
