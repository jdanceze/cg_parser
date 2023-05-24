package com.sun.xml.fastinfoset.stax.events;

import com.sun.xml.fastinfoset.CommonResourceBundle;
import java.io.Writer;
import javax.xml.namespace.QName;
import javax.xml.stream.Location;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
/* loaded from: gencallgraphv3.jar:FastInfoset-1.2.15.jar:com/sun/xml/fastinfoset/stax/events/EventBase.class */
public abstract class EventBase implements XMLEvent {
    protected int _eventType;
    protected Location _location = null;

    public EventBase() {
    }

    public EventBase(int eventType) {
        this._eventType = eventType;
    }

    public int getEventType() {
        return this._eventType;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setEventType(int eventType) {
        this._eventType = eventType;
    }

    public boolean isStartElement() {
        return this._eventType == 1;
    }

    public boolean isEndElement() {
        return this._eventType == 2;
    }

    public boolean isEntityReference() {
        return this._eventType == 9;
    }

    public boolean isProcessingInstruction() {
        return this._eventType == 3;
    }

    public boolean isStartDocument() {
        return this._eventType == 7;
    }

    public boolean isEndDocument() {
        return this._eventType == 8;
    }

    public Location getLocation() {
        return this._location;
    }

    public void setLocation(Location loc) {
        this._location = loc;
    }

    public String getSystemId() {
        if (this._location == null) {
            return "";
        }
        return this._location.getSystemId();
    }

    public Characters asCharacters() {
        if (isCharacters()) {
            return (Characters) this;
        }
        throw new ClassCastException(CommonResourceBundle.getInstance().getString("message.charactersCast", new Object[]{getEventTypeString()}));
    }

    public EndElement asEndElement() {
        if (isEndElement()) {
            return (EndElement) this;
        }
        throw new ClassCastException(CommonResourceBundle.getInstance().getString("message.endElementCase", new Object[]{getEventTypeString()}));
    }

    public StartElement asStartElement() {
        if (isStartElement()) {
            return (StartElement) this;
        }
        throw new ClassCastException(CommonResourceBundle.getInstance().getString("message.startElementCase", new Object[]{getEventTypeString()}));
    }

    public QName getSchemaType() {
        return null;
    }

    public boolean isAttribute() {
        return this._eventType == 10;
    }

    public boolean isCharacters() {
        return this._eventType == 4;
    }

    public boolean isNamespace() {
        return this._eventType == 13;
    }

    public void writeAsEncodedUnicode(Writer writer) throws XMLStreamException {
    }

    private String getEventTypeString() {
        switch (this._eventType) {
            case 1:
                return "StartElementEvent";
            case 2:
                return "EndElementEvent";
            case 3:
                return "ProcessingInstructionEvent";
            case 4:
                return "CharacterEvent";
            case 5:
                return "CommentEvent";
            case 6:
            default:
                return "UNKNOWN_EVENT_TYPE";
            case 7:
                return "StartDocumentEvent";
            case 8:
                return "EndDocumentEvent";
            case 9:
                return "EntityReferenceEvent";
            case 10:
                return "AttributeBase";
            case 11:
                return "DTDEvent";
            case 12:
                return "CDATA";
        }
    }
}
