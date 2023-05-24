package org.w3c.dom.stylesheets;

import org.w3c.dom.Node;
/* loaded from: gencallgraphv3.jar:xmlParserAPIs-2.6.2.jar:org/w3c/dom/stylesheets/StyleSheet.class */
public interface StyleSheet {
    String getType();

    boolean getDisabled();

    void setDisabled(boolean z);

    Node getOwnerNode();

    StyleSheet getParentStyleSheet();

    String getHref();

    String getTitle();

    MediaList getMedia();
}
