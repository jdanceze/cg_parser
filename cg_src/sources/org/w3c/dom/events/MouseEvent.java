package org.w3c.dom.events;

import org.w3c.dom.views.AbstractView;
/* loaded from: gencallgraphv3.jar:xmlParserAPIs-2.6.2.jar:org/w3c/dom/events/MouseEvent.class */
public interface MouseEvent extends UIEvent {
    int getScreenX();

    int getScreenY();

    int getClientX();

    int getClientY();

    boolean getCtrlKey();

    boolean getShiftKey();

    boolean getAltKey();

    boolean getMetaKey();

    short getButton();

    EventTarget getRelatedTarget();

    void initMouseEvent(String str, boolean z, boolean z2, AbstractView abstractView, int i, int i2, int i3, int i4, int i5, boolean z3, boolean z4, boolean z5, boolean z6, short s, EventTarget eventTarget);
}
