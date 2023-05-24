package com.sun.xml.fastinfoset.stax.events;

import javax.xml.stream.events.Comment;
/* loaded from: gencallgraphv3.jar:FastInfoset-1.2.15.jar:com/sun/xml/fastinfoset/stax/events/CommentEvent.class */
public class CommentEvent extends EventBase implements Comment {
    private String _text;

    public CommentEvent() {
        super(5);
    }

    public CommentEvent(String text) {
        this();
        this._text = text;
    }

    public String toString() {
        return "<!--" + this._text + "-->";
    }

    public String getText() {
        return this._text;
    }

    public void setText(String text) {
        this._text = text;
    }
}
