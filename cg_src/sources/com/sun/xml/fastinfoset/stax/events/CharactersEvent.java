package com.sun.xml.fastinfoset.stax.events;

import com.sun.xml.fastinfoset.org.apache.xerces.util.XMLChar;
import javax.xml.stream.events.Characters;
/* loaded from: gencallgraphv3.jar:FastInfoset-1.2.15.jar:com/sun/xml/fastinfoset/stax/events/CharactersEvent.class */
public class CharactersEvent extends EventBase implements Characters {
    private String _text;
    private boolean isCData;
    private boolean isSpace;
    private boolean isIgnorable;
    private boolean needtoCheck;

    public CharactersEvent() {
        super(4);
        this.isCData = false;
        this.isSpace = false;
        this.isIgnorable = false;
        this.needtoCheck = true;
    }

    public CharactersEvent(String data) {
        super(4);
        this.isCData = false;
        this.isSpace = false;
        this.isIgnorable = false;
        this.needtoCheck = true;
        this._text = data;
    }

    public CharactersEvent(String data, boolean isCData) {
        super(4);
        this.isCData = false;
        this.isSpace = false;
        this.isIgnorable = false;
        this.needtoCheck = true;
        this._text = data;
        this.isCData = isCData;
    }

    public String getData() {
        return this._text;
    }

    public void setData(String data) {
        this._text = data;
    }

    public boolean isCData() {
        return this.isCData;
    }

    public String toString() {
        if (this.isCData) {
            return "<![CDATA[" + this._text + "]]>";
        }
        return this._text;
    }

    public boolean isIgnorableWhiteSpace() {
        return this.isIgnorable;
    }

    public boolean isWhiteSpace() {
        if (this.needtoCheck) {
            checkWhiteSpace();
            this.needtoCheck = false;
        }
        return this.isSpace;
    }

    public void setSpace(boolean isSpace) {
        this.isSpace = isSpace;
        this.needtoCheck = false;
    }

    public void setIgnorable(boolean isIgnorable) {
        this.isIgnorable = isIgnorable;
        setEventType(6);
    }

    private void checkWhiteSpace() {
        if (!Util.isEmptyString(this._text)) {
            this.isSpace = true;
            for (int i = 0; i < this._text.length(); i++) {
                if (!XMLChar.isSpace(this._text.charAt(i))) {
                    this.isSpace = false;
                    return;
                }
            }
        }
    }
}
