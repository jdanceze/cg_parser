package org.w3c.dom.events;

import org.w3c.dom.views.AbstractView;
/* loaded from: gencallgraphv3.jar:xmlParserAPIs-2.6.2.jar:org/w3c/dom/events/UIEvent.class */
public interface UIEvent extends Event {
    AbstractView getView();

    int getDetail();

    void initUIEvent(String str, boolean z, boolean z2, AbstractView abstractView, int i);
}
