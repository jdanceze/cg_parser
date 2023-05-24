package org.w3c.dom.events;
/* loaded from: gencallgraphv3.jar:xmlParserAPIs-2.6.2.jar:org/w3c/dom/events/EventTarget.class */
public interface EventTarget {
    void addEventListener(String str, EventListener eventListener, boolean z);

    void removeEventListener(String str, EventListener eventListener, boolean z);

    boolean dispatchEvent(Event event) throws EventException;
}
