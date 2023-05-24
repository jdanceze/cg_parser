package org.w3c.dom.events;
/* loaded from: gencallgraphv3.jar:xmlParserAPIs-2.6.2.jar:org/w3c/dom/events/EventException.class */
public class EventException extends RuntimeException {
    public short code;
    public static final short UNSPECIFIED_EVENT_TYPE_ERR = 0;

    public EventException(short s, String str) {
        super(str);
        this.code = s;
    }
}
